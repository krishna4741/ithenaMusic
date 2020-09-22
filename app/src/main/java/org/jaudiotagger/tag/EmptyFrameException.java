
package org.jaudiotagger.tag;


public class EmptyFrameException extends InvalidFrameException
{

    public EmptyFrameException()
    {
    }


    public EmptyFrameException(Throwable ex)
    {
        super(ex);
    }


    public EmptyFrameException(String msg)
    {
        super(msg);
    }


    public EmptyFrameException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}
