
package org.jaudiotagger.audio.ogg;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.audio.ogg.util.VorbisHeader;
import org.jaudiotagger.audio.ogg.util.VorbisPacketType;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentReader;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class OggVorbisTagReader
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.ogg");

    private VorbisCommentReader vorbisCommentReader;

    public OggVorbisTagReader()
    {
        vorbisCommentReader = new VorbisCommentReader();
    }

    


    public Tag read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.config("Starting to read ogg vorbis tag from file:");
        byte[] rawVorbisCommentData = readRawPacketData(raf);

        //Begin tag reading
        VorbisCommentTag tag = vorbisCommentReader.read(rawVorbisCommentData, true);
        logger.fine("CompletedReadCommentTag");
        return tag;
    }


    public int readOggVorbisRawSize(RandomAccessFile raf) throws CannotReadException, IOException
    {
        byte[] rawVorbisCommentData = readRawPacketData(raf);
        return rawVorbisCommentData.length + VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH;
    }


    public byte[] readRawPacketData(RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.fine("Read 1st page");
        //1st page = codec infos
        OggPageHeader pageHeader = OggPageHeader.read(raf);
        //Skip over data to end of page header 1
        raf.seek(raf.getFilePointer() + pageHeader.getPageLength());

        logger.fine("Read 2nd page");
        //2nd page = comment, may extend to additional pages or not , may also have setup header
        pageHeader = OggPageHeader.read(raf);

        //Now at start of packets on page 2 , check this is the vorbis comment header 
        byte[] b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
        raf.read(b);
        if (!isVorbisCommentHeader(b))
        {
            throw new CannotReadException("Cannot find comment block (no vorbiscomment header)");
        }

        //Convert the comment raw data which maybe over many pages back into raw packet
        byte[] rawVorbisCommentData = convertToVorbisCommentPacket(pageHeader, raf);
        return rawVorbisCommentData;
    }



    public boolean isVorbisCommentHeader(byte[] headerData)
    {
        String vorbis = Utils.getString(headerData, VorbisHeader.FIELD_CAPTURE_PATTERN_POS, VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH, "ISO-8859-1");
        return !(headerData[VorbisHeader.FIELD_PACKET_TYPE_POS] != VorbisPacketType.COMMENT_HEADER.getType() || !vorbis.equals(VorbisHeader.CAPTURE_PATTERN));
    }


    public boolean isVorbisSetupHeader(byte[] headerData)
    {
        String vorbis = Utils.getString(headerData, VorbisHeader.FIELD_CAPTURE_PATTERN_POS, VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH, "ISO-8859-1");
        return !(headerData[VorbisHeader.FIELD_PACKET_TYPE_POS] != VorbisPacketType.SETUP_HEADER.getType() || !vorbis.equals(VorbisHeader.CAPTURE_PATTERN));
    }


    private byte[] convertToVorbisCommentPacket(OggPageHeader startVorbisCommentPage, RandomAccessFile raf) throws IOException, CannotReadException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[startVorbisCommentPage.getPacketList().get(0).getLength() - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH)];
        raf.read(b);
        baos.write(b);

        //Because there is at least one other packet (SetupHeaderPacket) this means the Comment Packet has finished
        //on this page so thats all we need and we can return
        if (startVorbisCommentPage.getPacketList().size() > 1)
        {
            logger.config("Comments finish on 2nd Page because there is another packet on this page");
            return baos.toByteArray();
        }

        //There is only the VorbisComment packet on page if it has completed on this page we can return
        if (!startVorbisCommentPage.isLastPacketIncomplete())
        {
            logger.config("Comments finish on 2nd Page because this packet is complete");
            return baos.toByteArray();
        }

        //The VorbisComment extends to the next page, so should be at end of page already
        //so carry on reading pages until we get to the end of comment
        while (true)
        {
            logger.config("Reading next page");
            OggPageHeader nextPageHeader = OggPageHeader.read(raf);
            b = new byte[nextPageHeader.getPacketList().get(0).getLength()];
            raf.read(b);
            baos.write(b);

            //Because there is at least one other packet (SetupHeaderPacket) this means the Comment Packet has finished
            //on this page so thats all we need and we can return
            if (nextPageHeader.getPacketList().size() > 1)
            {
                logger.config("Comments finish on Page because there is another packet on this page");
                return baos.toByteArray();
            }

            //There is only the VorbisComment packet on page if it has completed on this page we can return
            if (!nextPageHeader.isLastPacketIncomplete())
            {
                logger.config("Comments finish on Page because this packet is complete");
                return baos.toByteArray();
            }
        }
    }


    public byte[] convertToVorbisSetupHeaderPacket(long fileOffsetOfStartingOggPage, RandomAccessFile raf) throws IOException, CannotReadException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //Seek to specified offset
        raf.seek(fileOffsetOfStartingOggPage);

        //Read Page
        OggPageHeader setupPageHeader = OggPageHeader.read(raf);

        //Assume that if multiple packets first packet is VorbisComment and second packet
        //is setupheader
        if (setupPageHeader.getPacketList().size() > 1)
        {
            raf.skipBytes(setupPageHeader.getPacketList().get(0).getLength());
        }

        //Now should be at start of next packet, check this is the vorbis setup header
        byte[] b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
        raf.read(b);
        if (!isVorbisSetupHeader(b))
        {
            throw new CannotReadException("Unable to find setup header(2), unable to write ogg file");
        }

        //Go back to start of setupheader data
        raf.seek(raf.getFilePointer() - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));

        //Read data
        if (setupPageHeader.getPacketList().size() > 1)
        {
            b = new byte[setupPageHeader.getPacketList().get(1).getLength()];
            raf.read(b);
            baos.write(b);
        }
        else
        {
            b = new byte[setupPageHeader.getPacketList().get(0).getLength()];
            raf.read(b);
            baos.write(b);
        }

        //Return Data
        if (!setupPageHeader.isLastPacketIncomplete() || setupPageHeader.getPacketList().size() > 2)
        {
            logger.config("Setupheader finishes on this page");
            return baos.toByteArray();
        }

        //The Setupheader extends to the next page, so should be at end of page already
        //so carry on reading pages until we get to the end of comment
        while (true)
        {
            logger.config("Reading another page");
            OggPageHeader nextPageHeader = OggPageHeader.read(raf);
            b = new byte[nextPageHeader.getPacketList().get(0).getLength()];
            raf.read(b);
            baos.write(b);

            //Because there is at least one other packet this means the Setupheader Packet has finished
            //on this page so thats all we need and we can return
            if (nextPageHeader.getPacketList().size() > 1)
            {
                logger.config("Setupheader finishes on this page");
                return baos.toByteArray();
            }

            //There is only the Setupheader packet on page if it has completed on this page we can return
            if (!nextPageHeader.isLastPacketIncomplete())
            {
                logger.config("Setupheader finish on Page because this packet is complete");
                return baos.toByteArray();
            }
        }
    }



    public byte[] convertToVorbisSetupHeaderPacketAndAdditionalPackets(long fileOffsetOfStartingOggPage, RandomAccessFile raf) throws IOException, CannotReadException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //Seek to specified offset
        raf.seek(fileOffsetOfStartingOggPage);

        //Read Page
        OggPageHeader setupPageHeader = OggPageHeader.read(raf);

        //Assume that if multiple packets first packet is VorbisComment and second packet
        //is setupheader
        if (setupPageHeader.getPacketList().size() > 1)
        {
            raf.skipBytes(setupPageHeader.getPacketList().get(0).getLength());
        }

        //Now should be at start of next packet, check this is the vorbis setup header
        byte[] b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
        raf.read(b);
        if (!isVorbisSetupHeader(b))
        {
            throw new CannotReadException("Unable to find setup header(2), unable to write ogg file");
        }

        //Go back to start of setupheader data
        raf.seek(raf.getFilePointer() - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));

        //Read data
        if (setupPageHeader.getPacketList().size() > 1)
        {
            b = new byte[setupPageHeader.getPacketList().get(1).getLength()];
            raf.read(b);
            baos.write(b);
        }
        else
        {
            b = new byte[setupPageHeader.getPacketList().get(0).getLength()];
            raf.read(b);
            baos.write(b);
        }

        //Return Data
        if (!setupPageHeader.isLastPacketIncomplete() || setupPageHeader.getPacketList().size() > 2)
        {
            logger.config("Setupheader finishes on this page");
            if (setupPageHeader.getPacketList().size() > 2)
            {
                for (int i = 2; i < setupPageHeader.getPacketList().size(); i++)
                {
                    b = new byte[setupPageHeader.getPacketList().get(i).getLength()];
                    raf.read(b);
                    baos.write(b);
                }
            }
            return baos.toByteArray();
        }

        //The Setupheader extends to the next page, so should be at end of page already
        //so carry on reading pages until we get to the end of comment
        while (true)
        {
            logger.config("Reading another page");
            OggPageHeader nextPageHeader = OggPageHeader.read(raf);
            b = new byte[nextPageHeader.getPacketList().get(0).getLength()];
            raf.read(b);
            baos.write(b);

            //Because there is at least one other packet this means the Setupheader Packet has finished
            //on this page so thats all we need and we can return
            if (nextPageHeader.getPacketList().size() > 1)
            {
                logger.config("Setupheader finishes on this page");
                return baos.toByteArray();
            }

            //There is only the Setupheader packet on page if it has completed on this page we can return
            if (!nextPageHeader.isLastPacketIncomplete())
            {
                logger.config("Setupheader finish on Page because this packet is complete");
                return baos.toByteArray();
            }
        }
    }



    public OggVorbisHeaderSizes readOggVorbisHeaderSizes(RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.fine("Started to read comment and setup header sizes:");

        //Stores filepointers so return file in same state
        long filepointer = raf.getFilePointer();

        //Extra Packets on same page as setup header
        List<OggPageHeader.PacketStartAndLength> extraPackets = new ArrayList<OggPageHeader.PacketStartAndLength>();

        long commentHeaderStartPosition;
        long setupHeaderStartPosition;
        int commentHeaderSize = 0;
        int setupHeaderSize;
        //1st page = codec infos
        OggPageHeader pageHeader = OggPageHeader.read(raf);
        //Skip over data to end of page header 1
        raf.seek(raf.getFilePointer() + pageHeader.getPageLength());

        //2nd page = comment, may extend to additional pages or not , may also have setup header
        pageHeader = OggPageHeader.read(raf);
        commentHeaderStartPosition = raf.getFilePointer() - (OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);

        //Now at start of packets on page 2 , check this is the vorbis comment header
        byte[] b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
        raf.read(b);
        if (!isVorbisCommentHeader(b))
        {
            throw new CannotReadException("Cannot find comment block (no vorbiscomment header)");
        }
        raf.seek(raf.getFilePointer() - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));
        logger.config("Found start of comment header at:" + raf.getFilePointer());

        //Calculate Comment Size (not inc header)
        while (true)
        {
            List<OggPageHeader.PacketStartAndLength> packetList = pageHeader.getPacketList();
            commentHeaderSize += packetList.get(0).getLength();
            raf.skipBytes(packetList.get(0).getLength());

            //If this page contains multiple packets or if this last packet is complete then the Comment header
            //end son this page and we can break
            if (packetList.size() > 1 || !pageHeader.isLastPacketIncomplete())
            {
                //done comment size
                logger.config("Found end of comment:size:" + commentHeaderSize + "finishes at file position:" + raf.getFilePointer());
                break;
            }
            pageHeader = OggPageHeader.read(raf);
        }

        //If there are no more packets on this page we need to go to next page to get the setup header
        OggPageHeader.PacketStartAndLength       packet;
        if(pageHeader.getPacketList().size()==1)
        {
            pageHeader = OggPageHeader.read(raf);
            List<OggPageHeader.PacketStartAndLength> packetList = pageHeader.getPacketList();                       
            packet     = pageHeader.getPacketList().get(0);

            //Now at start of next packet , check this is the vorbis setup header
            b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
            raf.read(b);
            if (!isVorbisSetupHeader(b))
            {
                throw new CannotReadException(ErrorMessage.OGG_VORBIS_NO_VORBIS_HEADER_FOUND.getMsg());
            }
            raf.seek(raf.getFilePointer() - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));
            logger.config("Found start of vorbis setup header at file position:" + raf.getFilePointer());

            //Set this to the  start of the OggPage that setupheader was found on
            setupHeaderStartPosition = raf.getFilePointer() - (OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);

            //Add packet data to size to the setup header size
            setupHeaderSize = packet.getLength();
            logger.fine("Adding:" + packet.getLength() + " to setup header size");

            //Skip over the packet data
            raf.skipBytes(packet.getLength());

            //If there are other packets that follow this one, or if the last packet is complete then we must have
            //got the size of the setup header.
            if (packetList.size() > 1 || !pageHeader.isLastPacketIncomplete())
            {
                logger.config("Found end of setupheader:size:" + setupHeaderSize + "finishes at:" + raf.getFilePointer());
                if (packetList.size() > 1)
                {
                    extraPackets = packetList.subList(1, packetList.size());
                }
            }
            //The setup header continues onto the next page
            else
            {
                pageHeader = OggPageHeader.read(raf);
                packetList = pageHeader.getPacketList();
                while (true)
                {
                    setupHeaderSize += packetList.get(0).getLength();
                    logger.fine("Adding:" + packetList.get(0).getLength() + " to setup header size");
                    raf.skipBytes(packetList.get(0).getLength());
                    if (packetList.size() > 1 || !pageHeader.isLastPacketIncomplete())
                    {
                        //done setup size
                        logger.fine("Found end of setupheader:size:" + setupHeaderSize + "finishes at:" + raf.getFilePointer());
                        if (packetList.size() > 1)
                        {
                            extraPackets = packetList.subList(1, packetList.size());
                        }
                        break;
                    }
                    //Continues onto another page
                    pageHeader = OggPageHeader.read(raf);
                }
            }
        }
        //else its next packet on this page
        else
        {
            packet     = pageHeader.getPacketList().get(1);
            List<OggPageHeader.PacketStartAndLength> packetList = pageHeader.getPacketList();

            //Now at start of next packet , check this is the vorbis setup header
            b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
            raf.read(b);
            if (!isVorbisSetupHeader(b))
            {
                logger.warning("Expecting but got:"+new String(b)+ "at "+(raf.getFilePointer()  - b.length));
                throw new CannotReadException(ErrorMessage.OGG_VORBIS_NO_VORBIS_HEADER_FOUND.getMsg());
            }
            raf.seek(raf.getFilePointer() - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));
            logger.config("Found start of vorbis setup header at file position:" + raf.getFilePointer());

            //Set this to the  start of the OggPage that setupheader was found on
            setupHeaderStartPosition = raf.getFilePointer() - (OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length)
                    - pageHeader.getPacketList().get(0).getLength();

            //Add packet data to size to the setup header size
            setupHeaderSize = packet.getLength();
            logger.fine("Adding:" + packet.getLength() + " to setup header size");

            //Skip over the packet data
            raf.skipBytes(packet.getLength());

             //If there are other packets that follow this one, or if the last packet is complete then we must have
            //got the size of the setup header.
            if (packetList.size() > 2 || !pageHeader.isLastPacketIncomplete())
            {
                logger.fine("Found end of setupheader:size:" + setupHeaderSize + "finishes at:" + raf.getFilePointer());
                if (packetList.size() > 2)
                {
                    extraPackets = packetList.subList(2, packetList.size());
                }
            }
            //The setup header continues onto the next page
            else
            {
                pageHeader = OggPageHeader.read(raf);
                packetList = pageHeader.getPacketList();
                while (true)
                {
                    setupHeaderSize += packetList.get(0).getLength();
                    logger.fine("Adding:" + packetList.get(0).getLength() + " to setup header size");
                    raf.skipBytes(packetList.get(0).getLength());
                    if (packetList.size() > 1 || !pageHeader.isLastPacketIncomplete())
                    {
                        //done setup size
                        logger.fine("Found end of setupheader:size:" + setupHeaderSize + "finishes at:" + raf.getFilePointer());
                        if (packetList.size() > 1)
                        {
                            extraPackets = packetList.subList(1, packetList.size());
                        }
                        break;
                    }
                    //Continues onto another page
                    pageHeader = OggPageHeader.read(raf);
                }
            }
        }

        //Reset filepointer to location that it was in at start of method
        raf.seek(filepointer);
        return new OggVorbisHeaderSizes(commentHeaderStartPosition, setupHeaderStartPosition, commentHeaderSize, setupHeaderSize, extraPackets);
    }


    public static class OggVorbisHeaderSizes
    {
        private long commentHeaderStartPosition;
        private long setupHeaderStartPosition;
        private int commentHeaderSize;
        private int setupHeaderSize;
        private List<OggPageHeader.PacketStartAndLength> packetList;

        OggVorbisHeaderSizes(long commentHeaderStartPosition, long setupHeaderStartPosition, int commentHeaderSize, int setupHeaderSize, List<OggPageHeader.PacketStartAndLength> packetList)
        {
            this.packetList = packetList;
            this.commentHeaderStartPosition = commentHeaderStartPosition;
            this.setupHeaderStartPosition = setupHeaderStartPosition;
            this.commentHeaderSize = commentHeaderSize;
            this.setupHeaderSize = setupHeaderSize;
        }


        public int getCommentHeaderSize()
        {
            return commentHeaderSize;
        }


        public int getSetupHeaderSize()
        {
            return setupHeaderSize;
        }


        public int getExtraPacketDataSize()
        {
            int extraPacketSize = 0;
            for (OggPageHeader.PacketStartAndLength packet : packetList)
            {
                extraPacketSize += packet.getLength();
            }
            return extraPacketSize;
        }


        public long getCommentHeaderStartPosition()
        {
            return commentHeaderStartPosition;
        }


        public long getSetupHeaderStartPosition()
        {
            return setupHeaderStartPosition;
        }

        public List<OggPageHeader.PacketStartAndLength> getExtraPacketList()
        {
            return packetList;
        }
    }
}

