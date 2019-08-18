package com.example.chatapppfe.Model;

public class Users {
    public  String username ;
    public  String id ;
    public  String imageURL ;
    private String status ;

    public Users(String username, String id, String imageURL,String status) {
        this.username = username;
        this.id = id;
        this.imageURL = imageURL;
        this.status=status;
    }

    public Users() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
