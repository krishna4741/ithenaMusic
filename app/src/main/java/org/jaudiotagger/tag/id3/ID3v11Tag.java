
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;


public class ID3v11Tag extends ID3v1Tag
{

    //For writing output
    protected static final String TYPE_TRACK = "track";

    protected static final int TRACK_UNDEFINED = 0;
    protected static final int TRACK_MAX_VALUE = 255;
    protected static final int TRACK_MIN_VALUE = 1;

    protected static final int FIELD_COMMENT_LENGTH = 28;
    protected static final int FIELD_COMMENT_POS = 97;

    protected static final int FIELD_TRACK_INDICATOR_LENGTH = 1;
    protected static final int FIELD_TRACK_INDICATOR_POS = 125;

    protected static final int FIELD_TRACK_LENGTH = 1;
    protected static final int FIELD_TRACK_POS = 126;


    protected byte track = (byte) TRACK_UNDEFINED;

    private static final byte RELEASE = 1;
    private static final byte MAJOR_VERSION = 1;
    private static final byte REVISION = 0;


    public byte getRelease()
    {
        return RELEASE;
    }


    public byte getMajorVersion()
    {
        return MAJOR_VERSION;
    }


    public byte getRevision()
    {
        return REVISION;
    }


    public ID3v11Tag()
    {

    }

    public int getFieldCount()
    {
        return 7;
    }

    public ID3v11Tag(ID3v11Tag copyObject)
    {
        super(copyObject);
        this.track = copyObject.track;
    }


