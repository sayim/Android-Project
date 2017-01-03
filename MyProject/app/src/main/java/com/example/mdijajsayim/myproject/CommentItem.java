package com.example.mdijajsayim.myproject;

/**
 * Created by Md Ijaj Sayim on 31-Dec-16.
 */

public class CommentItem {

    private String userName;
    private String userImage;
    private   String userComment;

    public CommentItem() {
    }

    public CommentItem(String userName, String userImage, String userComment) {
        this.userName = userName;
        this.userImage = userImage;
        this.userComment = userComment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
}
