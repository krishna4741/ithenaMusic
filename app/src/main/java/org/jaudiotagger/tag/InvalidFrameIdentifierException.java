
package org.jaudiotagger.tag;


public class InvalidFrameIdentifierException extends InvalidFrameException
{

    public InvalidFrameIdentifierException()
    {
    }


    public InvalidFrameIdentifierException(Throwable ex)
    {
        super(ex);
    }


    public InvalidFrameIdentifierException(String msg)
    {
        super(msg);
    }


    public InvalidFrameIdentifierException(String msg, Throwable ex)
    {
        super(msg, ex);
    }

}
