package com.example.tareaprogramada2.Presentations.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tareaprogramada2.Data.GlideApp;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.Presentations.EditInfoActivity;
import com.example.tareaprogramada2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "user_id";
    private static final String IS_OWNWER = "isOwner";

    // TODO: Rename and change types of parameters
    private String user_id;
    private boolean isOwner;
    private View root;

    private StorageReference storageReference;
    private DatabaseReference database;

    private ImageView profilePicture;
    private TextView name;

    public SectionsPageAdapter pagesAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, boolean param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        args.putString(USER_ID, param1);
        args.putBoolean(IS_OWNWER, param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getString(USER_ID);
            isOwner = getArguments().getBoolean(IS_OWNWER);
        } else {
            System.out.println("There was and issue");
        }
    }

    public void initialize(View view){

        if (this.isOwner){
            initialize(Session.instance.currentUser, view);
        } else {
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users").child(user_id);
            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User myUser = dataSnapshot.getValue(User.class);
                    initialize(myUser, view);
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

    public void initialize(User myUser, View view) {
        storageReference = FirebaseStorage.getInstance().getReference(myUser._key).child(myUser.profilePic);
        profilePicture = view.findViewById(R.id.img_profilePic);
        name = view.findViewById(R.id.txt_username);

        GlideApp.with(this /* context */)
                .load(storageReference)
                .into(profilePicture);

        name.setText(myUser.name + " " + myUser.lastname);
        setupViewPager(view, null);
    }

    public void setupViewPager(View view, ViewPager viewPage){
        User myUser = Session.instance.currentUser;
        SectionsPageAdapter adapter = new SectionsPageAdapter(getFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.pages_profile);

        adapter.addFragment(TimelineFragment.newInstance(myUser._key, false), "Timeline");
        viewPager.setAdapter(adapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        Button edit = root.findViewById(R.id.btn_show);

        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), EditInfoActivity.class);
            startActivity(intent);
        });

        Button logOut = root.findViewById(R.id.btn_logOut);// btn_logOut
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });

        initialize(root);
        return root;
    }


}
