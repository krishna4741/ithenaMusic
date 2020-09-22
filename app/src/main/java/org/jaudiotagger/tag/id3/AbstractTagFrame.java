
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.utils.EqualsUtil;


public abstract class AbstractTagFrame extends AbstractTagItem
{


    protected AbstractTagFrameBody frameBody;

    public AbstractTagFrame()
    {
    }


    public AbstractTagFrame(AbstractTagFrame copyObject)
    {
        this.frameBody = (AbstractTagFrameBody) ID3Tags.copyObject(copyObject.frameBody);
        this.frameBody.setHeader(this);
    }


    public void setBody(AbstractTagFrameBody frameBody)
    {
        this.frameBody = frameBody;
        this.frameBody.setHeader(this);
    }


    public AbstractTagFrameBody getBody()
    {
        return this.frameBody;
    }


    public boolean isSubsetOf(Object obj)
    {
        if (!(obj instanceof AbstractTagFrame))
        {
            return false;
        }

        if ((frameBody == null) && (((AbstractTagFrame) obj).frameBody == null))
        {
            return true;
        }

        if ((frameBody == null) || (((AbstractTagFrame) obj).frameBody == null))
        {
            return false;
        }

        return frameBody.isSubsetOf(((AbstractTagFrame) obj).frameBody) && super.isSubsetOf(obj);

    }


    public boolean equals(Object obj)
    {
        if ( this == obj ) return true;
        if (!(obj instanceof AbstractTagFrame))
        {
            return false;
        }

        AbstractTagFrame that = (AbstractTagFrame) obj;
        return
              EqualsUtil.areEqual(this.getIdentifier(), that.getIdentifier()) &&
              EqualsUtil.areEqual(this.frameBody, that.frameBody) &&
              super.equals(that);

    }

    @Override
    public String toString ()
    {
        return getBody ().toString ();
    }
}
