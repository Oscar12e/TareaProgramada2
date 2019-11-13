package com.example.tareaprogramada2.Data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaprogramada2.Models.ContentType;
import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.Presentations.ProfileActivity;
import com.example.tareaprogramada2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostHolder extends RecyclerView.ViewHolder {

    TextView username, body, postedAgo, link, likes, dislikes;
    ImageView photo, profilePic;
    ImageButton show, btn_like, btn_dislike;
    Button delete;
    TableRow menu;
    Post myPost;
    boolean showingMenu;


    public PostHolder(View itemView) {
        super(itemView);
        initialize(itemView);
    }

    public void initialize(View view){
        username = view.findViewById(R.id.txt_postedBy);
        body = view.findViewById(R.id.txt_body);
        postedAgo = view.findViewById(R.id.txt_postedAt);
        link = view.findViewById(R.id.txt_link);
        likes = view.findViewById(R.id.txt_likes);
        dislikes = view.findViewById(R.id.txt_dislikes);

        photo = view.findViewById(R.id.img_photo);
        profilePic = view.findViewById(R.id.img_profilePic);

        menu = view.findViewById(R.id.row_options);
        btn_like = view.findViewById(R.id.btn_like);
        btn_dislike = view.findViewById(R.id.btn_dislike);

        show = view.findViewById(R.id.btn_show);
        delete = view.findViewById(R.id.btn_delete);
    }

    private void deletePost(){
        if (myPost.content.type == ContentType.photo){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(myPost._key).child(myPost.content.imageUrl);
            storageReference.delete();
        }
        DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts").child(myPost._key);
        postReference.removeValue();
    }

    private void refreshVotes(){
        //Toast.makeText(this, "El post se ha subido!" , Toast.LENGTH_SHORT ).show();
        DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts").child(myPost._key);
        postReference.setValue(myPost.toMap());
    }

    private void setupButtons(Context context, Post post, User user){
        showingMenu = false;
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingMenu = !showingMenu;

                if (showingMenu){
                    menu.setVisibility(View.VISIBLE);
                    show.setImageDrawable( context.getDrawable(R.drawable.close)  );
                } else {
                    menu.setVisibility(View.GONE);
                    show.setImageDrawable( context.getDrawable(R.drawable.menu)  );
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost();
            }
        });

        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.registerLike(user._key);
                refreshVotes();
            }
        });

        btn_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.registerDislike(user._key);
                refreshVotes();
            }
        });
    }

    private void setupContent(Context context){
        body.setText(myPost.content.body);
        postedAgo.setText( myPost.dateDifference());

        if (myPost.content.type == ContentType.photo){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(myPost._key).child(myPost.content.imageUrl);
            GlideApp.with(context /* context */)
                    .load(storageReference)
                    .into(photo);
            photo.setVisibility(View.VISIBLE);

        } else if (myPost.content.type == ContentType.youtube){
            link.setText(myPost.content.link);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myPost.content.link));
                    context.startActivity(intent);
                }
            });
            link.setVisibility(View.VISIBLE);
        }

        if (myPost.likedBy.size() == 0){
            likes.setVisibility(View.GONE);
        } else {
            likes.setText(Integer.toString( myPost.likedBy.size() ));
            likes.setVisibility(View.VISIBLE);
        }

        if (myPost.dislikedBy.size() == 0){
            dislikes.setVisibility(View.GONE);
        } else {
            dislikes.setText(Integer.toString( myPost.dislikedBy.size() ));
            dislikes.setVisibility(View.VISIBLE);
        }
    }

    public void setupUser(Post post, User user, Context context) {
        User myUser = Session.instance.currentUser;
        setupButtons(context, post, user);
        String fullName =  user.name + " " + user.lastname;
        if (post.postedBy.equals(myUser._key)){
            show.setVisibility(View.VISIBLE);
        }

        username.setClickable(true);
        profilePic.setClickable(true);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile(post, context, fullName);
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile(post, context, fullName);
            }
        });

        username.setText(fullName);
        StorageReference storageReference;
        if (!user.profilePic.equals("")){
            storageReference = FirebaseStorage.getInstance().getReference(user._key).child(user.profilePic);
        } else {
            storageReference = FirebaseStorage.getInstance().getReference("default").child("user_default.png");
        }
        GlideApp.with(context /* context */)
                .load(storageReference)
                .circleCrop()
                .into(profilePic);
    }

    public void bind(Post post, Context context){
        myPost = post;
        cleanView(context);
        setupContent(context);


        if (post.postedBy.equals(Session.instance.currentUser._key)){
            setupUser(post, Session.instance.currentUser, context);
        } else {
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users").child(post.postedBy);
            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    User myUser = dataSnapshot.getValue(User.class);
                    setupUser(post, myUser, context);
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

    private void cleanView(Context context){
        link.setVisibility(View.GONE);
        photo.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);
        show.setVisibility(View.GONE);
        likes.setVisibility(View.GONE);
        dislikes.setVisibility(View.GONE);
    }

    public void openProfile(Post post, Context context, String name){
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("USER_KEY", post.postedBy);
        intent.putExtra("USER_NAME", name);

        System.out.println("Storing");
        context.startActivity(intent);
    }

}