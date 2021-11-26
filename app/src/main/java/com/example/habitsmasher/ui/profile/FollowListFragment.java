package com.example.habitsmasher.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.ListFragment;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class FollowListFragment extends ListFragment<User> {
    // Initialize variables
    private static final String TAG = "HabitEventListFragment";
    // user who owns this list of habits displayed
    private User _user;
    private String _followType;
    private Context _context;
    private ArrayList<String> _followList;
    private FollowItemAdapter _followItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get context
        _context = getContext();

        _user = UserDatabaseHelper.getCurrentUser(_context);

        if(getArguments() != null){
            Bundle arguments = getArguments();
            _followType = arguments.getString("FollowType");
        }

        // query firebase for all habits that correspond to the current user
        Query query = getListFromFirebase();
        FirestoreRecyclerOptions<User> options;
        if(query != null){
            // populate the list with existing items in the database
            options = new FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(query, User.class)
                    .build();
        }
        else{
            View view = inflater.inflate(R.layout.fragment_follow_list, container, false);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(_followType + " List");
            return view;
        }
        if (_followType.equals("Followers")) {
            _followList = _user.getFollowers();
        } else {
            _followList = _user.getUsersFollowing();
        }
        //populateList(query);
        _followItemAdapter = new FollowItemAdapter(options, _followList, _user.getUsername());
        // Inflate habit event list view
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context, LinearLayoutManager.VERTICAL, false);
        View view = inflater.inflate(R.layout.fragment_follow_list, container, false);

        // Set header title
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(_followType + " List");

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    public void onStart(){
        super.onStart();
        if (_followItemAdapter != null) {
            _followItemAdapter.startListening();
        }
    }

    public void onStop(){
        super.onStop();
        if (_followItemAdapter != null) {
            _followItemAdapter.stopListening();
        }
    }

    @Override
    protected Query getListFromFirebase() {
        DocumentReference sampleDocument = _db.collection("Users").document(_user.getId());
        Task<DocumentSnapshot> querySnapshotTask = sampleDocument.get();

        // wait for all the snapshots to come in
        while (!querySnapshotTask.isComplete()) ;

        Map<String, Object> objectMap = querySnapshotTask.getResult().getData();

        ArrayList<String> followList = (ArrayList<String>) objectMap.get(_followType.toLowerCase());

        if(followList != null && !followList.isEmpty()) {
            return _db.collection("Users").whereIn("id", followList);
        }
        return null;

    }

    @Override
    protected void populateList(Query query) {
    }

    @Override
    protected void openAddDialogBox() {
    }

    /**
     * This helper method initializes the RecyclerView
     * @param layoutManager the associated LinearLayoutManager
     * @param view the associated View
     */
    @Override
    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.follow_list_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(_followItemAdapter);

        /* Implementation of swipe menu functionality came from this source:
        Name: Velmurugan
        Date: March 4, 2021
        URL: https://howtodoandroid.com/android-recyclerview-swipe-menu
         */
        // create a touch listener which handles the click and swipe function of the RecyclerView
        RecyclerTouchListener touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        // This is to differentiate between following and followers, it will enable or disable swiping
        if (_followType.equals("Following")) {
            touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
                @Override
                // if row at the specified position is clicked
                public void onRowClicked(int position) {
                    openViewWindowForItem(position);
                }
            })
                    // This is for the swipe options to unfollow a user.
                    .setSwipeOptionViews(R.id.unfollow_button)
                    .setSwipeable(R.id.follow_view, R.id.unfollow_follow_swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                        @Override
                        public void onSwipeOptionClicked(int viewID, int position) {
                            // unfollow user
                            switch (viewID) {
                                case R.id.unfollow_button:
                                    unfollowUser(position);
                                    break;
                            }
                        }
                    });
        } else {
            touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
                @Override
                // if row at the specified position is clicked
                public void onRowClicked(int position) {
                    openViewWindowForItem(position);
                }
            });
        }
        // connect listener to recycler view
        recyclerView.addOnItemTouchListener(touchListener);
    }

    private void unfollowUser(int position) {
        Toast.makeText(_context, "Clicked unfollow", Toast.LENGTH_SHORT).show();
    }

    private void openViewWindowForItem(int position) {
        Toast.makeText(_context, "Clicked on a row", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListAfterAdd(User addedObject) {
    }

    @Override
    public void updateListAfterEdit(User editedObject, int pos) {
    }
}
