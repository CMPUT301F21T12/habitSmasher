package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class HabitListFragment extends Fragment {

    private static final String TAG = "HabitListFragment";

    private final User _user = new User("TestUser", "123");
    private final HabitList _habitList = _user.getHabits();
    private HabitItemAdapter _habitItemAdapter;
    private final HabitListFragment _fragment = this;
    FirebaseFirestore _db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();

        // query firebase for all habits that correspond to the current user
        Query query = getListOfHabitsFromFirebase(_user.getUsername());

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<Habit> options = new FirestoreRecyclerOptions.Builder<Habit>()
                .setQuery(query, Habit.class)
                .build();

        Task<QuerySnapshot> querySnapshotTask = _db.collection("Users")
                                                    .document(_user.getUsername())
                                                    .collection("Habits")
                                                    .get();

        /*
        populate HabitList with current Habits and habit IDs to initialize state to match
        database, fills when habitList is empty and snapshot is not, which is only
        when app is initially launched
        */
        if (_habitList.getHabitList().isEmpty()) {
            while (!querySnapshotTask.isComplete());
            List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();
            for (int i = 0; i < snapshotList.size(); i++) {
                Map<String, Object> extractMap = snapshotList.get(i).getData();
                String title = (String) extractMap.get("title");
                String reason = (String) extractMap.get("reason");
                Timestamp date = (Timestamp) extractMap.get("date");
                Long id = (Long) extractMap.get("habitId");
                Habit addHabit = new Habit(title, reason, date.toDate() ,id, new HabitEventList());
                _habitList.addHabitLocal(addHabit);
                HabitList.habitIdSet.add(id);
            }
        }
        //wraps the snapshots representing the HabitList of the user in the HabitList
        _habitList.setSnapshots(options.getSnapshots());
        _habitItemAdapter = new HabitItemAdapter(options, getActivity(), _habitList, _fragment, _user.getUsername());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);

        View view = inflater.inflate(R.layout.fragment_habit_list, container, false);

        FloatingActionButton addHabitFab = view.findViewById(R.id.add_habit_fab);
        /**
         * When fab is pressed, method call to open dialog fragment.
         */
        addHabitFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddHabitDialogBox();
            }
        });

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    /**
     * This method queries the database for all habits that correspond to the specified user
     * @param username the user to get the habits for
     * @return resulting firebase query
     */
    @NonNull
    private Query getListOfHabitsFromFirebase(String username) {
        return _db.collection("Users")
                  .document(username)
                  .collection("Habits");
    }

    @Override public void onStart() {
        super.onStart();
        _habitItemAdapter.startListening();
    }

    @Override public void onStop()
    {
        super.onStop();
        _habitItemAdapter.stopListening();
    }

    /**
     * This helper method is responsible for opening the add habit dialog box
     */
    private void openAddHabitDialogBox() {
        AddHabitDialog addHabitDialog = new AddHabitDialog();
        addHabitDialog.setCancelable(true);
        addHabitDialog.setTargetFragment(HabitListFragment.this, 1);
        addHabitDialog.show(getFragmentManager(), "AddHabitDialog");
    }

    /**
     * This helper method initializes the RecyclerView
     * @param layoutManager the associated LinearLayoutManager
     * @param view the associated View
     */
    private void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_habitItemAdapter);

        /* Implementation of swipe menu came from this source
        Name: Velmurugan
        Date: March 4, 2021
        URL: https://howtodoandroid.com/android-recyclerview-swipe-menu
         */
        RecyclerTouchListener touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        touchListener.setSwipeOptionViews(R.id.delete_button, R.id.edit_button)
                    .setSwipeable(R.id.habit_view, R.id.swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                        @Override
                        public void onSwipeOptionClicked(int viewID, int position) {
                            switch (viewID){
                                case R.id.edit_button:
                                    EditHabitFragment editHabitFragment = new EditHabitFragment(position,
                                            _habitItemAdapter._snapshots.get(position),
                                            _fragment);
                                    editHabitFragment.show(_fragment.getFragmentManager(), "Edit Habit");
                                    break;
                                case R.id.delete_button:
                                    Habit habitToDelete = _habitItemAdapter._snapshots.get(position);
                                    _habitList.deleteHabit(_fragment.getActivity(), _user.getUsername(), habitToDelete, position);
                                    break;
                            }

                        }
                    });
    }

    /**
     * This method is responsible for adding a new habit to the user's HabitList
     * @param title the habit title
     * @param reason the habit reason
     * @param date the habit date
     * */
    public void addHabitToDatabase(String title, String reason, Date date){
       _habitList.addHabitToDatabase(title, reason, date, _user.getUsername());
    }

    @Override
    public void onDestroyView() { super.onDestroyView();
    }

    /**
     * This method is responsible for editing a certain habit at position pos
     * in the user's habit list
     * @param newTitle habit's new title
     * @param newReason habit's new reason
     * @param newDate habit's new date
     * @param pos position of edited habit
     */
    public void updateAfterEdit(String newTitle, String newReason, Date newDate, int pos) {
        _habitList.editHabitInDatabase(newTitle, newReason, newDate, pos, _user.getUsername());
        //viewHolder.setButtonsInvisible();
        _habitItemAdapter.notifyItemChanged(pos);
    }
}