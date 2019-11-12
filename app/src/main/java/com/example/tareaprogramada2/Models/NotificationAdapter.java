package com.example.tareaprogramada2.Models;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.tareaprogramada2.Data.NotificationHolder;
import com.example.tareaprogramada2.Data.PostHolder;
import com.example.tareaprogramada2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NotificationAdapter extends FirebaseRecyclerAdapter<FriendshipRequest, NotificationHolder> {
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<FriendshipRequest> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationHolder notificationHolder, int i, @NonNull FriendshipRequest notification) {
        notificationHolder.bind(notification, context);
    }


}
