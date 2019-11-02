package com.example.tareaprogramada2.Models;

import java.util.HashMap;

public class Post {
    public String postedBy;
    public String postedOn;

    public Content content;

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("postedBy", postedBy);
        result.put("postedOn", postedOn);
        result.put("content", content);

        return result;
    }
}
