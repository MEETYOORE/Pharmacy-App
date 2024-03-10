package com.example.pharmacy_app.models;

public class UserModel
{
    String name;
    String email;
    String password;

    public UserModel(String name, String email, String password)
    {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }
    public String getPassword()
    {
        return password;
    }

}
