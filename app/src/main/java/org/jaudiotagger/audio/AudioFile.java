
package org.jaudiotagger.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;
import java.util.ArrayList;

import org.jaudiotagger.audio.aiff.AiffTag;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.audio.wav.WavTag;
import org.jaudiotagger.audio.real.RealTag;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.flac.FlacTag;


public class AudioFile
{
    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio");


    protected File file;


    protected AudioHeader audioHeader;


    protected Tag tag;

    public AudioFile()
    {

    }


    public AudioFile(File f, AudioHeader audioHeader, Tag tag)
    {
        this.file = f;
        this.audioHeader = audioHeader;
        this.tag = tag;
    }



    public AudioFile(String s, AudioHeader audioHeader, Tag tag)
    {
        this.file = new File(s);
        this.audioHeader = audioHeader;
        this.tag = tag;
    }


    public void commit() throws CannotWriteException
    {
        AudioFileIO.write(this);
    }


    public void setFile(File file)
    {
        this.file = file;
    }


    public File getFile()
    {
        return file;
    }


    public void setTag(Tag tag)
    {
        this.tag = tag;
    }


    public AudioHeader getAudioHeader()
    {
        return audioHeader;
    }


    public Tag getTag()
    {
        return tag;
    }


    public String toString()
    {
        return "AudioFile " + getFile().getAbsolutePath()
                + "  --------\n" + audioHeader.toString() + "\n" + ((tag == null) ? "" : tag.toString()) + "\n-------------------";
    }


    public void checkFileExists(File file)throws FileNotFoundException
    {
        logger.config("Reading file:" + "path" + file.getPath() + ":abs:" + file.getAbsolutePath());
        if (!file.exists())
        {
            logger.severe("Unable to find:" + file.getPath());
            throw new FileNotFoundException(ErrorMessage.UNABLE_TO_FIND_FILE.getMsg(file.getPath()));
        }
    }


    protected RandomAccessFile checkFilePermissions(File file, boolean readOnly) throws ReadOnlyFileException, FileNotFoundException
    {
        RandomAccessFile newFile;

        checkFileExists(file);

        // Unless opened as readonly the file must be writable
        if (readOnly)
        {
            newFile = new RandomAccessFile(file, "r");
        }
        else
        {
            if (!file.canWrite())
            {
                logger.severe("Unable to write:" + file.getPath());
                throw new ReadOnlyFileException(ErrorMessage.NO_PERMISSIONS_TO_WRITE_TO_FILE.getMsg(file.getPath()));
            }
            newFile = new RandomAccessFile(file, "rw");
        }
        return newFile;
    }


    public String displayStructureAsXML()
    {
        return "";
    }


    public String displayStructureAsPlainText()
    {
        return "";
    }



    public Tag createDefaultTag()
    {
        if(SupportedFileFormat.FLAC.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new FlacTag(VorbisCommentTag.createNewTag(), new ArrayList< MetadataBlockDataPicture >());
        }
        else if(SupportedFileFormat.OGG.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return VorbisCommentTag.createNewTag();
        }
        else if(SupportedFileFormat.MP4.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new Mp4Tag();
        }
        else if(SupportedFileFormat.M4A.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new Mp4Tag();
        }
        else if(SupportedFileFormat.M4P.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new Mp4Tag();
        }
        else if(SupportedFileFormat.WMA.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new AsfTag();
        }
        else if(SupportedFileFormat.WAV.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new WavTag();
        }
        else if(SupportedFileFormat.RA.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new RealTag();
        }
        else if(SupportedFileFormat.RM.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new RealTag();
        }
        else if(SupportedFileFormat.AIF.getFilesuffix().equals(file.getName().substring(file.getName().lastIndexOf('.'))))
        {
            return new AiffTag();
        }
        else
        {
            throw new RuntimeException("Unable to create default tag for this file format");
        }

    }


    public Tag getTagOrCreateDefault()
    {
        Tag tag = getTag();
        if(tag==null)
        {
            return createDefaultTag();
        }
        return tag;
    }


    public Tag getTagOrCreateAndSetDefault()
    {
        Tag tag = getTagOrCreateDefault();
        setTag(tag);
        return tag;
    }

    public Tag getTagAndConvertOrCreateAndSetDefault()
    {
        return getTagOrCreateAndSetDefault();
    }


    public static String getBaseFilename(File file)
    {
        int index=file.getName().toLowerCase().lastIndexOf(".");
        if(index>0)
        {
            return file.getName().substring(0,index);
        }
        return file.getName();
    }
}
