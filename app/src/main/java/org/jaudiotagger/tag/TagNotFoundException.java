
package org.jaudiotagger.tag;


public class TagNotFoundException extends TagException
{

    public TagNotFoundException()
    {
    }


    public TagNotFoundException(Throwable ex)
    {
        super(ex);
    }


    public TagNotFoundException(String msg)
    {
        super(msg);
    }


    public TagNotFoundException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}