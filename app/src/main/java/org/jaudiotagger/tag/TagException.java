
package org.jaudiotagger.tag;


public class TagException extends Exception
{

    public TagException()
    {
    }


    public TagException(Throwable ex)
    {
        super(ex);
    }


    public TagException(String msg)
    {
        super(msg);
    }


    public TagException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}