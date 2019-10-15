package com.example.gdk19_nukipratama.Favorites.Show;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.gdk19_nukipratama.DB.Show.ShowDB;
import com.example.gdk19_nukipratama.DB.Show.ShowData;
import com.example.gdk19_nukipratama.DetailActivity;
import com.example.gdk19_nukipratama.R;
import com.example.gdk19_nukipratama.Show.Show;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    private ArrayList<ShowData> daftar;
    private Context context;
    private ShowDB db;

    public ShowAdapter(ArrayList<ShowData> barangs, Context ctx) {
        daftar = barangs;
        context = ctx;
        db = Room.databaseBuilder(context.getApplicationContext(), ShowDB.class, "tb_show").allowMainThreadQueries().build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_show, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.get().load("https://image.tmdb.org/t/p/original" + daftar.get(position).getGambar()).fit().into(holder.imgPhoto);
        holder.tvName.setText(daftar.get(position).getJudul());
        SimpleDateFormat month_date = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String actualDate = daftar.get(position).getTanggal();

        Date date = null;
        try {
            date = sdf.parse(actualDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String full_date = month_date.format(date);

        holder.tvDate.setText(full_date);
        holder.tvDesc.setText(daftar.get(position).getDeskripsi());
        holder.tvVote.setText(daftar.get(position).getRating());
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show show = new Show(daftar.get(position).getBarangId(), daftar.get(position).getJudul(), daftar.get(position).getDeskripsi(), daftar.get(position).getTanggal(), daftar.get(position).getGambar(), Float.parseFloat(daftar.get(position).getRating()));
                Intent detail = new Intent(view.getContext(), DetailActivity.class);
                detail.putExtra("type", 2);
                detail.putExtra("id", daftar.get(position).getBarangId());
                detail.putExtra(DetailActivity.EXTRA_DETAIL, show);
                view.getContext().startActivity(detail);
            }
        });
        holder.btnFav.setText("Remove Favorite");
        holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_forever_black_24dp, 0);
        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteShow(position);
                Toast.makeText(view.getContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return daftar.size();
    }

    private void onDeleteShow(final int position) {
        new AsyncTask<Integer, Integer, Integer>() {
            int x = position;

            @Override
            protected Integer doInBackground(Integer... position) {
                db.showDAO().deleteShow(daftar.get(x));
                return x;
            }

            @Override
            protected void onPostExecute(Integer x) {
                daftar.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, daftar.size());
            }
        }.execute();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView tvName, tvDate, tvDesc;
        Button btnMore, btnFav;
        TextView tvVote;

        ViewHolder(View itemview) {
            super(itemview);
            imgPhoto = itemview.findViewById(R.id.img_item_photo_show);
            tvName = itemview.findViewById(R.id.tv_item_name_show);
            tvDate = itemview.findViewById(R.id.tv_item_date_show);
            tvDesc = itemview.findViewById(R.id.tv_item_desc_show);
            tvVote = itemview.findViewById(R.id.tv_rating);
            btnMore = itemview.findViewById(R.id.btn_more);
            btnFav = itemview.findViewById(R.id.btn_fav);
        }
    }
}