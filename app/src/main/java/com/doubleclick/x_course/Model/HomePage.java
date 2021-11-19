package com.doubleclick.x_course.Model;

import com.doubleclick.x_course.Adapter.DiplomasAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePage {


    public static final int Advertisement = 0;
    public static final int AllDiploma = 1;
    public static final int ItemCourse = 2;


    private String email;
    private String UserId;
    private String userName;
    private String imageURL;

    public ArrayList<Diploma> getDiplomaArrayList() {
        return diplomaArrayList;
    }

    public void setDiplomaArrayList(ArrayList<Diploma> diplomaArrayList) {
        this.diplomaArrayList = diplomaArrayList;
    }

    private ArrayList<Diploma> diplomaArrayList;

    public HomePage(ArrayList<Diploma> diplomaArrayList, String email, String userId, String userName, String imageURL, int type) {
        this.email = email;
        UserId = userId;
        this.userName = userName;
        this.imageURL = imageURL;
        this.diplomaArrayList = diplomaArrayList;
        this.type = type;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public ArrayList<YouTubeDataModel> getYouTubeDataModelList() {
        return youTubeDataModelList;
    }

    public void setYouTubeDataModelList(ArrayList<YouTubeDataModel> youTubeDataModelList) {
        this.youTubeDataModelList = youTubeDataModelList;
    }

    private int type;

    private ArrayList<YouTubeDataModel> youTubeDataModelList;

    public ArrayList<Advertisement> getAdvertisementArrayList() {
        return advertisementArrayList;
    }

    public void setAdvertisementArrayList(ArrayList<Advertisement> advertisementArrayList) {
        this.advertisementArrayList = advertisementArrayList;
    }

    private ArrayList<Advertisement> advertisementArrayList;

    public HomePage(ArrayList<Advertisement> advertisementArrayList, int type) {
        this.type = type;
        this.advertisementArrayList = advertisementArrayList;
    }

    public HomePage(int type, ArrayList<YouTubeDataModel> youTubeDataModelList) {
        this.type = type;
        this.youTubeDataModelList = youTubeDataModelList;
    }

}
