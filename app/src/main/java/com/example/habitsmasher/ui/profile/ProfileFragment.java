package com.example.habitsmasher.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * UI class that represents and specifies the behaviour of the user's profile screen
 * Currently, only displays information of a test user
 */
public class ProfileFragment extends Fragment {
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private static final String USERNAME_SHARED_PREF_TAG = "username";
    private static final String USER_ID_SHARED_PREF_TAG = "userId";

    private ProfileFragment _fragment = this;

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
        Button numberOfFollowersButton = view.findViewById(R.id.number_followers);
        Button numberOfFollowingButton = view.findViewById(R.id.number_following);
        FloatingActionButton logoutButton = view.findViewById(R.id.logout_button);

        // set the UI elements
        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(currentUserId,
                                                                       numberOfFollowersButton,
                                                                       numberOfFollowingButton);
        usernameTextView.setText("@" + user.getUsername());
        userDatabaseHelper.setFollowingCountOfUser();
        userDatabaseHelper.setFollowerCountOfUser();

        setClickListenerForLogoutButton(logoutButton);
        setClickListenerForFollowersButton(numberOfFollowersButton);
        setClickListenerForFollowingButton(numberOfFollowingButton);

        return view;
    }

    private void setClickListenerForFollowingButton(TextView numberOfFollowing) {
        // TODO: extract click listener
        numberOfFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("FollowType", "Following");
                // navigate to following fragment
                navigateToFragmentWithAction(R.id.action_navigation_notifications_to_followListFragment, bundle);
            }
        });
    }

    private void setClickListenerForFollowersButton(TextView numberOfFollowers) {
        // TODO: extract click listener
        numberOfFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("FollowType", "Followers");
                // navigate to followers fragment
                navigateToFragmentWithAction(R.id.action_navigation_notifications_to_followListFragment, bundle);
            }
        });
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

    private void navigateToFragmentWithAction(int actionId, Bundle bundle) {
        NavController controller = NavHostFragment.findNavController(_fragment);
        controller.navigate(actionId, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}