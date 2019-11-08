package com.example.tareaprogramada2.Presentations.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tareaprogramada2.Presentations.EditInfoActivity;
import com.example.tareaprogramada2.R;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        Button edit = root.findViewById(R.id.btn_edit);

        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), EditInfoActivity.class);
            startActivity(intent);
        });

        return root;
    }


}
