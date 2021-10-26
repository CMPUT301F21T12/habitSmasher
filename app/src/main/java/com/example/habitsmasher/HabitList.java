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

    FirebaseFirestore _db = FirebaseFirestore.getInstance();

    /**
     * Getter method to access Habit at pos
     * @return
     */
    public Habit getHabit(int pos) {
        return _habits.get(pos);
    }

    /**
     * Returns the list of habits as an ArrayList
     * @return
     */
    public ArrayList<Habit> getHabitList() {
        return _habits;
    }

    /**
     * Wraps a snapshots array within the HabitList
     * @param snapshots
     */
    public void wrapSnapshots(ObservableSnapshotArray<Habit> snapshots) {
        _snapshots = snapshots;
    }

    /**
     * Method that adds a habit with specified fields
     * @param title
     * @param reason
     * @param date
     */
    public void addHabit(String title, String reason, Date date, String username) {

        // get collection of specified user
        final CollectionReference _collectionReference = _db.collection("users")
                                                            .document(username)
                                                            .collection("habits");
        // generate a random ID for HabitID
        UUID habitId = UUID.randomUUID();

        // initialize fields
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", title);
        habitData.put("reason", reason);
        habitData.put("date", date);
        habitData.put("habitId", habitId);

        // add habit to database, using it's habit ID as the document name
        _collectionReference
                .document(habitId.toString())
                .set(habitData)
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

    /**
     * Method that edits the habit at position pos
     * @param title New title of habit
     * @param reason New reason of habit
     * @param date New date of habit
     * @param pos Position of habit in the HabitList
     */
    public void editHabit(String title, String reason, Date date, int pos, String username) {

        // get collection of Habits for a specified user
        final CollectionReference _collectionReference = _db.collection("Users")
                                                            .document(username)
                                                            .collection("habits");

        // this acquires the unique habit ID of the habit to be edited
        UUID habitId = _habits.get(pos).getHabitId();

        // stores the new fields of the Habit into a hashmap
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", title);
        habitData.put("reason", reason);
        habitData.put("date", date);
        habitData.put("habitId", habitId);

        // replaces the old fields of the Habit with the new fields, using Habit ID to find document
        _collectionReference.document(habitId.toString())
                .set(habitData)
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