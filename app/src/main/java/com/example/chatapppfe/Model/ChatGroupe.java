package com.example.chatapppfe.Model;
public class ChatGroupe {
    public String id  ;
    public String message ;
    private String imageurl ;
    private String name ;

    public ChatGroupe() {
    }
    public ChatGroupe(String id, String message,String imageurl,String name ) {
        this.id = id;
        this.message = message;
        this.imageurl=imageurl ;
        this.name=name ;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
