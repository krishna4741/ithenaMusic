package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class Mp4TrackField extends Mp4TagTextNumberField
{
    private static final int NONE_VALUE_INDEX = 0;
    private static final int TRACK_NO_INDEX = 1;
    private static final int TRACK_TOTAL_INDEX = 2;
    private static final int NONE_END_VALUE_INDEX = 3;


    public Mp4TrackField(String trackValue) throws FieldDataInvalidException
    {
        super(Mp4FieldKey.TRACK.getFieldName(), trackValue);

        numbers = new ArrayList<Short>();
        numbers.add(new Short("0"));

        String values[] = trackValue.split("/");
        switch (values.length)
        {
            case 1:
                try
                {
                    numbers.add(Short.parseShort(values[0]));
                }
                catch (NumberFormatException nfe)
                {
                    throw new FieldDataInvalidException("Value of:" + values[0] + " is invalid for field:" + id);
                }
                numbers.add(new Short("0"));
                numbers.add(new Short("0"));
                break;

            case 2:
                try
                {
                    numbers.add(Short.parseShort(values[0]));
                }
                catch (NumberFormatException nfe)
                {
                    throw new FieldDataInvalidException("Value of:" + values[0] + " is invalid for field:" + id);
                }
                try
                {
                    numbers.add(Short.parseShort(values[1]));
                }
                catch (NumberFormatException nfe)
                {
                    throw new FieldDataInvalidException("Value of:" + values[1] + " is invalid for field:" + id);
                }
                numbers.add(new Short("0"));
                break;

            default:
                throw new FieldDataInvalidException("Value is invalid for field:" + id);
        }
    }



    public Mp4TrackField(int trackNo)
    {

        super(Mp4FieldKey.TRACK.getFieldName(), String.valueOf(trackNo));
        numbers = new ArrayList<Short>();
        numbers.add(new Short("0"));
        numbers.add((short) trackNo);
        numbers.add(new Short("0"));
        numbers.add(new Short("0"));
    }


    public Mp4TrackField(int trackNo, int total)
    {
        super(Mp4FieldKey.TRACK.getFieldName(), String.valueOf(trackNo));
        numbers = new ArrayList<Short>();
        numbers.add(new Short("0"));
        numbers.add((short) trackNo);
        numbers.add((short) total);
        numbers.add(new Short("0"));
    }


    public Mp4TrackField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        super(id, data);
    }


    protected void build(ByteBuffer data) throws UnsupportedEncodingException
    {
        //Data actually contains a 'Data' Box so process data using this
        Mp4BoxHeader header = new Mp4BoxHeader(data);
        Mp4DataBox databox = new Mp4DataBox(header, data);
        dataSize = header.getDataLength();
        numbers = databox.getNumbers();
        //Track number always hold three values, we can discard the first one, the second one is the track no
        //and the third is the total no of tracks so only use if not zero
        StringBuffer sb = new StringBuffer();
        if(numbers!=null)
        {
            if ((numbers.size() > TRACK_NO_INDEX) && (numbers.get(TRACK_NO_INDEX) > 0))
            {
                sb.append(numbers.get(TRACK_NO_INDEX));
            }
            if ((numbers.size() > TRACK_TOTAL_INDEX) && (numbers.get(TRACK_TOTAL_INDEX) > 0))
            {
                sb.append("/").append(numbers.get(TRACK_TOTAL_INDEX));
            }
        }
        content = sb.toString();
    }


    public Short getTrackNo()
    {
        return numbers.get(TRACK_NO_INDEX);
    }


    public Short getTrackTotal()
    {
        if(numbers.size()<=TRACK_TOTAL_INDEX)
        {
            return 0;
        }
        return numbers.get(TRACK_TOTAL_INDEX);
    }


    public void setTrackNo(int trackNo)
    {
        numbers.set(TRACK_NO_INDEX, (short) trackNo);
    }



    public void setTrackTotal(int trackTotal)
    {
       numbers.set(TRACK_TOTAL_INDEX, (short) trackTotal);
    }
}
