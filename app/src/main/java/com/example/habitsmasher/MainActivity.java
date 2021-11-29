package com.example.habitsmasher;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.habitsmasher.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * This is the one and only main driver activity for this application
 *
 * @author Rudy Patel, Kaden Dreger, Jason Kim
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding _binding;
    AppBarConfiguration _appBarConfiguration;
    private NavController _navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        _appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.user_login)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            _navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, _navController, _appBarConfiguration);
            NavigationUI.setupWithNavController(_binding.navView, _navController);
            _navController.navigate(R.id.user_login);
        }

        setOnDestinationChangedListenerForNavController();
    }

    private void setOnDestinationChangedListenerForNavController() {
        _navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                View bottomNav = findViewById(R.id.nav_view);
                if(destination.getId() == R.id.user_login ||
                    destination.getId() == R.id.user_registration) {
                    bottomNav.setVisibility(View.GONE);
                } else {
                    bottomNav.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    /**
     * This function allows for the user to navigate using the back button
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, _appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * This method allows us to inflate the menu bar, where we can add items if the bar is present
     * @param menu menu to inflate
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, adding items to the action bar if present
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * This method is called when an item is selected in the action bar
     * @param item the item selected
     * @return true if item selected, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.follow_user_search_button) {
            openFollowUserDialog();
            return true;
        }
        if (item.getItemId() == R.id.logout_button) {
            FirebaseAuth.getInstance().signOut();
            _navController.navigate(R.id.action_logout);
        }
        if (item.getItemId() == R.id.notifications_button) {
            _navController.navigate(R.id.open_notifications);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed in the application
     */
    @Override
    public void onBackPressed(){
        // if the back button is pressed on any top level destinations, do nothing
        if (!isControllerOnTopLevelDestination()) {
            super.onBackPressed();
        }
    }

    /**
     * This method checks if the application is on any of the specified top-level nav destinations
     * @return boolean true if on top-level, false otherwise
     */
    private boolean isControllerOnTopLevelDestination() {
        return Objects.requireNonNull(_navController.getCurrentDestination())
                      .getId() == R.id.user_login ||
                Objects.requireNonNull(_navController.getCurrentDestination())
                       .getId() == R.id.navigation_dashboard ||
                Objects.requireNonNull(_navController.getCurrentDestination())
                       .getId() == R.id.navigation_home ||
                Objects.requireNonNull(_navController.getCurrentDestination())
                       .getId() == R.id.navigation_notifications;
    }

    /**
     * This helper method is responsible for opening the FollowUserDialog box
     */
    private void openFollowUserDialog() {
        FollowUserDialog followUserDialog = new FollowUserDialog(new FollowUserDialogFragmentDismissHandler(),
                                                                 getApplicationContext());
        followUserDialog.setCancelable(true);
        followUserDialog.setTargetFragment(getSupportFragmentManager().getFragments().get(0), 1);
        followUserDialog.show(getSupportFragmentManager(), "FollowUserDialog");
    }

    /**
     * This class handles the dismissal of the follow user dialog box and updates the follower and
     * following text views on the profile page
     */
    private class FollowUserDialogFragmentDismissHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TextView numFollowers = findViewById(R.id.number_followers);
            TextView numFollowing = findViewById(R.id.number_following);

            UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(UserDatabaseHelper
                                                                                   .getCurrentUser(getApplicationContext())
                                                                                   .getId(),
                                                                           numFollowers,
                                                                           numFollowing);
            userDatabaseHelper.setFollowingCountOfUser();
            userDatabaseHelper.setFollowerCountOfUser();
        }
    }
}
