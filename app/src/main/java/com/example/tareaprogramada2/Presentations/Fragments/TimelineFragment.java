package com.example.tareaprogramada2.Presentations.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;

import com.example.tareaprogramada2.Data.GlideApp;
import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.PostAdapter;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.Presentations.PublishActivity;
import com.example.tareaprogramada2.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POST_FROM = "postFrom";
    private static final String ALLOW_POST = "allowPost";

    // TODO: Rename and change types of parameters
    private boolean allowPost;
    private String postFrom;
    private RecyclerView.Adapter adapter;
    private List<Post> postOnTimeline = new ArrayList<>();

    private DatabaseReference postsRef;

    public TimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String param1, boolean param2) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(POST_FROM, param1);
        args.putBoolean(ALLOW_POST, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postFrom = getArguments().getString(POST_FROM);
            allowPost = getArguments().getBoolean(ALLOW_POST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_timeline, container, false);
        TableRow row_publish = myView.findViewById(R.id.row_publish);

        row_publish.setVisibility( allowPost ? View.VISIBLE : View.GONE);

        row_publish.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), PublishActivity.class);
            startActivity(intent);
        });

        EditText text = myView.findViewById(R.id.tbox_dummy);
        text.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), PublishActivity.class);
            startActivity(intent);
        });

        ImageView mini = myView.findViewById(R.id.img_minicrop);



        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        RecyclerView recyclerView = myView.findViewById(R.id.recyler_timeline);
        adapter = new PostAdapter(postOnTimeline, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (postFrom.equals("")){

            User myUser = Session.instance.currentUser;
            StorageReference storageReference;
            if (!myUser.profilePic.equals("")){
                storageReference = FirebaseStorage.getInstance().getReference(myUser._key).child(myUser.profilePic);
            } else {
                storageReference = FirebaseStorage.getInstance().getReference("default").child("user_default.png");
            }

            GlideApp.with(this /* context */)
                    .load(storageReference)
                    .circleCrop()
                    .into(mini);

            attachFriendsListener();
        }

        else {
            attachProfileListener();
        }

        return myView;
    }

    private void attachFriendsListener(){
        Query query = postsRef.orderByChild("postedTime");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User myUser = Session.instance.currentUser;
                List<Post> result = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    if (myUser.isAFriend(post.postedBy) || myUser._key.equals(post.postedBy)){
                        result.add(post);
                    }
                }
                updateAdapterData(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addValueEventListener(listener);

    }

    private void attachProfileListener(){

        Query query = postsRef.orderByChild("postedTime");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Post> result = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    if (post.postedBy.equals(postFrom))
                        result.add(post);
                }
                updateAdapterData(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addValueEventListener(listener);
    }

    private void updateAdapterData(List<Post> data){
        postOnTimeline.clear();
        System.out.println("Modifing this");
        if (data != null)
            postOnTimeline.addAll(data);
        adapter.notifyDataSetChanged();
    }
}
