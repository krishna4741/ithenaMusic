package org.jaudiotagger.tag.mp4.atom;


public enum Mp4RatingValue
{
    CLEAN("Clean", 2),
    EXPLICIT("Explicit", 4);

    private String description;
    private int id;



    Mp4RatingValue(String description, int id)
    {
        this.description = description;
        this.id = id;
    }


    public int getId()
    {
        return id;
    }


    public String getDescription()
    {
        return description;
    }


}
