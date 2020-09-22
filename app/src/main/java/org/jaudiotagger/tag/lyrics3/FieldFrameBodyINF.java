
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;

import java.nio.ByteBuffer;

public class FieldFrameBodyINF extends AbstractLyrics3v2FieldFrameBody
{

    public FieldFrameBodyINF()
    {
        //        this.setObject("Additional Information", "");
    }

    public FieldFrameBodyINF(FieldFrameBodyINF body)
    {
        super(body);
    }


    public FieldFrameBodyINF(String additionalInformation)
    {
        this.setObjectValue("Additional Information", additionalInformation);
    }


    public FieldFrameBodyINF(ByteBuffer byteBuffer) throws InvalidTagException
    {
        this.read(byteBuffer);

    }


    public void setAdditionalInformation(String additionalInformation)
    {
        setObjectValue("Additional Information", additionalInformation);
    }


    public String getAdditionalInformation()
    {
        return (String) getObjectValue("Additional Information");
    }


    public String getIdentifier()
    {
        return "INF";
    }


    protected void setupObjectList()
    {
        objectList.add(new StringSizeTerminated("Additional Information", this));
    }
}
