package com.example.tareaprogramada2.Models;

import android.media.Image;

import java.util.HashMap;

public class Content {
    public ContentType type;
    public String body;
    public String link;
    public String videoKey;
    public String imageUrl;

    public Content(){

    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("body", body);
        result.put("link", link);
        result.put("videoKey", videoKey);
        result.put("image", imageUrl);
        return result;
    }
}
