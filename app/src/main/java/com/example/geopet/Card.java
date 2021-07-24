package com.example.geopet;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Card implements Serializable {
    private String imgUrl;
    private String title;
    private String userId;
    private ArrayList<String> Uris;



    public Card(String imgUrl, String title, ArrayList<String> Uris, String userId) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.Uris = Uris;
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
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
