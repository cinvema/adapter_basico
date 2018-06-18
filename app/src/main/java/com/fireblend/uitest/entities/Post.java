package com.fireblend.uitest.entities;

/**
 * Created by Sergio on 8/20/2017.
 */

public class Post {
    //Clase entidad para contener cada elemento de la lista, el cual representa un Contacto.
    public int userId;
    public int id;
    public String title;
    public String body;

    public Post(int userId, int id, String title, String body){
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }
}
