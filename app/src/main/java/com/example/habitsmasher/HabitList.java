package com.example.habitsmasher;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
 * Class that acts as a container for habits, allowing for edit, delete and add operations
 */
public class HabitList extends ArrayList<Habit>{
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
     * Method that adds a Habit to the local habitList
     * @param habit Habit to be added
     */
    public void addHabitLocal(Habit habit) {
        _habits.add(habit);
    }

    /**
     * Method that adds a habit with specified fields to the habit list of a specified
     * user in the database
     * @param newHabit habit to be added to the database
     * @param username user of the habit list habit is being added to
     */
    public void addHabitToDatabase(Habit newHabit, String username) {

        // get collection of specified user (do we need this?)
        FirebaseFirestore _db = FirebaseFirestore.getInstance();
        final CollectionReference _collectionReference = _db.collection("Users")
                                                            .document(username)
                                                            .collection("Habits");
        // generate ID
        String habitId = newHabit.getId();

        // initialize fields
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", newHabit.getTitle());
        habitData.put("reason", newHabit.getReason());
        habitData.put("date", newHabit.getDate());
        habitData.put("id", habitId);
        habitData.put("days", newHabit.getDays());

        // add habit to database, using it's habit ID as the document name
        setHabitDataInDatabase(username, habitId, habitData);
        addHabitLocal(newHabit);
    }


    /**
     * Method that edits a Habit in the specified pos in the local HabitList
     * @param editedHabit Habit containing the new fields of the editted habit
     * @param pos position of habit
     */
    public void editHabitLocal(Habit editedHabit, int pos) {
        Habit habit = _habits.get(pos);
        habit.setTitle(editedHabit.getTitle());
        habit.setReason(editedHabit.getReason());
        habit.setDate(editedHabit.getDate());
        habit.setDays(editedHabit.getDays());
    }

    /**
     * Method that a edits a Habit in the specified pos in the local HabitList
     * @param newTitle
     * @param newReason
     * @param newDate
     * @param tracker
     * @param pos
     */
    public void editHabitLocal(String newTitle, String newReason, Date newDate ,
                               DaysTracker tracker, int pos) {
        Habit habit = _habits.get(pos);
        habit.setTitle(newTitle);
        habit.setReason(newReason);
        habit.setDate(newDate);
        habit.setDays(tracker.getDays());
    }

    /**
     * Method that edits the habit at position pos in the database
     * @param editedHabit Habit containing the new fields of the editted habit
     * @param pos Position of habit in the HabitList
     * @param username Username of user whose habits we are editing
     */
    public void editHabitInDatabase(Habit editedHabit, int pos, String username) {

        // this acquires the unique habit ID of the habit to be edited
        String habitId = editedHabit.getId();

        // stores the new fields of the Habit into a hashmap
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", editedHabit.getTitle());
        habitData.put("reason", editedHabit.getReason());
        habitData.put("date", editedHabit.getDate());
        habitData.put("id", habitId);
        habitData.put("days", editedHabit.getDays());

        // replaces the old fields of the Habit with the new fields, using Habit ID to find document
        setHabitDataInDatabase(username, habitId, habitData);
        editHabitLocal(editedHabit, pos);
    }

    /**
     * Sets the fields of habit belonging the user username with the habit ID id
     * to the ones specified by data
     * @param username name of user
     * @param id id of habit
     * @param data fields of habit
     */
    private void setHabitDataInDatabase(String username, String id, HashMap<String, Object> data) {
        // get collection of Habits for a specified user
        FirebaseFirestore _db = FirebaseFirestore.getInstance();
        final CollectionReference _collectionReference = _db.collection("Users")
                .document(username)
                .collection("Habits");

        // create the new document and add it
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

    /**
     * This method is responsible for deleting a habit from both locally and the db
     * @param context the current application context
     * @param username the current user's username
     * @param habitToDelete the habit to delete
     * @param habitPosition the position of the habit to delete
     */
    public void deleteHabit(Context context,
                            String username,
                            Habit habitToDelete,
                            int habitPosition) {
        // delete locally
        deleteHabitLocal(habitPosition);

        // delete from firebase
        deleteHabitFromDatabase(context, username, habitToDelete);
    }

    /**
     * This method deletes a habit from the local habit list
     * @param habitPosition the index of the habit to delete
     */
    public void deleteHabitLocal(int habitPosition) {
        _habits.remove(habitPosition);
    }

    /**
     * This method deletes a habit from the database
     * @param context the current application context
     * @param username the current user's username
     * @param habitToDelete the habit to delete
     */
    private void deleteHabitFromDatabase(Context context, String username, Habit habitToDelete) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // get the habit to delete, then delete it
        db.collection("Users")
          .document(username)
          .collection("Habits")
          .document(String.valueOf(habitToDelete.getId()))
          .delete()
          .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {
                  Log.d("deleteHabit", "Data successfully deleted.");
                  Toast.makeText(context, "Habit deleted!", Toast.LENGTH_SHORT).show();
              }
          }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("deleteHabit", "Data failed to be deleted.");
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}