package com.example.tareaprogramada2.Presentations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tareaprogramada2.Data.GlideApp;
import com.example.tareaprogramada2.Models.PhotoContent;
import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.ContentType;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.TextContent;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.Models.VideoContent;
import com.example.tareaprogramada2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Map;


public class PublishActivity extends AppCompatActivity {

    TextView postType, name;
    ContentType type;
    EditText body;
    EditText link;
    ImageView preview, profilePic;
    Uri imageUri = null;
    Button loadImg;

    private static final int GALLERY_INTENT = 101;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initialize();
        setTypeText(null);
    }

    public void initialize(){
        database = FirebaseDatabase.getInstance().getReference("posts");
        type = ContentType.text;
        body = findViewById(R.id.tbox_text);
        link = findViewById(R.id.tbox_link);
        postType = findViewById(R.id.txt_typeOfPost);
        preview = findViewById(R.id.img_preview);
        loadImg = findViewById(R.id.btn_loadImage);
        profilePic = findViewById(R.id.img_profilePic);
        name = findViewById(R.id.txt_name);
    }

    @Override
    public void onStart(){
        super.onStart();
        setup();
    }

    private void setup(){
        User myUser = Session.instance.currentUser;

        name.setText(myUser.getFullName());

        StorageReference storageReference;
        if (!myUser.profilePic.equals("")){
            storageReference = FirebaseStorage.getInstance().getReference(myUser._key).child(myUser.profilePic);
        } else {
            storageReference = FirebaseStorage.getInstance().getReference("default").child("user_default.png");
        }

        GlideApp.with(this /* context */)
                .load(storageReference)
                .circleCrop()
                .into(profilePic);
    }

    public void setTypeText(View view){
        type = ContentType.text;
        postType.setText( "Nuevo post de texto" );

        body.setVisibility(View.VISIBLE);
        link.setVisibility(View.GONE);
        preview.setVisibility(View.GONE);
        loadImg.setVisibility(View.GONE);
        imageUri = null;
    }

    public void setTypeImg(View view){
        type = ContentType.photo;
        postType.setText( "Nuevo post de imagen" );

        preview.setVisibility(View.GONE);
        loadImg.setVisibility(View.VISIBLE);
        body.setVisibility(View.VISIBLE);
        link.setVisibility(View.GONE);
        imageUri = null;
    }

    public void setTypeVideo(View view){
        type = ContentType.youtube;
        postType.setText( "Nuevo post de video" );

        body.setVisibility(View.VISIBLE);
        link.setVisibility(View.VISIBLE);
        loadImg.setVisibility(View.GONE);
        preview.setVisibility(View.GONE);
        imageUri = null;


        link.setText("https://www.youtube.com/watch?v=hM7Eh0gGNKA&list=RDLDU_Txk06tM&index=4");

        //String url = "qvtCk1wZ7LM";
        //Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, "AIzaSyBEhtYHVK9BrgQgkTncrFfCXc1Nkl9LHJw", url);
        //startActivity(intent);

        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //startActivity(intent);
    }

    public void selectImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            preview.setImageURI(uri);
            imageUri = uri;
            System.out.println("Setted");
            preview.setVisibility(View.VISIBLE);
        } else {
            System.out.println("What??");
        }
    }


    /**
     * For the love of everything that is good on the world, finish the whole project so you can optimize this peacefully
     * @param view
     */
    public void buildPost(View view){
        Post rawPost = new Post();
        rawPost.postedBy = Session.instance.currentUser._key;
        rawPost.postedOn = Post.dateFormat.format(new Date());

        String bodyData = body.getText().toString();

        if (type == ContentType.text){
            TextContent content = new TextContent();

            if (bodyData.equals("")){
                Toast.makeText(this, "No se puede publicar un texto en blanco" , Toast.LENGTH_SHORT ).show();
                return;
            }

            content.body = bodyData;
            rawPost.setContent(content);// = content;
            uploadPost(rawPost);

        } else if (type == ContentType.photo){
            if (imageUri == null){
                Toast.makeText(this, "Debe de seleccionar una imagen para subirla." , Toast.LENGTH_SHORT ).show();
            } else if (imageUri.getLastPathSegment() != null){
                PhotoContent content = new PhotoContent();
                content.body = bodyData;
                content.imageUrl = imageUri.getLastPathSegment();
                rawPost.setContent(content);// = content;
                uploadImagePost(rawPost);
            }
        } else {
            String link = this.link.getText().toString();

            if (link.equals("")){
                Toast.makeText(this, "Ingrese un link para poder publicar el post." , Toast.LENGTH_SHORT ).show();
                return;
            }

            VideoContent content = new VideoContent();
            content.body = bodyData;
            content.link = link;

            if (!content.parseVideoKey(link)){
                Toast.makeText(this, "No se pudo extraer el id del video del link." , Toast.LENGTH_SHORT ).show();
                return;
            }

            rawPost.setContent(content);// = content;
            uploadPost(rawPost);
        }
    }


    public void uploadPost(Post post){
        String key = database.push().getKey();
        post._key = key;
        Map<String, Object> map = post.toMap();
        if (key != null){
            database.child(key).setValue(map);
            Toast.makeText(this, "El post se ha subido!" , Toast.LENGTH_SHORT ).show();
        } else {
            database.push().setValue(map);
        }

        finish();
    }

    public void uploadImagePost(Post post){
        String key = database.push().getKey();
        post._key = key;
        Map<String, Object> map = post.toMap();

        if (key == null){
            return;
        }

        StorageReference profPictureRef = FirebaseStorage.getInstance().getReference(key).child(post.content.imageUrl);
        UploadTask uploadTask = profPictureRef.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Got an issue");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                database.child(key).setValue(map);
                finish();
            }
        });


    }



}
