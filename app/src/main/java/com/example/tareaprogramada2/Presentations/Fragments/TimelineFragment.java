package com.example.tareaprogramada2.Presentations.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableRow;

import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.PostAdapter;
import com.example.tareaprogramada2.Presentations.PublishActivity;
import com.example.tareaprogramada2.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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

    protected static final Query teamQuery =
            FirebaseDatabase.getInstance().getReference("posts");


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
        ImageButton postText = myView.findViewById(R.id.btn_text);
        TableRow row_publish = myView.findViewById(R.id.row_publish);

        postText.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), PublishActivity.class);
            startActivity(intent);
        });

        if (allowPost){
            row_publish.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), PublishActivity.class);
                startActivity(intent);
            });
            row_publish.setVisibility(View.VISIBLE);
        } else {
            row_publish.setVisibility(View.GONE);
        }


        RecyclerView recyclerView = myView.findViewById(R.id.recyler_timeline);
        RecyclerView.Adapter adapter = newAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return myView;
    }

    protected RecyclerView.Adapter newAdapter() {
        Query query;
        ObservableSnapshotArray<Post> t;
        if (postFrom.equals("")){
            query = FirebaseDatabase.getInstance().getReference("posts").orderByChild("postedTime");
        } else {
            query = FirebaseDatabase.getInstance().getReference("posts").orderByChild("postedBy").equalTo(postFrom);
        }

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .setLifecycleOwner(this)
                        .build();

        return new PostAdapter(options, getContext());
    }
}
