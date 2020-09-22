package org.jaudiotagger.audio.exceptions;


public class CannotReadVideoException extends CannotReadException
{

    public CannotReadVideoException()
    {
        super();
    }

    public CannotReadVideoException(Throwable ex)
    {
        super(ex);
    }


    public CannotReadVideoException(String message)
    {
        super(message);
    }


    public CannotReadVideoException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
