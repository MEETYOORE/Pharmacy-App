package com.example.pharmacy_app.models;

public class ViewAllModel
{
    String name;
    String description;
    String rating;
    String type;
    String img_url;
    String price;

    public ViewAllModel()
    {

    }
    public ViewAllModel(String name, String description, String rating, String type, String img_url, String price)
    {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.type = type;
        this.img_url = img_url;
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getRating()
    {
        return rating;
    }

    public void setRating(String rating)
    {
        this.rating = rating;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }


    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }
}