
package org.jaudiotagger.tag;


public class InvalidFrameException extends InvalidTagException
{

    public InvalidFrameException()
    {
    }


    public InvalidFrameException(Throwable ex)
    {
        super(ex);
    }


    public InvalidFrameException(String msg)
    {
        super(msg);
    }


    public InvalidFrameException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}
