package com.ithena.krishna.ithenaMusic.models;

import java.util.ArrayList;
import java.util.List;


public class RecentlyPlayed {
    private List<UnifiedTrack> recentlyPlayed;

    public RecentlyPlayed() {
        recentlyPlayed = new ArrayList<>();
    }

    public List<UnifiedTrack> getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public void setRecentlyPlayed(List<UnifiedTrack> recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

    public void addSong(UnifiedTrack track){
        recentlyPlayed.add(track);
    }
}
