package com.example.habitsmasher;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * The UserDatabaseHelper is a collection of common db operations relating to the current logged in
 * user like getting/setting the number of followers/following from the database
 * @author Rudy Patel
 */
public class UserDatabaseHelper {
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private final String _userID;
    private final TextView _numberOfFollowers;
    private final TextView _numberOfFollowing;

    public UserDatabaseHelper(String userID, TextView numFollowers, TextView numFollowing) {
        _numberOfFollowers = numFollowers;
        _numberOfFollowing = numFollowing;
        _userID = userID;
    }

    /**
     * This method sets the following count of the user specified on the profile page
     */
    public void setFollowingCountOfUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("Users").document(_userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userSnapshot = task.getResult();

                    List<Object> following = (List<Object>) userSnapshot.get("following");

                    setNumberOfFollowing(following);
                }
            }
        });
    }

    /**
     * This method sets the follower count of the current user in the profile screen
     */
    public void setFollowerCountOfUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("Users").document(_userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userSnapshot = task.getResult();

                    List<Object> followers = (List<Object>) userSnapshot.get("followers");

                    setNumberOfFollowers(followers);
                }
            }
        });

    }

    /**
     * This helper method sets the number of following edit text on the profile screen
     * @param following the following collection of the user
     */
    private void setNumberOfFollowing(List<Object> following) {
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

    /**
     * This helper method sets the number of followers edit text on the profile screen
     * @param followers the followers collection of the user
     */
    private void setNumberOfFollowers(List<Object> followers) {
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

    /**
     * This method returns the current user logged in.
     * @param context The context of the call. Needed to get the info of the user.
     * @return Newly created user object
     */
    @NonNull
    public static User getCurrentUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);

        String username = sharedPref.getString("username", "user");
        String userId = sharedPref.getString("userId", "id");
        String email = sharedPref.getString("email", "email");
        String password = sharedPref.getString("password", "password");

        return new User(userId, username, email, password);
    }
}
