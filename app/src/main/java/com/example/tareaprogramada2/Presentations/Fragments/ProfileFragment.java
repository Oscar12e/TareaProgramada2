package com.example.tareaprogramada2.Presentations.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tareaprogramada2.Data.GlideApp;
import com.example.tareaprogramada2.Models.FriendshipRequest;
import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.Presentations.EditInfoActivity;
import com.example.tareaprogramada2.Presentations.LoginActivity;
import com.example.tareaprogramada2.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

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
    private User profileOwner;

    private ImageView profilePicture;
    private TextView name, message;
    private Button edit, logOut, sendRequest, removeFriend;
    private LinearLayout configLayout;


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
        args.putBoolean(IS_OWNWER, param1.equals(Session.instance.currentUser._key));

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

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            getChildFragmentManager().beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit();
        }
    }


    private void showOwnerWidgets(){
        configLayout.setVisibility(View.VISIBLE);
        removeFriend.setVisibility(View.GONE);
        sendRequest.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

    }

    private void visitingProfile(){
        configLayout.setVisibility(View.GONE);
        removeFriend.setVisibility(View.GONE);
        sendRequest.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

        User myUser = Session.instance.currentUser;
        if (myUser.friends.contains(profileOwner._key)){
            removeFriend.setVisibility(View.VISIBLE);
            return;
        }

        database = FirebaseDatabase.getInstance().getReference("request");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    FriendshipRequest request = ds.getValue(FriendshipRequest.class);
                    if (request.sender.equals(myUser._key) && request.receiver.equals(profileOwner._key)){
                        message.setText("Solicitud de amistad enviada.");
                        message.setVisibility(View.VISIBLE);
                        return;
                    } else if (request.receiver.equals(myUser._key) && request.sender.equals(profileOwner._key)){
                        message.setText("Para aceptar la solicitud vaya a la ventana de notificaciones.");
                        message.setVisibility(View.VISIBLE);
                        return;
                    }
                }

                sendRequest.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LOGIN", "Failed to read value.", error.toException());
                System.out.println("LOGIN" + "Failed to read value." + error.toException());
            }
        });

        // querying to know if request sent
    }

    public void initialize(View view){
        profilePicture = view.findViewById(R.id.img_profilePic);
        name = view.findViewById(R.id.txt_username);

        message = view.findViewById(R.id.txt_message);
        sendRequest = view.findViewById(R.id.btn_sendFriendship);
        removeFriend = view.findViewById(R.id.btn_remove);
        edit = root.findViewById(R.id.btn_edit);
        logOut = root.findViewById(R.id.btn_logOut);// btn_logOut
        configLayout = view.findViewById(R.id.layout_config);

        if (this.isOwner){
            setupComponents(Session.instance.currentUser, view);
            showOwnerWidgets();
        } else {
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users").child(user_id);
            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User myUser = dataSnapshot.getValue(User.class);
                    setupComponents(myUser, view);
                    visitingProfile();
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

    public void setupComponents(User myUser, View view) {
        profileOwner = myUser;

        if (!profileOwner.profilePic.equals("")){
            storageReference = FirebaseStorage.getInstance().getReference(profileOwner._key).child(profileOwner.profilePic);
        } else {
            storageReference = FirebaseStorage.getInstance().getReference("default").child("user_default.png");
        }


        GlideApp.with(this /* context */)
                .load(storageReference)
                .circleCrop()
                .into(profilePicture);

        name.setText(myUser.name + " " + myUser.lastname);

        ViewPager viewPager = view.findViewById(R.id.pages_profile);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs_profile);
        tabLayout.setupWithViewPager(viewPager);


        edit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditInfoActivity.class);
            startActivity(intent);
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFriendshipRequest();
            }
        });

    }

    public void setupViewPager(ViewPager viewPager){

        SectionsPageAdapter adapter = new SectionsPageAdapter(getFragmentManager());

        System.out.println("********************************");
        System.out.println("Setting up viewpager");

        adapter.addFragment(TimelineFragment.newInstance(profileOwner._key, false), "Muro");
        adapter.addFragment(FriendsFragment.newInstance(profileOwner._key, ""), "Amigos");
        adapter.addFragment(InformationFragment.newInstance("", ""), "Information");

        System.out.println("********************************");
        viewPager.setAdapter(adapter);
    }

    private void sendFriendshipRequest(){
        User myUser = Session.instance.currentUser;
        FriendshipRequest request = new FriendshipRequest();
        request.sender = myUser._key;
        request.receiver = profileOwner._key;

        database = FirebaseDatabase.getInstance().getReference("request");
        String key = database.push().getKey();
        request._key = key;
        Map<String, Object> map = request.toMap();
        database.child(key).setValue(map);
        //So, this should make a difference
        message.setText("¡Solicitud enviada!");
        sendRequest.setVisibility(View.GONE);
        message.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        initialize(root);
        return root;
    }
}
