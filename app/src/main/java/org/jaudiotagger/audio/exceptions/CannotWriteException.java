
package org.jaudiotagger.audio.exceptions;


public class CannotWriteException extends Exception
{

    public CannotWriteException()
    {
        super();
    }


    public CannotWriteException(String message)
    {
        super(message);
    }


    public CannotWriteException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public CannotWriteException(Throwable cause)
    {
        super(cause);

    }

}
