package com.example.retrofitexamplefull.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("postId")
    @Expose
    private String postId;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("body")
    @Expose
    private String text;

    public Comment(String postId, String id, String name, String email, String text) {
        this.postId = postId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.text = text;
    }

    public Comment() {
    }

    public String getPostId() {
        return postId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getText() {
        return text;
    }
}
