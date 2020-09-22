package org.jaudiotagger.audio;


public interface AudioHeader
{

    public abstract String getEncodingType();


    public String getBitRate();


    public long getBitRateAsNumber();



    public String getSampleRate();


    public int getSampleRateAsNumber();


    public String getFormat();


    public String getChannels();


    public boolean isVariableBitRate();


    public int getTrackLength();
    

    public int getBitsPerSample();


    public boolean isLossless();
}
