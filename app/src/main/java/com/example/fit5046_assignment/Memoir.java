package com.example.fit5046_assignment;



public class Memoir {
    private Integer memoirId;

    private String movieName;

    private String releaseDate;

    private String dateWatched;

    private String timeWatched;

    private String comment;

    private float ratingScore;

    private Cinema cinemaId;

    private Person personId;

    public  Memoir(){

    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getRatingScore() {
        return ratingScore;
    }

    public Integer getMemoirId() {
        return memoirId;
    }

    public String getDateWatched() {
        return dateWatched;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getComment() {
        return comment;
    }

    public void setCinemaId(Cinema cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getTimeWatched() {
        return timeWatched;
    }

    public Cinema getCinemaId() {
        return cinemaId;
    }

    public void setDateWatched(String dateWatched) {
        this.dateWatched = dateWatched;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMemoirId(Integer memoirId) {
        this.memoirId = memoirId;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setRatingScore(float ratingScore) {
        this.ratingScore = ratingScore;
    }

    public void setTimeWatched(String timeWatched) {
        this.timeWatched = timeWatched;
    }

}
