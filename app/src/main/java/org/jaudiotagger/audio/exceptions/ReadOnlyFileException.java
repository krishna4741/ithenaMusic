
package org.jaudiotagger.audio.exceptions;


public class ReadOnlyFileException extends Exception
{

    public ReadOnlyFileException()
    {
    }

    public ReadOnlyFileException(Throwable ex)
    {
        super(ex);
    }


    public ReadOnlyFileException(String msg)
    {
        super(msg);
    }

    public ReadOnlyFileException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}
