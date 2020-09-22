package com.ithena.krishna.ithenaMusic.interfaces;

import com.ithena.krishna.ithenaMusic.Config;
import com.ithena.krishna.ithenaMusic.models.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.http.GET;


public interface StreamService {
    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    Call<List<Track>> getTracks(@Query("q") String query, @Query("limit") int limit);
}
