package com.example.habitsmasher;

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

/**
 * The habit list class is a container for the list of habits. This class is now a wrapper
 * class for the _snapshots array, which reflects the state of the database
 */
// since HabitList is now a wrapper of snapshots, no longer needs to extend ArrayList
public class HabitList {

    // array of snapshots which reflect state of database, wrapped within HabitList
    private ObservableSnapshotArray<Habit> _habits;

    FirebaseFirestore _db = FirebaseFirestore.getInstance();
    final CollectionReference _collectionReference = _db.collection("Habits");


    public HabitList(ObservableSnapshotArray snapshotArray) {
        _habits = snapshotArray;
    }

    /**
     * Getter method to access Habit at pos
     * @return
     */
    public Habit getHabit(int pos) {
        return _habits.get(pos);
    }

    /**
     * Method that adds a habit with specified fields to database
     * @param title
     * @param reason
     * @param date
     */
    public void addHabit(String title, String reason, Date date) {
        // Handling of adding a habit to firebase
        HashMap<String, Object> habitData = new HashMap<>();

        //generate the habit ID for the added Habit
        Long habitId = generateHabitId();

        // initialize fields
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
    public void editHabit(String title, String reason, Date date, int pos) {
        // this acquires the unique habit ID of the habit to be edited
        Long habitId = _habits.get(pos).getHabitId();

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