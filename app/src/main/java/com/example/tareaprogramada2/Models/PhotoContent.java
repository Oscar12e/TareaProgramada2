package com.example.tareaprogramada2.Models;

import java.util.HashMap;

public class PhotoContent extends Content {
    public PhotoContent(){
        super.type = ContentType.photo;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("body", body);
        result.put("imageUrl", imageUrl);
        return result;
    }
}
