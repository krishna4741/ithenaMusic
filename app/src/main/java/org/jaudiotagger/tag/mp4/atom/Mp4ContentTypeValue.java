package org.jaudiotagger.tag.mp4.atom;



public enum Mp4ContentTypeValue
{
    MOVIE("Movie", 0),
    NORMAL("Normal", 1),
    AUDIO_BOOK("AudioBook", 2),
    BOOKMARK("Whacked Bookmark", 5),
    MUSIC_VIDEO("Music Video", 6),
    SHORT_FILM("Short Film", 9),
    TV_SHOW("TV Show", 10),
    BOOKLET("Booklet", 11);

    private String description;
    private int id;



    Mp4ContentTypeValue(String description, int id)
    {
        this.description = description;
        this.id = id;
    }


    public int getId()
    {
        return id;
    }


    public String getIdAsString()
    {
        return String.valueOf(id);
    }


    public String getDescription()
    {
        return description;
    }


}
