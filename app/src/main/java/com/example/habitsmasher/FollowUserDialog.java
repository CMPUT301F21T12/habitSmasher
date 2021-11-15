package com.example.habitsmasher;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FollowUserDialog extends DialogFragment implements DisplaysErrorMessages {
    private static final String TAG = "FollowUserDialog";
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private static final String USER_ID_SHARED_PREF_TAG = "userId";
    private static final String INVALID_USERNAME_ERROR_MESSAGE = "Please enter a valid username!";
    private static final String EMPTY_USERNAME_ERROR_MESSAGE = "Please enter a username!";
    private static final String USER_FOLLOWED_SUCCESS_MESSAGE = "User followed!";
    private static final int INVALID_USERNAME_ERROR = 1;
    private static final int EMPTY_USERNAME_ERROR = 2;

    private FirebaseFirestore _db;
    private EditText _userToFollow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.follow_user_dialog, container, false);

        final Dialog followUserDialog = getDialog();

        // Attach UI elements
        _userToFollow = view.findViewById(R.id.user_search_text);
        Button cancelButton = view.findViewById(R.id.cancel_user_follow);
        Button followButton = view.findViewById(R.id.follow_user_button);

        // cancel button logic
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUserDialog.dismiss();
            }
        });

        // follow button logic
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Follow");

                String usernameInput = _userToFollow.getText().toString().trim();

                if (usernameInput.isEmpty()) {
                    displayErrorMessage(EMPTY_USERNAME_ERROR);
                    return;
                }

                CollectionReference usersCollectionRef = _db.collection("Users");
                usersCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document: task.getResult()){
                                if (document.exists()) {
                                    if (document.get("username").toString().equals(usernameInput)){
                                        Log.d(TAG, "Username exists");

                                        // if username exists, update follower/following counts
                                        String currentUserId = getCurrentUserId();

                                        User userToFollow = document.toObject(User.class);
                                        userToFollow.addNewFollower(currentUserId);

                                        // add new follower to the user to follow
                                        addNewFollowerForUserInDatabase(userToFollow.getId(),
                                                                        currentUserId);

                                        // add to new followed user to following of current user
                                        addUserToFollowingForUserInDatabase(currentUserId,
                                                                            userToFollow.getId());

                                        showFollowSuccessMessage();

                                        followUserDialog.dismiss();

                                        return;
                                    }
                                }
                            }
                            // if username does not exist, throw error message on the front-end
                            Log.d(TAG, "Username does not exist");

                            displayErrorMessage(INVALID_USERNAME_ERROR);
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
            }
        });
        return view;
    }

    private void showFollowSuccessMessage() {
        Toast.makeText(getContext(),
                       USER_FOLLOWED_SUCCESS_MESSAGE,
                       Toast.LENGTH_LONG)
             .show();
    }

    private void addUserToFollowingForUserInDatabase(String userId, String followedUserId) {
        DocumentReference userRef = _db.collection("Users").document(userId);
        userRef.update("following", FieldValue.arrayUnion(followedUserId));
    }

    private void addNewFollowerForUserInDatabase(String userId, String newFollowerId) {
        DocumentReference userRef = _db.collection("Users").document(userId);
        userRef.update("followers", FieldValue.arrayUnion(newFollowerId));
    }


    public String getCurrentUserId() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);
        return sharedPref.getString(USER_ID_SHARED_PREF_TAG, "id");
    }

    @Override
    public void displayErrorMessage(int messageType) {
        switch(messageType) {
            case EMPTY_USERNAME_ERROR:
                _userToFollow.setError(EMPTY_USERNAME_ERROR_MESSAGE);
                _userToFollow.requestFocus();
                break;
            case INVALID_USERNAME_ERROR:
                _userToFollow.setError(INVALID_USERNAME_ERROR_MESSAGE);
                _userToFollow.requestFocus();
                break;
        }
    }
}
