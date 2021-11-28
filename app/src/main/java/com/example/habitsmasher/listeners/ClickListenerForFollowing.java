package com.example.habitsmasher.listeners;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.habitsmasher.R;

public class ClickListenerForFollowing implements View.OnClickListener {
    /**
     * This listener is used for the following button, and leads to the list of people the user
     * is following.
     */
    Fragment _fragment;
    int _navAction;

    public ClickListenerForFollowing(Fragment fragment, int navAction){
        _navAction = navAction;
        _fragment = fragment;
    }

    @Override
    public void onClick(View view) {
        // this is a bundle to tell the fragment whether its following or followers
        Bundle bundle = new Bundle();
        bundle.putString("FollowType", "Following");
        // navigate to following fragment
        navigateToFragmentWithAction(_navAction, bundle);
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
}
