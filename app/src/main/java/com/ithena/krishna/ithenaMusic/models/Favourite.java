package com.ithena.krishna.ithenaMusic.models;

import java.util.ArrayList;
import java.util.List;


public class Favourite {
    private List<UnifiedTrack> favourite;

    public Favourite() {
        favourite = new ArrayList<>();
    }

    public List<UnifiedTrack> getFavourite() {
        return favourite;
    }

    public void setFavourite(List<UnifiedTrack> favourite) {
        this.favourite = favourite;
    }
}
