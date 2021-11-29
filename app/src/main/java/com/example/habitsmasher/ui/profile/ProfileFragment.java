package com.example.habitsmasher.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.habitsmasher.AddPictureDialog;
import com.example.habitsmasher.DeleteUserDialog;
import com.example.habitsmasher.ImageDatabaseHelper;
import com.example.habitsmasher.PictureSelectionUser;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

/**
 * UI class that represents and specifies the behaviour of the user's profile screen
 * Currently, only displays information of a test user
 */
public class ProfileFragment extends Fragment implements PictureSelectionUser {
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private static final String USERNAME_SHARED_PREF_TAG = "username";
    private static final String USER_ID_SHARED_PREF_TAG = "userId";
    private static ArrayList<String> EMPTY_FOLLOWER_LIST = new ArrayList<>();
    private static ArrayList<String> EMPTY_FOLLOWING_LIST = new ArrayList<>();
    private static ArrayList<String> EMPTY_REQUEST_LIST = new ArrayList<>();

    private ProfileFragment _fragment = this;
    private ImageView _userImageView;
    private Uri _userImage;
    private User user = new User();
    private ImageDatabaseHelper _imageDatabaseHelper;
    private String _currentUserId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);
        user.setUsername(sharedPref.getString(USERNAME_SHARED_PREF_TAG, "user"));
        _currentUserId = sharedPref.getString(USER_ID_SHARED_PREF_TAG, "id");

        // get the UI elements
        TextView usernameTextView = view.findViewById(R.id.username);
        Button numberOfFollowersButton = view.findViewById(R.id.number_followers);
        Button numberOfFollowingButton = view.findViewById(R.id.number_following);
        FloatingActionButton logoutButton = view.findViewById(R.id.logout_button);
        _userImageView = view.findViewById(R.id.profile_picture);

        // set the UI elements
        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(_currentUserId,
                                                                       numberOfFollowersButton,
                                                                       numberOfFollowingButton);
        usernameTextView.setText("@" + user.getUsername());
        userDatabaseHelper.setFollowingCountOfUser();
        userDatabaseHelper.setFollowerCountOfUser();

        // Fetch profile picture from database
        _imageDatabaseHelper = new ImageDatabaseHelper();
        _imageDatabaseHelper.fetchImagesFromDB(_userImageView, _imageDatabaseHelper.getUserStorageReference(_currentUserId));

        // Add listener to allow users to change profile
        setImageViewListener();

        // Set click listeners for followers and following buttons
        setClickListenerForFollowersButton(numberOfFollowersButton);
        setClickListenerForFollowingButton(numberOfFollowingButton);

        // Set listener for garbage button
        ImageButton deleteButton = view.findViewById(R.id.delete_profile_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteUserDialog deleteUserDialog = new DeleteUserDialog();
                deleteUserDialog.setTargetFragment(ProfileFragment.this, 1);
                deleteUserDialog.show(getFragmentManager(), "DeleteUserDialog");
            }
        });

        return view;
    }

    /**
     * This is the click listener for the following button
     * @param numberOfFollowing
     */
    private void setClickListenerForFollowingButton(Button numberOfFollowing) {
        numberOfFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is a bundle to tell the fragment whether its following or followers
                Bundle bundle = new Bundle();
                bundle.putString("FollowType", "Following");
                // navigate to following fragment
                navigateToFragmentWithAction(R.id.action_navigation_notifications_to_followListFragment, bundle);
            }
        });
    }

    /**
     * This is the click listener for the followers button
     * @param numberOfFollowers
     */
    private void setClickListenerForFollowersButton(Button numberOfFollowers) {
        numberOfFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is a bundle to tell the fragment whether its following or followers
                Bundle bundle = new Bundle();
                bundle.putString("FollowType", "Followers");
                // navigate to followers fragment
                navigateToFragmentWithAction(R.id.action_navigation_notifications_to_followListFragment, bundle);
            }
        });
    }

    /**
     * This function navigates to another fragment with a given bundle
     * @param actionId The navigation action
     * @param bundle The bundle being passed in
     */
    private void navigateToFragmentWithAction(int actionId, Bundle bundle) {
        NavController controller = NavHostFragment.findNavController(_fragment);
        controller.navigate(actionId, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Adds listener to image view to allow user to select image
     */
    protected void setImageViewListener() {
        _userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create add picture dialog
                AddPictureDialog addPictureDialog = new AddPictureDialog();
                addPictureDialog.setTargetFragment(ProfileFragment.this, 1);
                addPictureDialog.show(getFragmentManager(), "AddPictureDialog");
            }
        });
    }

    /**
     * Handles the selected image
     * @param image (Uri) The image that the user has chosen
     */
    public void setImage(Uri image) {
        _userImage = image;
        _userImageView.setImageURI(_userImage);
        updateUserImageInDatabase();
    }

    /**
     * Updates the user profile picture in the database
     */
    public void updateUserImageInDatabase() {
        _imageDatabaseHelper.deleteImageFromDatabase(_imageDatabaseHelper.getUserStorageReference(_currentUserId));
        _imageDatabaseHelper.addImageToDatabase(_imageDatabaseHelper.getUserStorageReference(_currentUserId), _userImage);
    }
}