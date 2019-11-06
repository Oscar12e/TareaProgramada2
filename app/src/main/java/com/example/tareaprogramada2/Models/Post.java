package com.example.tareaprogramada2.Models;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Post {
    public String postedBy;
    public String postedOn;

    public List<User> likedBy;
    public List<User> dislikedBy;

    public Content content;

    public Post(){

    }

    public Post(String postedBy, String postedOn, List<User> likedBy, List<User> dislikedBy, Content content) {
        this.postedBy = postedBy;
        this.postedOn = postedOn;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
        this.content = content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("postedBy", postedBy);
        result.put("postedOn", postedOn);
        result.put("likedBy", likedBy);
        result.put("dislikedBy", dislikedBy);
        result.put("content", content.toMap());

        return result;
    }

    public String dateDifference(){

        return null;
    }
}
