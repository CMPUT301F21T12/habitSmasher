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
import com.example.habitsmasher.HabitList;
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
import com.google.firebase.Timestamp;
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
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private static final String USERNAME_SHARED_PREF_TAG = "username";
    private static final String USER_ID_SHARED_PREF_TAG = "userId";
    private static ArrayList<String> EMPTY_FOLLOWER_LIST = new ArrayList<>();
    private static ArrayList<String> EMPTY_FOLLOWING_LIST = new ArrayList<>();
    private User _user;

    private ProfileFragment _fragment = this;
    private ImageView _userImageView;
    private Bitmap _userImage;

    private HabitItemAdapter _habitItemAdapter;
    private HabitEventItemAdapter _habitEventItemAdaptor;
    private HabitEventList _habitEventList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        _user = UserDatabaseHelper.getCurrentUser(getContext());
        _habitEventList = new HabitEventList();
        setUpEventRecycler(view);

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

    private Habit getParentHabit(String userID, String habitID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("users").document(userID).collection("habits").whereEqualTo("id", habitID);

        Task<QuerySnapshot> querySnapshotTask = query.get();

        while (!querySnapshotTask.isComplete()) ;

        // make a list of all of the habit snapshots
        List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();

        return snapshotList.get(0).toObject(Habit.class);
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

    @Override
    protected Query getListFromFirebase() {

        // habit event query
        // TODO: ONLY PUBLIC HABIT EVENTS
        // Sample the current user
        DocumentReference sampleDocument = _db.collection("Users").document(_user.getId());
        Task<DocumentSnapshot> querySnapshotTask = sampleDocument.get();

        // wait for all the snapshots to come in
        while (!querySnapshotTask.isComplete()) ;

        Map<String, Object> objectMap = querySnapshotTask.getResult().getData();

        // get the list of followers/following
        ArrayList<String> followingList = (ArrayList<String>) objectMap.get("following");

        // ensure not empty and return
        if(followingList != null && !followingList.isEmpty()) {
            return _db.collection("Users")
                    .document().collection("Habits")
                    .document().collection("Events")
                    .whereIn("id", followingList);
        }
        return null;

    }

    @Override
    protected void populateList(Query query) {
    }

    @Override
    public void onStart() {
        super.onStart();
        _habitEventItemAdaptor.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        _habitEventItemAdaptor.stopListening();
    }

    @Override
    protected void openAddDialogBox() {
        //not needed
    }

    @Override
    public void openEditDialogBox(int position) {
        //not needed
    }

    @Override
    protected void openViewWindowForItem(int position) {
        // Get the selected habit event
        HabitEvent currentHabitEvent = _habitEventItemAdaptor._snapshots.get(position);

        // Create a bundle to be passed into the habitEventViewFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("habitEvent", currentHabitEvent);
        bundle.putSerializable("parentHabit",
                getParentHabit(currentHabitEvent.getUserID(),
                        currentHabitEvent.getParentHabitID()));
        bundle.putSerializable("userId", currentHabitEvent.getUserID());

        NavController controller = NavHostFragment.findNavController(this);

        // Navigate to the habitViewFragment
        controller.navigate(R.id.action_navigation_notifications_to_habitEventViewFragment, bundle);


 /*
 // Get the selected habit
            Habit currentHabit = _habitItemAdapter._snapshots.get(position);

            // Create a bundle to be passed into the habitViewFragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("habit", currentHabit);
            bundle.putSerializable("userId", _user.getId());
            NavController controller = NavHostFragment.findNavController(this);

            // Navigate to the habitViewFragment
            controller.navigate(R.id.action_navigation_notifications_to_habitViewFragment, bundle);
  */
    }

    @Override
    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.profile_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(_habitEventItemAdaptor);

        /* Implementation of swipe menu functionality came from this source:
        Name: Velmurugan
        Date: March 4, 2021
        URL: https://howtodoandroid.com/android-recyclerview-swipe-menu
         */
        // create a touch listener which handles the click and swipe function of the RecyclerView
        RecyclerTouchListener touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            // if row at the specified position is clicked
            public void onRowClicked(int position) {
                openViewWindowForItem(position);
            }
        });

        recyclerView.addOnItemTouchListener(touchListener);
    }

    @Override
    public void updateListAfterAdd(User addedObject) {
        //not needed
    }

    @Override
    public void updateListAfterEdit(User editedObject, int pos) {
        //not needed
    }

    @Override
    public void updateListAfterDelete(int pos) {
        //not needed
    }

    private void setUpEventRecycler(View view){
        try {
            // Get query
            Query query = getListFromFirebase();

            // Populate the list with existing items in the database
            FirestoreRecyclerOptions<HabitEvent> options = new FirestoreRecyclerOptions.Builder<HabitEvent>()
                    .setQuery(query, HabitEvent.class)
                    .build();
            populateList(query);

            _habitEventItemAdaptor = new HabitEventItemAdapter(options,
                    _user.getId(),
                    _habitEventList
            );
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);
            initializeRecyclerView(layoutManager, view);

        }
        catch (NullPointerException e){
            // Try catch statement is needed so code doesn't break if there's no events yet, and thus no possible query
            Log.d(TAG, "No habit events to show");
        }
    }
}