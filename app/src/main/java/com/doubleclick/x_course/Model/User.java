package com.doubleclick.x_course.Model;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String email;
    // new things
    private String Country;
    private String Whatsapp;
    private String bio;


    public String getWhatsapp() {
        return Whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        Whatsapp = whatsapp;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }





    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public User(String id, String username, String imageURL, String status) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;

    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
