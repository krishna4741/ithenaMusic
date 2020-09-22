package org.jaudiotagger.tag;


public class FieldDataInvalidException extends TagException
{

    public FieldDataInvalidException()
    {
    }


    public FieldDataInvalidException(Throwable ex)
    {
        super(ex);
    }


    public FieldDataInvalidException(String msg)
    {
        super(msg);
    }


    public FieldDataInvalidException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}
