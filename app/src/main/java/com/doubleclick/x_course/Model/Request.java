package com.doubleclick.x_course.Model;

public class Request {

    private String email;
    private String userId;
    private String imageURL;
    private String request;
    private String track;
    private String username;
    private String numberOfDiploma;
    private String nameOfDiploma;
    private String PushId;


    public String getPushId() {
        return PushId;
    }

    public void setPushId(String pushId) {
        PushId = pushId;
    }

    public String getNameOfDiploma() {
        return nameOfDiploma;
    }

    public void setNameOfDiploma(String nameOfDiploma) {
        this.nameOfDiploma = nameOfDiploma;
    }


    public String getNumberOfDiploma() {
        return numberOfDiploma;
    }

    public void setNumberOfDiploma(String numberOfDiploma) {
        this.numberOfDiploma = numberOfDiploma;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
