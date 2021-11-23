package com.example.habitsmasher.ui.profile;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.example.habitsmasher.ui.history.HabitEventItemAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * UI class that represents and specifies the behaviour of the user's profile screen
 * Currently, only displays information of a test user
 */
public class ProfileFragment extends Fragment {
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private static final String USERNAME_SHARED_PREF_TAG = "username";
    private static final String USER_ID_SHARED_PREF_TAG = "userId";

    private ProfileFragment _fragment = this;
    private TextView _numberOfFollowers;
    private TextView _numberOfFollowing;
    private ImageView _userImageView;
    private Bitmap _userImage;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // create the sample user
        User user = new User("1", "TestUser", "123@gmail.com", "123");

        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);
        user.setUsername(sharedPref.getString(USERNAME_SHARED_PREF_TAG, "user"));
        String currentUserId = sharedPref.getString(USER_ID_SHARED_PREF_TAG, "id");

        // get the UI elements
        TextView usernameTextView = view.findViewById(R.id.username);
        _numberOfFollowers = view.findViewById(R.id.number_followers);
        _numberOfFollowing = view.findViewById(R.id.number_following);
        _userImageView = view.findViewById(R.id.profile_picture);
        FloatingActionButton logoutButton = view.findViewById(R.id.logout_button);

        // set the UI elements
        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(currentUserId,
                                                                       _numberOfFollowers,
                                                                       _numberOfFollowing);
        usernameTextView.setText("@" + user.getUsername());
        userDatabaseHelper.setFollowingCountOfUser();
        userDatabaseHelper.setFollowerCountOfUser();

        setClickListenerForLogoutButton(logoutButton);
        fetchUserImageFromDB(currentUserId);

        return view;
    }

    private void setClickListenerForLogoutButton(FloatingActionButton logoutButton) {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                navigateToFragmentWithAction(R.id.action_logout);
            }
        });
    }

    private void navigateToFragmentWithAction(int actionId) {
        NavController controller = NavHostFragment.findNavController(_fragment);
        controller.navigate(actionId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void fetchUserImageFromDB(String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/" + userId + "/" + "userImage");

        final long ONE_MEGABYTE = 1024 * 1024;

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                _userImage = bm;
                _userImageView.setImageBitmap(_userImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get image");
                // If the image hasn't finished uploading to the db yet, try again
                fetchUserImageFromDB(userId);

            }
        });
    }
}