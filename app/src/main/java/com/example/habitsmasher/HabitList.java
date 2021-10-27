package com.example.habitsmasher;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.habitsmasher.ui.dashboard.HabitItemAdapter;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * The habit list class is a container for the list of habits.
 */
public class HabitList extends ArrayList<Habit>{

    // array of Habits which reflect the database wrapped within HabitList
    private ObservableSnapshotArray<Habit> _snapshots;

    // arraylist of habits, auto-updates whenever Habit added or edited
    private ArrayList<Habit> _habits = new ArrayList<>();

    // set of IDs of existing Habits
    public static HashSet<Long> habitIdSet = new HashSet<>();

    /**
     * Getter method to access Habit at pos
     * @return habit
     */
    public Habit getHabit(int pos) {
        return _habits.get(pos);
    }

    /**
     * Returns the list of habits as an ArrayList
     * @return _habits
     */
    public ArrayList<Habit> getHabitList() {
        return _habits;
    }

    /**
     * Wraps a snapshots array within the HabitList
     * @param snapshots
     */
    public void setSnapshots(ObservableSnapshotArray<Habit> snapshots) {
        _snapshots = snapshots;
    }

    /**
     * Method that adds a habit with specified fields to the database
     * @param title
     * @param reason
     * @param date
     * @param username
     */
    public void addHabitToDatabase(String title, String reason, Date date, String username) {

        // get collection of specified user
        FirebaseFirestore _db = FirebaseFirestore.getInstance();
        final CollectionReference _collectionReference = _db.collection("Users")
                                                            .document(username)
                                                            .collection("Habits");
        // generate a random ID for HabitID
        Long habitId = generateHabitId();

        // initialize fields
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", title);
        habitData.put("reason", reason);
        habitData.put("date", date);
        habitData.put("habitId", habitId);

        // add habit to database, using it's habit ID as the document name
        setHabitDataInDatabase(username, habitId.toString(), habitData);
        addHabitLocal(new Habit(title, reason, date, habitId));
    }

    /**
     * Method that adds a Habit to the local habitList
     * @param habit Habit to be added
     */
    public void addHabitLocal(Habit habit) {
        _habits.add(habit);
    }

    /**
     * Method that edits a Habit in the specified pos in the local HabitList
     * @param title new title of habit
     * @param reason new reason of habit
     * @param date new date of habit
     * @param pos position of habit
     */
    public void editHabitLocal(String title, String reason, Date date, int pos) {
        Habit habit = _habits.get(pos);
        habit.setTitle(title);
        habit.setReason(reason);
        habit.setDate(date);
    }

    /**
     * Method that edits the habit at position pos in the database
     * @param title New title of habit
     * @param reason New reason of habit
     * @param date New date of habit
     * @param pos Position of habit in the HabitList
     * @param username Username of user whose habits we are editing
     */
    public void editHabitInDatabase(String title, String reason, Date date, int pos, String username) {

        // this acquires the unique habit ID of the habit to be edited
        Long habitId = _habits.get(pos).getHabitId();

        // stores the new fields of the Habit into a hashmap
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", title);
        habitData.put("reason", reason);
        habitData.put("date", date);
        habitData.put("habitId", habitId);

        // replaces the old fields of the Habit with the new fields, using Habit ID to find document
        setHabitDataInDatabase(username, habitId.toString(), habitData);
    }

    // this is a temporary implementation of generating unique habitIDs
    private long generateHabitId() {
        long habitIdCounter = 1;
        while(habitIdSet.contains(habitIdCounter)) {
            habitIdCounter++;
        }
        habitIdSet.add(habitIdCounter);
        return habitIdCounter;
    }

    private void setHabitDataInDatabase(String username, String id, HashMap<String, Object> data) {
        // get collection of Habits for a specified user
        FirebaseFirestore _db = FirebaseFirestore.getInstance();
        final CollectionReference _collectionReference = _db.collection("Users")
                .document(username)
                .collection("Habits");

        _collectionReference.document(id)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data successfully added.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added." + e.toString());
                    }
                });
    }
}