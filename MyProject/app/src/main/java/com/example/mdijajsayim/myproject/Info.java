package com.example.mdijajsayim.myproject;

/**
 * Created by Md Ijaj Sayim on 01-Jan-17.
 */

public class Info {
    private University university;
    private double rating;

    public Info() {
    }

    public Info(University university, double rating, String link) {
        this.university = university;
        this.rating = rating;
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
}
