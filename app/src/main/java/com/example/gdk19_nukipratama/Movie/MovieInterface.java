package com.example.gdk19_nukipratama.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieInterface {
    @GET("movie/")
    Call<MovieResponse> getMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/")
    Call<MovieResponse> getRelease(@Query("api_key") String apiKey, @Query("language") String language, @Query("primary_release_date.gte") String date1, @Query("primary_release_date.lte") String date2);

    @GET("movie/")
    Call<MovieResponse> getSearchMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query);

}
