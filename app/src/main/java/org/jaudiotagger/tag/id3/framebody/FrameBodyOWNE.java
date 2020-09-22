
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3TextEncodingConversion;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class FrameBodyOWNE extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyOWNE()
    {
        //        this.setObject("Text Encoding", new Byte((byte) 0));
        //        this.setObject("Price Paid", "");
        //        this.setObject("Date Of Purchase", "");
        //        this.setObject("Seller", "");
    }

    public FrameBodyOWNE(FrameBodyOWNE body)
    {
        super(body);
    }


    public FrameBodyOWNE(byte textEncoding, String pricePaid, String dateOfPurchase, String seller)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setObjectValue(DataTypes.OBJ_PRICE_PAID, pricePaid);
        this.setObjectValue(DataTypes.OBJ_PURCHASE_DATE, dateOfPurchase);
        this.setObjectValue(DataTypes.OBJ_SELLER_NAME, seller);
    }


    public FrameBodyOWNE(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_OWNERSHIP;
    }


    public void write(ByteArrayOutputStream tagBuffer)
    {
        //Ensure valid for type
        setTextEncoding(ID3TextEncodingConversion.getTextEncoding(getHeader(), getTextEncoding()));

        //Ensure valid for data
        if (!((AbstractString) getObject(DataTypes.OBJ_SELLER_NAME)).canBeEncoded())
        {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(getHeader()));
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_PRICE_PAID, this));
        objectList.add(new StringDate(DataTypes.OBJ_PURCHASE_DATE, this));
        objectList.add(new TextEncodedStringSizeTerminated(DataTypes.OBJ_SELLER_NAME, this));
    }
}
