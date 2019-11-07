package com.example.tareaprogramada2.Data;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.R;

public class PostHolder extends RecyclerView.ViewHolder {

    TextView username, body, privacy, postedAgo, link;


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
        privacy = view.findViewById(R.id.txt_privacy);
        postedAgo = view.findViewById(R.id.txt_postedAt);
    }


    public void bind(Post post){
        System.out.println("Welp, here we are");
        body.setText( post.content.body);
        postedAgo.setText( post.dateDifference());
    }
}