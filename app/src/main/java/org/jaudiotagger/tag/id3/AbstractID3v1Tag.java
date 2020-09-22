
package org.jaudiotagger.tag.id3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Pattern;


abstract public class AbstractID3v1Tag extends AbstractID3Tag
{

    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.id3");


    public AbstractID3v1Tag()
    {
    }

    public AbstractID3v1Tag(AbstractID3v1Tag copyObject)
    {
        super(copyObject);
    }

    //If field is less than maximum field length this is how it is terminated
    protected static final byte END_OF_FIELD = (byte) 0;

    //Used to detect end of field in String constructed from Data
    protected static Pattern endofStringPattern = Pattern.compile("\\x00");

    //Tag ID as held in file
    protected static final byte[] TAG_ID = {(byte) 'T', (byte) 'A', (byte) 'G'};

    //Fields Lengths common to v1 and v1.1 tags
    protected static final int TAG_LENGTH = 128;
    protected static final int TAG_DATA_LENGTH = 125;
    protected static final int FIELD_TAGID_LENGTH = 3;
    protected static final int FIELD_TITLE_LENGTH = 30;
    protected static final int FIELD_ARTIST_LENGTH = 30;
    protected static final int FIELD_ALBUM_LENGTH = 30;
    protected static final int FIELD_YEAR_LENGTH = 4;
    protected static final int FIELD_GENRE_LENGTH = 1;

    //Field Positions, starting from zero so fits in with Java Terminology
    protected static final int FIELD_TAGID_POS = 0;
    protected static final int FIELD_TITLE_POS = 3;
    protected static final int FIELD_ARTIST_POS = 33;
    protected static final int FIELD_ALBUM_POS = 63;
    protected static final int FIELD_YEAR_POS = 93;
    protected static final int FIELD_GENRE_POS = 127;

    //For writing output
    protected static final String TYPE_TITLE = "title";
    protected static final String TYPE_ARTIST = "artist";
    protected static final String TYPE_ALBUM = "album";
    protected static final String TYPE_YEAR = "year";
    protected static final String TYPE_GENRE = "genre";


    public int getSize()
    {
        return TAG_LENGTH;
    }


    public static boolean seekForV1OrV11Tag(ByteBuffer byteBuffer)
    {
        byte[] buffer = new byte[FIELD_TAGID_LENGTH];
        // read the TAG value
        byteBuffer.get(buffer, 0, FIELD_TAGID_LENGTH);
        return (Arrays.equals(buffer, TAG_ID));
    }

    public void delete(RandomAccessFile file) throws IOException
    {
        //Read into Byte Buffer
        logger.config("Deleting ID3v1 from file if exists");

        FileChannel fc;
        ByteBuffer byteBuffer;
        fc = file.getChannel();

        if(file.length() < TAG_LENGTH)
        {
            throw new IOException("File not not appear large enough to contain a tag");
        }
        fc.position(file.length() - TAG_LENGTH);
        byteBuffer = ByteBuffer.allocate(TAG_LENGTH);
        fc.read(byteBuffer);
        byteBuffer.rewind();
        if (AbstractID3v1Tag.seekForV1OrV11Tag(byteBuffer))
        {
            try
            {
                logger.config("Deleted ID3v1 tag");
                file.setLength(file.length() - TAG_LENGTH);
            }
            catch(IOException ex)
            {
                logger.severe("Unable to delete existing ID3v1 Tag:"+ex.getMessage());
            }
        }
        else
        {
            logger.config("Unable to find ID3v1 tag to deleteField");
        }
    }

 
}
