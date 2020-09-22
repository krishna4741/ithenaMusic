
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.NumberVariableLength;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyPOPM extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    private static final int    RATING_FIELD_SIZE = 1;
    private static final int    COUNTER_MINIMUM_FIELD_SIZE = 0;
    public  static final String MEDIA_MONKEY_NO_EMAIL = "no@email";

    public FrameBodyPOPM()
    {
        this.setObjectValue(DataTypes.OBJ_EMAIL, "");
        this.setObjectValue(DataTypes.OBJ_RATING, (long) 0);
        this.setObjectValue(DataTypes.OBJ_COUNTER, (long) 0);
    }

    public FrameBodyPOPM(FrameBodyPOPM body)
    {
        super(body);
    }


    public FrameBodyPOPM(String emailToUser, long rating, long counter)
    {
        this.setObjectValue(DataTypes.OBJ_EMAIL, emailToUser);
        this.setObjectValue(DataTypes.OBJ_RATING, rating);
        this.setObjectValue(DataTypes.OBJ_COUNTER, counter);
    }


    public FrameBodyPOPM(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public void setEmailToUser(String description)
    {
        setObjectValue(DataTypes.OBJ_EMAIL, description);
    }


    public String getEmailToUser()
    {
        return (String) getObjectValue(DataTypes.OBJ_EMAIL);
    }


    public long getRating()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_RATING)).longValue();
    }


    public void setRating(long rating)
    {
        setObjectValue(DataTypes.OBJ_RATING, rating);
    }


    public long getCounter()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_COUNTER)).longValue();
    }


    public void setCounter(long counter)
    {
        setObjectValue(DataTypes.OBJ_COUNTER, counter);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_POPULARIMETER;
    }

    public String getUserFriendlyValue()
    {
        return getEmailToUser()+":"+getRating()+":"+getCounter();
    }

    public void parseString(String data)
    {
        try
        {
            int value = Integer.parseInt(data);
            setRating(value);
            setEmailToUser(MEDIA_MONKEY_NO_EMAIL);
        }
        catch(NumberFormatException nfe)
        {

        }
    }


    protected void setupObjectList()
    {
        objectList.add(new StringNullTerminated(DataTypes.OBJ_EMAIL, this));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_RATING, this, RATING_FIELD_SIZE));
        objectList.add(new NumberVariableLength(DataTypes.OBJ_COUNTER, this, COUNTER_MINIMUM_FIELD_SIZE));
    }
}
