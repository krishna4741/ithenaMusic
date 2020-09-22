package com.ithena.krishna.ithenaMusic.models;

import java.util.List;


public class Artist {
    private String Name;
    private List<LocalTrack> artistSongs;

    public Artist(String name, List<LocalTrack> artistSongs) {
        Name = name;
        this.artistSongs = artistSongs;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<LocalTrack> getArtistSongs() {
        return artistSongs;
    }

    public void setArtistSongs(List<LocalTrack> artistSongs) {
        this.artistSongs = artistSongs;
    }
}
