package com.example.android_retrofit;

import com.google.gson.annotations.SerializedName;

public class Post {
    private int userId;
    private Integer id;
    private String title;

    @SerializedName("body")
    private String bodyText;

    public Post(int userId, String title, String bodyText) {
        this.userId = userId;
        this.title = title;
        this.bodyText = bodyText;
    }

    public int getUserId() {
        return userId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBodyText() {
        return bodyText;
    }
}
