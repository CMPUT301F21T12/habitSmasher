package com.example.habitsmasher;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    // user who this notification list belongs to
    private User _user;

    // list of users currently requesting to follow this user at the moment
    private static ArrayList<String> _requestingUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _user = UserDatabaseHelper.getCurrentUser(getContext());
        Query query = getListFromFirebase();
        FirestoreRecyclerOptions<User> options;
        if (query != null){
            // populate the list with existing items in the database
            options = new FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(query, User.class)
                    .build();
        }
        else {

        }
    }

    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.notifications_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

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
        })
                .setSwipeOptionViews(R.id.accept_request_button, R.id.deny_request_button)
                .setSwipeable(R.id.requester_view, R.id.request_swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                            @Override
                            public void onSwipeOptionClicked(int viewID, int position) {
                                switch (viewID) {
                                    case R.id.accept_request_button:
                                        // accept the request
                                    case R.id.deny_request_button:
                                        // deny the request
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

        ArrayList<String> requestList = (ArrayList<String>) objectMap.get("requesters");
        if(requestList != null && !requestList.isEmpty()) {
            return _db.collection("Users").whereIn("id",requestList);
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

}
