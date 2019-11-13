package com.example.tareaprogramada2.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaprogramada2.Data.UserHolder;
import com.example.tareaprogramada2.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserHolder>{
    private Context context;
    private List<String> data;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UsersAdapter(List<String> data, Context context) {
        this.context = context;
        this.data = data;
    }



    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        //holder.bind( context );
        holder.bind(data.get(position), context);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
