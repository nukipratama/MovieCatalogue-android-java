package com.example.gdk19_nukipratama.Movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @SerializedName("overview")
    private String desc;
    @SerializedName("release_date")
    private String date;
    @SerializedName("title")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("poster_path")
    private String img;
    @SerializedName("vote_average")
    private float voteAvg;

    public Movie(int id, String name, String desc, String date, String img, float voteAvg) {
        this.id = id;
        this.desc = desc;
        this.date = date;
        this.name = name;
        this.img = img;
        this.voteAvg = voteAvg;
    }

    protected Movie(Parcel in) {
        this.desc = in.readString();
        this.date = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
        this.img = in.readString();
        this.voteAvg = in.readFloat();
    }

    public int getId() {
        return id;
    }

    public float getVote() {
        return voteAvg;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getImg() {
        return img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.desc);
        dest.writeString(this.date);
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeString(this.img);
        dest.writeFloat(this.voteAvg);
    }
}
