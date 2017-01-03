package com.example.mdijajsayim.myproject;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Md Ijaj Sayim on 22-Dec-16.
 */

public class Post {
    private Users user;
    private University university;
    private double rating;
    private String description;
    private   String userProfilePic;
    private String uniqueId;

    public Post() {
        String uniqueID = UUID.randomUUID().toString();
        this.uniqueId=uniqueID;
    }

    public Post(Users user, University university, double rating, String description,String userProfilePic) {
        //String uniqueID = UUID.randomUUID().toString();
        //this.uniqueId=uniqueID;
        this.university = university;
        this.rating = rating;
        this.description = description;
        this.user=user;
        this.userProfilePic=userProfilePic;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
