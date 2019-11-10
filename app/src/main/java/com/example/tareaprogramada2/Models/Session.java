package com.example.tareaprogramada2.Models;

public class Session {

    public static Session instance = new Session();
    public User currentUser;

    private Session(){
    }

    public void setUser(User user){
        this.currentUser = user;
    }

    public User getUser(){
        return currentUser;
    }


}
