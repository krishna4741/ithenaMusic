package org.jaudiotagger.audio.exceptions;

import java.io.IOException;


public class UnableToModifyFileException extends IOException
{
    public UnableToModifyFileException(String message)
    {
        super(message);
    }
}
