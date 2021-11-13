package com.doubleclick.x_course.Model;

public class ItemCourse {

    public ItemCourse() {
    }

    public ItemCourse(int image, String itemCourse) {
        this.image_ = image;
        this.itemCourse = itemCourse;
    }

    public ItemCourse(int image_, String itemCourse, int bd_item) {
        this.image_ = image_;
        this.itemCourse = itemCourse;
        this.bd_item = bd_item;
    }


    public int getImage_() {
        return image_;
    }

    public void setImage_(int image_) {
        this.image_ = image_;
    }

    private int image_;
    private String itemCourse;

    public int getBd_item() {
        return bd_item;
    }

    public void setBd_item(int bd_item) {
        this.bd_item = bd_item;
    }

    private int bd_item;

    public String getItemCourse() {
        return itemCourse;
    }

    public void setItemCourse(String itemCourse) {
        this.itemCourse = itemCourse;
    }


}
