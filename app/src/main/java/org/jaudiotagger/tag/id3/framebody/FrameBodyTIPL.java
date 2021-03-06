
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.Pair;
import org.jaudiotagger.tag.datatype.PairedTextEncodedStringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.StringTokenizer;



public class FrameBodyTIPL extends AbstractID3v2FrameBody implements ID3v24FrameBody
{
    //Standard function names, taken from Picard Mapping
    public static final String ENGINEER = "engineer";
    public static final String MIXER    = "mix";
    public static final String DJMIXER  = "DJ-mix";
    public static final String PRODUCER = "producer";
    public static final String ARRANGER = "arranger";


    public FrameBodyTIPL()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
    }


    public FrameBodyTIPL(byte textEncoding, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setText(text);
    }


    public FrameBodyTIPL(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public FrameBodyTIPL(FrameBodyIPLS body)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding());
        setObjectValue(DataTypes.OBJ_TEXT, body.getPairing());
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE;
    }


    public void setText(String text)
    {
        PairedTextEncodedStringNullTerminated.ValuePairs value = new PairedTextEncodedStringNullTerminated.ValuePairs();
        StringTokenizer stz = new StringTokenizer(text, "\0");

        while (stz.hasMoreTokens())
        {
            String key =stz.nextToken();
            if(stz.hasMoreTokens())
            {
                value.add(key, stz.nextToken());
            }
            
        }
        setObjectValue(DataTypes.OBJ_TEXT, value);
    }


    public void addPair(String text)
    {
        StringTokenizer stz = new StringTokenizer(text, "\0");
        if (stz.countTokens()==2)
        {
            addPair(stz.nextToken(),stz.nextToken());
        }
    }


    public void addPair(String function,String name)
    {
        PairedTextEncodedStringNullTerminated.ValuePairs value = ((PairedTextEncodedStringNullTerminated) getObject(DataTypes.OBJ_TEXT)).getValue();
        value.add(function, name);

    }


    public void resetPairs()
    {
        PairedTextEncodedStringNullTerminated.ValuePairs value = ((PairedTextEncodedStringNullTerminated) getObject(DataTypes.OBJ_TEXT)).getValue();
        value.getMapping().clear();
    }


    public void write(ByteArrayOutputStream tagBuffer)
    {
        if (!((PairedTextEncodedStringNullTerminated) getObject(DataTypes.OBJ_TEXT)).canBeEncoded())
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new PairedTextEncodedStringNullTerminated(DataTypes.OBJ_TEXT, this));
    }

    public PairedTextEncodedStringNullTerminated.ValuePairs getPairing()
    {
        return (PairedTextEncodedStringNullTerminated.ValuePairs) getObject(DataTypes.OBJ_TEXT).getValue();
    }


    public String getKeyAtIndex(int index)
    {
        PairedTextEncodedStringNullTerminated text = (PairedTextEncodedStringNullTerminated) getObject(DataTypes.OBJ_TEXT);
        return (String) text.getValue().getMapping().get(index).getKey();
    }


    public String getValueAtIndex(int index)
    {
        PairedTextEncodedStringNullTerminated text = (PairedTextEncodedStringNullTerminated) getObject(DataTypes.OBJ_TEXT);
        return (String) text.getValue().getMapping().get(index).getValue();
    }


    public int getNumberOfPairs()
    {
        PairedTextEncodedStringNullTerminated text = (PairedTextEncodedStringNullTerminated) getObject(DataTypes.OBJ_TEXT);
        return text.getValue().getNumberOfPairs();
    }

    public String getText()
    {
        PairedTextEncodedStringNullTerminated text = (PairedTextEncodedStringNullTerminated) getObject(DataTypes.OBJ_TEXT);
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (Pair entry : text.getValue().getMapping())
        {
            sb.append(entry.getKey() + '\0' + entry.getValue());
            if (count != getNumberOfPairs())
            {
                sb.append('\0');
            }
            count++;
        }
        return sb.toString();
    }

    public String getUserFriendlyValue()
    {
        return getText();
    }
}
