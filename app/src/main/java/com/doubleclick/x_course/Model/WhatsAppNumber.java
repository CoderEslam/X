package com.doubleclick.x_course.Model;

public class WhatsAppNumber {

    public WhatsAppNumber() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private String number;

    public WhatsAppNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "WhatsAppNumber{" +
                "number='" + number + '\'' +
                '}';
    }
}
