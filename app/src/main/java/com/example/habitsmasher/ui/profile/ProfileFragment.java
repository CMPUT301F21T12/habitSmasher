package com.example.habitsmasher.ui.profile;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.ImageDatabaseHelper;
import com.example.habitsmasher.ListFragment;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.example.habitsmasher.listeners.ClickListenerForFollowers;
import com.example.habitsmasher.listeners.ClickListenerForFollowing;
import com.example.habitsmasher.ui.dashboard.HabitItemAdapter;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
import com.example.habitsmasher.ui.history.HabitEventItemAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * UI class that represents and specifies the behaviour of the user's profile screen
 * Currently, only displays information of a test user
 */
public class ProfileFragment extends ListFragment<User> {
    private static ArrayList<String> EMPTY_FOLLOWER_LIST = new ArrayList<>();
    private static ArrayList<String> EMPTY_FOLLOWING_LIST = new ArrayList<>();
    private User _user;
    private static ArrayList<String> EMPTY_REQUEST_LIST = new ArrayList<>();

    private ProfileFragment _fragment = this;
    private ImageView _userImageView;
    private Bitmap _userImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        _user = UserDatabaseHelper.getCurrentUser(getContext());

        // get the UI elements
        TextView usernameTextView = view.findViewById(R.id.username);
        Button numberOfFollowersButton = view.findViewById(R.id.number_followers);
        Button numberOfFollowingButton = view.findViewById(R.id.number_following);
        FloatingActionButton logoutButton = view.findViewById(R.id.logout_button);
        _userImageView = view.findViewById(R.id.profile_picture);

        // set the UI elements
        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(_user.getId(),
                                                                       numberOfFollowersButton,
                                                                       numberOfFollowingButton);
        usernameTextView.setText("@" + _user.getUsername());
        userDatabaseHelper.setFollowingCountOfUser();
        userDatabaseHelper.setFollowerCountOfUser();

        // Fetch profile picture from database
        ImageDatabaseHelper imageDatabaseHelper = new ImageDatabaseHelper();
        imageDatabaseHelper.fetchImagesFromDB(_userImageView, imageDatabaseHelper.getUserStorageReference(_user.getId()));

        // Set click listeners for followers and following buttons
        numberOfFollowersButton.setOnClickListener(
                new ClickListenerForFollowers(this, R.id.action_navigation_notifications_to_followListFragment));
        numberOfFollowingButton.setOnClickListener(
                new ClickListenerForFollowing(this, R.id.action_navigation_notifications_to_followListFragment));

        return view;
    }

    /**
     * Not needed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Not needed
     */
    @Override
    protected Query getListFromFirebase() {

        return null;
    }

    /**
     * Not needed
     */
    @Override
    protected void populateList(Query query) {
    }

    /**
     * Not needed
     */
    @Override
    public void onStart() {
        super.onStart();
        //_habitEventItemAdaptor.startListening();
    }

    /**
     * Not needed
     */
    @Override
    public void onStop()
    {
        super.onStop();
        //_habitEventItemAdaptor.stopListening();
    }

    /**
     * Not needed
     */
    @Override
    protected void openAddDialogBox() {
        //not needed
    }

    /**
     * Not needed
     */
    @Override
    public void openEditDialogBox(int position) {
        //not needed
    }

    /**
     * Not needed
     */
    @Override
    protected void openViewWindowForItem(int position) {
    }

    /**
     * Not needed
     */
    @Override
    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {

    }

    /**
     * Not needed
     */
    @Override
    public void updateListAfterAdd(User addedObject) {

    }

    /**
     * Not needed
     */
    @Override
    public void updateListAfterEdit(User editedObject, int pos) {

    }

    /**
     * Not needed
     */
    @Override
    public void updateListAfterDelete(int pos) {

    }

}