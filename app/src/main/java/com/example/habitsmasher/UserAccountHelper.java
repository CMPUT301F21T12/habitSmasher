package com.example.habitsmasher;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * This helper class is responsible for storing any user account operations used during the login flow
 * @author Rudy Patel
 */
public class UserAccountHelper {
    private static final String LOGIN_FAILED_MESSAGE = "Login failed!";
    private static final String DELETE_USER_FAILED_MESSAGE = "Failed to delete user!";

    private final Context _context;
    private final Fragment _fragment;

    public UserAccountHelper(Context context, Fragment fragment) {
        _context = context;
        _fragment = fragment;
    }

    /**
     * This method adds a new user to the Firestore database
     * @param user the new user to add
     */
    public void addNewUserToDatabase(User user) {
        FirebaseFirestore.getInstance()
                         .collection("Users")
                         .document(user.getId())
                         .set(buildUserDataMap(user))
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if (task.isSuccessful()) {
                                     navigateToFragmentWithAction(R.id.action_login);
                                 } else {
                                     showMessage(LOGIN_FAILED_MESSAGE);
                                 }
                             }
                         });
    }

    public void deleteUserFromDatabase(User user) {
        HabitList userHabitList = user.getHabits();

        // Delete all of a users habits
        for (int i = 0; i < userHabitList.getHabitList().size(); i++) {
            user.getHabits().deleteHabit(_context, user.getId(), user.getHabits().getHabit(i), i);
        }

        // Unfollow all other users
        for (int i = 0; i < user.getFollowerCount(); i++) {
            user.unFollowUser(user.getFollowers().get(i));
        }

        // Remove from all follower lists
        for (int i = 0; i< user.getFollowers().size(); i++) {
            String willUnfollow = user.getFollowers().get(i);
            //UserDatabaseHelper dbHelper = new UserDatabaseHelper();

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
                            deleteUserFromAuth();
                            navigateToFragmentWithAction(R.id.action_logout);
                        } else {
                            showMessage(DELETE_USER_FAILED_MESSAGE);
                        }
                    }
                });
    }

    private void deleteUserFromAuth() {
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
                showMessage(DELETE_USER_FAILED_MESSAGE);
            }
        });
    }

    /**
     * This helper method shows a toast message to the screen
     * @param message message to display
     */
    public void showMessage(String message) {
        Toast.makeText(_context,
                       message, Toast.LENGTH_LONG).show();
    }

    /**
     * This helper method builds up the user data to insert into the database
     * @param user the user data to build up
     * @return a hashmap of key/value pairs of the user data
     */
    @NonNull
    public HashMap<String, Object> buildUserDataMap(User user) {
        HashMap<String, Object> userData = new HashMap<>();

        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("id", user.getId());

        return userData;
    }

    /**
     * This method is responsible for switching the application context to a new fragment
     * @param actionId the action corresponding to the routing of the new fragment
     */
    private void navigateToFragmentWithAction(int actionId) {
        NavController controller = NavHostFragment.findNavController(_fragment);
        controller.navigate(actionId);
    }
}
