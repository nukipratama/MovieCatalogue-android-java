package com.example.gdk19_nukipratama;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.gdk19_nukipratama.DB.Movie.MovieDB;
import com.example.gdk19_nukipratama.DB.Movie.MovieData;
import com.example.gdk19_nukipratama.DB.Show.ShowDB;
import com.example.gdk19_nukipratama.DB.Show.ShowData;
import com.example.gdk19_nukipratama.Movie.Movie;
import com.example.gdk19_nukipratama.Show.Show;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_DETAIL = "extra_detail";
    private TextView tvName, tvDate, tvDesc;
    private ImageView imgPoster;
    private int type, id;
    private ProgressBar progressBar;
    private ShowDB showDB;
    private MovieDB movieDB;
    private ShowData sh;
    private MovieData mv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        progressBar = findViewById(R.id.progressBar);
        showLoading(true);
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getIntExtra("id", 0);
        sh = new ShowData();
        mv = new MovieData();
        if (type == 1) {
            movieDB = Room.databaseBuilder(getApplicationContext(), MovieDB.class, "tb_movie").allowMainThreadQueries().build();
            Movie parcel = getIntent().getParcelableExtra(EXTRA_DETAIL);
            movieParcel(parcel, id, getDB(type, id), type);
        } else if (type == 2) {
            showDB = Room.databaseBuilder(getApplicationContext(), ShowDB.class, "tb_show").allowMainThreadQueries().build();
            Show parcel = getIntent().getParcelableExtra(EXTRA_DETAIL);
            showParcel(parcel, id, getDB(type, id), type);
        } else {
            finish();
        }
        showLoading(false);
    }


    private int getDB(final int type, final int id) {
        switch (type) {
            case 1:
                return movieDB.movieDAO().checkFav(id);
            case 2:
                return showDB.showDAO().checkFav(id);
        }
        return 0;
    }

    private void deleteFav(final int id, final int type, final ShowData sh, final MovieData mv, final ImageButton btnFav) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                long status;
                switch (type) {
                    case 1:
                        status = movieDB.movieDAO().deleteID(id);
                        return status;
                    case 2:
                        status = showDB.showDAO().deleteID(id);
                        return status;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long status) {
                btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insertFav(id, type, sh, mv, btnFav);
                        btnFav.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                        Toast.makeText(view.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.execute();
    }

    private void insertFav(final int id, final int type, final ShowData sh, final MovieData mv, final ImageButton btnFav) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                long status;
                switch (type) {
                    case 1:
                        status = movieDB.movieDAO().insertMovie(mv);
                        return status;
                    case 2:
                        status = showDB.showDAO().insertShow(sh);
                        return status;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long status) {
                btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFav(id, type, sh, mv, btnFav);
                        btnFav.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                        Toast.makeText(view.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }.execute();
    }


    private void movieParcel(Movie parcel, int id, int favStatus, final int type) {
        actionBar(parcel.getName(), id, favStatus, type);
        tvName = findViewById(R.id.tv_detail_title);
        tvDate = findViewById(R.id.tv_detail_date);
        tvDesc = findViewById(R.id.tv_detail_desc);
        imgPoster = findViewById(R.id.img_item_detail);
        tvName.setText(parcel.getName());
        tvDate.setText(parcel.getDate());
        tvDesc.setText(parcel.getDesc());
        Picasso.get().load("https://image.tmdb.org/t/p/original" + parcel.getImg()).fit().into(imgPoster);
        mv.setBarangId(id);
        mv.setDeskripsi(parcel.getDesc());
        mv.setGambar(parcel.getImg());
        mv.setJudul(parcel.getName());
        mv.setRating(String.valueOf(parcel.getVote()));
        mv.setTanggal(parcel.getDate());
    }

    private void showParcel(Show parcel, int id, int favStatus, final int type) {
        actionBar(parcel.getName(), id, favStatus, type);
        tvName = findViewById(R.id.tv_detail_title);
        tvDate = findViewById(R.id.tv_detail_date);
        tvDesc = findViewById(R.id.tv_detail_desc);
        imgPoster = findViewById(R.id.img_item_detail);
        tvName.setText(parcel.getName());
        tvDate.setText(parcel.getDate());
        tvDesc.setText(parcel.getDesc());
        Picasso.get().load("https://image.tmdb.org/t/p/original" + parcel.getImg()).fit().into(imgPoster);
        sh.setBarangId(id);
        sh.setDeskripsi(parcel.getDesc());
        sh.setGambar(parcel.getImg());
        sh.setJudul(parcel.getName());
        sh.setRating(String.valueOf(parcel.getVote()));
        sh.setTanggal(parcel.getDate());
    }

    private void actionBar(String text, final int id, final int status, final int type) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_actionbar_detail);
            final View view = getSupportActionBar().getCustomView();

            final ImageButton favButton = view.findViewById(R.id.action_fav);
            ImageButton backButton = view.findViewById(R.id.action_back);
            TextView textTitle = view.findViewById(R.id.text_title);
            textTitle.setText(text);

            if (status == 1) {
                favButton.setImageResource(R.drawable.ic_delete_forever_black_24dp);
            }

            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status == 1) {
                        deleteFav(id, type, sh, mv, favButton);
                        favButton.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                        Toast.makeText(view.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        insertFav(id, type, sh, mv, favButton);
                        favButton.setImageResource(R.drawable.ic_delete_forever_black_24dp);
                        Toast.makeText(view.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
