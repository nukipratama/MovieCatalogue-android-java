package com.example.gdk19_consumer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDao implements Parcelable {
    public static final String COLUMN_NAME = "judul";
    public static final Creator<MovieDao> CREATOR = new Creator<MovieDao>() {
        @Override
        public MovieDao createFromParcel(Parcel in) {
            return new MovieDao(in);
        }

        @Override
        public MovieDao[] newArray(int size) {
            return new MovieDao[size];
        }
    };
    private int id;
    private String title;
    private String date;
    private String desc;
    private String image;
    private String vote;

    public MovieDao(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        desc = in.readString();
        image = in.readString();
        vote = in.readString();
    }

    public MovieDao(int id, String title, String date, String desc, String image, String vote) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.image = image;
        this.vote = vote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(desc);
        parcel.writeString(image);
        parcel.writeString(vote);
    }
}