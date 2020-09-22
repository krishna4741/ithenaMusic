package com.ithena.krishna.ithenaMusic.utilities.comparators;

import com.ithena.krishna.ithenaMusic.models.Album;

import java.util.Comparator;



public class AlbumComparator implements Comparator<Album> {

    @Override
    public int compare(Album lhs, Album rhs) {
        return lhs.getName().toString().compareTo(rhs.getName().toString());
    }
}
