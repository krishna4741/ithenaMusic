package org.jaudiotagger.tag.id3;

import java.nio.ByteBuffer;


public class ID3SyncSafeInteger
{
    public static final int INTEGRAL_SIZE = 4;


    public static final int MAX_SAFE_SIZE = 127;


    private static int bufferToValue(byte[] buffer)
    {
        //Note Need to && with 0xff otherwise if value is greater than 128 we get a negative number
        //when cast byte to int
        return ((buffer[0] & 0xff) << 21) + ((buffer[1] & 0xff) << 14) + ((buffer[2] & 0xff) << 7) + ((buffer[3]) & 0xff);
    }


    protected static int bufferToValue(ByteBuffer buffer)
    {
        byte byteBuffer[] = new byte[INTEGRAL_SIZE];
        buffer.get(byteBuffer, 0, INTEGRAL_SIZE);
        return bufferToValue(byteBuffer);
    }


    protected static boolean isBufferNotSyncSafe(ByteBuffer buffer)
    {
        int position = buffer.position();

        //Check Bit7 not set
        for (int i = 0; i < INTEGRAL_SIZE; i++)
        {
            byte nextByte = buffer.get(position + i);
            if ((nextByte & 0x80) > 0)
            {
                return true;
            }
        }
        return false;
    }


    protected static boolean isBufferEmpty(byte[] buffer)
    {
        for (byte aBuffer : buffer)
        {
            if (aBuffer != 0)
            {
                return false;
            }
        }
        return true;
    }


    protected static byte[] valueToBuffer(int size)
    {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) ((size & 0x0FE00000) >> 21);
        buffer[1] = (byte) ((size & 0x001FC000) >> 14);
        buffer[2] = (byte) ((size & 0x00003F80) >> 7);
        buffer[3] = (byte) (size & 0x0000007F);
        return buffer;
    }
}
