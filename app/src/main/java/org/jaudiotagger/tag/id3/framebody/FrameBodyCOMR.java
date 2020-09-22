
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.ReceivedAsTypes;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class FrameBodyCOMR extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyCOMR()
    {
        //        this.setObject("Text Encoding", new Byte((byte) 0));
        //        this.setObject("Price String", "");
        //        this.setObject("Valid Until", "");
        //        this.setObject("Contact URL", "");
        //        this.setObject("Recieved As", new Byte((byte) 0));
        //        this.setObject("Name Of Seller", "");
        //        this.setObject(ObjectTypes.OBJ_DESCRIPTION, "");
        //        this.setObject("Picture MIME Type", "");
        //        this.setObject("Seller Logo", new byte[0]);
    }

    public FrameBodyCOMR(FrameBodyCOMR body)
    {
        super(body);
    }


    public FrameBodyCOMR(byte textEncoding, String priceString, String validUntil, String contactUrl, byte recievedAs, String nameOfSeller, String description, String mimeType, byte[] sellerLogo)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setObjectValue(DataTypes.OBJ_PRICE_STRING, priceString);
        this.setObjectValue(DataTypes.OBJ_VALID_UNTIL, validUntil);
        this.setObjectValue(DataTypes.OBJ_CONTACT_URL, contactUrl);
        this.setObjectValue(DataTypes.OBJ_RECIEVED_AS, recievedAs);
        this.setObjectValue(DataTypes.OBJ_SELLER_NAME, nameOfSeller);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType);
        this.setObjectValue(DataTypes.OBJ_SELLER_LOGO, sellerLogo);
    }


    public FrameBodyCOMR(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_COMMERCIAL_FRAME;
    }


    public String getOwner()
    {
        return (String) getObjectValue(DataTypes.OBJ_OWNER);
    }


    public void getOwner(String description)
    {
        setObjectValue(DataTypes.OBJ_OWNER, description);
    }


    public void write(ByteArrayOutputStream tagBuffer)
    {
        if (!((AbstractString) getObject(DataTypes.OBJ_SELLER_NAME)).canBeEncoded())
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded())
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_PRICE_STRING, this));
        objectList.add(new StringDate(DataTypes.OBJ_VALID_UNTIL, this));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_CONTACT_URL, this));
        objectList.add(new NumberHashMap(DataTypes.OBJ_RECIEVED_AS, this, ReceivedAsTypes.RECEIVED_AS_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_SELLER_NAME, this));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_MIME_TYPE, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_SELLER_LOGO, this));
    }
}
