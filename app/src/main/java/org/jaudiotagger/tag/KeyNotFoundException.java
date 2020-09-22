package org.jaudiotagger.tag;


public class KeyNotFoundException extends RuntimeException
{

    public KeyNotFoundException()
    {
    }


    public KeyNotFoundException(Throwable ex)
    {
        super(ex);
    }


    public KeyNotFoundException(String msg)
    {
        super(msg);
    }


    public KeyNotFoundException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}
