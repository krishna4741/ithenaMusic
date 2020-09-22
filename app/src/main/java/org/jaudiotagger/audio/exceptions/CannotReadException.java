
package org.jaudiotagger.audio.exceptions;


public class CannotReadException extends Exception
{

    public CannotReadException()
    {
        super();
    }

    public CannotReadException(Throwable ex)
    {
        super(ex);
    }


    public CannotReadException(String message)
    {
        super(message);
    }


    public CannotReadException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
