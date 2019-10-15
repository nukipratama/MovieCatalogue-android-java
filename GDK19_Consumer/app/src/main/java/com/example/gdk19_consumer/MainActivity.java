package com.example.gdk19_consumer;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.gdk19_consumer.db.DatabaseContract.CONTENT_URI;


public class MainActivity extends AppCompatActivity {

    Cursor c;
    ListView lvMovies;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        c = getApplicationContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        mAdapter = new MovieAdapter(getApplicationContext(), c, true);
        mAdapter.notifyDataSetChanged();
        lvMovies = findViewById(R.id.rv_movie);
        lvMovies.setAdapter(mAdapter);
        pb.setVisibility(View.GONE);

        Button btnrefresh = findViewById(R.id.btn_refresh);
        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = getApplicationContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
                c.moveToFirst();
                mAdapter = new MovieAdapter(getApplicationContext(), c, true);
                mAdapter.notifyDataSetChanged();
                lvMovies = findViewById(R.id.rv_movie);
                lvMovies.setAdapter(mAdapter);
                pb.setVisibility(View.GONE);
                lvMovies.scheduleLayoutAnimation();
            }
        });
    }


}




