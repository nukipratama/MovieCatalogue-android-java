package com.example.gdk19_nukipratama.DB.Movie;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MovieData.TABLE_NAME)
public class MovieData implements Serializable, Parcelable {

    public static final String TABLE_NAME = "tb_movie";

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    public int barangId;
    @ColumnInfo(name = "judul")
    public String judul;
    @ColumnInfo(name = "tanggal")
    public String tanggal;
    @ColumnInfo(name = "deskripsi")
    public String deskripsi;
    @ColumnInfo(name = "rating")
    public String rating;
    @ColumnInfo(name = "gambar")
    public String gambar;

    public MovieData() {
    }

    protected MovieData(Parcel in) {
        this.barangId = in.readInt();
        this.judul = in.readString();
        this.tanggal = in.readString();
        this.deskripsi = in.readString();
        this.rating = in.readString();
        this.gambar = in.readString();
    }

    public int getBarangId() {
        return barangId;
    }

    public void setBarangId(int barangId) {
        this.barangId = barangId;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.barangId);
        dest.writeString(this.judul);
        dest.writeString(this.tanggal);
        dest.writeString(this.deskripsi);
        dest.writeString(this.rating);
        dest.writeString(this.gambar);
    }
}