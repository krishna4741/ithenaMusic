package org.jaudiotagger.tag.id3.valuepair;

import org.jaudiotagger.tag.reference.GenreTypes;

import java.util.Collections;
import java.util.List;


public class V2GenreTypes
{
    private static V2GenreTypes v2GenresTypes;

    private V2GenreTypes()
    {

    }

    public static V2GenreTypes getInstanceOf()
    {
        if (v2GenresTypes == null)
        {
            v2GenresTypes = new V2GenreTypes();
        }
        return v2GenresTypes;
    }


    public List<String> getAlphabeticalValueList()
    {
        List<String> genres = GenreTypes.getInstanceOf().getAlphabeticalValueList();
        genres.add(ID3V2ExtendedGenreTypes.CR.getDescription());
        genres.add(ID3V2ExtendedGenreTypes.RX.getDescription());

        //Sort
        Collections.sort(genres);
        return genres;
    }
}
