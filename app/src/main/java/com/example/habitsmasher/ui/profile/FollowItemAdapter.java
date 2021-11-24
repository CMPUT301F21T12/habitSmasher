package com.example.habitsmasher.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.ItemAdapter;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class FollowItemAdapter extends ItemAdapter<User, FollowItemAdapter.FollowViewHolder> {
    private static ArrayList<String> _followList;
    private final String _userId;
    private Context _context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FollowItemAdapter(@NonNull FirestoreRecyclerOptions<User> options,
                             ArrayList<String> followList,
                             String userId) {
        super(options);
        _followList = followList;
        _userId = userId;
    }


    @Override
    protected void onBindViewHolder(@NonNull FollowViewHolder holder, int position, @NonNull User user) {
        // set necessary elements of the habit
        holder._userName.setText(user.getUsername());
        // not implemented
        //holder._profilePicture.setImageURI(user.getProfilePicture());
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        View view = LayoutInflater.from(_context).inflate(R.layout.follow_row, parent, false);
        return new FollowItemAdapter.FollowViewHolder(view);
    }

    /**
     * Class that holds the necessary elements of an individual row in the HabitList
     */
    public static class FollowViewHolder extends RecyclerView.ViewHolder {
        private final TextView _userName;
        private final ImageView _profilePicture;
        private final ConstraintLayout _followRows;

        /**
         * Constructs a view holder
         * @param itemView view of row of RecyclerView which is held in ViewHolder
         */
        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            _followRows = itemView.findViewById(R.id.follow_rows);
            _userName = itemView.findViewById(R.id.follow_username);
            _profilePicture = itemView.findViewById(R.id.follow_profile_picture);
        }
    }
}
