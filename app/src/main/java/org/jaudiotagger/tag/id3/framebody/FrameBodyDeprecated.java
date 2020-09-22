package org.jaudiotagger.tag.id3.framebody;


public class FrameBodyDeprecated extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    private AbstractID3v2FrameBody originalFrameBody;



    public FrameBodyDeprecated(AbstractID3v2FrameBody frameBody)
    {
        this.originalFrameBody = frameBody;
    }


    public FrameBodyDeprecated(FrameBodyDeprecated copyObject)
    {
        super(copyObject);
    }



    public String getIdentifier()
    {
        return originalFrameBody.getIdentifier();
    }


    public int getSize()
    {
        return originalFrameBody.getSize();
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof FrameBodyDeprecated))
        {
            return false;
        }

        FrameBodyDeprecated object = (FrameBodyDeprecated) obj;
        return this.getIdentifier().equals(object.getIdentifier()) && super.equals(obj);
    }


    public AbstractID3v2FrameBody getOriginalFrameBody()
    {
        return originalFrameBody;
    }


    public String toString()
    {
        return getIdentifier();
    }


    protected void setupObjectList()
    {

    }

    public String getBriefDescription()
    {
        //TODO When is this null, it seems it can be but Im not sure why
        if (originalFrameBody != null)
        {
            return originalFrameBody.getBriefDescription();
        }
        return "";
    }
}
