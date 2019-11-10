package com.example.tareaprogramada2.Data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaprogramada2.Models.ContentType;
import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostHolder extends RecyclerView.ViewHolder {

    TextView username, body, postedAgo, link;
    ImageView photo;


    public PostHolder(View itemView) {
        super(itemView);
        initialize(itemView);

        itemView.setOnClickListener(view -> {
            //int pos = getAdapterPosition();
        });
    }

    public void initialize(View view){
        username = view.findViewById(R.id.txt_postedBy);
        body = view.findViewById(R.id.txt_body);
        postedAgo = view.findViewById(R.id.txt_postedAt);
        link = view.findViewById(R.id.txt_link);
        photo = view.findViewById(R.id.img_photo);


    }


    public void bind(Post post, Context context){
        link.setVisibility(View.GONE);
        photo.setVisibility(View.GONE);

        System.out.println("Welp, here we are");

        body.setText( post.content.body);
        postedAgo.setText( post.dateDifference());

        if (post.content.type == ContentType.photo){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(post._key).child(post.content.imageUrl);
            GlideApp.with(context /* context */)
                    .load(storageReference)
                    .into(photo);

            photo.setVisibility(View.VISIBLE);
        } else if (post.content.type == ContentType.youtube){
            link.setText(post.content.link);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.content.link));
                    context.startActivity(intent);
                }
            });
            link.setVisibility(View.VISIBLE);
        }
    }
}