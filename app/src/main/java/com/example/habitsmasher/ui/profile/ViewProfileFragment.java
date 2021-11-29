package com.example.habitsmasher.ui.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.ImageDatabaseHelper;
import com.example.habitsmasher.ListFragment;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.example.habitsmasher.ui.dashboard.HabitItemAdapter;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

/**
 * This class handles the logic for viewing some else's profile. This includes
 * Displaying their follower count, and their public habits.
 * @author Cameron Matthew
 */
public class ViewProfileFragment extends ListFragment<Habit> {

    private static final String TAG = "HabitListFragment";

    // user who owns this list of habits displayed
    private User _user;
    private Context _context;

    private ImageView _userImageView;
    private Bitmap _userImage;

    // list of habits being displayed
    private HabitList _habitList;

    private HabitItemAdapter _habitItemAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _context = getContext();

        _user = UserDatabaseHelper.getUser((String) getArguments().get("user"));
        _habitList = _user.getHabits();

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(_user.getUsername());


        // query firebase for all habits that correspond to the current user
        Query query = getListFromFirebase();

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<Habit> options = new FirestoreRecyclerOptions.Builder<Habit>()
                .setQuery(query, Habit.class)
                .build();

        populateList(query);
        _habitItemAdapter = new HabitItemAdapter(options, _habitList, _user.getId(), false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);

        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

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

        initializeRecyclerView(layoutManager, view);

        return view;
    }

    /**
     * This helper method initializes the RecyclerView
     * @param layoutManager the associated LinearLayoutManager
     * @param view the associated View
     */
    public void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.view_profile_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_habitItemAdapter);

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
                if (_habitItemAdapter._snapshots.isEmpty()){}
                else {
                    openViewWindowForItem(position);
                }
            }
        }).setSwipeOptionViews()
                .setSwipeable(R.id.habit_rows_not_swipable, R.id.empty_swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
            @Override
            public void onSwipeOptionClicked(int viewID, int position) {
                return;
            }
        });

        // connect listener to recycler view
        recyclerView.addOnItemTouchListener(touchListener);
    }
    /**
     * This method queries the database for all habits that correspond to the specified user
     * @return resulting firebase query
     */
    @NonNull
    public Query getListFromFirebase() {
        return _db.collection("Users")
                  .document(_user.getId())
                  .collection("Habits")
                  .whereEqualTo("public", true);
    }

    /**
     * Not Needed
     */
    protected void populateList(Query query) {
    }

    @Override
    public void onStart() {
        super.onStart();
        _habitItemAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        _habitItemAdapter.stopListening();
    }

    /**
     * not needed.
     */
    protected void openAddDialogBox() {
        // not needed
    }

    /**
     * not needed.
     */
    public void openEditDialogBox(int position) {
        // not needed
    }

    /**
     * Navigates to the habit view of the specified habit
     * @param position The position of the specified habit
     */
    protected void openViewWindowForItem(int position) {
        // Get the selected habit
        Habit currentHabit = _habitItemAdapter._snapshots.get(position);

        // Create a bundle to be passed into the habitViewFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("habit", currentHabit);
        bundle.putSerializable("userId", _user.getId());
        bundle.putSerializable("isOwner", false);
        NavController controller = NavHostFragment.findNavController(this);

        // Navigate to the habitViewFragment
        controller.navigate(R.id.action_viewProfileFragment_to_habitViewFragment, bundle);
    }

    /**
     * not needed.
     */
    public void updateListAfterAdd(Habit addedHabit){
        // not needed
    }

    /**
     * not needed.
     */
    public void updateListAfterEdit(Habit editedHabit, int pos) {
        // not needed
    }

    /**
     * not needed.
     */
    public void updateListAfterDelete(int pos) {
        // not needed
    }

}