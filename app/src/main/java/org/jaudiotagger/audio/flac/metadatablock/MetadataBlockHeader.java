
package org.jaudiotagger.audio.flac.metadatablock;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.logging.ErrorMessage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class MetadataBlockHeader
{
    public static final int BLOCK_TYPE_LENGTH   = 1;
    public static final int BLOCK_LENGTH        = 3;
    public static final int HEADER_LENGTH       = BLOCK_TYPE_LENGTH + BLOCK_LENGTH;

    private boolean isLastBlock;
    private int dataLength;
    private byte[] bytes;
    private BlockType blockType;


    public static MetadataBlockHeader readHeader(RandomAccessFile raf) throws CannotReadException, IOException
    {
        ByteBuffer rawdata = ByteBuffer.allocate(HEADER_LENGTH);
        int bytesRead = raf.getChannel().read(rawdata);
        if (bytesRead < HEADER_LENGTH)
        {
            throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + HEADER_LENGTH);
        }
        rawdata.rewind();
        return new MetadataBlockHeader(rawdata);
    }

    public String toString()
    {
        return "BlockType:"+blockType + " DataLength:"+dataLength + " isLastBlock:"+isLastBlock;
    }


    public MetadataBlockHeader(ByteBuffer rawdata) throws CannotReadException
    {
        isLastBlock = ((rawdata.get(0) & 0x80) >>> 7) == 1;

        int type = rawdata.get(0) & 0x7F;
        if (type < BlockType.values().length)
        {
            blockType = BlockType.values()[type];
            dataLength = (u(rawdata.get(1)) << 16) + (u(rawdata.get(2)) << 8) + (u(rawdata.get(3)));

            bytes = new byte[HEADER_LENGTH];
            for (int i = 0; i < HEADER_LENGTH; i++)
            {
                bytes[i] = rawdata.get(i);
            }
        }
        else
        {
            throw new CannotReadException(ErrorMessage.FLAC_NO_BLOCKTYPE.getMsg(type));
        }
    }


    public MetadataBlockHeader(boolean isLastBlock, BlockType blockType, int dataLength)
    {
        ByteBuffer rawdata = ByteBuffer.allocate(HEADER_LENGTH);
        this.blockType = blockType;
        this.isLastBlock = isLastBlock;
        this.dataLength = dataLength;

        byte type;
        if (isLastBlock)
        {
            type = (byte) (0x80 | blockType.getId());
        }
        else
        {
            type = (byte) blockType.getId();
        }
        rawdata.put(type);

        //Size is 3Byte BigEndian int
        rawdata.put((byte) ((dataLength & 0xFF0000) >>> 16));
        rawdata.put((byte) ((dataLength & 0xFF00) >>> 8));
        rawdata.put((byte) (dataLength & 0xFF));

        bytes = new byte[HEADER_LENGTH];
        for (int i = 0; i < HEADER_LENGTH; i++)
        {
            bytes[i] = rawdata.get(i);
        }
    }

    private int u(int i)
    {
        return i & 0xFF;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public BlockType getBlockType()
    {
        return blockType;
    }

    public boolean isLastBlock()
    {
        return isLastBlock;
    }

    public byte[] getBytesWithoutIsLastBlockFlag()
    {
        bytes[0] = (byte) (bytes[0] & 0x7F);
        return bytes;
    }

    public byte[] getBytes()
    {
        return bytes;
    }
}
