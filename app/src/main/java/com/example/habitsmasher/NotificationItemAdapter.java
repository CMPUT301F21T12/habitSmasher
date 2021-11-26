package com.example.habitsmasher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import java.util.ArrayList;

public class NotificationItemAdapter extends
            ItemAdapter<User, NotificationItemAdapter.RequestViewHolder>{

    public ObservableSnapshotArray<User> _snapshots;

    private static ArrayList<String> _requestList;
    private final String _userId;
    private Context _context;

    public NotificationItemAdapter(@NonNull FirestoreRecyclerOptions<User> options,
                             ArrayList<String> requestList,
                             String userId) {
        super(options);
        _snapshots = options.getSnapshots();
        _requestList = requestList;
        _userId = userId;
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull User user) {
        holder._requestUserName.setText(user.getUsername());
    }

    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        View view = LayoutInflater.from(_context).inflate(R.layout.fragment_notification_row,
                                                          parent, false);
        return new NotificationItemAdapter.RequestViewHolder(view);

    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        private final TextView _requestUserName;
        private final ImageView _profilePicture;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            _requestUserName = itemView.findViewById(R.id.requester_username);
            _profilePicture = itemView.findViewById(R.id.profile_pic_requester);
        }

    }

}
