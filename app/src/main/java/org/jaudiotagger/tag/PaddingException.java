
package org.jaudiotagger.tag;


public class PaddingException extends InvalidFrameIdentifierException
{

    public PaddingException()
    {
    }


    public PaddingException(Throwable ex)
    {
        super(ex);
    }


    public PaddingException(String msg)
    {
        super(msg);
    }


    public PaddingException(String msg, Throwable ex)
    {
        super(msg, ex);
    }

}