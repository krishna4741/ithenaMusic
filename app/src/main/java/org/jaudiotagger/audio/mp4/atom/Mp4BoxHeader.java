
package org.jaudiotagger.audio.mp4.atom;

import org.jaudiotagger.audio.exceptions.InvalidBoxHeaderException;
import org.jaudiotagger.audio.exceptions.NullBoxIdException;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.logging.ErrorMessage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;


public class Mp4BoxHeader
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.mp4.atom");

    public static final int OFFSET_POS = 0;
    public static final int IDENTIFIER_POS = 4;
    public static final int OFFSET_LENGTH = 4;
    public static final int IDENTIFIER_LENGTH = 4;
    public static final int HEADER_LENGTH = OFFSET_LENGTH + IDENTIFIER_LENGTH;

    //Box identifier
    private String id;

    //Box length
    protected int length;

    //If reading from file , this can be used to hold the headers position in the file
    private long filePos;

    //Raw Header data
    protected ByteBuffer dataBuffer;

    //Mp4 uses UTF-8 for all text
    public static final String CHARSET_UTF_8 = "UTF-8";


    public Mp4BoxHeader()
    {

    }


    public Mp4BoxHeader(String id)
    {
        if(id.length()!=IDENTIFIER_LENGTH)
        {
            throw new RuntimeException("Invalid length:atom idenifier should always be 4 characters long");
        }
        dataBuffer = ByteBuffer.allocate(HEADER_LENGTH);
        try
        {
            this.id    = id;
            dataBuffer.put(4, id.getBytes("ISO-8859-1")[0]);
            dataBuffer.put(5, id.getBytes("ISO-8859-1")[1]);
            dataBuffer.put(6, id.getBytes("ISO-8859-1")[2]);
            dataBuffer.put(7, id.getBytes("ISO-8859-1")[3]);
        }
        catch(UnsupportedEncodingException uee)
        {
            //Should never happen
            throw new RuntimeException(uee);
        }
    }


    public Mp4BoxHeader(ByteBuffer headerData)
    {
        update(headerData);
    }


    public void update(ByteBuffer headerData)
    {
        //Read header data into byte array
        byte[] b = new byte[HEADER_LENGTH];
        headerData.get(b);
        //Keep reference to copy of RawData
        dataBuffer = ByteBuffer.wrap(b);

        //Calculate box size
        this.length = Utils.getIntBE(b, OFFSET_POS, OFFSET_LENGTH - 1);
        //Calculate box id
        this.id = Utils.getString(b, IDENTIFIER_POS, IDENTIFIER_LENGTH, "ISO-8859-1");

        logger.finest("Mp4BoxHeader id:"+id+":length:"+length);
        if (id.equals("\0\0\0\0"))
        {
            throw new NullBoxIdException(ErrorMessage.MP4_UNABLE_TO_FIND_NEXT_ATOM_BECAUSE_IDENTIFIER_IS_INVALID.getMsg(id));
        }

        if(length<HEADER_LENGTH)
        {
            throw new InvalidBoxHeaderException(ErrorMessage.MP4_UNABLE_TO_FIND_NEXT_ATOM_BECAUSE_IDENTIFIER_IS_INVALID.getMsg(id,length));
        }
    }


    public String getId()
    {
        return id;
    }


    public int getLength()
    {
        return length;
    }


    public void setLength(int length)
    {
        byte[] headerSize = Utils.getSizeBEInt32(length);
        dataBuffer.put(0, headerSize[0]);
        dataBuffer.put(1, headerSize[1]);
        dataBuffer.put(2, headerSize[2]);
        dataBuffer.put(3, headerSize[3]);

        this.length = length;

    }


    public void setId(int length)
    {
        byte[] headerSize = Utils.getSizeBEInt32(length);
        dataBuffer.put(5, headerSize[0]);
        dataBuffer.put(6, headerSize[1]);
        dataBuffer.put(7, headerSize[2]);
        dataBuffer.put(8, headerSize[3]);

        this.length = length;

    }


    public ByteBuffer getHeaderData()
    {
        dataBuffer.rewind();
        return dataBuffer;
    }


    public int getDataLength()
    {
        return length - HEADER_LENGTH;
    }

    public String toString()
    {
        return "Box " + id + ":length" + length + ":filepos:" + filePos;
    }


    public String getEncoding()
    {
        return CHARSET_UTF_8;
    }



    public static Mp4BoxHeader seekWithinLevel(RandomAccessFile raf, String id) throws IOException
    {
        logger.finer("Started searching for:" + id + " in file at:" + raf.getChannel().position());

        Mp4BoxHeader boxHeader = new Mp4BoxHeader();
        ByteBuffer headerBuffer = ByteBuffer.allocate(HEADER_LENGTH);
        int bytesRead = raf.getChannel().read(headerBuffer);
        if (bytesRead != HEADER_LENGTH)
        {
            return null;
        }
        headerBuffer.rewind();
        boxHeader.update(headerBuffer);
        while (!boxHeader.getId().equals(id))
        {
            logger.finer("Found:" + boxHeader.getId() + " Still searching for:" + id + " in file at:" + raf.getChannel().position());

            //Something gone wrong probably not at the start of an atom so return null;
            if (boxHeader.getLength() < Mp4BoxHeader.HEADER_LENGTH)
            {
                return null;
            }
            int noOfBytesSkipped = raf.skipBytes(boxHeader.getDataLength());
            logger.finer("Skipped:" + noOfBytesSkipped);
            if (noOfBytesSkipped < boxHeader.getDataLength())
            {
                return null;
            }
            headerBuffer.rewind();
            bytesRead = raf.getChannel().read(headerBuffer);
            logger.finer("Header Bytes Read:" + bytesRead);
            headerBuffer.rewind();
            if (bytesRead == Mp4BoxHeader.HEADER_LENGTH)
            {
                boxHeader.update(headerBuffer);
            }
            else
            {
                return null;
            }
        }
        return boxHeader;
    }



    public static Mp4BoxHeader seekWithinLevel(ByteBuffer data, String id) throws IOException
    {
        logger.finer("Started searching for:" + id + " in bytebuffer at" + data.position());

        Mp4BoxHeader boxHeader = new Mp4BoxHeader();
        if (data.remaining() >= Mp4BoxHeader.HEADER_LENGTH)
        {
            boxHeader.update(data);
        }
        else
        {
            return null;
        }
        while (!boxHeader.getId().equals(id))
        {
            logger.finer("Found:" + boxHeader.getId() + " Still searching for:" + id + " in bytebuffer at" + data.position());
            //Something gone wrong probably not at the start of an atom so return null;
            if (boxHeader.getLength() < Mp4BoxHeader.HEADER_LENGTH)
            {
                return null;
            }
            if(data.remaining()<(boxHeader.getLength() - HEADER_LENGTH))
            {
                //i.e Could happen if Moov header had size incorrectly recorded
                return null;    
            }
            data.position(data.position() + (boxHeader.getLength() - HEADER_LENGTH));
            if (data.remaining() >= Mp4BoxHeader.HEADER_LENGTH)
            {
                boxHeader.update(data);
            }
            else
            {
                return null;
            }
        }
        logger.finer("Found:" + id + " in bytebuffer at" + data.position());

        return boxHeader;
    }


    public long getFilePos()
    {
        return filePos;
    }


    public void setFilePos(long filePos)
    {
        this.filePos = filePos;
    }
}
