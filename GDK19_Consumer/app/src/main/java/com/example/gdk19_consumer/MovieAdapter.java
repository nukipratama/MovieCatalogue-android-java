package com.example.gdk19_consumer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gdk19_consumer.db.DatabaseContract;
import com.example.gdk19_consumer.model.MovieDao;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c, boolean autoRequery) {

        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, viewGroup, false);
        return view;
    }


    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            ImageView imgPoster = view.findViewById(R.id.img_item_photo);
            TextView tvTitle = view.findViewById(R.id.tv_item_name);
            TextView tvDate = view.findViewById(R.id.tv_item_date);
            TextView tvDesc = view.findViewById(R.id.tv_item_desc);
            TextView tvVote = view.findViewById(R.id.tv_rating);
            Button detail = view.findViewById(R.id.btn_more);
            final MovieDao data = DatabaseContract.getMovieDao(cursor);
            final MovieDao md = new MovieDao(data.getId(), data.getTitle(), data.getDate(), data.getDesc(), data.getImage(), data.getVote());
            final String mtitle = md.getTitle();
            final String imagePath = md.getImage();

            final String mdate = md.getDate();
            final String mdesc = md.getDesc();
            final String mvote = md.getVote();

            Picasso.get().load("https://image.tmdb.org/t/p/original" + imagePath).into(imgPoster);

            tvTitle.setText(mtitle);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate;
            try {
                Date dateRelease = sdf.parse(mdate);
                formattedDate = DateFormat.format("dd MMM yyyy", dateRelease).toString();
            } catch (ParseException e) {
                e.printStackTrace();
                formattedDate = data.getDate();
            }
            tvDate.setText(formattedDate);
            tvDesc.setText(mdesc);
            tvVote.setText(mvote);
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = md.getId();
                    MovieDao movieDao = new MovieDao(id, mtitle, mdate, mdesc, imagePath, mvote);
                    Intent detailActivity = new Intent(context, DetailActivity.class);
                    detailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    detailActivity.putExtra("data", movieDao);
                    context.startActivity(detailActivity);
                }
            });

        }
    }


}