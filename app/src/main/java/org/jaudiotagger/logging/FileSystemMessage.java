package org.jaudiotagger.logging;


public enum FileSystemMessage
{
    ACCESS_IS_DENIED("Access is denied"),

    ;
    String msg;

    FileSystemMessage(String msg)
    {
        this.msg = msg;
    }

    public String getMsg()
    {
        return msg;
    }
}
