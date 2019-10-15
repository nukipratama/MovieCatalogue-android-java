package com.example.gdk19_nukipratama.Show;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShowInterface {
    @GET("tv/")
    Call<ShowResponse> getShows(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("tv/")
    Call<ShowResponse> getSearchShows(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query);
}
