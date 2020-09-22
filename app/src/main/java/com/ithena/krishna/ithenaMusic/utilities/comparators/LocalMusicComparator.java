package com.ithena.krishna.ithenaMusic.utilities.comparators;

import com.ithena.krishna.ithenaMusic.models.LocalTrack;

import java.util.Comparator;



public class LocalMusicComparator implements Comparator<LocalTrack> {

    @Override
    public int compare(LocalTrack lhs, LocalTrack rhs) {
        return lhs.getTitle().toString().compareTo(rhs.getTitle().toString());
    }
}
