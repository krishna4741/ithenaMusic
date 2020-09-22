package org.jaudiotagger.tag.id3.valuepair;


public enum ID3V2ExtendedGenreTypes
{
    RX("Remix"),
    CR("Cover");

    private String description;

    ID3V2ExtendedGenreTypes(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}
