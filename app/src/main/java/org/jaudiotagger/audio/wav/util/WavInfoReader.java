
package org.jaudiotagger.audio.wav.util;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WavInfoReader
{
    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        // Reads wav header----------------------------------------
        GenericAudioHeader info = new GenericAudioHeader();

        if (raf.length() < 12)
        {
            throw new CannotReadException("This is not a WAV File (<12 bytes)");
        }
        byte[] b = new byte[12];
        raf.read(b);

        WavRIFFHeader wh = new WavRIFFHeader(b);
        if (wh.isValid())
        {
            b = new byte[34];
            raf.read(b);

            WavFormatHeader wfh = new WavFormatHeader(b);
            if (wfh.isValid())
            {
                // Populates
                // encodingInfo----------------------------------------------------
                info.setPreciseLength(((float) raf.length() - (float) 36) / wfh.getBytesPerSecond());
                info.setChannelNumber(wfh.getChannelNumber());
                info.setSamplingRate(wfh.getSamplingRate());
                info.setBitsPerSample(wfh.getBitsPerSample());
                info.setEncodingType("WAV-RIFF " + wfh.getBitsPerSample() + " bits");
                info.setExtraEncodingInfos("");
                info.setBitrate(wfh.getBytesPerSecond() * 8 / 1000);
                info.setVariableBitRate(false);
            }
            else
            {
                throw new CannotReadException("Wav Format Header not valid");
            }
        }
        else
        {
            throw new CannotReadException("Wav RIFF Header not valid");
        }

        return info;
    }
}
