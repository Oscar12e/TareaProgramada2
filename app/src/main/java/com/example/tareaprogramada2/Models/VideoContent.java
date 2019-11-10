package com.example.tareaprogramada2.Models;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoContent extends Content {
    public VideoContent(){
        super.type = ContentType.youtube;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("body", body);
        result.put("link", link);
        result.put("videoKey", videoKey);
        return result;
    }

    public Boolean parseVideoKey(String url) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        this.videoKey = vId;
        return vId == null;
    }
}
