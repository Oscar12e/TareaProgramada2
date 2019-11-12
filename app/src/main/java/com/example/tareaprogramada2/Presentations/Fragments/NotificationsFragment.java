package com.example.tareaprogramada2.Presentations.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tareaprogramada2.Models.FriendshipRequest;
import com.example.tareaprogramada2.Models.NotificationAdapter;
import com.example.tareaprogramada2.Models.Post;
import com.example.tareaprogramada2.Models.PostAdapter;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_notificacions);
        RecyclerView.Adapter adapter = newAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    protected RecyclerView.Adapter newAdapter() {
        User myUser = Session.instance.currentUser;
        Query query;

        query = FirebaseDatabase.getInstance().getReference("request").orderByChild("receiver").equalTo(myUser._key);


        FirebaseRecyclerOptions<FriendshipRequest> options =
                new FirebaseRecyclerOptions.Builder<FriendshipRequest>()
                        .setQuery(query, FriendshipRequest.class)
                        .setLifecycleOwner(this)
                        .build();

        return new NotificationAdapter(options, getContext());
    }


}
