package com.example.tareaprogramada2.Data;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaprogramada2.Models.Post;

public class PostHolder extends RecyclerView.ViewHolder {

    TextView username, body, privacy, postedAgo;


    public PostHolder(View itemView) {
        super(itemView);
        initialize(itemView);

        itemView.setOnClickListener(view -> {
            //int pos = getAdapterPosition();
        });
    }

    public void initialize(View view){

    }


    public void bind(Post post){
        System.out.println("Welp, here we are");
        //System.out.println(post.toMap());
    }
}