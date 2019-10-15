package com.example.gdk19_nukipratama.Movie;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.gdk19_nukipratama.DB.Movie.MovieDB;
import com.example.gdk19_nukipratama.DB.Movie.MovieData;
import com.example.gdk19_nukipratama.DetailActivity;
import com.example.gdk19_nukipratama.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.CardViewHolder> {
    private MovieDB db;
    private List<Movie> movies;
    private int rowLayout;

    public ListMovieAdapter(List<Movie> movies, int rowLayout) {
        this.movies = movies;
        this.rowLayout = rowLayout;
    }


    @NonNull
    @Override
    public ListMovieAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = Room.databaseBuilder(parent.getContext(), MovieDB.class, "tb_movie").allowMainThreadQueries().build();
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {

        final Movie movie = movies.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/original" + movie.getImg()).fit().into(holder.imgPhoto);
        holder.tvName.setText(movie.getName());
        SimpleDateFormat month_date = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String actualDate = movie.getDate();

        Date date = null;
        try {
            date = sdf.parse(actualDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String full_date = month_date.format(date);

        holder.tvDate.setText(full_date);
        holder.tvDesc.setText(movie.getDesc());
        holder.tvVote.setText(String.valueOf(movie.getVote()));
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie movie = new Movie(movies.get(position).getId(), movies.get(position).getName(), movies.get(position).getDesc(), movies.get(position).getDate(), movies.get(position).getImg(), movies.get(position).getVote());
                Intent detail = new Intent(view.getContext(), DetailActivity.class);
                detail.putExtra("type", 1);
                detail.putExtra("id", movies.get(position).getId());
                detail.putExtra(DetailActivity.EXTRA_DETAIL, movie);
                view.getContext().startActivity(detail);
            }
        });
        if (getDB(movie.getId()) != 1) {
            holder.btnFav.setText("Add to Favorite");
            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_playlist_add_black_24dp, 0);
        } else {
            holder.btnFav.setText("Remove Favorite");
            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_forever_black_24dp, 0);

        }


        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getDB(movie.getId()) == 1) {
                    MovieData sh = new MovieData();
                    sh.setBarangId(movie.getId());
                    sh.setJudul(movie.getName());
                    sh.setDeskripsi(movie.getDesc());
                    sh.setTanggal(movie.getDate());
                    sh.setRating(String.valueOf(movie.getVote()));
                    sh.setGambar(movie.getImg());
                    deleteFav(sh, movie, holder);
                    holder.btnFav.setText("Add to Favorite");
                    holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_playlist_add_black_24dp, 0);
                } else {
                    MovieData sh = new MovieData();
                    sh.setBarangId(movie.getId());
                    sh.setJudul(movie.getName());
                    sh.setDeskripsi(movie.getDesc());
                    sh.setTanggal(movie.getDate());
                    sh.setRating(String.valueOf(movie.getVote()));
                    sh.setGambar(movie.getImg());
                    insertFav(sh, movie, holder);
                    holder.btnFav.setText("Remove Favorite");
                    holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_forever_black_24dp, 0);
                }

            }
        });

    }

    private int getDB(final int id) {
        int check = db.movieDAO().checkFav(id);
        return check;
    }

    private void insertFav(final MovieData sh, final Movie movie, @NonNull final ListMovieAdapter.CardViewHolder holder) {

        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.movieDAO().insertMovie(sh);
                return status;
            }

            @Override
            protected void onPostExecute(Long status) {
                holder.btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getDB(movie.getId()) == 1) {
                            MovieData sh = new MovieData();
                            sh.setBarangId(movie.getId());
                            sh.setJudul(movie.getName());
                            sh.setDeskripsi(movie.getDesc());
                            sh.setTanggal(movie.getDate());
                            sh.setRating(String.valueOf(movie.getVote()));
                            sh.setGambar(movie.getImg());
                            deleteFav(sh, movie, holder);
                            holder.btnFav.setText("Add to Favorite");
                            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_playlist_add_black_24dp, 0);
                        } else {
                            MovieData sh = new MovieData();
                            sh.setBarangId(movie.getId());
                            sh.setJudul(movie.getName());
                            sh.setDeskripsi(movie.getDesc());
                            sh.setTanggal(movie.getDate());
                            sh.setRating(String.valueOf(movie.getVote()));
                            sh.setGambar(movie.getImg());
                            insertFav(sh, movie, holder);
                            holder.btnFav.setText("Remove Favorite");
                            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_forever_black_24dp, 0);
                        }

                    }
                });

            }
        }.execute();
    }

    private void deleteFav(final MovieData sh, final Movie movie, @NonNull final ListMovieAdapter.CardViewHolder holder) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.movieDAO().deleteMovie(sh);
                return status;
            }

            @Override
            protected void onPostExecute(Long status) {
                holder.btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getDB(movie.getId()) == 1) {
                            MovieData sh = new MovieData();
                            sh.setBarangId(movie.getId());
                            sh.setJudul(movie.getName());
                            sh.setDeskripsi(movie.getDesc());
                            sh.setTanggal(movie.getDate());
                            sh.setRating(String.valueOf(movie.getVote()));
                            sh.setGambar(movie.getImg());
                            deleteFav(sh, movie, holder);
                            holder.btnFav.setText("Add to Favorite");
                            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_playlist_add_black_24dp, 0);
                        } else {
                            MovieData sh = new MovieData();
                            sh.setBarangId(movie.getId());
                            sh.setJudul(movie.getName());
                            sh.setDeskripsi(movie.getDesc());
                            sh.setTanggal(movie.getDate());
                            sh.setRating(String.valueOf(movie.getVote()));
                            sh.setGambar(movie.getImg());
                            insertFav(sh, movie, holder);
                            holder.btnFav.setText("Remove Favorite");
                            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_forever_black_24dp, 0);
                        }

                    }
                });

            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvDate, tvDesc;
        Button btnMore, btnFav;
        TextView tvVote;

        CardViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
            tvVote = itemView.findViewById(R.id.tv_rating);
            btnMore = itemView.findViewById(R.id.btn_more);
            btnFav = itemView.findViewById(R.id.btn_fav);
        }

    }

}