
package org.jaudiotagger.tag;


public class InvalidDataTypeException extends InvalidTagException
{

    public InvalidDataTypeException()
    {
    }


    public InvalidDataTypeException(Throwable ex)
    {
        super(ex);
    }


    public InvalidDataTypeException(String msg)
    {
        super(msg);
    }


    public InvalidDataTypeException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}
