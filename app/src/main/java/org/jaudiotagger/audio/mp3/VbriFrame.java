package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class VbriFrame
{

    //The offset into frame
    private static final int VBRI_OFFSET = MPEGFrameHeader.HEADER_SIZE + 32;

    private static final int VBRI_HEADER_BUFFER_SIZE = 120; //TODO this is just a guess, not right
    private static final int VBRI_IDENTIFIER_BUFFER_SIZE = 4;
    private static final int VBRI_DELAY_BUFFER_SIZE = 2;
    private static final int VBRI_QUALITY_BUFFER_SIZE = 2;
    private static final int VBRI_AUDIOSIZE_BUFFER_SIZE = 4;
    private static final int VBRI_FRAMECOUNT_BUFFER_SIZE = 4;
    private static final int VBRI_TOC_ENTRY_BUFFER_SIZE = 2;

    public static final int MAX_BUFFER_SIZE_NEEDED_TO_READ_VBRI = VBRI_OFFSET + VBRI_HEADER_BUFFER_SIZE;

    private static final int BYTE_1 = 0;
    private static final int BYTE_2 = 1;
    private static final int BYTE_3 = 2;
    private static final int BYTE_4 = 3;


    private static final byte[] VBRI_VBR_ID = {'V', 'B', 'R', 'I'};

    private ByteBuffer header;

    private boolean vbr = false;
    private int frameCount = -1;
    private int audioSize = -1;
    private LameFrame lameFrame;


    private VbriFrame(ByteBuffer header)
    {
        this.header=header;
        //Go to start of Buffer
        header.rewind();
        header.position(10);
        setAudioSize();
        setFrameCount();
    }


    private void setAudioSize()
    {
        byte frameSizeBuffer[] = new byte[VBRI_AUDIOSIZE_BUFFER_SIZE];
        header.get(frameSizeBuffer);
        boolean audioSizeEnabled = true;
        audioSize = (frameSizeBuffer[BYTE_1] << 24) & 0xFF000000 | (frameSizeBuffer[BYTE_2] << 16) & 0x00FF0000 | (frameSizeBuffer[BYTE_3] << 8) & 0x0000FF00 | frameSizeBuffer[BYTE_4] & 0x000000FF;
    }


    private void setFrameCount()
    {
        byte frameCountBuffer[] = new byte[VBRI_FRAMECOUNT_BUFFER_SIZE];
        header.get(frameCountBuffer);
        boolean frameCountEnabled = true;
        frameCount = (frameCountBuffer[BYTE_1] << 24) & 0xFF000000 | (frameCountBuffer[BYTE_2] << 16) & 0x00FF0000 | (frameCountBuffer[BYTE_3] << 8) & 0x0000FF00 | frameCountBuffer[BYTE_4] & 0x000000FF;
    }



    public final int getFrameCount()
    {
        return frameCount;
    }


    public final int getAudioSize()
    {
        return audioSize;
    }


    public static VbriFrame parseVBRIFrame(ByteBuffer header) throws InvalidAudioFrameException
    {
        VbriFrame VBRIFrame = new VbriFrame(header);
        return VBRIFrame;
    }


    public static ByteBuffer isVbriFrame(ByteBuffer bb, MPEGFrameHeader mpegFrameHeader)
    {

        //We store this so can return here after scanning through buffer
        int startPosition = bb.position();
        MP3File.logger.finest("Checking VBRI Frame at" + startPosition);

        bb.position(startPosition + VBRI_OFFSET);

        //Create header from here
        ByteBuffer header = bb.slice();

        // Return Buffer to start Point
        bb.position(startPosition);

        //Check Identifier
        byte[] identifier = new byte[VBRI_IDENTIFIER_BUFFER_SIZE];
        header.get(identifier);
        if ((!Arrays.equals(identifier, VBRI_VBR_ID)))
        {
            return null;
        }
        MP3File.logger.finest("Found VBRI Frame");
        return header;
    }


    public final boolean isVbr()
    {
        return true;
    }

    public String getEncoder()
    {
        return "Fraunhofer";
    }


    public String toString()
    {
        return "VBRIheader" + " vbr:" + vbr + " frameCount:" + frameCount + " audioFileSize:" + audioSize + " encoder:" + getEncoder();
    }
}
