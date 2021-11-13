package com.doubleclick.x_course.Model;

public class Rate {

    private String psuhId;
    private String id;
    private String track;
    private String number;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public String getPsuhId() {
        return psuhId;
    }

    public void setPsuhId(String psuhId) {
        this.psuhId = psuhId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Rate{" +
                "psuhId='" + psuhId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
