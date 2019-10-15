package com.example.gdk19_nukipratama;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://api.themoviedb.org/3/discover/";
    private static final String SEARCH_BASE_URL = "http://api.themoviedb.org/3/search/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getSearch() {
        retrofit = new Retrofit.Builder()
                .baseUrl(SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
