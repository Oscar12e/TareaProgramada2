package com.example.tareaprogramada2.Presentations.Fragments;


import android.database.DataSetObservable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.PostAdapter;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.Models.UsersAdapter;
import com.example.tareaprogramada2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference usersRef;
    DatabaseReference postsRef;

    private List<String> profilesResults = new ArrayList<>();
    private List<Post> postResults = new ArrayList<>();
    private RecyclerView.Adapter adapterUser, adapterPosts;

    private RadioGroup radioGroup;
    private RecyclerView recyclerUsers, recyclerPosts;
    private View root;
    private Button searchBtn;
    private EditText searchBar;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initialize(){
        recyclerUsers = root.findViewById(R.id.recycler_profiles);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerPosts = root.findViewById(R.id.recycler_posts);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getContext()));


        adapterPosts = new PostAdapter(postResults, getContext());
        recyclerPosts.setAdapter(adapterPosts);

        adapterUser = new UsersAdapter(profilesResults, getContext());
        recyclerUsers.setAdapter(adapterUser);


        radioGroup = root.findViewById(R.id.searchFilters);
        searchBtn = root.findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 searchPost();
             }
         });

        searchBar = root.findViewById(R.id.tbox_searchQuery);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        postsRef = FirebaseDatabase.getInstance().getReference("posts");


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radbtn_post){
                    searchBtn.setOnClickListener(view -> searchPost());

                } else {
                    searchBtn.setOnClickListener(view -> searchProfiles());
                }
            }
        });

    }

    private String[] getSearchCriteria(){
        String searchCriteria = searchBar.getText().toString().toLowerCase();
        return searchCriteria.split(" ");
    }

    private void searchPost(){
        String[] searchPieces = getSearchCriteria();

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> result = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Post currentPost = ds.getValue(Post.class);
                    String[] postPieces = currentPost.content.body.split(" ");

                    match:
                    for (String pPiece : postPieces){
                        for (String sPiece : searchPieces){
                            if (pPiece.contains(sPiece)){
                                result.add(currentPost);
                                break match;
                            }
                        }
                    }
                }
                //Fin de las comparaciones
                startPostAdapter(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchProfiles(){
        String[] searchPieces = getSearchCriteria();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> result = new ArrayList<>();

                match:
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User currentUser = ds.getValue(User.class);
                    String[] postPieces = currentUser.getFullName().toLowerCase().split(" ");

                    for (String pPiece : postPieces) {
                        for (String sPiece : searchPieces) {
                            if (pPiece.contains(sPiece)) {
                                result.add(currentUser._key);
                                continue match;
                            }
                        }
                    }
                }

                //Fin de las comparaciones
                startUserAdapter(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


    private void startPostAdapter(List<Post> result){
        postResults.clear();

        if (result != null) postResults.addAll(result);

        adapterPosts.notifyDataSetChanged();

        recyclerUsers.setVisibility(View.GONE);
        recyclerPosts.setVisibility(View.VISIBLE);
    }

    private void startUserAdapter(List<String> result){
        profilesResults.clear();

        if (result != null){
            profilesResults.addAll(result);
        }

        adapterUser.notifyDataSetChanged();
        recyclerUsers.setVisibility(View.VISIBLE);
        recyclerPosts.setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_search, container, false);
        initialize();
        return root;
    }

}
