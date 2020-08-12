package com.example.fit5046_assignment;

public class Cinema {
    private Integer cinemaId;
    private String cinemaName;
    private String postcode;

    public Cinema(){
    }

    public Cinema(Integer cinemaId,String cinemaName,String postcode){
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.postcode = postcode;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }
}
