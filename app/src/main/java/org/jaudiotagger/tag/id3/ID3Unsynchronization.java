package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.mp3.MPEGFrameHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import java.util.logging.Level;


public class ID3Unsynchronization
{
    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.id3");



    public static boolean requiresUnsynchronization(byte[] abySource)
    {
        for (int i = 0; i < abySource.length - 1; i++)
        {
            if (((abySource[i] & MPEGFrameHeader.SYNC_BYTE1) == MPEGFrameHeader.SYNC_BYTE1) && ((abySource[i + 1] & MPEGFrameHeader.SYNC_BYTE2) == MPEGFrameHeader.SYNC_BYTE2))
            {
                if (logger.isLoggable(Level.FINEST))
                {
                    logger.finest("Unsynchronisation required found bit at:" + i);
                }
                return true;
            }
        }

        return false;
    }


    public static byte[] unsynchronize(byte[] abySource)
    {
        ByteArrayInputStream input = new ByteArrayInputStream(abySource);
        ByteArrayOutputStream output = new ByteArrayOutputStream(abySource.length);

        int count = 0;
        while (input.available() > 0)
        {
            int firstByte = input.read();
            count++;
            output.write(firstByte);
            if ((firstByte & MPEGFrameHeader.SYNC_BYTE1) == MPEGFrameHeader.SYNC_BYTE1)
            {
                // if byte is $FF, we must check the following byte if there is one
                if (input.available() > 0)
                {
                    input.mark(1);  // remember where we were, if we don'timer need to unsynchronize
                    int secondByte = input.read();
                    if ((secondByte & MPEGFrameHeader.SYNC_BYTE2) == MPEGFrameHeader.SYNC_BYTE2)
                    {
                        // we need to unsynchronize here
                        if (logger.isLoggable(Level.FINEST))
                        {
                            logger.finest("Writing unsynchronisation bit at:" + count);
                        }
                        output.write(0);

                    }
                    else if (secondByte == 0)
                    {
                        // we need to unsynchronize here
                        if (logger.isLoggable(Level.FINEST))
                        {
                            logger.finest("Inserting zero unsynchronisation bit at:" + count);
                        }
                        output.write(0);
                    }
                    input.reset();
                }
            }
        }
        // if we needed to unsynchronize anything, and this tag ends with 0xff, we have to append a zero byte,
        // which will be removed on de-unsynchronization later
        if ((abySource[abySource.length - 1] & MPEGFrameHeader.SYNC_BYTE1) == MPEGFrameHeader.SYNC_BYTE1)
        {
            logger.finest("Adding unsynchronisation bit at end of stream");
            output.write(0);
        }
        return output.toByteArray();
    }










    public static ByteBuffer synchronize(ByteBuffer source)
    {
        //long start = System.nanoTime();
        
        int len = source.remaining();
        byte[] bytes = new byte[len + 1]; // an extra byte saves a check later.
        source.get(bytes, 0, len);
        int from = 0, to = 0;
        boolean copy = true; // whether to copy the byte, if false, check the byte != 0.
        while (from < len)
        {
            byte byteValue = bytes[from++];
            if (copy || byteValue != 0) bytes[to++] = byteValue;
            copy = ((byteValue & MPEGFrameHeader.SYNC_BYTE1) != MPEGFrameHeader.SYNC_BYTE1);
        }

        ByteBuffer bb2 = ByteBuffer.wrap(bytes, 0, to);
        //long time = System.nanoTime() - start;
        //System.out.printf("Took %6.3f ms, was %d bytes, now %,d bytes%n", time/1e6, source.limit(), bb2.limit());
        return bb2;
    }

}
