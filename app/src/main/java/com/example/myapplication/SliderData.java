package com.example.myapplication;

public class SliderData {

    // image url is used to
    // store the url of image
    private String imgUrl;
    private String id;
    private String media;
    private String showname;

    // Constructor method.
    public SliderData(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
