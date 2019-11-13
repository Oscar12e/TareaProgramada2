package com.example.tareaprogramada2.Presentations.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TableRow;

import com.example.tareaprogramada2.Models.FriendsAdapter;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRIENDS_FROM = "friendsFrom";
    private static final String IS_OWNER = "isOwner";

    // TODO: Rename and change types of parameters
    private String friendsFrom;
    private Boolean isOwner;
    private User profileOwner;
    private RadioGroup filter;

    private View view;
    private RecyclerView recyclerView;
    private TableRow filterRow;

    DatabaseReference reference;
    ValueEventListener currentListener;
    RecyclerView.Adapter adapter;
    List<String> friendsCodes = new ArrayList<>();

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(FRIENDS_FROM, param1);
        args.putBoolean(IS_OWNER, param1.equals(Session.instance.currentUser._key));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            friendsFrom = getArguments().getString(FRIENDS_FROM);
            isOwner = getArguments().getBoolean(IS_OWNER);
        }
    }

    private void initialize(){
        reference = FirebaseDatabase.getInstance().getReference("users").child(this.friendsFrom).child("friends");

        recyclerView = view.findViewById(R.id.recycler_friends);
        //RecyclerView.Adapter adapter = allFriendsApapter();
        //recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        filterRow = view.findViewById(R.id.row_friends);
        filter = view.findViewById(R.id.radGroup_filters);
        adapter = new FriendsAdapter (friendsCodes, getContext());
        recyclerView.setAdapter(adapter);

        filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radbtn_all){
                    setAllFriendsApapter();
                } else {
                    setCommonFriendsAdapter();
                }
            }
        });

        if (this.isOwner){
            filterRow.setVisibility(View.GONE);
        } else {
            filterRow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        initialize();
        setAllFriendsApapter();
        return view;
    }


    private void setAllFriendsApapter() {
        if (currentListener!= null)
            reference.removeEventListener(currentListener);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                List<String> friendsData = dataSnapshot.getValue(t);
                updateAdapterData(friendsData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listener);
        currentListener = listener;
    }

    private void setCommonFriendsAdapter() {
        if (currentListener!= null)
            reference.removeEventListener(currentListener);


        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User myUser = Session.instance.currentUser;
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                List<String> friendsData = dataSnapshot.getValue(t);
                updateAdapterData(myUser.getCommonFriends(friendsData));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listener);
        currentListener = listener;
    }

    private void updateAdapterData(List<String> data){
        friendsCodes.clear();
        System.out.println("Modifing this");
        if (data != null)
            friendsCodes.addAll(data);
        adapter.notifyDataSetChanged();
    }
}
