
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioHeader;

import java.util.HashMap;
import java.util.Set;


public class GenericAudioHeader implements AudioHeader
{


    public final static String FIELD_BITRATE = "BITRATE";


    public final static String FIELD_CHANNEL = "CHANNB";


    public final static String FIELD_INFOS = "INFOS";


    public final static String FIELD_LENGTH = "LENGTH";


    public final static String FIELD_SAMPLERATE = "SAMPLING";
    

    public final static String FIELD_BITSPERSAMPLE = "BITSPERSAMPLE";


    public final static String FIELD_TYPE = "TYPE";


    public final static String FIELD_VBR = "VBR";


    private boolean isLossless = false;


    protected HashMap<String, Object> content;


    public GenericAudioHeader()
    {
        content = new HashMap<String, Object>(6);
        content.put(FIELD_BITRATE, -1);
        content.put(FIELD_CHANNEL, -1);
        content.put(FIELD_TYPE, "");
        content.put(FIELD_INFOS, "");
        content.put(FIELD_SAMPLERATE, -1);
        content.put(FIELD_BITSPERSAMPLE, -1);
        content.put(FIELD_LENGTH, (float) -1);
        content.put(FIELD_VBR, true);
    }

    public String getBitRate()
    {
        return content.get(FIELD_BITRATE).toString();
    }



    public long getBitRateAsNumber()
    {
        return ((Integer) content.get(FIELD_BITRATE)).longValue();
    }


    public int getChannelNumber()
    {
        return (Integer) content.get(FIELD_CHANNEL);
    }


    public String getChannels()
    {
        return String.valueOf(getChannelNumber());
    }


    public String getEncodingType()
    {
        return (String) content.get(FIELD_TYPE);
    }


    public String getFormat()
    {
        return (String) content.get(FIELD_TYPE);
    }


    public String getExtraEncodingInfos()
    {
        return (String) content.get(FIELD_INFOS);
    }


    public int getTrackLength()
    {
        return (int) getPreciseLength();
    }


    public float getPreciseLength()
    {
        return (Float) content.get(FIELD_LENGTH);
    }


    public String getSampleRate()
    {
        return content.get(FIELD_SAMPLERATE).toString();
    }

    public int getSampleRateAsNumber()
    {
        return (Integer) content.get(FIELD_SAMPLERATE);
    }
    

    public int getBitsPerSample()
    {
    	return (Integer) content.get(FIELD_BITSPERSAMPLE);
    }


    public boolean isVariableBitRate()
    {
        return (Boolean) content.get(FIELD_VBR);
    }


    public boolean isLossless()
    {
        return isLossless;
    }


    public void setBitrate(int bitrate)
    {
        content.put(FIELD_BITRATE, bitrate);
    }


    public void setChannelNumber(int chanNb)
    {
        content.put(FIELD_CHANNEL, chanNb);
    }


    public void setEncodingType(String encodingType)
    {
        content.put(FIELD_TYPE, encodingType);
    }


    public void setExtraEncodingInfos(String infos)
    {
        content.put(FIELD_INFOS, infos);
    }


    public void setLength(int length)
    {
        content.put(FIELD_LENGTH, (float) length);
    }


    public void setPreciseLength(float seconds)
    {
        content.put(FIELD_LENGTH, seconds);
    }


    public void setSamplingRate(int samplingRate)
    {
        content.put(FIELD_SAMPLERATE, samplingRate);
    }
    

    public void setBitsPerSample(int bitsPerSample)
    {
    	content.put(FIELD_BITSPERSAMPLE, bitsPerSample);
    }


    public void setVariableBitRate(boolean b)
    {
        content.put(FIELD_VBR, b);
    }


    public void setLossless(boolean b)
    {
        isLossless = b;
    }


    public void setExtra(String key, Object value)
    {
        content.put(key, value);
    }


    public String toString()
    {
        StringBuffer out = new StringBuffer(50);
        out.append("Encoding infos content:\n");
        Set<String> set = content.keySet();
        for (String key : set)
        {
            Object val = content.get(key);
            out.append("\t");
            out.append(key);
            out.append(" : ");
            out.append(val);
            out.append("\n");
        }
        return out.toString().substring(0, out.length() - 1);
    }
}
