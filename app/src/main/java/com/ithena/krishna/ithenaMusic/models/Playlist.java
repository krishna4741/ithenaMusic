package com.ithena.krishna.ithenaMusic.models;

import java.util.ArrayList;
import java.util.List;


public class Playlist {
    private List<UnifiedTrack> songList;
    private String playlistName;

    public Playlist(String name) {
        playlistName = name;
        songList = new ArrayList<UnifiedTrack>();
    }

    public Playlist(List<UnifiedTrack> songList, String playlistName) {
        this.songList = songList;
        this.playlistName = playlistName;
    }

    public List<UnifiedTrack> getSongList() {
        return songList;
    }

    public void setSongList(List<UnifiedTrack> songList) {
        this.songList = songList;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void addSong(UnifiedTrack track) {
        songList.add(track);
    }

}
