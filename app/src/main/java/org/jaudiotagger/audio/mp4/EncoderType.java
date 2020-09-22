package org.jaudiotagger.audio.mp4;


public enum EncoderType
{
    AAC("AAC"),
    DRM_AAC("DRM AAC"),
    APPLE_LOSSLESS("Apple Lossless"),;

    private String description;

    EncoderType(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}