    public ID3v11Tag(AbstractTag mp3tag)
    {
        if (mp3tag != null)
        {
            if (mp3tag instanceof ID3v1Tag)
            {
                if (mp3tag instanceof ID3v11Tag)
                {
                    throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
                }
                // id3v1_1 objects are also id3v1 objects
                ID3v1Tag id3old = (ID3v1Tag) mp3tag;
                this.title = id3old.title;
                this.artist = id3old.artist;
                this.album = id3old.album;
                this.comment = id3old.comment;
                this.year = id3old.year;
                this.genre = id3old.genre;
            }
            else
            {
                ID3v24Tag id3tag;
                // first change the tag to ID3v2_4 tag if not one already
                if (!(mp3tag instanceof ID3v24Tag))
                {
                    id3tag = new ID3v24Tag(mp3tag);
                }
                else
                {
                    id3tag = (ID3v24Tag) mp3tag;
                }
                ID3v24Frame frame;
                String text;
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_TITLE))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_TITLE);
                    text = ((FrameBodyTIT2) frame.getBody()).getText();
                    this.title = ID3Tags.truncate(text, FIELD_TITLE_LENGTH);
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_ARTIST))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_ARTIST);
                    text = ((FrameBodyTPE1) frame.getBody()).getText();
                    this.artist = ID3Tags.truncate(text, FIELD_ARTIST_LENGTH);
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_ALBUM))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_ALBUM);
                    text = ((FrameBodyTALB) frame.getBody()).getText();
                    this.album = ID3Tags.truncate(text, FIELD_ALBUM_LENGTH);
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_YEAR))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_YEAR);
                    text = ((FrameBodyTDRC) frame.getBody()).getText();
                    this.year = ID3Tags.truncate(text, FIELD_YEAR_LENGTH);
                }

                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_COMMENT))
                {
                    Iterator iterator = id3tag.getFrameOfType(ID3v24Frames.FRAME_ID_COMMENT);
                    text = "";
                    while (iterator.hasNext())
                    {
                        frame = (ID3v24Frame) iterator.next();
                        text += (((FrameBodyCOMM) frame.getBody()).getText() + " ");
                    }
                    this.comment = ID3Tags.truncate(text, FIELD_COMMENT_LENGTH);
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_GENRE))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_GENRE);
                    text = ((FrameBodyTCON) frame.getBody()).getText();
                    try
                    {
                        this.genre = (byte) ID3Tags.findNumber(text);
                    }
                    catch (TagException ex)
                    {
                        logger.log(Level.WARNING, getLoggingFilename() + ":" + "Unable to convert TCON frame to format suitable for v11 tag", ex);
                        this.genre = (byte) ID3v1Tag.GENRE_UNDEFINED;
                    }
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_TRACK))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_TRACK);
                    this.track = (byte) ((FrameBodyTRCK) frame.getBody()).getTrackNo().intValue();
                }
            }
        }
    }


    public ID3v11Tag(RandomAccessFile file, String loggingFilename) throws TagNotFoundException, IOException
    {
        setLoggingFilename(loggingFilename);
        FileChannel fc;
        ByteBuffer byteBuffer = ByteBuffer.allocate(TAG_LENGTH);

        fc = file.getChannel();
        fc.position(file.length() - TAG_LENGTH);

        fc.read(byteBuffer);
        byteBuffer.flip();
        read(byteBuffer);

    }


    public ID3v11Tag(RandomAccessFile file) throws TagNotFoundException, IOException
    {
        this(file, "");

    }


    public void setComment(String comment)
    {
        if (comment == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        this.comment = ID3Tags.truncate(comment, FIELD_COMMENT_LENGTH);
    }


    public String getFirstComment()
    {
        return comment;
    }


    
    public void setTrack(String trackValue)
    {
        int trackAsInt;
        //Try and convert String representation of track into an integer
        try
        {
            trackAsInt = Integer.parseInt(trackValue);
        }
        catch (NumberFormatException e)
        {
            trackAsInt = 0;
        }

        //This value cannot be held in v1_1
        if ((trackAsInt > TRACK_MAX_VALUE) || (trackAsInt < TRACK_MIN_VALUE))
        {
            this.track = (byte) TRACK_UNDEFINED;
        }
        else
        {
            this.track = (byte) Integer.parseInt(trackValue);
        }
    }



    public String getFirstTrack()
    {
        return String.valueOf(track & BYTE_TO_UNSIGNED);
    }

    public void addTrack(String track)
    {
        setTrack(track);
    }

    public List<TagField> getTrack()
    {
        if (getFirst(FieldKey.TRACK).length() > 0)
        {
            ID3v1TagField field = new ID3v1TagField(ID3v1FieldKey.TRACK.name(), getFirst(FieldKey.TRACK));
            return returnFieldToList(field);
        }
        else
        {
            return new ArrayList<TagField>();
        }
    }

    public void setField(TagField field)
    {
        FieldKey genericKey = FieldKey.valueOf(field.getId());
        if (genericKey == FieldKey.TRACK)
        {
            setTrack(field.toString());
        }
        else
        {
            super.setField(field);
        }
    }

    public List<TagField> getFields(FieldKey genericKey)
    {
        if (genericKey == FieldKey.TRACK)
        {
            return getTrack();
        }
        else
        {
            return super.getFields(genericKey);
        }
    }

    public String getFirst(FieldKey genericKey)
    {
        switch (genericKey)
        {
            case ARTIST:
                return getFirstArtist();

            case ALBUM:
                return getFirstAlbum();

            case TITLE:
                return getFirstTitle();

            case GENRE:
                return getFirstGenre();

            case YEAR:
                return getFirstYear();

            case TRACK:
                return getFirstTrack();

            case COMMENT:
                return getFirstComment();

            default:
                return "";
        }
    }

    public TagField getFirstField(String id)
    {
        List<TagField> results;

        if (FieldKey.TRACK.name().equals(id))
        {
            results = getTrack();
            if (results != null)
            {
                if (results.size() > 0)
                {
                    return results.get(0);
                }
            }
            return null;
        }
        else
        {
            return super.getFirstField(id);
        }
    }

    public boolean isEmpty()
    {
        return track <= 0 && super.isEmpty();
    }


    public void deleteField(FieldKey genericKey)
    {
        if (genericKey == FieldKey.TRACK)
        {
            track = 0;
        }
        else
        {
            super.deleteField(genericKey);
        }
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof ID3v11Tag))
        {
            return false;
        }
        ID3v11Tag object = (ID3v11Tag) obj;
        return this.track == object.track && super.equals(obj);
    }



    public boolean seek(ByteBuffer byteBuffer)
    {
        byte[] buffer = new byte[FIELD_TAGID_LENGTH];
        // read the TAG value
        byteBuffer.get(buffer, 0, FIELD_TAGID_LENGTH);
        if (!(Arrays.equals(buffer, TAG_ID)))
        {
            return false;
        }

        // Check for the empty byte before the TRACK
        byteBuffer.position(FIELD_TRACK_INDICATOR_POS);
        if (byteBuffer.get() != END_OF_FIELD)
        {
            return false;
        }
        //Now check for TRACK if the next byte is also null byte then not v1.1
        //tag, however this means cannot have v1_1 tag with track setField to zero/undefined
        //because on next read will be v1 tag.
        return byteBuffer.get() != END_OF_FIELD;
    }


    public void read(ByteBuffer byteBuffer) throws TagNotFoundException
    {
        if (!seek(byteBuffer))
        {
            throw new TagNotFoundException("ID3v1 tag not found");
        }
        logger.finer("Reading v1.1 tag");

        //Do single file read of data to cut down on file reads
        byte[] dataBuffer = new byte[TAG_LENGTH];
        byteBuffer.position(0);
        byteBuffer.get(dataBuffer, 0, TAG_LENGTH);
        title = Utils.getString(dataBuffer, FIELD_TITLE_POS, FIELD_TITLE_LENGTH, "ISO-8859-1").trim();
        Matcher m = AbstractID3v1Tag.endofStringPattern.matcher(title);
        if (m.find())
        {
            title = title.substring(0, m.start());
        }
        artist = Utils.getString(dataBuffer, FIELD_ARTIST_POS, FIELD_ARTIST_LENGTH, "ISO-8859-1").trim();
        m = AbstractID3v1Tag.endofStringPattern.matcher(artist);
        if (m.find())
        {
            artist = artist.substring(0, m.start());
        }
        album = Utils.getString(dataBuffer, FIELD_ALBUM_POS, FIELD_ALBUM_LENGTH, "ISO-8859-1").trim();
        m = AbstractID3v1Tag.endofStringPattern.matcher(album);
        if (m.find())
        {
            album = album.substring(0, m.start());
        }
        year = Utils.getString(dataBuffer, FIELD_YEAR_POS, FIELD_YEAR_LENGTH, "ISO-8859-1").trim();
        m = AbstractID3v1Tag.endofStringPattern.matcher(year);
        if (m.find())
        {
            year = year.substring(0, m.start());
        }
        comment = Utils.getString(dataBuffer, FIELD_COMMENT_POS, FIELD_COMMENT_LENGTH, "ISO-8859-1").trim();
        m = AbstractID3v1Tag.endofStringPattern.matcher(comment);
        if (m.find())
        {
            comment = comment.substring(0, m.start());
        }
        track = dataBuffer[FIELD_TRACK_POS];
        genre = dataBuffer[FIELD_GENRE_POS];
    }



    public void write(RandomAccessFile file) throws IOException
    {
        logger.config("Saving ID3v11 tag to file");
        byte[] buffer = new byte[TAG_LENGTH];
        int i;
        String str;
        delete(file);
        file.seek(file.length());
        System.arraycopy(TAG_ID, FIELD_TAGID_POS, buffer, FIELD_TAGID_POS, TAG_ID.length);
        int offset = FIELD_TITLE_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveTitle())
        {
            str = ID3Tags.truncate(title, FIELD_TITLE_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = FIELD_ARTIST_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveArtist())
        {
            str = ID3Tags.truncate(artist, FIELD_ARTIST_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = FIELD_ALBUM_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveAlbum())
        {
            str = ID3Tags.truncate(album, FIELD_ALBUM_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = FIELD_YEAR_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveYear())
        {
            str = ID3Tags.truncate(year, FIELD_YEAR_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = FIELD_COMMENT_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveComment())
        {
            str = ID3Tags.truncate(comment, FIELD_COMMENT_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = FIELD_TRACK_POS;
        buffer[offset] = track; // skip one byte extra blank for 1.1 definition
        offset = FIELD_GENRE_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveGenre())
        {
            buffer[offset] = genre;
        }
        file.write(buffer);

        logger.config("Saved ID3v11 tag to file");
    }


    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_TAG, getIdentifier());
        //Header
        MP3File.getStructureFormatter().addElement(TYPE_TITLE, this.title);
        MP3File.getStructureFormatter().addElement(TYPE_ARTIST, this.artist);
        MP3File.getStructureFormatter().addElement(TYPE_ALBUM, this.album);
        MP3File.getStructureFormatter().addElement(TYPE_YEAR, this.year);
        MP3File.getStructureFormatter().addElement(TYPE_COMMENT, this.comment);
        MP3File.getStructureFormatter().addElement(TYPE_TRACK, this.track);
        MP3File.getStructureFormatter().addElement(TYPE_GENRE, this.genre);
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_TAG);

    }
}
