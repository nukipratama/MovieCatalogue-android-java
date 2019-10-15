package com.example.gdk19_nukipratama.Show;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Show> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;


    public List<Show> getResults() {
        return results;
    }
}
