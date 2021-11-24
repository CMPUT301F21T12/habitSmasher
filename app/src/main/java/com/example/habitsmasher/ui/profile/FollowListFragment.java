package com.example.habitsmasher.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.ListFragment;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.example.habitsmasher.ui.FollowList;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FollowListFragment extends ListFragment<User> {
    // Initialize variables
    private static final String TAG = "HabitEventListFragment";
    // user who owns this list of habits displayed
    private User _user;
    private String _followType;
    private Context _context;
    private FollowList _followList;
    private FollowItemAdapter _followItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get context
        _context = getContext();

        _user = UserDatabaseHelper.getCurrentUser(_context);
        //_followList = _user.getFollowers();

        if(getArguments() != null){
            Bundle arguments = getArguments();
            _followType = arguments.getString("FollowType");
        }

        // query firebase for all habits that correspond to the current user
        Query query = getListFromFirebase();

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        //populateList(query);
        _followItemAdapter = new FollowItemAdapter(options, _user.getFollowers(), _user.getUsername());
        // Inflate habit event list view
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context, LinearLayoutManager.VERTICAL, false);
        View view = inflater.inflate(R.layout.fragment_follow_list, container, false);

        // Set header title
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(_followType + " List");

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    @Override
    protected Query getListFromFirebase() {
        DocumentReference sampleDocument = _db.collection("Users").document(_user.getId());
        Task<DocumentSnapshot> querySnapshotTask = sampleDocument.get();

        // wait for all the snapshots to come in
        while (!querySnapshotTask.isComplete()) ;

        Map<String, Object> objectMap = querySnapshotTask.getResult().getData();

        ArrayList<String> followList = (ArrayList<String>) objectMap.get("followers");

        return _db.collection("Users").whereIn("id", followList);
    }

    @Override
    protected void populateList(Query query) {
        //get all of the habits
        Task<QuerySnapshot> querySnapshotTask = query.get();

        /*
        populate HabitList with current Habits and habit IDs to initialize state to match
        database, fills when habitList is empty and snapshot is not, which is only
        when app is initially launched
        */
        if (_followList.getFollowList().isEmpty()) {
            // wait for all the snapshots to come in
            while (!querySnapshotTask.isComplete()) ;

            // make a list of all of the habit snapshots
            List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();

            // convert all of the snapshots into proper habits
            for (int i = 0; i < snapshotList.size(); i++) {
                // get the data and convert to hashmap, also print to log
                Map<String, Object> extractMap = snapshotList.get(i).getData();
                Log.d(TAG, extractMap.toString());

                // get all of the data from the snapshot
                ArrayList<String> followers = (ArrayList<String>) extractMap.get("followers");
                ArrayList<String> following = (ArrayList<String>) extractMap.get("following");
                String id = (String) extractMap.get("id");
                String email = (String) extractMap.get("email");
                String username = (String) extractMap.get("username");
                String password = (String) extractMap.get("password");

                // create a new habit with the snapshot data
                User addUser = new User(id, username, email, password, followers, following);

                // add the habit to the local list
                //_followList.addUserLocal(addUser);
            }
        }
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
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            // if row at the specified position is clicked
            public void onRowClicked(int position) {
                openViewWindowForItem(position);
            }
        });
        // connect listener to recycler view
        recyclerView.addOnItemTouchListener(touchListener);
    }

    private void openViewWindowForItem(int position) {
    }

    @Override
    public void updateListAfterAdd(User addedObject) {

    }

    @Override
    public void updateListAfterEdit(User editedObject, int pos) {

    }
}
