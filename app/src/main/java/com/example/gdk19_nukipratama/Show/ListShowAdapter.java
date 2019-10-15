package com.example.gdk19_nukipratama.Show;

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

import com.example.gdk19_nukipratama.DB.Show.ShowDB;
import com.example.gdk19_nukipratama.DB.Show.ShowData;
import com.example.gdk19_nukipratama.DetailActivity;
import com.example.gdk19_nukipratama.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListShowAdapter extends RecyclerView.Adapter<ListShowAdapter.CardViewHolder> {
    private List<Show> shows;
    private int rowLayout;
    private ShowDB db;

    public ListShowAdapter(List<Show> shows, int rowLayout) {
        this.shows = shows;
        this.rowLayout = rowLayout;
    }

    @NonNull
    @Override
    public ListShowAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = Room.databaseBuilder(parent.getContext(), ShowDB.class, "tb_show").allowMainThreadQueries().build();
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListShowAdapter.CardViewHolder holder, final int position) {
        final Show show = shows.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/original" + show.getImg()).fit().into(holder.imgPhoto);
        holder.tvName.setText(show.getName());

        DateFormat month_dateS = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        SimpleDateFormat sdfS = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String actualDateS = show.getDate();

        Date dateS = null;
        String full_date = null;
        try {
            dateS = sdfS.parse(actualDateS);
            full_date = month_dateS.format(dateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.tvDate.setText(full_date);
        holder.tvDesc.setText(show.getDesc());
        holder.tvVote.setText(String.valueOf(show.getVote()));
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show show = new Show(shows.get(position).getId(), shows.get(position).getName(), shows.get(position).getDesc(), shows.get(position).getDate(), shows.get(position).getImg(), shows.get(position).getVote());
                Intent detail = new Intent(view.getContext(), DetailActivity.class);
                detail.putExtra("type", 2);
                detail.putExtra("id", shows.get(position).getId());
                detail.putExtra(DetailActivity.EXTRA_DETAIL, show);
                view.getContext().startActivity(detail);
            }
        });

        if (getDB(show.getId()) != 1) {
            holder.btnFav.setText("Add to Favorite");
            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_playlist_add_black_24dp, 0);
        } else {
            holder.btnFav.setText("Remove Favorite");
            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_forever_black_24dp, 0);

        }

        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getDB(show.getId()) == 1) {
                    ShowData sh = new ShowData();
                    sh.setBarangId(show.getId());
                    sh.setJudul(show.getName());
                    sh.setDeskripsi(show.getDesc());
                    sh.setTanggal(show.getDate());
                    sh.setRating(String.valueOf(show.getVote()));
                    sh.setGambar(show.getImg());
                    deleteFav(sh, show, holder);
                    holder.btnFav.setText("Add to Favorite");
                    holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_playlist_add_black_24dp, 0);
                } else {
                    ShowData sh = new ShowData();
                    sh.setBarangId(show.getId());
                    sh.setJudul(show.getName());
                    sh.setDeskripsi(show.getDesc());
                    sh.setTanggal(show.getDate());
                    sh.setRating(String.valueOf(show.getVote()));
                    sh.setGambar(show.getImg());
                    insertFav(sh, show, holder);
                    holder.btnFav.setText("Remove Favorite");
                    holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_forever_black_24dp, 0);
                }

            }
        });

    }

    private int getDB(final int id) {
        int check = db.showDAO().checkFav(id);
        return check;
    }

    private void insertFav(final ShowData sh, final Show show, @NonNull final ListShowAdapter.CardViewHolder holder) {

        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.showDAO().insertShow(sh);
                return status;
            }

            @Override
            protected void onPostExecute(Long status) {
                holder.btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getDB(show.getId()) == 1) {
                            ShowData sh = new ShowData();
                            sh.setBarangId(show.getId());
                            sh.setJudul(show.getName());
                            sh.setDeskripsi(show.getDesc());
                            sh.setTanggal(show.getDate());
                            sh.setRating(String.valueOf(show.getVote()));
                            sh.setGambar(show.getImg());
                            deleteFav(sh, show, holder);
                            holder.btnFav.setText("Add to Favorite");
                            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_playlist_add_black_24dp, 0);
                        } else {
                            ShowData sh = new ShowData();
                            sh.setBarangId(show.getId());
                            sh.setJudul(show.getName());
                            sh.setDeskripsi(show.getDesc());
                            sh.setTanggal(show.getDate());
                            sh.setRating(String.valueOf(show.getVote()));
                            sh.setGambar(show.getImg());
                            insertFav(sh, show, holder);
                            holder.btnFav.setText("Remove Favorite");
                            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_forever_black_24dp, 0);
                        }

                    }
                });

            }
        }.execute();
    }

    private void deleteFav(final ShowData sh, final Show show, @NonNull final ListShowAdapter.CardViewHolder holder) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.showDAO().deleteShow(sh);
                return status;
            }

            @Override
            protected void onPostExecute(Long status) {
                holder.btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getDB(show.getId()) == 1) {
                            ShowData sh = new ShowData();
                            sh.setBarangId(show.getId());
                            sh.setJudul(show.getName());
                            sh.setDeskripsi(show.getDesc());
                            sh.setTanggal(show.getDate());
                            sh.setRating(String.valueOf(show.getVote()));
                            sh.setGambar(show.getImg());
                            deleteFav(sh, show, holder);
                            holder.btnFav.setText("Add to Favorite");
                            holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_playlist_add_black_24dp, 0);
                        } else {
                            ShowData sh = new ShowData();
                            sh.setBarangId(show.getId());
                            sh.setJudul(show.getName());
                            sh.setDeskripsi(show.getDesc());
                            sh.setTanggal(show.getDate());
                            sh.setRating(String.valueOf(show.getVote()));
                            sh.setGambar(show.getImg());
                            insertFav(sh, show, holder);
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
        return shows.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvDate, tvDesc;
        Button btnMore, btnFav;
        TextView tvVote;

        CardViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo_show);
            tvName = itemView.findViewById(R.id.tv_item_name_show);
            tvDate = itemView.findViewById(R.id.tv_item_date_show);
            tvDesc = itemView.findViewById(R.id.tv_item_desc_show);
            tvVote = itemView.findViewById(R.id.tv_rating);
            btnMore = itemView.findViewById(R.id.btn_more);
            btnFav = itemView.findViewById(R.id.btn_fav);
        }
    }
}