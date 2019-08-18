package com.example.chatapppfe.Model;
public class Chat {
    private String sender ;
    private String receiver ;
    private String message ;
    private String id ;
    private String time ;
    private boolean isseen ;



    public Chat(String sender, String receiver, String message,String id ,String time,boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.id=id ;
        this.time=time;
        this.isseen=isseen;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }


}
