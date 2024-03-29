package com.example.pharmacy_app.models;

public class NavCategoryModel
{
    String name;
    String description;
    String img_url;

    public NavCategoryModel()
    {
        // Default constructor
    }
    public NavCategoryModel(String name, String description, String img_url) {
        this.name = name;
        this.description = description;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
