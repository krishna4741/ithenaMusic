package org.jaudiotagger.audio;


public enum SupportedFileFormat
{
    OGG("ogg"),
    MP3("mp3"),
    FLAC("flac"),
    MP4("mp4"),
    M4A("m4a"),
    M4P("m4p"),
    WMA("wma"),
    WAV("wav"),
    RA("ra"),
    RM("rm"),
    M4B("m4b"),
    AIF("aif");

    private String filesuffix;


    SupportedFileFormat(String filesuffix)
    {
        this.filesuffix = filesuffix;
    }


    public String getFilesuffix()
    {
        return filesuffix;
    }
}
