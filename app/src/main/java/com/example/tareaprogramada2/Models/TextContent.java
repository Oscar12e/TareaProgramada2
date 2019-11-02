package com.example.tareaprogramada2.Models;

import java.util.HashMap;

public class TextContent extends Content {

    public String body;

    public TextContent(){
        type = ContentType.text;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("body", body);
        return result;
    }
}
