package com.example.gdk19_nukipratama;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gdk19_nukipratama.Favorites.FavoritesActivity;
import com.example.gdk19_nukipratama.Movie.MovieFragment;
import com.example.gdk19_nukipratama.Show.ShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    MovieFragment movieFragment = new MovieFragment();
                    fragmentStart(movieFragment);
                    return true;
                case R.id.navigation_show:
                    ShowFragment showFragment = new ShowFragment();
                    fragmentStart(showFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movie);
        }
    }

    private void actionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_actionbar);
            View view = getSupportActionBar().getCustomView();

            ImageButton favButton = view.findViewById(R.id.action_bar_fav);
            ImageButton langButton = view.findViewById(R.id.action_bar_lang);
            ImageButton notifButton = view.findViewById(R.id.action_bar_reminder);

            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getApplicationContext(), FavoritesActivity.class);
                    startActivity(mIntent);
                }
            });

            langButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                    startActivity(mIntent);
                }
            });

            notifButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(getApplicationContext(), ReminderActivity.class);
                    startActivity(mIntent);
                }
            });
        }

    }


    private void fragmentStart(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


}

