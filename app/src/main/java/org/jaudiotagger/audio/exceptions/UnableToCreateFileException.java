package org.jaudiotagger.audio.exceptions;

import java.io.IOException;


public class UnableToCreateFileException extends IOException
{
    public UnableToCreateFileException(String message)
    {
        super(message);
    }
}
