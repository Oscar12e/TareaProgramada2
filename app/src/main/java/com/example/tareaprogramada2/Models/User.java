package com.example.tareaprogramada2.Models;

import android.media.Image;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    public String _key = "";

    public String name = "";
    public String lastname = "";
    public Image profilePic;
    public List<String> education = new ArrayList<>();
    public String birthday = "";
    public String phoneNumber = "";
    public String email = "";
    public String sex = "";

    public User(){
    }

    public User(String _key, Map<String, Object> values){
        this._key = _key;

        name = (String) values.get("name");
        lastname = (String) values.get("lastname");
        profilePic = (Image) values.get("profilePic");
        education = (List<String>) values.get("education");
        birthday = (String) values.get("birthday");
        phoneNumber = (String) values.get("phoneNumber");
        email = (String) values.get("email");
        sex = (String) values.get("sex");

    }

    public User(String _email){
        email = _email;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("lastname", lastname);
        result.put("profilePic", profilePic);
        result.put("education", education);
        result.put("birthday", birthday);
        result.put("phoneNumber", phoneNumber);
        result.put("email", email);
        result.put("sex", sex);
        return result;
    }
}
