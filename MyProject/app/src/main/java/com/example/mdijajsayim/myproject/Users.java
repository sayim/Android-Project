package com.example.mdijajsayim.myproject;

/**
 * Created by Md Ijaj Sayim on 17-Dec-16.
 */

public class Users{
    private String name;
    private String universityName;
    private String image;

    public Users() {
    }

    public Users(String name, String universityName,String image) {
        this.name = name;
        this.universityName = universityName;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
