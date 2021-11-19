package com.doubleclick.x_course.Model;

public class ItemCourse {


    private int id;
    private int image_;
    private String itemCourse;
    private String ImageDB;

    public String getImageDB() {
        return ImageDB;
    }

    public void setImageDB(String imageDB) {
        ImageDB = imageDB;
    }


    public ItemCourse() {
    }

    public ItemCourse(int image, String itemCourse) {
        this.image_ = image;
        this.itemCourse = itemCourse;
    }


    public int getImage_() {
        return image_;
    }

    public void setImage_(int image_) {
        this.image_ = image_;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getItemCourse() {
        return itemCourse;
    }

    public void setItemCourse(String itemCourse) {
        this.itemCourse = itemCourse;
    }


    @Override
    public String toString() {
        return "ItemCourse{" +
                "id=" + id +
                ", image_=" + image_ +
                ", itemCourse='" + itemCourse + '\'' +
                ", ImageDB='" + ImageDB + '\'' +
                '}';
    }
}
