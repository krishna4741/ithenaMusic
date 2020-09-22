
package org.jaudiotagger.audio.wav.util;

public class WavRIFFHeader
{

    private boolean isValid = false;

    public WavRIFFHeader(byte[] b)
    {
        //System.err.println(b.length);
        String RIFF = new String(b, 0, 4);
        //System.err.println(RIFF);
        String WAVE = new String(b, 8, 4);
        //System.err.println(WAVE);
        if (RIFF.equals("RIFF") && WAVE.equals("WAVE"))
        {
            isValid = true;
        }

    }

    public boolean isValid()
    {
        return isValid;
    }

    public String toString()
    {
        String out = "RIFF-WAVE Header:\n";
        out += "Is valid?: " + isValid;
        return out;
    }
}