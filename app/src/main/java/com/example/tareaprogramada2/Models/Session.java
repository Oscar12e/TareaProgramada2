package com.example.tareaprogramada2.Models;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Session {

    public static Session instance = new Session();
    public User currentUser;
    private DatabaseReference database;

    private Session(){
    }

    public void setUser(User user){
        this.currentUser = user;
    }

    public User getUser(){
        return currentUser;
    }

    public void startListening(String key){
        database = FirebaseDatabase.getInstance().getReference("users").child(key);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("############################################");
                System.out.println("Actualizando el usuario en Session");
                currentUser = dataSnapshot.getValue(User.class); //Para mantener el usuario al dia
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println("Error en Actualizando el usuario en Session");
            }
        });
    }
}
