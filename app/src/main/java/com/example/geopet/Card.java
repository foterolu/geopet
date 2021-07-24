package com.example.geopet;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Card implements Serializable {
    private String imgUrl, username;
    private String descripcion;
    private String userId;
    private String contacto, lat, lon, nombrePublicacion, raza;
    private ArrayList<String> Uris;



    public Card(String imgUrl, String descripcion, ArrayList<String> Uris, String userId, String contacto, String lat, String lon, String nombreMascota, String raza, String username) {
        this.imgUrl = imgUrl;
        this.descripcion = descripcion;
        this.Uris = Uris;
        this.userId = userId;
        this.contacto=contacto;
        this.lat=lat;
        this.lon=lon;
        this.nombrePublicacion=nombrePublicacion;
        this.raza=raza;
        this.username=username;
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

    public String getDescripcion() {
        return descripcion;
    }

    public String getContacto(){ return contacto;}

    public String getLat(){ return lat;}

    public String getLon(){ return lon;}

    public String getNombrePublicacion(){ return nombrePublicacion;}

    public String getRaza(){ return raza;}

    public String getUsername(){return username;}

}
