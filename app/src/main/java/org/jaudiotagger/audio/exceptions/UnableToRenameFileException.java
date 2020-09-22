package org.jaudiotagger.audio.exceptions;

import java.io.IOException;


public class UnableToRenameFileException extends IOException
{
    public UnableToRenameFileException(String message)
    {
        super(message);
    }
}
