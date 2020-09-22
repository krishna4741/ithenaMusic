
package org.jaudiotagger.tag;


public class InvalidTagException extends TagException
{

    public InvalidTagException()
    {
    }


    public InvalidTagException(Throwable ex)
    {
        super(ex);
    }


    public InvalidTagException(String msg)
    {
        super(msg);
    }


    public InvalidTagException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}