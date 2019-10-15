package com.example.gdk19_nukipratama.Show;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Show implements Parcelable {
    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel source) {
            return new Show(source);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };
    @SerializedName("overview")
    private String desc;
    @SerializedName("first_air_date")
    private String date;
    @SerializedName("name")
    private String name;
    @SerializedName("poster_path")
    private String img;
    @SerializedName("id")
    private int id;
    @SerializedName("vote_average")
    private float voteAvg;

    public Show(int id, String name, String desc, String date, String img, float voteAvg) {
        this.id = id;
        this.desc = desc;
        this.date = date;
        this.name = name;
        this.img = img;
        this.voteAvg = voteAvg;
    }

    protected Show(Parcel in) {
        this.desc = in.readString();
        this.date = in.readString();
        this.name = in.readString();
        this.img = in.readString();
        this.id = in.readInt();
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
        dest.writeString(this.img);
        dest.writeInt(this.id);
        dest.writeFloat(this.voteAvg);
    }
}
