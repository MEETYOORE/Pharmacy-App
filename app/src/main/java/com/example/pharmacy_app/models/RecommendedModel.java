package com.example.pharmacy_app.models;

import java.io.Serializable;

public class RecommendedModel implements Serializable
{
    String name;
    String description;
    String rating;
    String img_url;
    String type;
    int price;

    public RecommendedModel()
    {

    }

    public RecommendedModel(String name, String description, String rating, String img_url, int price, String type)
    {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.img_url = img_url;
        this.price = price;
        this.type = type;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getType() {
        return type;
    }

    public void setType(String rating) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
