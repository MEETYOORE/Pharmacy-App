package com.example.pharmacy_app.models;

public class NavCategoryDetailedModel
{
    String name;
    String type;
    String img_url;
    Float price;

    public NavCategoryDetailedModel(String name, String type, String img_url, Float price) {
        this.name = name;
        this.type = type;
        this.img_url = img_url;
        this.price = price;
    }

    public NavCategoryDetailedModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}

