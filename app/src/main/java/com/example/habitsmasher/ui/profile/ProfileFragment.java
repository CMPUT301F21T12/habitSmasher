package com.example.habitsmasher.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * UI class that represents and specifies the behaviour of the user's profile screen
 * Currently, only displays information of a test user
 */
public class ProfileFragment extends Fragment {
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private ProfileFragment _fragment = this;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // create the sample user
        User user = new User("1", "TestUser", "123@gmail.com", "123");

        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);
        user.setUsername(sharedPref.getString("username", "user"));

        // get the UI elements
        TextView usernameTextView = view.findViewById(R.id.username);
        TextView followers = view.findViewById(R.id.number_followers);
        TextView following = view.findViewById(R.id.number_following);
        FloatingActionButton logoutButton = view.findViewById(R.id.logout_button);

        // set the UI elements
        usernameTextView.setText("@" + user.getUsername());
        followers.setText(String.valueOf(user.getFollowerCount()));
        following.setText(String.valueOf(user.getFollowingCount()));

        setClickListenerForLogoutButton(logoutButton);

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
}