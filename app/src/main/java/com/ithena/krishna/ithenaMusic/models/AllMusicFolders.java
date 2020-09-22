package com.ithena.krishna.ithenaMusic.models;

import java.util.ArrayList;
import java.util.List;


public class AllMusicFolders {
    List<MusicFolder> musicFolders;

    public AllMusicFolders() {
        musicFolders = new ArrayList<>();
    }

    public List<MusicFolder> getMusicFolders() {
        return musicFolders;
    }

    public void setMusicFolders(List<MusicFolder> musicFolders) {
        this.musicFolders = musicFolders;
    }
}
