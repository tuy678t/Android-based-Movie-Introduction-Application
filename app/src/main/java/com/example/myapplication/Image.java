package com.example.myapplication;

public class Image {
    private String showname;

    public Image() {
        this.showname="showname";
        this.media="media";
        this.id="id";
        this.src="src";
    }

    private String media;

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    private String id;

    public Image(String showname, String media, String id, String src) {
        this.showname = showname;
        this.media = media;
        this.id = id;
        this.src = src;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
