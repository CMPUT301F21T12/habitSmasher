package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.DaysTracker;
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

/**
 * UI class that represents and specifies the behaviour of the user interface
 * displayed when a user is accessing their own habit list
 */
public class HabitListFragment extends Fragment {
    private static final String TAG = "HabitListFragment";
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";

    // user who owns this list of habits displayed
    private User _user;

    // list of habits being displayed
    private HabitList _habitList;

    // adapter that connects the RecyclerView to the database
    private HabitItemAdapter _habitItemAdapter;

    // needed for dialogs spawned from this fragment
    private final HabitListFragment _fragment = this;

    FirebaseFirestore _db = FirebaseFirestore.getInstance();

    private Context _context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _context = getContext();

        _user = getCurrentUser();
        _habitList = _user.getHabits();

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Habit List");

        // query firebase for all habits that correspond to the current user
        Query query = getListOfHabitsFromFirebase(_user.getId());

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<Habit> options = new FirestoreRecyclerOptions.Builder<Habit>()
                .setQuery(query, Habit.class)
                .build();

        //get all of the habits
        Task<QuerySnapshot> querySnapshotTask = _db.collection("Users")
                                                    .document(_user.getId())
                                                    .collection("Habits")
                                                    .get();

        /*
        populate HabitList with current Habits and habit IDs to initialize state to match
        database, fills when habitList is empty and snapshot is not, which is only
        when app is initially launched
        */
        if (_habitList.getHabitList().isEmpty()) {
            // wait for all the snapshots to come in
            while (!querySnapshotTask.isComplete());

            // make a list of all of the habit snapshots
            List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();

            // convert all of the snapshots into proper habits
            for (int i = 0; i < snapshotList.size(); i++) {
                // get the data and convert to hashmap, also print to log
                Map<String, Object> extractMap = snapshotList.get(i).getData();
                Log.d(TAG, extractMap.toString());

                // get all of the data from the snapshot
                String title = (String) extractMap.get("title");
                String reason = (String) extractMap.get("reason");
                Timestamp date = (Timestamp) extractMap.get("date");
                Long id = (Long) extractMap.get("id");
                String days = (String) extractMap.get("days");
                boolean isPublic = (boolean) extractMap.get("public");

                // create a new habit with the snapshot data
                Habit addHabit = new Habit(title, reason, date.toDate(), days, isPublic, id, new HabitEventList());

                Log.d(TAG, String.valueOf(addHabit.getPublic()));

                // add the habit to the local list
                _habitList.addHabitLocal(addHabit);
                HabitList.habitIdSet.add(id);
            }

        }
        //wraps the snapshots representing the HabitList of the user in the HabitList
        _habitList.setSnapshots(options.getSnapshots());
        _habitItemAdapter = new HabitItemAdapter(options, getActivity(), _habitList, _fragment, _user.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);

        View view = inflater.inflate(R.layout.fragment_habit_list, container, false);
        FloatingActionButton addHabitFab = view.findViewById(R.id.add_habit_fab);

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
     * @param userId the user to get the habits for
     * @return resulting firebase query
     */
    @NonNull
    private Query getListOfHabitsFromFirebase(String userId) {
        return _db.collection("Users")
                  .document(userId)
                  .collection("Habits");
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
                // Get the selected habit
                Habit currentHabit = _habitItemAdapter._snapshots.get(position);
                Log.d(TAG, "onRowClicked: current habit:" + currentHabit.getPublic());

                // Create a bundle to be passed into the habitViewFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", currentHabit);
                bundle.putSerializable("userId", _user.getId());
                NavController controller = NavHostFragment.findNavController(_fragment);

                // Navigate to the habitViewFragment
                controller.navigate(R.id.action_navigation_dashboard_to_habitViewFragment, bundle);
            }
        })
                    .setSwipeOptionViews(R.id.edit_button, R.id.delete_button)
                    .setSwipeable(R.id.habit_view, R.id.swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                        @Override
                        public void onSwipeOptionClicked(int viewID, int position) {
                            // edit and delete functionality below
                            switch (viewID){
                                // if edit button clicked
                                case R.id.edit_button:
                                    EditHabitFragment editHabitFragment = new EditHabitFragment(position,
                                            _habitItemAdapter._snapshots.get(position),
                                            _fragment);
                                    editHabitFragment.show(_fragment.getFragmentManager(), "Edit Habit");
                                    break;
                                // if delete button clicked
                                case R.id.delete_button:
                                    Habit habitToDelete = _habitItemAdapter._snapshots.get(position);
                                    _habitList.deleteHabit(_fragment.getActivity(), _user.getId(), habitToDelete, position);
                                    break;
                            }

                        }
                    });

        // connect listener to recycler view
        recyclerView.addOnItemTouchListener(touchListener);
    }

    /**
     * This method is responsible for adding a new habit to the user's HabitList
     * @param title the habit title
     * @param reason the habit reason
     * @param date the habit date
     * */
    public void addHabitToDatabase(String title, String reason, Date date, DaysTracker tracker, boolean isPublic){
       _habitList.addHabitToDatabase(title, reason, date, tracker, isPublic, _user.getId());
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
    public void updateAfterEdit(String newTitle, String newReason, Date newDate, int pos,
                                DaysTracker tracker, boolean isPublic) {
        _habitList.editHabitInDatabase(newTitle, newReason, newDate, tracker, isPublic, pos, _user.getId());
        _habitList.editHabitLocal(newTitle, newReason, newDate, tracker, isPublic, pos);
        _habitItemAdapter.notifyItemChanged(pos);
    }

    @NonNull
    private User getCurrentUser() {
        SharedPreferences sharedPref = _context.getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);

        String username = sharedPref.getString("username", "user");
        String userId = sharedPref.getString("userId", "id");
        String email = sharedPref.getString("email", "email");
        String password = sharedPref.getString("password", "password");

        return new User(userId, username, email, password);
    }
}