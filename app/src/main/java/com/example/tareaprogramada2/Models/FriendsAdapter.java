package com.example.tareaprogramada2.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.tareaprogramada2.Data.FriendsHolder;
import com.example.tareaprogramada2.Data.PostHolder;
import com.example.tareaprogramada2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FriendsAdapter extends FirebaseRecyclerAdapter<String, FriendsHolder> {
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FriendsAdapter(@NonNull FirebaseRecyclerOptions<String> options , Context context) {
        super(options);
        this.context = context;
    }



    @NonNull
    @Override
    public FriendsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false));

    }

    @Override
    protected void onBindViewHolder(@NonNull FriendsHolder friendsHolder, int i, @NonNull String user) {
        friendsHolder.bind(user, context);
    }
}
