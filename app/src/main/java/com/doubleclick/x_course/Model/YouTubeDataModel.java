package com.doubleclick.x_course.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class YouTubeDataModel implements Parcelable {

    private String title = "";
    private String describtion = "";
    private String publishedAt = "";
    private String thumnail = "";
    private String video_id = "";

    protected YouTubeDataModel(Parcel in) {
        title = in.readString();
        describtion = in.readString();
        publishedAt = in.readString();
        thumnail = in.readString();
        video_id = in.readString();
    }

    public static final Creator<YouTubeDataModel> CREATOR = new Creator<YouTubeDataModel>() {
        @Override
        public YouTubeDataModel createFromParcel(Parcel in) {
            return new YouTubeDataModel(in);
        }

        @Override
        public YouTubeDataModel[] newArray(int size) {
            return new YouTubeDataModel[size];
        }
    };

    public YouTubeDataModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }


    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }


    @Override
    public String toString() {
        return "YouTubeDataModel{" +
                "title='" + title + '\'' +
                ", describtion='" + describtion + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", thumnail='" + thumnail + '\'' +
                ", video_id='" + video_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(describtion);
        parcel.writeString(publishedAt);
        parcel.writeString(thumnail);
        parcel.writeString(video_id);
    }
}
