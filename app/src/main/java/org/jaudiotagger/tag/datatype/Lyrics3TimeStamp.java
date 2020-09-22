
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;

public class Lyrics3TimeStamp extends AbstractDataType
{

    private long minute = 0;


    private long second = 0;


    public void readString(String s)
    {
    }


    public Lyrics3TimeStamp(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public Lyrics3TimeStamp(String identifier)
    {
        super(identifier, null);
    }

    public Lyrics3TimeStamp(Lyrics3TimeStamp copy)
    {
        super(copy);
        this.minute = copy.minute;
        this.second = copy.second;
    }

    public void setMinute(long minute)
    {
        this.minute = minute;
    }


    public long getMinute()
    {
        return minute;
    }

    public void setSecond(long second)
    {
        this.second = second;
    }


    public long getSecond()
    {
        return second;
    }


    public int getSize()
    {
        return 7;
    }


    public void setTimeStamp(long timeStamp, byte timeStampFormat)
    {

        timeStamp = timeStamp / 1000;
        minute = timeStamp / 60;
        second = timeStamp % 60;
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof Lyrics3TimeStamp))
        {
            return false;
        }

        Lyrics3TimeStamp object = (Lyrics3TimeStamp) obj;

        if (this.minute != object.minute)
        {
            return false;
        }

        return this.second == object.second && super.equals(obj);

    }


    public void readString(String timeStamp, int offset)
    {
        if (timeStamp == null)
        {
            throw new NullPointerException("Image is null");
        }

        if ((offset < 0) || (offset >= timeStamp.length()))
        {
            throw new IndexOutOfBoundsException("Offset to timeStamp is out of bounds: offset = " + offset + ", timeStamp.length()" + timeStamp.length());
        }

        timeStamp = timeStamp.substring(offset);

        if (timeStamp.length() == 7)
        {
            minute = Integer.parseInt(timeStamp.substring(1, 3));
            second = Integer.parseInt(timeStamp.substring(4, 6));
        }
        else
        {
            minute = 0;
            second = 0;
        }
    }


    public String toString()
    {
        return writeString();
    }


    public String writeString()
    {
        String str;
        str = "[";

        if (minute < 0)
        {
            str += "00";
        }
        else
        {
            if (minute < 10)
            {
                str += '0';
            }

            str += Long.toString(minute);
        }

        str += ':';

        if (second < 0)
        {
            str += "00";
        }
        else
        {
            if (second < 10)
            {
                str += '0';
            }

            str += Long.toString(second);
        }

        str += ']';

        return str;
    }

    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        readString(arr.toString(), offset);
    }

    public byte[] writeByteArray()
    {
        return Utils.getDefaultBytes(writeString(), "ISO8859-1");
    }

}
