package com.example.myapplication;

public class SeachResult {
    private String showname;
    private String media;
    private String id;
    private String backdrop;
    private String rate;

    public SeachResult(String showname, String media, String id, String backdrop, String rate) {
        this.showname = showname;
        this.media = media;
        this.id = id;
        this.backdrop = backdrop;
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }
}
