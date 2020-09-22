package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.tag.reference.Tagger;


public enum Mp4NonStandardFieldKey
{
    AAPR("AApr", "MM3 Album Art Attributes", Tagger.MEDIA_MONKEY),
    ALFN("Alfn", "MM3 Album Art Unknown", Tagger.MEDIA_MONKEY),
    AMIM("AMIM", "MM3 Album Art MimeType", Tagger.MEDIA_MONKEY),
    ADCP("Adcp", "MM3 Album Art Description", Tagger.MEDIA_MONKEY),
    APTY("Apty", "MM3 Album Art ID3 Picture Type", Tagger.MEDIA_MONKEY);

    private String fieldName;
    private String description;
    private Tagger tagger;

    Mp4NonStandardFieldKey(String fieldName, String description, Tagger tagger)
    {
        this.fieldName = fieldName;
        this.description = description;
        this.tagger = tagger;

    }


    public String getFieldName()
    {
        return fieldName;
    }


    public String getDescription()
    {
        return description;
    }


    public Tagger geTagger()
    {
        return tagger;
    }
}
