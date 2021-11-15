package com.example.habitsmasher;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserDatabaseHelper {
    private String _userID;
    private final TextView _numberOfFollowers;
    private final TextView _numberOfFollowing;

    public UserDatabaseHelper(String userID, TextView numFollowers, TextView numFollowing) {
        _numberOfFollowers = numFollowers;
        _numberOfFollowing = numFollowing;
        _userID = userID;
    }

    public void setFollowingCountOfUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("Users").document(_userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userSnapshot = task.getResult();

                    List<Object> following = (List<Object>) userSnapshot.get("following");

                    if (following != null && !following.isEmpty()) {
                        if (following.get(0) == "" && !following.isEmpty()) {
                            _numberOfFollowing.setText("0");
                        } else {
                            _numberOfFollowing.setText(String.valueOf(following.size()));
                        }
                    } else {
                        _numberOfFollowing.setText("0");
                    }
                }
            }
        });
    }

    public void setFollowerCountOfUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("Users").document(_userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userSnapshot = task.getResult();

                    List<Object> followers = (List<Object>) userSnapshot.get("followers");

                    if (followers != null && !followers.isEmpty()) {
                        if (followers.get(0) == "") {
                            _numberOfFollowers.setText("0");
                        } else {
                            _numberOfFollowers.setText(String.valueOf(followers.size()));
                        }
                    } else {
                        _numberOfFollowers.setText("0");
                    }
                }
            }
        });

    }
}
