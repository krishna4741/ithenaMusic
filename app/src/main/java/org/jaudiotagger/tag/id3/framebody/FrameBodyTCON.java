
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.TCONString;
import org.jaudiotagger.tag.datatype.TextEncodedStringSizeTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.ID3V2ExtendedGenreTypes;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.GenreTypes;

import java.nio.ByteBuffer;


public class FrameBodyTCON extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTCON()
    {
    }

    public FrameBodyTCON(FrameBodyTCON body)
    {
        super(body);
    }


    public FrameBodyTCON(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTCON(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }



    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_GENRE;
    }



    public static String convertGenericToID3v24Genre(String value)
    {
        try
        {
            //If passed id and known value use it
            int genreId = Integer.parseInt(value);
            if (genreId < GenreTypes.getMaxGenreId())
            {
                return String.valueOf(genreId);
            }
            else
            {
                return value;
            }
        }
        catch (NumberFormatException nfe)
        {
            //If passed String, use matching intregral value if can
            Integer genreId = GenreTypes.getInstanceOf().getIdForName(value);
            if (genreId != null)
            {
                return String.valueOf(genreId);
            }

            //Covert special string values
            if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.RX.getDescription()))
            {
                value = ID3V2ExtendedGenreTypes.RX.name();
            }
            else if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.CR.getDescription()))
            {
                value = ID3V2ExtendedGenreTypes.CR.name();
            }
            else if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.RX.name()))
            {
                value = ID3V2ExtendedGenreTypes.RX.name();
            }
            else if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.CR.name()))
            {
                value = ID3V2ExtendedGenreTypes.CR.name();
            }
        }
        return value;
    }


    public static String convertGenericToID3v23Genre(String value)
    {
        try
        {
            //If passed integer and in list use numeric form else use original value
            int genreId = Integer.parseInt(value);
            if (genreId < GenreTypes.getMaxGenreId())
            {
                return bracketWrap(String.valueOf(genreId));
            }
            else
            {
                return value;
            }
        }
        catch (NumberFormatException nfe)
        {
            //if passed text try and find integral value otherwise use text
            Integer genreId = GenreTypes.getInstanceOf().getIdForName(value);
            if (genreId != null)
            {
                return bracketWrap(String.valueOf(genreId));
            }

            //But special handling for these text values
            if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.RX.getDescription()))
            {
                value = bracketWrap(ID3V2ExtendedGenreTypes.RX.name());
            }
            else if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.CR.getDescription()))
            {
                value = bracketWrap(ID3V2ExtendedGenreTypes.CR.name());
            }
            else if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.RX.name()))
            {
                value = bracketWrap(ID3V2ExtendedGenreTypes.RX.name());
            }
            else if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.CR.name()))
            {
                value = bracketWrap(ID3V2ExtendedGenreTypes.CR.name());
            }
        }
        return value;
    }

    public static String convertGenericToID3v22Genre(String value)
    {
        return convertGenericToID3v23Genre(value);
    }

    private static String bracketWrap(Object value)
    {
        return "(" + value + ')';
    }


    public static String convertID3v24GenreToGeneric(String value)
    {
        try
        {
            int genreId = Integer.parseInt(value);
            if (genreId < GenreTypes.getMaxStandardGenreId())
            {
                return GenreTypes.getInstanceOf().getValueForId(genreId);
            }
            else
            {
                return value;
            }
        }
        catch (NumberFormatException nfe)
        {
            if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.RX.name()))
            {
                value = ID3V2ExtendedGenreTypes.RX.getDescription();
            }
            else if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.CR.name()))
            {
                value = ID3V2ExtendedGenreTypes.CR.getDescription();
            }
            else
            {
                return value;
            }
        }
        return value;
    }

    private static String checkBracketed(String value)
    {
        value=value.replace("(", "");
        value=value.replace(")", "");
        try
        {
            int genreId = Integer.parseInt(value);
            if (genreId < GenreTypes.getMaxStandardGenreId())
            {
                return GenreTypes.getInstanceOf().getValueForId(genreId);
            }
            else
            {
                return value;
            }
        }
        catch (NumberFormatException nfe)
        {
            if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.RX.name()))
            {
                value = ID3V2ExtendedGenreTypes.RX.getDescription();
            }
            else if (value.equalsIgnoreCase(ID3V2ExtendedGenreTypes.CR.name()))
            {
                value = ID3V2ExtendedGenreTypes.CR.getDescription();
            }
            else
            {
                return value;
            }
        }
        return value;
    }


    public static String convertID3v23GenreToGeneric(String value)
    {
        if(value.contains(")") && value.lastIndexOf(')')<value.length()-1)
        {
            return checkBracketed(value.substring(0,value.lastIndexOf(')'))) + ' ' + value.substring(value.lastIndexOf(')')+1);
        }
        else
        {
            return checkBracketed(value);
        }
    }

    public static String convertID3v22GenreToGeneric(String value)
    {
        return convertID3v23GenreToGeneric(value);
    }

    public void setV23Format()
    {
        TCONString text = (TCONString) getObject(DataTypes.OBJ_TEXT);
        text.setNullSeperateMultipleValues(false);
    }

    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new TCONString(DataTypes.OBJ_TEXT, this));
    }


}
