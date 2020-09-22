package org.jaudiotagger.audio.ogg;


public enum VorbisVersion
{
    VERSION_ONE("Ogg Vorbis v1");

    //The display name for this version
    private String displayName;


    VorbisVersion(String displayName)
    {
        this.displayName = displayName;
    }

    public String toString()
    {
        return displayName;
    }
}
