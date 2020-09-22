
package org.jaudiotagger.audio.ogg.util;

import java.util.logging.Logger;



public class OggCRCFactory
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.ogg");

    private static long[] crc_lookup = new long[256];
    private static boolean init = false;


    public static void init()
    {
        for (int i = 0; i < 256; i++)
        {
            long r = i << 24;

            for (int j = 0; j < 8; j++)
            {
                if ((r & 0x80000000L) != 0)
                {
                    r = (r << 1) ^ 0x04c11db7L;
                }
                else
                {
                    r <<= 1;
                }
            }

            crc_lookup[i] = (r);
        }
        init = true;
    }


    public boolean checkCRC(byte[] data, byte[] crc)
    {
        return new String(crc).equals(new String(computeCRC(data)));
    }

    public static byte[] computeCRC(byte[] data)
    {

        if (!init)
        {
            init();
        }

        long crc_reg = 0;

        for (byte aData : data)
        {
            int tmp = (int) (((crc_reg >>> 24) & 0xff) ^ u(aData));

            crc_reg = (crc_reg << 8) ^ crc_lookup[tmp];
            crc_reg &= 0xffffffff;
        }

        byte[] sum = new byte[4];

        sum[0] = (byte) (crc_reg & 0xffL);
        sum[1] = (byte) ((crc_reg >>> 8) & 0xffL);
        sum[2] = (byte) ((crc_reg >>> 16) & 0xffL);
        sum[3] = (byte) ((crc_reg >>> 24) & 0xffL);

        return sum;
    }


    private static int u(int n)
    {
        return n & 0xff;
    }
}

