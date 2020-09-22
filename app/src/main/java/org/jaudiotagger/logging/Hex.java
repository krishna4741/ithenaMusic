package org.jaudiotagger.logging;


public class Hex
{

    public static String asHex(long value)
    {
        return "0x" + Long.toHexString(value);
    }


    public static String asHex(byte value)
    {
        return "0x" + Integer.toHexString(value);
    }
}
