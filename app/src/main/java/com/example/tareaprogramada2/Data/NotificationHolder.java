package com.example.tareaprogramada2.Data;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaprogramada2.Models.FriendshipRequest;
import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class NotificationHolder extends RecyclerView.ViewHolder {
    Button accept, delete;
    TextView mutualFriends, body;
    DatabaseReference database;
    ImageView profile;
    User sender, receiver;

    public NotificationHolder(@NonNull View itemView) {
        super(itemView);
        initialize(itemView);
    }

    private void initialize(View view){
        accept = view.findViewById(R.id.btn_accept);
        delete = view.findViewById(R.id.btn_delete);
        mutualFriends = view.findViewById(R.id.txt_commonFriends);
        profile = view.findViewById(R.id.img_profilePic);
        body = view.findViewById(R.id.txt_body);
    }

    private void acceptFriendship(FriendshipRequest request, Context context){
        User myUser = Session.instance.currentUser;
        myUser.friends.add(request.sender);

        database = FirebaseDatabase.getInstance().getReference("users").child(myUser._key);
        database.setValue(myUser.toMap());

        sender.friends.add(myUser._key);

        database = FirebaseDatabase.getInstance().getReference("users").child(request.sender).child("friends");
        database.setValue(sender.friends);

        deleteRequest(request, "Se ha aceptado la solicitud de amistad.", context);
    }

    private void deleteRequest(FriendshipRequest request, String message, Context context){
        database = FirebaseDatabase.getInstance().getReference("request/"+request._key);
        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, message + " :p", Toast.LENGTH_SHORT).show();
                    System.out.println("Deleted");
                } else {
                    System.out.println(task.getException().toString());
                    System.out.println("Not deleted");
                }
            }
        });

    }


    public void setupUI(Context context){
        User myUser = Session.instance.currentUser;
        int commond = myUser.getCommonFriends(sender);

        mutualFriends.setText(commond + " amigos en comun.");
        String fullName = sender.name + " " + sender.lastname;

        String bodyMsg = "Has recibido una solicitud de amistad de " + fullName;
        body.setText(bodyMsg);

        StorageReference storageReference;
        if (!sender.profilePic.equals("")){
            storageReference = FirebaseStorage.getInstance().getReference(sender._key).child(sender.profilePic);
        } else {
            storageReference = FirebaseStorage.getInstance().getReference("default").child("user_default.png");
        }

        GlideApp.with(context /* context */)
                .load(storageReference)
                .into(profile);
    }


    private void setup(FriendshipRequest request, Context context){
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptFriendship(request, context);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRequest(request, "Se ha rechazado la solicitud de amistad." , context);
            }
        });

        setupUI(context);
    }

    public void bind(FriendshipRequest request, Context context){

        database = FirebaseDatabase.getInstance().getReference("users").child(request.sender);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                sender = dataSnapshot.getValue(User.class);
                setup(request, context);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LOGIN", "Failed to read value.", error.toException());
                System.out.println("LOGIN" + "Failed to read value." + error.toException());
            }
        });







    }
}
