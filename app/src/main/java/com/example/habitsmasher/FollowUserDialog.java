package com.example.habitsmasher;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.habitsmasher.listeners.ClickListenerForCancel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * This FollowUserDialog class implements the user search pop-up, where a user can follow another
 * user by specifying their username
 * @author Rudy Patel
 */
public class FollowUserDialog extends DialogFragment implements DisplaysErrorMessages {
    private static final String TAG = "FollowUserDialog";
    private static final String INVALID_USERNAME_ERROR_MESSAGE = "Please enter a valid username!";
    private static final String EMPTY_USERNAME_ERROR_MESSAGE = "Please enter a username!";
    private static final String USER_FOLLOWED_SUCCESS_MESSAGE = "User followed!";
    private static final String CANNOT_FOLLOW_YOURSELF_MESSAGE = "You cannot follow yourself!";
    private static final String ALREADY_REQUESTED_TO_FOLLOW_USER_MESSAGE = "Already requested to follow that user";
    private static final int INVALID_USERNAME_ERROR = 1;
    private static final int EMPTY_USERNAME_ERROR = 2;
    private static final int CANNOT_FOLLOW_YOURSELF_ERROR = 3;
    private static final int ALREADY_REQUESTED_TO_FOLLOW_USER = 4;
    private static final String USERS_COLLECTION_PATH = "Users";
    private static final String USERNAME_FIELD = "username";
    private static final String FOLLOWING_FIELD = "following";
    private static final String FOLLOWERS_FIELD = "followers";

    private FirebaseFirestore _db;
    private EditText _userToFollow;
    private Handler _handler;
    private Context _context;

    public FollowUserDialog(Handler handler, Context context) {
        _handler = handler;
        _context = context;
    }

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
        cancelButton.setOnClickListener(new ClickListenerForCancel(followUserDialog, TAG));

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

                CollectionReference usersCollectionRef = _db.collection(USERS_COLLECTION_PATH);
                usersCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document: task.getResult()){
                                if (document.exists()) {
                                    if (document.get(USERNAME_FIELD).toString().equals(usernameInput)){
                                        Log.d(TAG, "Username exists");

                                        // if username exists, update follower/following counts
                                        String currentUserId = UserDatabaseHelper.getCurrentUser(_context).getId();
                                        String currentUserUsername = UserDatabaseHelper.getCurrentUser(_context).getUsername();

                                        User userToFollow = document.toObject(User.class);

                                        // if the user follows themselves, display error and return
                                        if (userToFollow.getUsername().equals(currentUserUsername)) {
                                            displayErrorMessage(CANNOT_FOLLOW_YOURSELF_ERROR);
                                            return;
                                        }
                                        // if the user requests to follow someone they have already followed
                                        if (searchInRequestsList(userToFollow.getId(),currentUserId)) {
                                            displayErrorMessage(ALREADY_REQUESTED_TO_FOLLOW_USER);
                                            return;
                                        }
                                        userToFollow.addFollowRequest(currentUserId);
                                        
                                        addUsertoRequestsListInDatabase(currentUserId, userToFollow.getId());

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

    /**
     * A helper method that shows a toast message after the user follow operation is successful
     */
    private void showFollowSuccessMessage() {
        Toast.makeText(getContext(),
                       USER_FOLLOWED_SUCCESS_MESSAGE,
                       Toast.LENGTH_LONG)
             .show();
    }

    /**
     * Detrimines if a user has already requested to follow this user already
     * @param followedId
     * @param followerId
     * @return
     */
    private boolean searchInRequestsList(String followedId, String followerId) {
        // retrieving document of followed user
        DocumentReference userDocument = _db.collection("Users").document(followedId);
        Task<DocumentSnapshot> querySnapshotTask = userDocument.get();

        while (!querySnapshotTask.isComplete());
        Map<String, Object> objectMap = querySnapshotTask.getResult().getData();
        ArrayList<String> requestList = (ArrayList<String>) objectMap.get("followRequest");
        if (requestList != null && !requestList.isEmpty()) {
            return requestList.contains(followerId);
        }
        return false;
    }

    /**
     * Adds a user to the list of users requesting to follow another user
     * @param followerUserId user requesting to follow
     * @param followedUserId user following
     */
    private void addUsertoRequestsListInDatabase(String followerUserId, String followedUserId) {
        DocumentReference userRef = _db.collection(USERS_COLLECTION_PATH).document(followedUserId);
        userRef.update("followRequests", FieldValue.arrayUnion(followerUserId));
    }

    //TODO: migrate these methods to NotificationFragment
    /**
     * This method is responsible for adding a new user to the following array of the given user
     * @param userId the user performing the operation
     * @param followedUserId the followed user to add to the collection
     */
    private void addUserToFollowingForUserInDatabase(String userId, String followedUserId) {
        DocumentReference userRef = _db.collection(USERS_COLLECTION_PATH).document(userId);
        userRef.update(FOLLOWING_FIELD, FieldValue.arrayUnion(followedUserId));
    }

    /**
     * This method is responsible for adding a new user to the follower array of the given user
     * @param userId the user performing the operation
     * @param newFollowerId the user that is now a new follower of the given user
     */
    private void addNewFollowerForUserInDatabase(String userId, String newFollowerId) {
        DocumentReference userRef = _db.collection(USERS_COLLECTION_PATH).document(userId);
        userRef.update(FOLLOWERS_FIELD, FieldValue.arrayUnion(newFollowerId));
    }

    /**
     * This helper method displays error messages in the dialog box
     * @param messageType code indicating the type of error message to
     */
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
            case CANNOT_FOLLOW_YOURSELF_ERROR:
                _userToFollow.setError(CANNOT_FOLLOW_YOURSELF_MESSAGE);
                _userToFollow.requestFocus();
                break;
            case ALREADY_REQUESTED_TO_FOLLOW_USER:
                _userToFollow.setError(ALREADY_REQUESTED_TO_FOLLOW_USER_MESSAGE);
                _userToFollow.requestFocus();
                break;
        }
    }

    /**
     * This helper method allows us to trigger an action on the dismissal of the dialog box
     * @param dialog the dialog box to dismiss
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        _handler.sendEmptyMessage(0);
    }
}
