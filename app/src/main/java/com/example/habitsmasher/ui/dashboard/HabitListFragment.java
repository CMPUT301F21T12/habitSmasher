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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
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
    private HabitListFragment _fragment = this;
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
        while (!querySnapshotTask.isComplete());
        List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();
        for (int i = 0; i < snapshotList.size(); i++) {
            Map<String, Object> extractMap = snapshotList.get(i).getData();
            Long id = (Long) extractMap.get("habitId");
            HabitList.habitIdSet.add(id);
        }
        //wraps the snapshots representing the HabitList of the user in the HabitList
        _habitList.wrapSnapshots(options.getSnapshots());
        _habitItemAdapter = new HabitItemAdapter(options, getActivity(), _habitList, _fragment);
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
        Query query = _db.collection("Users")
                         .document(username)
                         .collection("Habits");
        return query;
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
        new ItemTouchHelper(_itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    /**
     * The implementation of the swipe to delete functionality below came from the following URL:
     * https://stackoverflow.com/questions/33985719/android-swipe-to-delete-recyclerview
     *
     *
     * Name: Rahul Raina
     * Date: November 2, 2016
     */
    //this probably won't be the way to do it, looks ugly as hell
    ItemTouchHelper.SimpleCallback _itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // if the habit row is swiped to the left, spawn edit and delete button
            // if swiped to the right, despawn them
            View habitView = viewHolder.itemView;

            /*
            Cast into HabitViewHolder so we can use setButtonVisible and
            setButtonsInvisible methods
             */
            HabitItemAdapter.HabitViewHolder habitViewHolder = (HabitItemAdapter.HabitViewHolder)
                    viewHolder;

            if (direction == ItemTouchHelper.LEFT) {
                habitViewHolder.setButtonsVisible();
            } else if (direction == ItemTouchHelper.RIGHT) {
                habitViewHolder.setButtonsInvisible();
            }
            _habitItemAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    };

    /**
     * This method is responsible for adding a new habit to the user's HabitList
     * @param title the habit title
     * @param reason the habit reason
     * @param date the habit date
     * */
    public void addHabitToDatabase(String title, String reason, Date date){
       _habitList.addHabit(title, reason, date, _user.getUsername());
    }

    @Override
    public void onDestroyView() { super.onDestroyView();
    }

    /**
     * This method is responsible for editing a certain habit at position pos
     * in the user's habit list
     * @param title habit's new title
     * @param reason habit's new reason
     * @param date habit's new date
     * @param pos position of edited habit
     * @param viewHolder viewholder of associated habit in the RecyclerView
     */
    public void updateAfterEdit(String title, String reason, Date date, int pos,
                                HabitItemAdapter.HabitViewHolder viewHolder) {
        _habitList.editHabit(title, reason, date, pos, _user.getUsername());
        viewHolder.setButtonsInvisible();
        _habitItemAdapter.notifyItemChanged(pos);
    }
}