package com.ithena.krishna.ithenaMusic.utilities.comparators;

import com.ithena.krishna.ithenaMusic.models.Artist;

import java.util.Comparator;



public class ArtistComparator implements Comparator<Artist> {

    @Override
    public int compare(Artist lhs, Artist rhs) {
        return lhs.getName().toString().compareTo(rhs.getName().toString());
    }
}
