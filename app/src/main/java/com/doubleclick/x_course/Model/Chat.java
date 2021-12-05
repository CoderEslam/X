package com.doubleclick.x_course.Model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Chat {

    @Override
    public String toString() {

        return "Chat{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", isseen=" + isseen +
                ", type='" + type + '\'' +
                ", pushId='" + pushId + '\'' +
                ", time=" + time +
                ", id=" + id +
                '}';
    }

    private String sender;
    private String receiver;
    private String message;
    private boolean isseen;
    private String type;

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    private String pushId;
    private long time;
    @PrimaryKey(autoGenerate = true)// to defined id as a primary key
    private int id;

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

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Chat(String sender, String receiver, String message, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
    }

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String type, String pushid, long time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
        this.pushId = pushid;
        this.time = time;
    }



}
