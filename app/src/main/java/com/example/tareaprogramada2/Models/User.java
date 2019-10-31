package com.example.tareaprogramada2.Models;

import android.media.Image;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class User {
    public String _id;

    public String name;
    public String lastname;
    public Image profilePic;
    public List<String> education;
    public Date birthday;
    public String phoneNumber;
    public String email;
    public String sex;


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
        result.put("_id", _id);
        return result;
    }
}
