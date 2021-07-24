package com.example.geopet;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Card implements Serializable {
    private String imgUrl;
    private String title;
    private String userId;
    private String contacto, lat, lon, nombreMascota, raza;
    private ArrayList<String> Uris;



    public Card(String imgUrl, String title, ArrayList<String> Uris, String userId, String contacto, String lat, String lon, String nombreMascota, String raza) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.Uris = Uris;
        this.userId = userId;
        this.contacto=contacto;
        this.lat=lat;
        this.lon=lon;
        this.nombreMascota=nombreMascota;
        this.raza=raza;
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

    public String getContacto(){ return contacto;}

    public String getLat(){ return lat;}

    public String getLon(){ return lon;}

    public String getNombreMascota(){ return nombreMascota;}

    public String getRaza(){ return raza;}

    public void setTitle(String title) {
        this.title = title;
    }
}
