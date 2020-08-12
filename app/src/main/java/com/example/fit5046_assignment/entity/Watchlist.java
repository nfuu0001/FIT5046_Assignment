package com.example.fit5046_assignment.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Watchlist {
    @PrimaryKey(autoGenerate = true)
    public int wid;
    @ColumnInfo(name = "person_id")
    public String personId;
    @ColumnInfo(name = "movie_name")
    public String movieName;
    @ColumnInfo(name = "release_date")
    public String releaseDate;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "time")
    public String time;
    @ColumnInfo(name = "movie_id")
    public String movieId;

    public Watchlist(){

    }

    public Watchlist(String personId,String movieName,String releaseDate,String date,String time,String movieId){
        this.personId = personId;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.date = date;
        this.time = time;
        this.movieId = movieId;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getPeronId() {
        return personId;
    }

    public void setPeronId(String peronId) {
        this.personId = peronId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
