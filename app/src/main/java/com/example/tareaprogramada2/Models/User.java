package com.example.tareaprogramada2.Models;

import android.media.Image;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User {
    public String _key = "";
    public String name = "";
    public String lastname = "";
    public String profilePic = "";
    public List<String> education = new ArrayList<>();
    public String birthday = "1990-01-01";
    public String phoneNumber = "";
    public String email = "";
    public String genre = "";
    public String city = "";
    public List<String> friends = new ArrayList<>();

    public User(){
    }

    public User(String _key, Map<String, Object> values){
        this._key = _key;

        name = (String) values.get("name");
        lastname = (String) values.get("lastname");
        profilePic = (String) values.get("profilePic");
        birthday = (String) values.get("birthday");
        phoneNumber = (String) values.get("phoneNumber");
        email = (String) values.get("email");
        genre = (String) values.get("genre");
        city = (String) values.get("city");
        education = (List<String>) values.get("education");
        friends = (List<String>) values.get("friends");
    }


    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("_key", _key);
        result.put("name", name);
        result.put("lastname", lastname);
        result.put("profilePic", profilePic);
        result.put("city", city);
        result.put("birthday", birthday);
        result.put("phoneNumber", phoneNumber);
        result.put("email", email);
        result.put("genre", genre);
        result.put("education", education);
        result.put("friends", friends);
        return result;
    }

    @Exclude
    public int getCommonFriendsSize(User other){
        return getCommonFriends(other).size();
    }

    @Exclude
    public List<String> getCommonFriends(User other){
        List<String> common = new ArrayList<>();
        common.addAll(other.friends);
        common.retainAll(this.friends);
        System.out.println("This are the common friends " + common.toString());
        return common;
    }

    @Exclude
    public List<String> getCommonFriends(List<String> otherFriends){
        List<String> common = new ArrayList<>();
        common.addAll(otherFriends);
        common.retainAll(this.friends);
        return common;
    }

    @Exclude
    public boolean isAFriend(String userKey){
        return friends.contains(userKey);
    }



    @Exclude
    public String getFullName(){
        return name + " " + lastname;
    }
}
