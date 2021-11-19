package com.example.habitsmasher.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.ListFragment;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.example.habitsmasher.ui.dashboard.HomeHabitItemAdapter;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/**
 * This fragment produces the home screen, which displays all of the habits that take place on
 * the current day. This class also allows upfront habit event addition for an easy way to
 * add an event.
 *
 * @author Cameron Matthew
 */
public class HomeFragment extends ListFragment {

    // user who owns this list of habits displayed
    private User _user;

    private Context _context;

    // list of habits being displayed
    private HabitList _habitList;

    private HomeHabitItemAdapter _habitItemAdapter;

    /**
     * This is the main function of the class, which creates and inflates the fragment layout, and
     * sets all of the elements
     * @param inflater The inflater used to inflate the layout
     * @param container The viewgroup used to inflate the layout
     * @param savedInstanceState The bundle passed on the call to create the view
     * @return The HomeFragment view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        _context = getContext();

        _user = UserDatabaseHelper.getCurrentUser(_context);
        _habitList = _user.getHabits();

        // getting the local date and updating the date string in the layout

        TextView date = view.findViewById(R.id.home_date);
        date.setText(getDateString());

        // query firebase for all habits that correspond to the current user
        Query query = getListFromFirebase();

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<Habit> options = new FirestoreRecyclerOptions.Builder<Habit>()
                .setQuery(query, Habit.class)
                .build();

        populateList(query);
        _habitItemAdapter = new HomeHabitItemAdapter(options, _habitList, _user.getUsername());
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context,
                LinearLayoutManager.VERTICAL,
                false);

        //View view = inflater.inflate(R.layout.fragment_habit_list, container, false);
        initializeRecyclerView(layoutManager, view);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        _habitItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        _habitItemAdapter.stopListening();
    }

    /**
     * This function queries the DB for the habits with the same day as the current day.
     * @return all of said habits
     */
    @Override
    protected Query getListFromFirebase() {

        // returns all of the habits that take place on the same day (Today)
        return _db.collection("Users")
                .document(_user.getId())
                .collection("Habits")
                .whereArrayContains("days", getCurrentDay());
    }

    /**
     * This function populates the list with all of the habits that have the same day as the
     * current day.
     * @param query The query that contains all of the habits with the same day as the current day
     */
    @Override
    protected void populateList(Query query) {
        //get all of the habits
        Task<QuerySnapshot> querySnapshotTask = query.get();

        /*
        populate HabitList with current Habits and habit IDs to initialize state to match
        database, fills when habitList is empty and snapshot is not, which is only
        when app is initially launched
        */
        if (_habitList.getHabitList().isEmpty()) {
            // wait for all the snapshots to come in
            while (!querySnapshotTask.isComplete()) ;

            // make a list of all of the habit snapshots
            List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();

            // convert all of the snapshots into proper habits
            for (int i = 0; i < snapshotList.size(); i++) {
                // get the data and convert to hashmap
                Map<String, Object> extractMap = snapshotList.get(i).getData();

                // create a new habit with the snapshot data
                Habit addHabit = makeHabit(extractMap);

                // add the habit to the local list
                _habitList.addHabitLocal(addHabit);

                Log.d("Added Habit", _habitList.getHabit(i).getTitle());


            }
        }
    }

    /**
     * This helper method initializes the RecyclerView
     * @param layoutManager the associated LinearLayoutManager
     * @param view the associated View
     */
    @Override
    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
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
                openViewWindowForItem(position);
            }
        })
                .setSwipeOptionViews(R.id.home_add_event_button)
                .setSwipeable(R.id.habit_view, R.id.swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        // open the habit event dialog
                        openHabitEventsView(position);
                    }
                });

        recyclerView.addOnItemTouchListener(touchListener);
    }

    @Override
    protected void openAddDialogBox() {
        // not needed

    }

    @Override
    public void updateListAfterAdd(Object addedObject) {
        // not needed
    }

    @Override
    public void updateListAfterEdit(Object editedObject, int pos) {
        //not needed
    }

    // helper method to make a habit
    private Habit makeHabit(Map<String, Object> extractMap) {
        // get all of the data from the snapshot
        String title = (String) extractMap.get("title");
        String reason = (String) extractMap.get("reason");
        Timestamp date = (Timestamp) extractMap.get("date");
        String id = (String) extractMap.get("id");
        String days = extractMap.get("days").toString();
        boolean isPublic = (boolean) extractMap.get("public");

        return new Habit(title, reason, date.toDate(), days, isPublic, id, new HabitEventList());
    }

    // get the current day of the week formatted as the first two letters of the day capitalized
    // ex) "MO" or "TU"
    private String getCurrentDay() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getDayOfWeek().toString().toUpperCase().substring(0, 2);
    }

    // get the current day, in the format of "Month day".
    // ex) "Nov 16"
    private String getDateString() {
        LocalDate currentDate = LocalDate.now();
        String abbreviatedMonth = currentDate.getMonth().toString().substring(0, 3).toLowerCase();
        abbreviatedMonth = abbreviatedMonth.substring(0, 1).toUpperCase() + abbreviatedMonth.substring(1);
        return abbreviatedMonth + " " + currentDate.getDayOfMonth();
    }

    // Open the habit view when the habit is clicked on in the list
    private void openViewWindowForItem(int position) {
        // Get the selected habit
        Habit currentHabit = _habitItemAdapter._snapshots.get(position);

        // Create a bundle to be passed into the habitViewFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("habit", currentHabit);
        bundle.putSerializable("userId", _user.getId());
        NavController controller = NavHostFragment.findNavController(this);

        // Navigate to the habitViewFragment
        controller.navigate(R.id.action_navigation_home_to_habitViewFragment, bundle);
    }

    // Open the habit event list view when the add event button is pressed.
    private void openHabitEventsView(int position) {
        // Create a bundle to be passed into the HabitEventListFragment
        Habit currentHabit = _habitItemAdapter._snapshots.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("parentHabit", currentHabit);
        bundle.putSerializable("parentUser", _user.getId());

        NavController controller = NavHostFragment.findNavController(this);

        // Navigate to the habitEventListFragment
        controller.navigate(R.id.action_navigation_home_to_habitEventListFragment, bundle);
    }
}