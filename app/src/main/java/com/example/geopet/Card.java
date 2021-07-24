package com.example.geopet;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Card implements Serializable {
    private String imgUrl;
    private String title;
    private ArrayList<String> Uris;

    public Card(String imgUrl, String title, ArrayList<String> Uris) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.Uris = Uris;
    }

    public ArrayList<String> getUris() {
        return Uris;
    }

    public void setUris(ArrayList<String> uris) {
        Uris = uris;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
