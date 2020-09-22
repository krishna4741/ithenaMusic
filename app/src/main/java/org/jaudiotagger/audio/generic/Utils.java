
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Utils
{

     // Logger Object
    public static Logger logger = Logger
             .getLogger("org.jaudiotagger.audio.generic.utils");
    private static final int MAX_BASE_TEMP_FILENAME_LENGTH = 20;


    public static void copy(byte[] src, byte[] dst, int dstOffset)
    {
        System.arraycopy(src, 0, dst, dstOffset, src.length);
    }



    public static byte[] getDefaultBytes(String s, String charSet)
    {
        try
        {
            return s.getBytes(charSet);
        }
        catch (UnsupportedEncodingException uee)
        {
            throw new RuntimeException(uee);
        }

    }


    public static String getExtension(File f)
    {
        String name = f.getName().toLowerCase();
        int i = name.lastIndexOf(".");
        if (i == -1)
        {
            return "";
        }

        return name.substring(i + 1);
    }



    public static long getLongLE(ByteBuffer b, int start, int end)
    {
        long number = 0;
        for (int i = 0; i < (end - start + 1); i++)
        {
            number += ((b.get(start + i) & 0xFF) << i * 8);
        }

        return number;
    }


    public static long getLongBE(ByteBuffer b, int start, int end)
    {
        long number = 0;
        for (int i = 0; i < (end - start + 1); i++)
        {
            number += ((long)((b.get(end - i) & 0xFF)) << i * 8);
        }

        return number;
    }


    public static int getIntLE(byte[] b)
    {
        return (int) getLongLE(ByteBuffer.wrap(b), 0, b.length - 1);
    }


    public static int getIntLE(byte[] b, int start, int end)
    {
        return (int) getLongLE(ByteBuffer.wrap(b), start, end);
    }


    public static int getIntBE(byte[] b, int start, int end)
    {
        return (int) getLongBE(ByteBuffer.wrap(b), start, end);
    }


    public static int getIntBE(ByteBuffer b, int start, int end)
    {
        return (int) getLongBE(b, start, end);
    }


    public static short getShortBE(ByteBuffer b, int start, int end)
    {
        return (short) getIntBE(b, start, end);
    }


    public static byte[] getSizeBEInt32(int size)
    {
        byte[] b = new byte[4];
        b[0] = (byte) ((size >> 24) & 0xFF);
        b[1] = (byte) ((size >> 16) & 0xFF);
        b[2] = (byte) ((size >> 8) & 0xFF);
        b[3] = (byte) (size & 0xFF);
        return b;
    }


    public static byte[] getSizeBEInt16(short size)
    {
        byte[] b = new byte[2];
        b[0] = (byte) ((size >> 8) & 0xFF);
        b[1] = (byte) (size & 0xFF);
        return b;
    }



    public static byte[] getSizeLEInt32(int size)
    {
        byte[] b = new byte[4];
        b[0] = (byte) (size & 0xff);
        b[1] = (byte) ((size >>> 8) & 0xffL);
        b[2] = (byte) ((size >>> 16) & 0xffL);
        b[3] = (byte) ((size >>> 24) & 0xffL);
        return b;
    }


    public static String getString(byte[] b, int offset, int length, String encoding)
    {
        try
        {
            return new String(b, offset, length, encoding);
        }
        catch (UnsupportedEncodingException ue)
        {
            //Shouldnt have to worry about this exception as should only be calling with well defined charsets
            throw new RuntimeException(ue);
        }
    }


    public static String getString(ByteBuffer buffer, int offset, int length, String encoding)
    {
        byte[] b = new byte[length];
        buffer.position(buffer.position() + offset);
        buffer.get(b);
        try
        {
            return new String(b, 0, length, encoding);
        }
        catch (UnsupportedEncodingException uee)
        {
            //TODO, will we ever use unsupported encodings
            throw new RuntimeException(uee);
        }
    }


    public static byte[] getUTF8Bytes(String s) throws UnsupportedEncodingException
    {
        return s.getBytes("UTF-8");
    }


    public static int readUint32AsInt(DataInput di) throws IOException
    {
        final long l = readUint32(di);
        if (l > Integer.MAX_VALUE)
        {
            throw new IOException("uint32 value read overflows int");
        }
        return (int) l;
    }


    public static long readUint32(DataInput di) throws IOException
    {
        final byte[] buf8 = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        di.readFully(buf8, 4, 4);
        final long l = ByteBuffer.wrap(buf8).getLong();
        return l;
    }


    public static int readUint16(DataInput di) throws IOException
    {
        final byte[] buf = {0x00, 0x00, 0x00, 0x00};
        di.readFully(buf, 2, 2);
        final int i = ByteBuffer.wrap(buf).getInt();
        return i;
    }


    public static int readInt16(DataInput di) throws IOException
    {
        final byte[] buf = {0x00, 0x00};
        di.readFully(buf, 0, 2);
        final int i = ByteBuffer.wrap(buf).getShort();
        return i;
    }


    public static String readString(DataInput di, int charsToRead) throws IOException
    {
        final byte[] buf = new byte[charsToRead];
        di.readFully(buf);
        return new String(buf);
    }


    public static long readUInt64(ByteBuffer b)
    {
        long result = 0;
        result += (readUBEInt32(b) << 32);
        result += readUBEInt32(b);
        return result;
    }


    public static int readUBEInt32(ByteBuffer b)
    {
        int result = 0;
        result += readUBEInt16(b) << 16;
        result += readUBEInt16(b);
        return result;
    }


    public static int readUBEInt24(ByteBuffer b)
    {
        int result = 0;
        result += readUBEInt16(b) << 16;
        result += readUInt8(b);
        return result;
    }


    public static int readUBEInt16(ByteBuffer b)
    {
        int result = 0;
        result += readUInt8(b) << 8;
        result += readUInt8(b);
        return result;
    }


    public static int readUInt8(ByteBuffer b)
    {
        return read(b);
    }


    public static int read(ByteBuffer b)
    {
        int result = (b.get() & 0xFF);
        return result;
    }


    public static String getBaseFilenameForTempFile(File file)
    {
        String filename = getMinBaseFilenameAllowedForTempFile(file);
        if(filename.length()<= MAX_BASE_TEMP_FILENAME_LENGTH)
        {
           return filename;
        }
        return filename.substring(0,MAX_BASE_TEMP_FILENAME_LENGTH);
    }


    public static String getMinBaseFilenameAllowedForTempFile(File file)
    {
        String s = AudioFile.getBaseFilename(file);
        if (s.length() >= 3)
        {
            return s;
        }
        if (s.length() == 1)
        {
            return s + "000";
        }
        else if (s.length() == 1)
        {
            return s + "00";
        }
        else if (s.length() == 2)
        {
            return s + "0";
        }
        return s;
    }


    public static boolean rename(File fromFile, File toFile)
    {
        logger.log(Level.CONFIG,"Renaming From:"+fromFile.getAbsolutePath() + " to "+toFile.getAbsolutePath());

        if(toFile.exists())
        {
            logger.log(Level.SEVERE,"Destination File:"+toFile + " already exists");
            return false;
        }

        //Rename File, could fail because being  used or because trying to rename over filesystems
        final boolean result = fromFile.renameTo(toFile);
        if (!result)
        {
            // Might be trying to rename over filesystem, so try copy and delete instead
            if (copy(fromFile, toFile))
            {
                //If copy works but deletion of original file fails then it is because the file is being used
                //so we need to delete the file we have just created
                boolean deleteResult=fromFile.delete();
                if(!deleteResult)
                {
                    logger.log(Level.SEVERE,"Unable to delete File:"+fromFile);
                    toFile.delete();
                    return false;
                }
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }


    public static boolean copy(File fromFile, File toFile)
    {
        try
        {
            FileInputStream in = new FileInputStream(fromFile);
            FileOutputStream out = new FileOutputStream(toFile);
            byte[] buf = new byte[8192];

            int len;

            while ((len = in.read(buf)) > -1)
            {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();

            // cleanup if files are not the same length
            if (fromFile.length() != toFile.length())
            {
                toFile.delete();

                return false;
            }

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
