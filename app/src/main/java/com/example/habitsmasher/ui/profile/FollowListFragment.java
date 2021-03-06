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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * This class deals with the creation of the FollowListFragment
 * This class handles both the following and followers lists
 *
 * @author Kaden Dreger
 */
public class FollowListFragment extends ListFragment<User> {
    // Initialize variables
    private static final String TAG = "FollowListFragment";
    private static final String UNFOLLOW_MESSAGE = "Clicked unfollow";
    private static final String CLICKED_FOLLOWER_MESSAGE = "Clicked on a row";
    // user who owns this list of habits displayed
    private User _user;
    // var to tell whether it is the following or followers list
    private String _followType;
    private Context _context;
    private ArrayList<String> _followList;
    private FollowItemAdapter _followItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get context
        _context = getContext();

        // get the current user
        _user = UserDatabaseHelper.getCurrentUser(_context);

        View view = inflater.inflate(R.layout.fragment_follow_list, container, false);

        getBundleArguments();
        // Set header title
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(_followType);

        // query firebase for all habits that correspond to the current user
        Query query = getListFromFirebase();

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        setFollowList();
        // set the item adapter
        _followItemAdapter = new FollowItemAdapter(options, _followList, _user.getId(), _followType);
        // Inflate follow list fragment
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context, LinearLayoutManager.VERTICAL, false);
        initializeRecyclerView(layoutManager, view);
        return view;
    }

    private void setFollowList() {
        // check the follow type and get the corresponding data
        if (_followType.equals("Followers")) {
            _followList = _user.getFollowers();
        } else {
            _followList = _user.getUsersFollowing();
        }
    }

    private void getBundleArguments() {
        // get the bundle arguments
        if(getArguments() != null){
            Bundle arguments = getArguments();
            _followType = arguments.getString("FollowType");
        }
    }

    /**
     * This starts the item adapter listener
     */
    public void onStart(){
        super.onStart();
        _followItemAdapter.startListening();
    }

    /**
     * This stops the item adapter listener
     */
    public void onStop(){
        super.onStop();
        _followItemAdapter.stopListening();
    }

    /**
     * This function performs the action of getting the query from firebase
     * @return The query obtained
     */
    @Override
    protected Query getListFromFirebase() {
        // Sample the current user
        DocumentReference sampleDocument = _db.collection("Users").document(_user.getId());
        Task<DocumentSnapshot> querySnapshotTask = sampleDocument.get();

        // wait for all the snapshots to come in
        while (!querySnapshotTask.isComplete()) ;

        Map<String, Object> objectMap = querySnapshotTask.getResult().getData();

        // get the list of followers/following
        ArrayList<String> followList = (ArrayList<String>) objectMap.get(_followType.toLowerCase());

        // ensure not empty and return
        if(followList != null && !followList.isEmpty()) {
            return _db.collection("Users").whereIn("id", followList);
        }
        // return an empty query
        return _db.collection("Users").whereIn("id", Arrays.asList(""));
    }

    @Override
    protected void populateList(Query query) {
        // not used
    }

    @Override
    protected void openAddDialogBox() {
        // not used
    }

    @Override
    public void openEditDialogBox(int position) {
        // not used
    }

    /**
     * This helper method initializes the RecyclerView
     * @param layoutManager the associated LinearLayoutManager
     * @param view the associated View
     */
    @Override
    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.follow_list_recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
            // set a dummy swipe method to prevent issues
            touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
                @Override
                // if row at the specified position is clicked
                public void onRowClicked(int position) {
                    openViewWindowForItem(position);
                }

            }).setSwipeOptionViews()
                    .setSwipeable(R.id.follow_view_not_swipeable, R.id.empty_swipeable_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                        @Override
                        public void onSwipeOptionClicked(int viewID, int position) {
                            return;
                        }
                    });
        }
        // connect listener to recycler view
        recyclerView.addOnItemTouchListener(touchListener);
    }

    /**
     * This function handles unfollowing the user swiped on
     * @param position The position in the list
     */
    private void unfollowUser(int position) {
        // Toast message to let the user know you have unfollowed someone
        Toast.makeText(_context, UNFOLLOW_MESSAGE, Toast.LENGTH_SHORT).show();
        // get the Id of the user being unfollowed
        String userToRemove = _followItemAdapter.getItem(position).getId();
        // unfollow locally
        _user.unFollowUser(userToRemove);
        // unfollow in db
        removeFollowing(_user.getId(), userToRemove);
        removeFollower(userToRemove, _user.getId());
        // update adapter with new query
        resetOptionsOfAdapter();
    }

    /**
     * This method is responsible for removing a user from following
     * @param userId the user performing the operation
     * @param userToRemove the user that is being removed
     */
    private void removeFollowing(String userId, String userToRemove) {
        DocumentReference userRef = _db.collection("Users").document(userId);
        userRef.update("following", FieldValue.arrayRemove(userToRemove));
    }

    /**
     * This method is responsible for removing the follower of a user
     * @param userId the user performing the operation
     * @param userToRemove the user that is being removed
     */
    private void removeFollower(String userId, String userToRemove) {
        DocumentReference userRef = _db.collection("Users").document(userId);
        userRef.update("followers", FieldValue.arrayRemove(userToRemove));
    }

    /**
     * This function handles opening the view for the selected user
     * @param position Position of the user in the list
     */
    protected void openViewWindowForItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", _followItemAdapter._snapshots.get(position).getId());

        // Navigate to the profileFragment
        NavController controller = NavHostFragment.findNavController(this);
        controller.navigate(R.id.action_followListFragment_to_viewProfileFragment, bundle);

    }

    /**
     * This function updates the _followItemAdapter with any changes to the db
     */
    protected void resetOptionsOfAdapter() {
        _followItemAdapter.stopListening();
        // query firebase for all habits that correspond to the current user
        Query query = getListFromFirebase();

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        _followItemAdapter.updateOptions(options);
        _followItemAdapter.startListening();
    }

    @Override
    public void updateListAfterAdd(User addedObject) {
        // not used
    }

    @Override
    public void updateListAfterEdit(User editedObject, int pos) {
        // not used
    }

    @Override
    public void updateListAfterDelete(int pos) {
        // not used
    }
}
