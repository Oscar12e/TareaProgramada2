package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.ContentType;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.TextContent;
import com.example.tareaprogramada2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Map;

public class PublishActivity extends AppCompatActivity {

    ContentType type;
    //String body;
    String link;
    Image image;

    EditText body;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        database = FirebaseDatabase.getInstance().getReference("posts");
        type = ContentType.text;
        body = findViewById(R.id.tbox_text);
    }


    public void buildPost(View view){
        Post rawPost = new Post();
        rawPost.postedBy = Session.instance.currentUser._key;
        rawPost.postedOn = new Date().toString();

        if (type == ContentType.text){
            TextContent content = new TextContent();
            String bodyData = body.getText().toString();

            if (bodyData.equals("")){
                System.out.println("No se puede publicar en blanco");
            } else {
                content.body = bodyData;
                rawPost.setContent(content);// = content;
                uploadPost(rawPost);
            }

        }
    }

    public void uploadPost(Post post){
        String key = database.push().getKey();
        Map<String, Object> map = post.toMap();
        if (key != null){
            database.child(key).setValue(map);
        } else {
            database.push().setValue(map);
        }
        finish();
    }
}
