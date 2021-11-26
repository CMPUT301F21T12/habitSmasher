package com.example.habitsmasher;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.listeners.SwipeListener;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Fragment representing the list of follow requests
 * currently queued for a user.
 */
public class NotificationFragment extends ListFragment<User> {

    private static final String FOLLOWING_FIELD = "following";
    private static final String FOLLOWERS_FIELD = "followers";
    private static final String FOLLOW_REQUEST_FIELD = "followRequests";
    private static final String USERS_COLLECTION_PATH = "Users";
    private static final String NOTIFICATIONS_HEADER = "Notifications";

    // user who this notification list belongs to
    private User _user;

    private Context _context;

    // list of users currently requesting to follow this user at the moment
    private static ArrayList<String> _requestingUsers;

    public NotificationItemAdapter _notificationItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _context = getContext();
        _user = UserDatabaseHelper.getCurrentUser(_context);

        // Set up view
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Set header title
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setTitle(NOTIFICATIONS_HEADER);

        // querying for user requesting to follow this user
        Query query = getListFromFirebase();

        if (query != null){
            // populate the list with existing items in the database
            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                                                                                 .setQuery(query,
                                                                                           User.class)
                                                                                 .build();
            _requestingUsers = _user.getFollowRequests();

            _notificationItemAdapter = new NotificationItemAdapter(options,
                                                                   _requestingUsers,
                                                                   _user.getId());

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                                                                        LinearLayoutManager.VERTICAL,
                                                             false);
            initializeRecyclerView(layoutManager, view);
        }

        return view;
    }

    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.notifications_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_notificationItemAdapter);

        /* Implementation of swipe menu functionality came from this source:
        Name: Velmurugan
        Date: March 4, 2021
        URL: https://howtodoandroid.com/android-recyclerview-swipe-menu
         */
        // create a touch listener which handles the click and swipe function of the RecyclerView
        RecyclerTouchListener touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        touchListener.setSwipeOptionViews(R.id.accept_request_button, R.id.deny_request_button)
                .setSwipeable(R.id.requester_view, R.id.request_swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                            @Override
                            public void onSwipeOptionClicked(int viewID, int position) {
                                String followerID = _notificationItemAdapter._snapshots.get(position).getId();
                                switch (viewID) {
                                    case R.id.accept_request_button:
                                        // accept the request
                                        removeFollowRequestForUserInDatabase(_user.getId(),
                                                followerID);
                                        _notificationItemAdapter.notifyItemChanged(position);
                                        addNewFollowerForUserInDatabase(_user.getId(), followerID);
                                        addUserToFollowingForUserInDatabase(followerID, _user.getId());
                                    case R.id.deny_request_button:
                                        // deny the request
                                        removeFollowRequestForUserInDatabase(_user.getId(),
                                                _notificationItemAdapter._snapshots.get(position).getId());
                                        _notificationItemAdapter.notifyItemChanged(position);
                                }
                            }
                });
        // connect listener to recycler view
        recyclerView.addOnItemTouchListener(touchListener);
    }

    protected Query getListFromFirebase() {
        DocumentReference sampleDocument = _db.collection("Users").document(_user.getId());
        Task<DocumentSnapshot> querySnapshotTask = sampleDocument.get();

        while (!querySnapshotTask.isComplete());

        Map<String, Object> objectMap = querySnapshotTask.getResult().getData();

        ArrayList<String> requestList = (ArrayList<String>) objectMap.get(FOLLOW_REQUEST_FIELD);
        if(requestList != null && !requestList.isEmpty()) {
            return _db.collection("Users").whereIn("id", requestList);
        }
        return null;
    }

    protected void populateList(Query query) {

    }

    @Override
    protected void openAddDialogBox() {
        // not needed
    };

    @Override
    public void openEditDialogBox(int position) {
        // not needed
    }

    @Override
    protected void openViewWindowForItem(int position) {
        // not needed
    }

    @Override
    public void updateListAfterAdd(User addedObject) {
        // list does not support adding
    };

    @Override
    public void updateListAfterEdit(User editedObject, int pos) {
        // list does not support editing
    };

    @Override
    public void updateListAfterDelete(int pos) {
        // list does not support deletion
    };

    /**
     * This starts the item adapter listener
     */
    @Override
    public void onStart(){
        super.onStart();
        if (_notificationItemAdapter != null) {
            _notificationItemAdapter.startListening();
        }
    }

    /**
     * This stops the item adapter listener
     */
    @Override
    public void onStop(){
        super.onStop();
        if (_notificationItemAdapter != null) {
            _notificationItemAdapter.stopListening();
        }
    }

    /**
     * This method is responsible for adding a new user to the following array of the given user
     * @param userId the user performing the operation
     * @param followedUserId the followed user to add to the collection
     */
    private void addUserToFollowingForUserInDatabase(String userId, String followedUserId) {
        DocumentReference userRef = _db.collection(USERS_COLLECTION_PATH).document(userId);
        userRef.update(FOLLOWING_FIELD, FieldValue.arrayUnion(followedUserId));
    }

    /**
     * This method is responsible for adding a new user to the follower array of the given user
     * @param userId the user performing the operation
     * @param newFollowerId the user that is now a new follower of the given user
     */
    private void addNewFollowerForUserInDatabase(String userId, String newFollowerId) {
        DocumentReference userRef = _db.collection(USERS_COLLECTION_PATH).document(userId);
        userRef.update(FOLLOWERS_FIELD, FieldValue.arrayUnion(newFollowerId));
    }

    /**
     * Method that removes a request to follow a user from the database
     * @param followerId user being sent request
     * @param followedId user sending request
     */
    private void removeFollowRequestForUserInDatabase(String followedId, String followerId) {
        DocumentReference userRef = _db.collection(USERS_COLLECTION_PATH).document(followedId);
        userRef.update(FOLLOW_REQUEST_FIELD, FieldValue.arrayRemove(followerId));
    }


}
