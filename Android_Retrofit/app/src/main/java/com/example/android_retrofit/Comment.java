package com.example.android_retrofit;

import com.google.gson.annotations.SerializedName;

public class Comment {
    private int postId, id;
    private String name, email;

    @SerializedName("body")
    private String bodyText;

    public int getPostId() {
        return postId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBodyText() {
        return bodyText;
    }
}
