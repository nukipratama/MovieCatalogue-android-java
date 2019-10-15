package com.example.gdk19_nukipratama.Movie;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gdk19_nukipratama.ApiClient;
import com.example.gdk19_nukipratama.R;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MovieFragment extends Fragment {
    private final String API_KEY = "fd779f23434a798e13c0d6e760ee3075";
    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Movie> movies;
    private ArrayList savedRecyclerLayoutState;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        rvMovies = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(getActivity());
        rvMovies.setLayoutManager(layoutManager);
        if (savedInstanceState == null) {
            showLoading(true);
            showMovieList();
        } else {
            savedRecyclerLayoutState = savedInstanceState.getParcelableArrayList("key");
            movies = savedRecyclerLayoutState;
            rvMovies.setAdapter(new ListMovieAdapter(savedRecyclerLayoutState, R.layout.item_row_movie));
        }

        View searchLayout = ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView();
        final SearchView searchView = searchLayout.findViewById(R.id.search);
        final TextView header = searchLayout.findViewById(R.id.app_title);
        final ImageButton notif = searchLayout.findViewById(R.id.action_bar_reminder);


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header.setVisibility(View.GONE);
                notif.setVisibility(View.GONE);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), getString(R.string.searchQuery) + " " + query + " " + getString(R.string.mvQuery), Toast.LENGTH_SHORT).show();
                searchMovieList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                header.setVisibility(View.VISIBLE);
                notif.setVisibility(View.VISIBLE);
                showMovieList();
                return false;
            }
        });
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", movies);
        super.onSaveInstanceState(outState);

    }

    private void showMovieList() {
        Locale current = getResources().getConfiguration().locale;
        MovieInterface apiService = ApiClient.getClient().create(MovieInterface.class);
        Call<MovieResponse> call = apiService.getMovies(API_KEY, current.toLanguageTag());
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movies = (ArrayList<Movie>) response.body().getResults();
                rvMovies.setAdapter(new ListMovieAdapter(movies, R.layout.item_row_movie));
                showLoading(false);
                rvMovies.scheduleLayoutAnimation();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void searchMovieList(String query) {
        Locale current = getResources().getConfiguration().locale;
        MovieInterface apiService = ApiClient.getSearch().create(MovieInterface.class);
        Call<MovieResponse> call = apiService.getSearchMovies(API_KEY, current.toLanguageTag(), query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movies = (ArrayList<Movie>) response.body().getResults();
                rvMovies.setAdapter(new ListMovieAdapter(movies, R.layout.item_row_movie));
                showLoading(false);
                rvMovies.scheduleLayoutAnimation();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
