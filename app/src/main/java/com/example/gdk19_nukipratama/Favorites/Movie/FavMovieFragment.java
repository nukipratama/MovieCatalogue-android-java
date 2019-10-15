package com.example.gdk19_nukipratama.Favorites.Movie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.gdk19_nukipratama.DB.Movie.MovieDB;
import com.example.gdk19_nukipratama.DB.Movie.MovieData;
import com.example.gdk19_nukipratama.R;

import java.util.ArrayList;
import java.util.Arrays;

public class FavMovieFragment extends Fragment {
    private MovieDB db;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MovieData> daftar;
    private RecyclerView rvdaftar;
    private ProgressBar progressBar;
    private ArrayList savedRecyclerLayoutState;

    public FavMovieFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        daftar = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                MovieDB.class, "tb_movie").allowMainThreadQueries().build();
        rvdaftar = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(getActivity());
        rvdaftar.setLayoutManager(layoutManager);
        if (savedInstanceState == null) {
            movieLoading(true);
            getDB();
        } else {
            savedRecyclerLayoutState = savedInstanceState.getParcelableArrayList("key");
            daftar = savedRecyclerLayoutState;
            rvdaftar.setAdapter(new MovieAdapter(daftar, getActivity().getApplicationContext()));
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", daftar);
        super.onSaveInstanceState(outState);

    }


    private void getDB() {
        new AsyncTask<Void, Void, ArrayList<MovieData>>() {
            @Override
            protected ArrayList<MovieData> doInBackground(Void... voids) {
                daftar.addAll(Arrays.asList(db.movieDAO().selectAllMovies()));
                return daftar;
            }

            @Override
            protected void onPostExecute(ArrayList<MovieData> daftar) {
                rvdaftar.setAdapter(new MovieAdapter(daftar, getActivity().getApplicationContext()));
                movieLoading(false);
                rvdaftar.scheduleLayoutAnimation();
            }
        }.execute();
    }

    private void movieLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
