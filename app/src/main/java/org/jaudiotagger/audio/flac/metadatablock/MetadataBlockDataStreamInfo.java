
package org.jaudiotagger.audio.flac.metadatablock;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.logging.Logger;


public class MetadataBlockDataStreamInfo  implements MetadataBlockData
{
    public static final int STREAM_INFO_DATA_LENGTH = 34;

    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.flac.MetadataBlockDataStreamInfo");

    private int minBlockSize, maxBlockSize, minFrameSize, maxFrameSize, samplingRate, samplingRatePerChannel, bitsPerSample, channelNumber, totalNumberOfSamples;
    private float songLength;
    private String md5;
    private boolean isValid = true;

    private ByteBuffer rawdata;

    public MetadataBlockDataStreamInfo(MetadataBlockHeader header, RandomAccessFile raf) throws IOException
    {
        rawdata = ByteBuffer.allocate(header.getDataLength());
        int bytesRead = raf.getChannel().read(rawdata);
        if (bytesRead < header.getDataLength())
        {
            throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + header.getDataLength());
        }
        rawdata.rewind();

        minBlockSize = rawdata.getShort();
        maxBlockSize = rawdata.getShort();
        minFrameSize = readThreeByteInteger(rawdata.get(), rawdata.get(), rawdata.get());
        maxFrameSize = readThreeByteInteger(rawdata.get(), rawdata.get(), rawdata.get());

        samplingRate = readSamplingRate(rawdata.get(), rawdata.get(), rawdata.get());
        channelNumber = ((u(rawdata.get(12)) & 0x0E) >>> 1) + 1;
        samplingRatePerChannel = samplingRate / channelNumber;
        bitsPerSample = ((u(rawdata.get(12)) & 0x01) << 4) + ((u(rawdata.get(13)) & 0xF0) >>> 4) + 1;

        totalNumberOfSamples = readTotalNumberOfSamples(rawdata.get(13), rawdata.get(14), rawdata.get(15), rawdata.get(16), rawdata.get(17));

        StringBuilder sb = new StringBuilder();
        for(int i=18;i<34;i++) 
        { 
            byte dataByte = rawdata.get(i); 
            sb.append(String.format("%x",dataByte)); 
        }
        md5 = sb.toString();
        
        songLength = (float) ((double) totalNumberOfSamples / samplingRate);
        logger.config(this.toString());
    }


    public byte[] getBytes()
    {
        return rawdata.array();
    }

    public int getLength()
    {
        return getBytes().length;
    }

    

    public String toString()
    {

        return "MinBlockSize:" + minBlockSize + "MaxBlockSize:" + maxBlockSize + "MinFrameSize:" + minFrameSize + "MaxFrameSize:" + maxFrameSize + "SampleRateTotal:" + samplingRate + "SampleRatePerChannel:" + samplingRatePerChannel + ":Channel number:" + channelNumber + ":Bits per sample: " + bitsPerSample + ":TotalNumberOfSamples: " + totalNumberOfSamples + ":Length: " + songLength;

    }

    public int getSongLength()
    {
        return (int) songLength;
    }

    public float getPreciseLength()
    {
        return songLength;
    }

    public int getChannelNumber()
    {
        return channelNumber;
    }

    public int getSamplingRate()
    {
        return samplingRate;
    }

    public int getSamplingRatePerChannel()
    {
        return samplingRatePerChannel;
    }

    public String getEncodingType()
    {
        return "FLAC " + bitsPerSample + " bits";
    }
    
    public int getBitsPerSample()
    {
    	return bitsPerSample;
    }

    public String getMD5Signature()
    {
        return md5;
    }

    public boolean isValid()
    {
        return isValid;
    }

    private int readThreeByteInteger(byte b1, byte b2, byte b3)
    {
        int rate = (u(b1) << 16) + (u(b2) << 8) + (u(b3));
        return rate;
    }

    //TODO this code seems to be give a sampling rate over 21 bytes instead of 20 bytes but attempt to change
    //to 21 bytes give wrong value
    private int readSamplingRate(byte b1, byte b2, byte b3)
    {
        int rate = (u(b1) << 12) + (u(b2) << 4) + ((u(b3) & 0xF0) >>> 4);
        return rate;

    }

    private int readTotalNumberOfSamples(byte b1, byte b2, byte b3, byte b4, byte b5)
    {
        int nb = u(b5);
        nb += u(b4) << 8;
        nb += u(b3) << 16;
        nb += u(b2) << 24;
        nb += (u(b1) & 0x0F) << 32;
        return nb;
    }

    private int u(int i)
    {
        return i & 0xFF;
    }
}
