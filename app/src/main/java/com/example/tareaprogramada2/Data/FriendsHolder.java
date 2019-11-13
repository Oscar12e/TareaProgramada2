package com.example.tareaprogramada2.Data;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FriendsHolder extends RecyclerView.ViewHolder {
    User friend;
    Context context;
    TextView name, amount;
    ImageView profilePic;

    public FriendsHolder(@NonNull View itemView) {
        super(itemView);
        initialize(itemView);
    }

    public void initialize(View view){
        name = view.findViewById(R.id.txt_friendName);
        amount = view.findViewById(R.id.txt_commonFriends);
        profilePic = view.findViewById(R.id.img_profilePic);
    }

    public void setup(){
        User myUser = Session.instance.currentUser;
        int commonFriends = myUser.getCommonFriendsSize(friend);

        name.setText(friend.getFullName());
        if (myUser._key.equals(friend._key))
            amount.setText( "" );
        else
            amount.setText( commonFriends + " amigos en común." );

        StorageReference storageReference;
        if (!friend.profilePic.equals("")){
            storageReference = FirebaseStorage.getInstance().getReference(friend._key).child(friend.profilePic);
        } else {
            storageReference = FirebaseStorage.getInstance().getReference("default").child("user_default.png");
        }
        GlideApp.with(context /* context */)
                .load(storageReference)
                .circleCrop()
                .into(profilePic);
    }

    public void bind(String friendCode, Context context){
        this.context = context;

        FirebaseDatabase.getInstance().getReference("users").child(friendCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friend = dataSnapshot.getValue(User.class);
                setup();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
