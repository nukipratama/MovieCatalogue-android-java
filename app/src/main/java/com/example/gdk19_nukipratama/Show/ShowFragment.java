package com.example.gdk19_nukipratama.Show;


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


public class ShowFragment extends Fragment {
    private final static String API_KEY = "fd779f23434a798e13c0d6e760ee3075";
    private RecyclerView rvShows;
    private ProgressBar progressBar;
    private ArrayList<Show> shows;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList savedRecyclerLayoutState;

    public ShowFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        rvShows = view.findViewById(R.id.rv_show);
        progressBar = view.findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(getActivity());
        rvShows.setLayoutManager(layoutManager);
        if (savedInstanceState == null) {
            showLoading(true);
            showShowList();
        } else {
            savedRecyclerLayoutState = savedInstanceState.getParcelableArrayList("key");
            shows = savedRecyclerLayoutState;
            rvShows.setAdapter(new ListShowAdapter(savedRecyclerLayoutState, R.layout.item_row_show));
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
                Toast.makeText(getContext(), getString(R.string.searchQuery) + " " + query + " " + getString(R.string.shQuery), Toast.LENGTH_SHORT).show();
                searchShowList(query);
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
                showShowList();
                return false;
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", shows);
        super.onSaveInstanceState(outState);

    }

    private void searchShowList(String query) {
        Locale current = getResources().getConfiguration().locale;
        ShowInterface apiService = ApiClient.getSearch().create(ShowInterface.class);
        Call<ShowResponse> call = apiService.getSearchShows(API_KEY, current.toLanguageTag(), query);
        call.enqueue(new Callback<ShowResponse>() {
            @Override
            public void onResponse(Call<ShowResponse> call, Response<ShowResponse> response) {
                shows = (ArrayList<Show>) response.body().getResults();
                rvShows.setAdapter(new ListShowAdapter(shows, R.layout.item_row_show));
                showLoading(false);
                rvShows.scheduleLayoutAnimation();
            }

            @Override
            public void onFailure(Call<ShowResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void showShowList() {
        Locale current = getResources().getConfiguration().locale;
        ShowInterface apiService = ApiClient.getClient().create(ShowInterface.class);
        Call<ShowResponse> call = apiService.getShows(API_KEY, current.toLanguageTag());
        call.enqueue(new Callback<ShowResponse>() {
            @Override
            public void onResponse(Call<ShowResponse> call, Response<ShowResponse> response) {
                shows = (ArrayList<Show>) response.body().getResults();
                rvShows.setAdapter(new ListShowAdapter(shows, R.layout.item_row_show));
                showLoading(false);
                rvShows.scheduleLayoutAnimation();
            }

            @Override
            public void onFailure(Call<ShowResponse> call, Throwable t) {
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
