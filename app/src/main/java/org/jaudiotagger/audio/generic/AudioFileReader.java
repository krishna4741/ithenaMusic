
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.logging.ErrorMessage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;
import java.util.logging.Level;



public abstract class AudioFileReader
{

    // Logger Object
      public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.generic");
    private static final int MINIMUM_SIZE_FOR_VALID_AUDIO_FILE = 150;


    protected abstract GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException;


    protected abstract Tag getTag(RandomAccessFile raf) throws CannotReadException, IOException;


    public AudioFile read(File f) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
        if(logger.isLoggable(Level.CONFIG))
        {
            logger.config(ErrorMessage.GENERAL_READ.getMsg(f.getAbsolutePath()));
        }

        if (!f.canRead())
        {
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_FILE_TOO_SMALL.getMsg(f.getAbsolutePath()));
        }

        if (f.length() <= MINIMUM_SIZE_FOR_VALID_AUDIO_FILE)
        {
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_FILE_TOO_SMALL.getMsg(f.getAbsolutePath()));
        }

        RandomAccessFile raf = null;
        try
        {
            raf = new RandomAccessFile(f, "r");
            raf.seek(0);

            GenericAudioHeader info = getEncodingInfo(raf);
            raf.seek(0);
            Tag tag = getTag(raf);
            return new AudioFile(f, info, tag);

        }
        catch (CannotReadException cre)
        {
            throw cre;
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, ErrorMessage.GENERAL_READ.getMsg(f.getAbsolutePath()),e);
            throw new CannotReadException(f.getAbsolutePath()+":" + e.getMessage(), e);
        }
        finally
        {
            try
            {
                if (raf != null)
                {
                    raf.close();
                }
            }
            catch (Exception ex)
            {
                logger.log(Level.WARNING, ErrorMessage.GENERAL_READ_FAILED_UNABLE_TO_CLOSE_RANDOM_ACCESS_FILE.getMsg(f.getAbsolutePath()));
            }
        }
    }
}
