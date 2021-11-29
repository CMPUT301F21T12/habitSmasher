package com.example.habitsmasher;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.habitsmasher.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * The UserDatabaseHelper is a collection of common db operations relating to the current logged in
 * user like getting/setting the number of followers/following from the database
 * @author Rudy Patel
 */
public class UserDatabaseHelper {
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private static final String TAG = "UserDatabaseHelper";
    private static final String USERNAME_SHARED_PREF_TAG = "username";
    private static final String USER_ID_SHARED_PREF_TAG = "userId";
    private static final String USER_PASSWORD_SHARED_PREF_TAG = "password";
    private static final String USER_EMAIL_SHARED_PREF_TAG = "email";
    private static final String DELETE_USER_FAILED_MESSAGE = "Failed to delete user!";

    private static ArrayList<String> EMPTY_REQUEST_LIST = new ArrayList<>();

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
        try {
            if (following != null && !following.isEmpty()) {
                if (following.get(0) == "") {
                    _numberOfFollowing.setText("0");
                } else {
                    _numberOfFollowing.setText(String.valueOf(following.size()));
                }
            } else {
                _numberOfFollowing.setText("0");
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "TextView for number following not found");
        }
    }

    /**
     * This helper method sets the number of followers edit text on the profile screen
     * @param followers the followers collection of the user
     */
    private void setNumberOfFollowers(List<Object> followers) {
        try {
            if (followers != null && !followers.isEmpty()) {
                if (followers.get(0) == "") {
                    _numberOfFollowers.setText("0");
                } else {
                    _numberOfFollowers.setText(String.valueOf(followers.size()));
                }
            } else {
                _numberOfFollowers.setText("0");
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "TextView for number followers not found");
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

        String username = sharedPref.getString(USERNAME_SHARED_PREF_TAG, "user");
        String userId = sharedPref.getString(USER_ID_SHARED_PREF_TAG, "id");
        String email = sharedPref.getString(USER_EMAIL_SHARED_PREF_TAG, "email");
        String password = sharedPref.getString(USER_PASSWORD_SHARED_PREF_TAG, "password");
        ArrayList<String> followers = getFollowers(userId);
        ArrayList<String> following = getFollowing(userId);

        return new User(userId, username, email, password, followers, following, EMPTY_REQUEST_LIST);
    }

    public static ArrayList<String> getFollowers(String userID){
        DocumentSnapshot userSnapshot = getUserSnapshot(userID);

        return (ArrayList<String>) userSnapshot.get("followers");
    }

    public static ArrayList<String> getFollowing(String userID){
        DocumentSnapshot userSnapshot = getUserSnapshot(userID);

        return (ArrayList<String>) userSnapshot.get("following");
    }

    public static User getUser(String userID){
        DocumentSnapshot userSnapshot = getUserSnapshot(userID);

        String username = (String) userSnapshot.get("username");
        String email = (String) userSnapshot.get("email");
        String password = (String) userSnapshot.get("password");
        ArrayList<String> followers = getFollowers(userID);
        ArrayList<String> following = getFollowing(userID);

        return new User(userID, username, email, password, followers, following, EMPTY_REQUEST_LIST);
    }

    private static DocumentSnapshot getUserSnapshot(String userID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Users").document(userID);

        Task<DocumentSnapshot> documentSnapshotTask = docRef.get();
        while (!documentSnapshotTask.isComplete()) ;

        return documentSnapshotTask.getResult();
    }


    /**
     * Deletes a user from the database
     * @param user (User) The user to delete
     * @param context (Context) The current context
     * @param fragment (ProfileFragment) The parent fragment
     */
    public void deleteUserFromDatabase(User user, Context context, ProfileFragment fragment) {
        HabitList userHabitList = user.getHabits();

        // Delete all of a users habits
        for (int i = 0; i < userHabitList.getHabitList().size(); i++) {
            user.getHabits().deleteHabit(context, user.getId(), user.getHabits().getHabit(i), i);
        }

        ArrayList<String> followers = getFollowers(user.getId());
        ArrayList<String> following = getFollowing(user.getId());

        // Unfollow all other users
        for (int i = 0; i < following.size(); i++) {
            user.unFollowUser(following.get(i));
        }

        // Remove from all follower lists
        for (int i = 0; i < followers.size(); i++) {
            String willUnfollow = followers.get(i);
            User unfollow = getUser(willUnfollow);
            unfollow.unFollowUser(user.getId());
        }

        // Delete user from data base
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(user.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            deleteUserFromAuth(context);
                            navigateToFragmentWithAction(R.id.action_logout, fragment);
                        } else {
                            showMessage(DELETE_USER_FAILED_MESSAGE, context);
                        }
                    }
                });
    }

    /**
     * Deletes a user from firebase authentication
     * @param context (Context) The current cotnext
     */
    private void deleteUserFromAuth(Context context) {
        // Delete user from auth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        currentUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "User deleted successfully.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage(DELETE_USER_FAILED_MESSAGE, context);
            }
        });
    }

    /**
     * This helper method shows a toast message to the screen
     * @param message message to display
     */
    public void showMessage(String message, Context context) {
        Toast.makeText(context,
                message, Toast.LENGTH_LONG).show();
    }

    /**
     * This method is responsible for switching the application context to a new fragment
     * @param actionId the action corresponding to the routing of the new fragment
     */
    private void navigateToFragmentWithAction(int actionId, Fragment fragment) {
        NavController controller = NavHostFragment.findNavController(fragment);
        controller.navigate(actionId);
    }
}
