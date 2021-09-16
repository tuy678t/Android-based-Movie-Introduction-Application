package com.example.myapplication;

public class Review {
    String info;
    String rating;

    public Review(String info, String rating, String content) {
        this.info = info;
        this.rating = rating;
        this.content = content;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String content;
}

