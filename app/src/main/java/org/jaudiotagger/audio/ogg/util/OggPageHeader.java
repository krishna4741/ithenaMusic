
package org.jaudiotagger.audio.ogg.util;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.nio.ByteBuffer;



public class OggPageHeader
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.ogg.atom");

    //Capture pattern at start of header
    public static final byte[] CAPTURE_PATTERN = {'O', 'g', 'g', 'S'};

    //Ogg Page header is always 27 bytes plus the size of the segment table which is variable
    public static final int OGG_PAGE_HEADER_FIXED_LENGTH = 27;

    //Can have upto 255 segments in a page
    public static final int MAXIMUM_NO_OF_SEGMENT_SIZE = 255;

    //Each segment can be upto 255 bytes
    public static final int MAXIMUM_SEGMENT_SIZE = 255;

    //Maximum size of pageheader (27 + 255 = 282)
    public static final int MAXIMUM_PAGE_HEADER_SIZE = OGG_PAGE_HEADER_FIXED_LENGTH + MAXIMUM_NO_OF_SEGMENT_SIZE;

    //Maximum size of page data following the page header (255 * 255 = 65025)
    public static final int MAXIMUM_PAGE_DATA_SIZE = MAXIMUM_NO_OF_SEGMENT_SIZE * MAXIMUM_SEGMENT_SIZE;

    //Maximum size of page includes header and data (282 + 65025 = 65307 bytes)
    public static final int MAXIMUM_PAGE_SIZE = MAXIMUM_PAGE_HEADER_SIZE + MAXIMUM_PAGE_DATA_SIZE;

    //Starting positions of the various attributes
    public static final int FIELD_CAPTURE_PATTERN_POS = 0;
    public static final int FIELD_STREAM_STRUCTURE_VERSION_POS = 4;
    public static final int FIELD_HEADER_TYPE_FLAG_POS = 5;
    public static final int FIELD_ABSOLUTE_GRANULE_POS = 6;
    public static final int FIELD_STREAM_SERIAL_NO_POS = 14;
    public static final int FIELD_PAGE_SEQUENCE_NO_POS = 18;
    public static final int FIELD_PAGE_CHECKSUM_POS = 22;
    public static final int FIELD_PAGE_SEGMENTS_POS = 26;
    public static final int FIELD_SEGMENT_TABLE_POS = 27;

    //Length of various attributes
    public static final int FIELD_CAPTURE_PATTERN_LENGTH = 4;
    public static final int FIELD_STREAM_STRUCTURE_VERSION_LENGTH = 1;
    public static final int FIELD_HEADER_TYPE_FLAG_LENGTH = 1;
    public static final int FIELD_ABSOLUTE_GRANULE_LENGTH = 8;
    public static final int FIELD_STREAM_SERIAL_NO_LENGTH = 4;
    public static final int FIELD_PAGE_SEQUENCE_NO_LENGTH = 4;
    public static final int FIELD_PAGE_CHECKSUM_LENGTH = 4;
    public static final int FIELD_PAGE_SEGMENTS_LENGTH = 1;

    private byte[] rawHeaderData;
    private double absoluteGranulePosition;
    private int checksum;
    private byte headerTypeFlag;

    private boolean isValid = false;
    private int pageLength = 0;
    private int pageSequenceNumber, streamSerialNumber;
    private byte[] segmentTable;

    private List<PacketStartAndLength> packetList = new ArrayList<PacketStartAndLength>();
    private boolean lastPacketIncomplete = false;

    private long startByte = 0;

    public static OggPageHeader read(ByteBuffer byteBuffer) throws IOException, CannotReadException
       {
           //byteBuffer
           int start = byteBuffer.position();
           logger.fine("Trying to read OggPage at:" + start);

           byte[] b = new byte[OggPageHeader.CAPTURE_PATTERN.length];
           byteBuffer.get(b);
           if (!(Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN)))
           {
               throw new CannotReadException(ErrorMessage.OGG_HEADER_CANNOT_BE_FOUND.getMsg(new String(b)));
           }

           byteBuffer.position(start + OggPageHeader.FIELD_PAGE_SEGMENTS_POS);
           int pageSegments = byteBuffer.get() & 0xFF; //unsigned
           byteBuffer.position(start);

           b = new byte[OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageSegments];
           byteBuffer.get(b);
           OggPageHeader pageHeader = new OggPageHeader(b);

           //Now just after PageHeader, ready for Packet Data
           return pageHeader;
       }


    public static OggPageHeader read(RandomAccessFile raf) throws IOException, CannotReadException
    {
        long start = raf.getFilePointer();
        logger.fine("Trying to read OggPage at:" + start);

        byte[] b = new byte[OggPageHeader.CAPTURE_PATTERN.length];
        raf.read(b);
        if (!(Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN)))
        {
            raf.seek(start);
            if(AbstractID3v2Tag.isId3Tag(raf))
            {
                logger.warning(ErrorMessage.OGG_CONTAINS_ID3TAG.getMsg(raf.getFilePointer() - start));
                raf.read(b);
                if ((Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN)))
                {
                    //Go to the end of the ID3 header
                    start=raf.getFilePointer() - OggPageHeader.CAPTURE_PATTERN.length;
                }
            }
            else
            {
                throw new CannotReadException(ErrorMessage.OGG_HEADER_CANNOT_BE_FOUND.getMsg(new String(b)));
            }
        }

        raf.seek(start + OggPageHeader.FIELD_PAGE_SEGMENTS_POS);
        int pageSegments = raf.readByte() & 0xFF; //unsigned
        raf.seek(start);

        b = new byte[OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageSegments];
        raf.read(b);


        OggPageHeader pageHeader = new OggPageHeader(b);
        pageHeader.setStartByte(start);
        //Now just after PageHeader, ready for Packet Data
        return pageHeader;
    }

    public OggPageHeader(byte[] b)
    {
        this.rawHeaderData = b;
        int streamStructureRevision = b[FIELD_STREAM_STRUCTURE_VERSION_POS];
        headerTypeFlag = b[FIELD_HEADER_TYPE_FLAG_POS];
        if (streamStructureRevision == 0)
        {
            this.absoluteGranulePosition = 0;
            for (int i = 0; i < FIELD_ABSOLUTE_GRANULE_LENGTH; i++)
            {
                this.absoluteGranulePosition += u(b[i + FIELD_ABSOLUTE_GRANULE_POS]) * Math.pow(2, 8 * i);
            }

            streamSerialNumber = Utils.getIntLE(b, FIELD_STREAM_SERIAL_NO_POS, 17);
            pageSequenceNumber = Utils.getIntLE(b, FIELD_PAGE_SEQUENCE_NO_POS, 21);
            checksum = Utils.getIntLE(b, FIELD_PAGE_CHECKSUM_POS, 25);
            int pageSegments = u(b[FIELD_PAGE_SEGMENTS_POS]);

            this.segmentTable = new byte[b.length - OGG_PAGE_HEADER_FIXED_LENGTH];
            int packetLength = 0;
            Integer segmentLength = null;
            for (int i = 0; i < segmentTable.length; i++)
            {
                segmentTable[i] = b[OGG_PAGE_HEADER_FIXED_LENGTH + i];
                segmentLength = u(segmentTable[i]);
                this.pageLength += segmentLength;
                packetLength += segmentLength;

                if (segmentLength < MAXIMUM_SEGMENT_SIZE)
                {
                    packetList.add(new PacketStartAndLength(pageLength - packetLength, packetLength));
                    packetLength = 0;
                }
            }

            //If last segment value is 255 this packet continues onto next page
            //and will not have been added to the packetStartAndEnd list yet
            if(segmentLength!=null)
            {
                if (segmentLength == MAXIMUM_SEGMENT_SIZE)
                {
                    packetList.add(new PacketStartAndLength(pageLength - packetLength, packetLength));
                    lastPacketIncomplete = true;
                }
            }
            isValid = true;
        }

        if(logger.isLoggable(Level.CONFIG))
        {
            logger.config("Constructed OggPage:" + this.toString());
        }
    }

    private int u(int i)
    {
        return i & 0xFF;
    }


    public boolean isLastPacketIncomplete()
    {
        return lastPacketIncomplete;
    }

    public double getAbsoluteGranulePosition()
    {
        logger.fine("Number Of Samples: " + absoluteGranulePosition);
        return this.absoluteGranulePosition;
    }


    public int getCheckSum()
    {
        return checksum;
    }


    public byte getHeaderType()
    {
        return headerTypeFlag;
    }


    public int getPageLength()
    {
        logger.fine("This page length: " + pageLength);
        return this.pageLength;
    }

    public int getPageSequence()
    {
        return pageSequenceNumber;
    }

    public int getSerialNumber()
    {
        return streamSerialNumber;
    }

    public byte[] getSegmentTable()
    {
        return this.segmentTable;
    }

    public boolean isValid()
    {
        return isValid;
    }


    public List<PacketStartAndLength> getPacketList()
    {
        return packetList;
    }


    public byte[] getRawHeaderData()
    {
        return rawHeaderData;
    }

    public String toString()
    {
        String out = "Ogg Page Header:isValid:" + isValid + ":type:" + headerTypeFlag + ":oggPageHeaderLength:" + rawHeaderData.length + ":length:" + pageLength + ":seqNo:" + getPageSequence() + ":packetIncomplete:" + isLastPacketIncomplete() + ":serNum:" + this.getSerialNumber();

        for (PacketStartAndLength packet : getPacketList())
        {
            out += packet.toString();
        }
        return out;
    }


    public long getStartByte()
    {
        return startByte;
    }

    public void setStartByte(long startByte)
    {
        this.startByte = startByte;
    }


    public static class PacketStartAndLength
    {
        private Integer startPosition = 0;
        private Integer length = 0;

        public PacketStartAndLength(int startPosition, int length)
        {
            this.startPosition = startPosition;
            this.length = length;
        }

        public int getStartPosition()
        {
            return startPosition;
        }

        public void setStartPosition(int startPosition)
        {
            this.startPosition = startPosition;
        }

        public int getLength()
        {
            return length;
        }

        public void setLength(int length)
        {
            this.length = length;
        }

        public String toString()
        {
            return "NextPkt(start:" + startPosition + ":length:" + length + "),";
        }
    }


    public static enum HeaderTypeFlag
    {
        FRESH_PACKET((byte) 0x0),
        CONTINUED_PACKET((byte) 0x1),
        START_OF_BITSTREAM((byte) 0x2),
        END_OF_BITSTREAM((byte) 0x4);

        byte fileValue;

        HeaderTypeFlag(byte fileValue)
        {
            this.fileValue = fileValue;
        }


        public byte getFileValue()
        {
            return fileValue;
        }
    }
}

