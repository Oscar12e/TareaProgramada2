package com.example.tareaprogramada2.Models;

import java.util.HashMap;
import java.util.Map;

public class FriendshipRequest {
    public String _key;
    public String sender;
    public String receiver;

    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();

        result.put("_key", _key);
        result.put("sender", sender);
        result.put("receiver", receiver);

        return result;

    }
}
