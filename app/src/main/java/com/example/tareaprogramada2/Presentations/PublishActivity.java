package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.ContentType;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.TextContent;
import com.example.tareaprogramada2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.Date;
import java.util.Map;

import static android.provider.MediaStore.Video.Thumbnails.VIDEO_ID;

public class PublishActivity extends AppCompatActivity {

    TextView postType;
    ContentType type;
    EditText body;
    EditText link;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        database = FirebaseDatabase.getInstance().getReference("posts");
        type = ContentType.text;
        body = findViewById(R.id.tbox_text);
        link = findViewById(R.id.tbox_link);
        postType = findViewById(R.id.txt_typeOfPost);

        setTypeText(null);
    }

    public void setTypeText(View view){
        body.setVisibility(View.VISIBLE);
        link.setVisibility(View.GONE);

        postType.setText( "Nuevo post de texto" );
    }

    public void setTypeImg(View view){
        postType.setText( "Nuevo post de imagen" );

    }

    public void setTypeVideo(View view){
        body.setVisibility(View.VISIBLE);
        link.setVisibility(View.VISIBLE);
        postType.setText( "Nuevo post de video" );
        String url = "qvtCk1wZ7LM";
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, "AIzaSyBEhtYHVK9BrgQgkTncrFfCXc1Nkl9LHJw", url);
        startActivity(intent);

        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //startActivity(intent);
    }


    public void buildPost(View view){
        Post rawPost = new Post();
        rawPost.postedBy = Session.instance.currentUser._key;
        rawPost.postedOn = Post.dateFormat.format(new Date());

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
        post._key = key;
        Map<String, Object> map = post.toMap();
        if (key != null){
            database.child(key).setValue(map);
        } else {
            database.push().setValue(map);
        }
        finish();
    }
}
