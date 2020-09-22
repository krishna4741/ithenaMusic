
package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.logging.*;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.lyrics3.AbstractLyrics3;
import org.jaudiotagger.tag.reference.ID3V2Version;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;


public class MP3File extends AudioFile
{
    private static final int MINIMUM_FILESIZE = 150;

    protected static AbstractTagDisplayFormatter tagFormatter;


    private AbstractID3v2Tag id3v2tag = null;


    private ID3v24Tag id3v2Asv24tag = null;


    private AbstractLyrics3 lyrics3tag = null;



    private ID3v1Tag id3v1tag = null;


    public MP3File()
    {
    }


    public MP3File(String filename) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
        this(new File(filename));
    }



    public static final int LOAD_IDV1TAG = 2;


    public static final int LOAD_IDV2TAG = 4;


    public static final int LOAD_LYRICS3 = 8;

    public static final int LOAD_ALL = LOAD_IDV1TAG | LOAD_IDV2TAG | LOAD_LYRICS3;


    public MP3File(File file, int loadOptions) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
        this(file, loadOptions, false);
    }


    private void readV1Tag(File file, RandomAccessFile newFile, int loadOptions) throws IOException
    {
        if ((loadOptions & LOAD_IDV1TAG) != 0)
        {
            logger.finer("Attempting to read id3v1tags");
            try
            {
                id3v1tag = new ID3v11Tag(newFile, file.getName());
            }
            catch (TagNotFoundException ex)
            {
                logger.config("No ids3v11 tag found");
            }

            try
            {
                if (id3v1tag == null)
                {
                    id3v1tag = new ID3v1Tag(newFile, file.getName());
                }
            }
            catch (TagNotFoundException ex)
            {
                logger.config("No id3v1 tag found");
            }
        }
    }


    private void readV2Tag(File file, int loadOptions, int startByte) throws IOException, TagException
    {
        //We know where the actual Audio starts so load all the file from start to that point into
        //a buffer then we can read the IDv2 information without needing any more File I/O
        if (startByte >= AbstractID3v2Tag.TAG_HEADER_LENGTH)
        {
            logger.finer("Attempting to read id3v2tags");
            FileInputStream fis = null;
            FileChannel fc = null;
            ByteBuffer bb;
            try
            {
                fis = new FileInputStream(file);
                fc = fis.getChannel();
                // avoid using fc.map method since it does not work on Android ICS and JB. Bug report: https://code.google.com/p/android/issues/detail?id=53637
                // bb = fc.map(FileChannel.MapMode.READ_ONLY,0,startByte);
                bb = ByteBuffer.allocate(startByte);
                fc.read(bb, 0);
            }
            finally
            {
                if (fc != null)
                {
                    fc.close();
                }

                if (fis != null)
                {
                    fis.close();
                }
            }

            try
            {
                bb.rewind();

                if ((loadOptions & LOAD_IDV2TAG) != 0)
                {
                    logger.config("Attempting to read id3v2tags");
                    try
                    {
                        this.setID3v2Tag(new ID3v24Tag(bb, file.getName()));
                    }
                    catch (TagNotFoundException ex)
                    {
                        logger.config("No id3v24 tag found");
                    }

                    try
                    {
                        if (id3v2tag == null)
                        {
                            this.setID3v2Tag(new ID3v23Tag(bb, file.getName()));
                        }
                    }
                    catch (TagNotFoundException ex)
                    {
                        logger.config("No id3v23 tag found");
                    }

                    try
                    {
                        if (id3v2tag == null)
                        {
                            this.setID3v2Tag(new ID3v22Tag(bb, file.getName()));
                        }
                    }
                    catch (TagNotFoundException ex)
                    {
                        logger.config("No id3v22 tag found");
                    }
                }
            }
            finally
            {
                bb.clear();
            }
        }
        else
        {
            logger.config("Not enough room for valid id3v2 tag:" + startByte);
        }
    }


    private void readLyrics3Tag(File file, RandomAccessFile newFile, int loadOptions) throws IOException
    {

    }



    private boolean isFilePortionNull(int startByte, int endByte) throws IOException
    {
        logger.config("Checking file portion:" + Hex.asHex(startByte) + ":" + Hex.asHex(endByte));
        FileInputStream fis=null;
        FileChannel     fc=null;
        try
        {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            fc.position(startByte);
            ByteBuffer bb = ByteBuffer.allocateDirect(endByte - startByte);
            fc.read(bb);
            while(bb.hasRemaining())
            {
                if(bb.get()!=0)
                {
                    return false;
                }
            }
        }
        finally
        {
            if (fc != null)
            {
                fc.close();
            }

            if (fis != null)
            {
                fis.close();
            }
        }
        return true;
    }

    private MP3AudioHeader checkAudioStart(long startByte, MP3AudioHeader firstHeaderAfterTag) throws IOException, InvalidAudioFrameException
    {
        MP3AudioHeader headerOne;
        MP3AudioHeader headerTwo;

        logger.warning(ErrorMessage.MP3_ID3TAG_LENGTH_INCORRECT.getMsg(file.getPath(), Hex.asHex(startByte), Hex.asHex(firstHeaderAfterTag.getMp3StartByte())));

        //because we cant agree on start location we reread the audioheader from the start of the file, at least
        //this way we cant overwrite the audio although we might overwrite part of the tag if we write this file
        //back later
        headerOne = new MP3AudioHeader(file, 0);
        logger.config("Checking from start:" + headerOne);

        //Although the id3 tag size appears to be incorrect at least we have found the same location for the start
        //of audio whether we start searching from start of file or at the end of the alleged of file so no real
        //problem
        if (firstHeaderAfterTag.getMp3StartByte() == headerOne.getMp3StartByte())
        {
            logger.config(ErrorMessage.MP3_START_OF_AUDIO_CONFIRMED.getMsg(file.getPath(),
                    Hex.asHex(headerOne.getMp3StartByte())));
            return firstHeaderAfterTag;
        }
        else
        {

            //We get a different value if read from start, can'timer guarantee 100% correct lets do some more checks
            logger.config((ErrorMessage.MP3_RECALCULATED_POSSIBLE_START_OF_MP3_AUDIO.getMsg(file.getPath(),
                            Hex.asHex(headerOne.getMp3StartByte()))));

            //Same frame count so probably both audio headers with newAudioHeader being the first one
            if (firstHeaderAfterTag.getNumberOfFrames() == headerOne.getNumberOfFrames())
            {
                logger.warning((ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(file.getPath(),
                                Hex.asHex(headerOne.getMp3StartByte()))));
                return headerOne;
            }

            //If the size reported by the tag header is a little short and there is only nulls between the recorded value
            //and the start of the first audio found then we stick with the original header as more likely that currentHeader
            //DataInputStream not really a header
            if(isFilePortionNull((int) startByte,(int) firstHeaderAfterTag.getMp3StartByte()))
            {
                return firstHeaderAfterTag;
            }

            //Skip to the next header (header 2, counting from start of file)
            headerTwo = new MP3AudioHeader(file, headerOne.getMp3StartByte()
                    + headerOne.mp3FrameHeader.getFrameLength());

            //It matches the header we found when doing the original search from after the ID3Tag therefore it
            //seems that newAudioHeader was a false match and the original header was correct
            if (headerTwo.getMp3StartByte() == firstHeaderAfterTag.getMp3StartByte())
            {
                logger.warning((ErrorMessage.MP3_START_OF_AUDIO_CONFIRMED.getMsg(file.getPath(),
                                Hex.asHex(firstHeaderAfterTag.getMp3StartByte()))));
                return firstHeaderAfterTag;
            }

            //It matches the frameCount the header we just found so lends weight to the fact that the audio does indeed start at new header
            //however it maybe that neither are really headers and just contain the same data being misrepresented as headers.
            if (headerTwo.getNumberOfFrames() == headerOne.getNumberOfFrames())
            {
                logger.warning((ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(file.getPath(),
                                Hex.asHex(headerOne.getMp3StartByte()))));
                return headerOne;
            }
            ///Doesnt match the frameCount lets go back to the original header
            else
            {
                logger.warning((ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(file.getPath(),
                                Hex.asHex(firstHeaderAfterTag.getMp3StartByte()))));
                return firstHeaderAfterTag;
            }
        }
    }


    public MP3File(File file, int loadOptions, boolean readOnly) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
        RandomAccessFile newFile = null;
        try
        {
            this.file = file;

            //Check File accessibility
            newFile = checkFilePermissions(file, readOnly);

            //Read ID3v2 tag size (if tag exists) to allow audioHeader parsing to skip over tag
            long tagSizeReportedByHeader = AbstractID3v2Tag.getV2TagSizeIfExists(file);
            logger.config("TagHeaderSize:" + Hex.asHex(tagSizeReportedByHeader));
            audioHeader = new MP3AudioHeader(file, tagSizeReportedByHeader);

            //If the audio header is not straight after the end of the tag then search from start of file
            if (tagSizeReportedByHeader != ((MP3AudioHeader) audioHeader).getMp3StartByte())
            {
                logger.config("First header found after tag:" + audioHeader);
                audioHeader = checkAudioStart(tagSizeReportedByHeader, (MP3AudioHeader) audioHeader);
            }

            //Read v1 tags (if any)
            readV1Tag(file, newFile, loadOptions);

            //Read v2 tags (if any)
            readV2Tag(file, loadOptions, (int)((MP3AudioHeader) audioHeader).getMp3StartByte());

            //If we have a v2 tag use that, if we do not but have v1 tag use that
            //otherwise use nothing
            //TODO:if have both should we merge
            //rather than just returning specific ID3v22 tag, would it be better to return v24 version ?
            if (this.getID3v2Tag() != null)
            {
                tag = this.getID3v2Tag();
            }
            else if (id3v1tag != null)
            {
                tag = id3v1tag;
            }
        }
        finally
        {
            if (newFile != null)
            {
                newFile.close();
            }
        }
    }


    public long getMP3StartByte(File file) throws InvalidAudioFrameException, IOException
    {
        try
        {
            //Read ID3v2 tag size (if tag exists) to allow audio header parsing to skip over tag
            long startByte = AbstractID3v2Tag.getV2TagSizeIfExists(file);

            MP3AudioHeader audioHeader = new MP3AudioHeader(file, startByte);
            if (startByte != audioHeader.getMp3StartByte())
            {
                logger.config("First header found after tag:" + audioHeader);
                audioHeader = checkAudioStart(startByte, audioHeader);
            }
            return audioHeader.getMp3StartByte();
        }
        catch (InvalidAudioFrameException iafe)
        {
            throw iafe;
        }
        catch (IOException ioe)
        {
            throw ioe;
        }
    }


    public File extractID3v2TagDataIntoFile(File outputFile) throws TagNotFoundException, IOException
    {
        int startByte = (int) ((MP3AudioHeader) audioHeader).getMp3StartByte();
        if (startByte >= 0)
        {

            //Read byte into buffer
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(startByte);
            fc.read(bb);

            //Write bytes to outputFile
            FileOutputStream out = new FileOutputStream(outputFile);
            out.write(bb.array());
            out.close();
            fc.close();
            fis.close();
            return outputFile;
        }
        throw new TagNotFoundException("There is no ID3v2Tag data in this file");
    }


    public MP3AudioHeader getMP3AudioHeader()
    {
        return (MP3AudioHeader) getAudioHeader();
    }


    public boolean hasID3v1Tag()
    {
        return (id3v1tag != null);
    }


    public boolean hasID3v2Tag()
    {
        return (id3v2tag != null);
    }





    public MP3File(File file) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
        this(file, LOAD_ALL);
    }


    public void setID3v1Tag(ID3v1Tag id3v1tag)
    {
        logger.config("setting tagv1:v1 tag");
        this.id3v1tag = id3v1tag;
    }

    public void setID3v1Tag(Tag id3v1tag)
    {
        logger.config("setting tagv1:v1 tag");
        this.id3v1tag = (ID3v1Tag) id3v1tag;
    }


    public void setID3v1Tag(AbstractTag mp3tag)
    {
        logger.config("setting tagv1:abstract");
        id3v1tag = new ID3v11Tag(mp3tag);
    }


    public ID3v1Tag getID3v1Tag()
    {
        return id3v1tag;
    }


    public void setID3v2Tag(AbstractTag mp3tag)
    {
        id3v2tag = new ID3v24Tag(mp3tag);

    }


    public void setID3v2Tag(AbstractID3v2Tag id3v2tag)
    {
        this.id3v2tag = id3v2tag;
        if (id3v2tag instanceof ID3v24Tag)
        {
            this.id3v2Asv24tag = (ID3v24Tag) this.id3v2tag;
        }
        else
        {
            this.id3v2Asv24tag = new ID3v24Tag(id3v2tag);
        }
    }


    //TODO temp its rather messy
    public void setID3v2TagOnly(AbstractID3v2Tag id3v2tag)
    {
        this.id3v2tag = id3v2tag;
        this.id3v2Asv24tag = null;
    }


    public AbstractID3v2Tag getID3v2Tag()
    {
        return id3v2tag;
    }


    public ID3v24Tag getID3v2TagAsv24()
    {
        return id3v2Asv24tag;
    }











    public void delete(AbstractTag mp3tag) throws FileNotFoundException, IOException
    {
        RandomAccessFile raf = new RandomAccessFile(this.file, "rw");
        mp3tag.delete(raf);
        raf.close();
        if(mp3tag instanceof ID3v1Tag)
        {
            id3v1tag=null;
        }

        if(mp3tag instanceof AbstractID3v2Tag)
        {
            id3v2tag=null;
        }
    }


    public void save() throws IOException, TagException
    {
        save(this.file);
    }


    public void commit() throws CannotWriteException
    {
        try
        {
            save();
        }
        catch (IOException ioe)
        {
            throw new CannotWriteException(ioe);
        }
        catch (TagException te)
        {
            throw new CannotWriteException(te);
        }
    }


    public void precheck(File file) throws IOException
    {
        if (!file.exists())
        {
            logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_NOT_FOUND.getMsg(file.getName()));
            throw new IOException(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_NOT_FOUND.getMsg(file.getName()));
        }

        if (!file.canWrite())
        {
            logger.severe(ErrorMessage.GENERAL_WRITE_FAILED.getMsg(file.getName()));
            throw new IOException(ErrorMessage.GENERAL_WRITE_FAILED.getMsg(file.getName()));
        }

        if (file.length() <= MINIMUM_FILESIZE)
        {
            logger.severe(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_IS_TOO_SMALL.getMsg(file.getName()));
            throw new IOException(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_IS_TOO_SMALL.getMsg(file.getName()));
        }
    }


    public void save(File fileToSave) throws IOException
    {
        //Ensure we are dealing with absolute filepaths not relative ones
        File file = fileToSave.getAbsoluteFile();

        logger.config("Saving  : " + file.getPath());

        //Checks before starting write
        precheck(file);

        RandomAccessFile rfile = null;
        try
        {
            //ID3v2 Tag
            if (TagOptionSingleton.getInstance().isId3v2Save())
            {
                if (id3v2tag == null)
                {
                    rfile = new RandomAccessFile(file, "rw");
                    (new ID3v24Tag()).delete(rfile);
                    (new ID3v23Tag()).delete(rfile);
                    (new ID3v22Tag()).delete(rfile);
                    logger.config("Deleting ID3v2 tag:"+file.getName());
                    rfile.close();
                }
                else
                {
                    logger.config("Writing ID3v2 tag:"+file.getName());
                    final MP3AudioHeader mp3AudioHeader = (MP3AudioHeader) this.getAudioHeader();
                    final long mp3StartByte = mp3AudioHeader.getMp3StartByte();
                    final long newMp3StartByte = id3v2tag.write(file, mp3StartByte);
                    if (mp3StartByte != newMp3StartByte) {
                        logger.config("New mp3 start byte: " + newMp3StartByte);
                        mp3AudioHeader.setMp3StartByte(newMp3StartByte);
                    }

                }
            }
            rfile = new RandomAccessFile(file, "rw");

            //Lyrics 3 Tag
            if (TagOptionSingleton.getInstance().isLyrics3Save())
            {
                if (lyrics3tag != null)
                {
                    lyrics3tag.write(rfile);
                }
            }
            //ID3v1 tag
            if (TagOptionSingleton.getInstance().isId3v1Save())
            {
                logger.config("Processing ID3v1");
                if (id3v1tag == null)
                {
                    logger.config("Deleting ID3v1");
                    (new ID3v1Tag()).delete(rfile);
                }
                else
                {
                    logger.config("Saving ID3v1");
                    id3v1tag.write(rfile);
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            logger.log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_NOT_FOUND.getMsg(file.getName()), ex);
            throw ex;
        }
        catch (IOException iex)
        {
            logger.log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE.getMsg(file.getName(), iex.getMessage()), iex);
            throw iex;
        }
        catch (RuntimeException re)
        {
            logger.log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE.getMsg(file.getName(), re.getMessage()), re);
            throw re;
        }
        finally
        {
            if (rfile != null)
            {
                rfile.close();
            }
        }
    }


    public String displayStructureAsXML()
    {
        createXMLStructureFormatter();
        tagFormatter.openHeadingElement("file", this.getFile().getAbsolutePath());
        if (this.getID3v1Tag() != null)
        {
            this.getID3v1Tag().createStructure();
        }
        if (this.getID3v2Tag() != null)
        {
            this.getID3v2Tag().createStructure();
        }
        tagFormatter.closeHeadingElement("file");
        return tagFormatter.toString();
    }


    public String displayStructureAsPlainText()
    {
        createPlainTextStructureFormatter();
        tagFormatter.openHeadingElement("file", this.getFile().getAbsolutePath());
        if (this.getID3v1Tag() != null)
        {
            this.getID3v1Tag().createStructure();
        }
        if (this.getID3v2Tag() != null)
        {
            this.getID3v2Tag().createStructure();
        }
        tagFormatter.closeHeadingElement("file");
        return tagFormatter.toString();
    }

    private static void createXMLStructureFormatter()
    {
        tagFormatter = new XMLTagDisplayFormatter();
    }

    private static void createPlainTextStructureFormatter()
    {
        tagFormatter = new PlainTextTagDisplayFormatter();
    }

    public static AbstractTagDisplayFormatter getStructureFormatter()
    {
        return tagFormatter;
    }


    public void setTag(Tag tag)
    {
        this.tag = tag;
        if (tag instanceof ID3v1Tag)
        {
            setID3v1Tag((ID3v1Tag) tag);
        }
        else
        {
            setID3v2Tag((AbstractID3v2Tag) tag);
        }
    }



    @Override
    public Tag createDefaultTag()
    {
        if(TagOptionSingleton.getInstance().getID3V2Version()==ID3V2Version.ID3_V24)
        {    
            return new ID3v24Tag();
        }
        else if(TagOptionSingleton.getInstance().getID3V2Version()==ID3V2Version.ID3_V23)
        {
            return new ID3v23Tag();
        }
        else if(TagOptionSingleton.getInstance().getID3V2Version()==ID3V2Version.ID3_V22)
        {
            return new ID3v22Tag();
        }
        //Default in case not set somehow
        return new ID3v24Tag();
    }


    public Tag convertTag(Tag tag, ID3V2Version id3V2Version)
    {
        if(tag instanceof ID3v24Tag)
        {
            switch(id3V2Version)
            {
                case ID3_V22:
                    return new ID3v22Tag((ID3v24Tag)tag);
                case ID3_V23:
                    return new ID3v23Tag((ID3v24Tag)tag);
                case ID3_V24:
                    return tag;
            }
        }
        else if(tag instanceof ID3v23Tag)
        {
            switch(id3V2Version)
            {
                case ID3_V22:
                    return new ID3v22Tag((ID3v23Tag)tag);
                case ID3_V23:
                    return tag;
                case ID3_V24:
                    return new ID3v24Tag((ID3v23Tag)tag);
            }
        }
        else if(tag instanceof ID3v22Tag)
        {
            switch(id3V2Version)
            {
                case ID3_V22:
                    return tag;
                case ID3_V23:
                    return new ID3v23Tag((ID3v22Tag)tag);
                case ID3_V24:
                    return new ID3v24Tag((ID3v22Tag)tag);
            }
        }
        return tag;
    }


    @Override
    public Tag getTagOrCreateDefault()
    {
        Tag tag = getID3v2Tag();
        if(tag==null)
        {
            return createDefaultTag();
        }
        return tag;
    }



    @Override
    public Tag getTagAndConvertOrCreateAndSetDefault()
    {
        Tag tag = getTagOrCreateDefault();
        tag=convertTag(tag, TagOptionSingleton.getInstance().getID3V2Version());
        setTag(tag);
        return tag;
    }
}

