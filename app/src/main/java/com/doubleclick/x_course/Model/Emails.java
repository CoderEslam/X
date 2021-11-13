package com.doubleclick.x_course.Model;

public class Emails {



    private String email;
    private String diploma;
    private String numberOfDiploma;
    private int type;
    private String track;
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }




    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public Emails() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiploma() {
        return diploma;
    }

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    public String getNumberOfDiploma() {
        return numberOfDiploma;
    }

    public void setNumberOfDiploma(String numberOfDiploma) {
        this.numberOfDiploma = numberOfDiploma;
    }

    public Emails(String email, String diploma, String numberOfDiploma) {
        this.email = email;
        this.diploma = diploma;
        this.numberOfDiploma = numberOfDiploma;
    }

    @Override
    public String toString() {
        return "Emails{" +
                "email='" + email + '\'' +
                ", diploma='" + diploma + '\'' +
                ", numberOfDiploma='" + numberOfDiploma + '\'' +
                ", type=" + type +
                '}';
    }
}
