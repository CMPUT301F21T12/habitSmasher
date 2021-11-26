package com.example.habitsmasher;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.example.habitsmasher.listeners.FailureListener;
import com.example.habitsmasher.listeners.SuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that acts as a container for habits, allowing for edit, delete and add operations
 */
public class HabitList extends ArrayList<Habit>{
    // arraylist of habits, auto-updates whenever Habit added or edited
    private ArrayList<Habit> _habits = new ArrayList<>();

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
     * @param userId id of the user of the habit list the habit is being added to
     */
    public void addHabitToDatabase(Habit newHabit, String userId) {
        // get collection of specified user (do we need this?)
        FirebaseFirestore _db = FirebaseFirestore.getInstance();
        final CollectionReference _collectionReference = _db.collection("Users")
                                                            .document(userId)
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
        habitData.put("public", newHabit.getPublic());

        // add habit to database, using it's habit ID as the document name
        setHabitDataInDatabase(userId, habitId, habitData);
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
        habit.setPublic(editedHabit.getPublic());
    }

    /**
     * Method that edits the habit at position pos in the database
     * @param editedHabit Habit containing the new fields of the editted habit
     * @param pos Position of habit in the HabitList
     * @param userId id of user whose habits we are editing
     */
    public void editHabitInDatabase(Habit editedHabit, int pos, String userId) {
        // this acquires the unique habit ID of the habit to be edited
        String habitId = editedHabit.getId();

        // stores the new fields of the Habit into a hashmap
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", editedHabit.getTitle());
        habitData.put("reason", editedHabit.getReason());
        habitData.put("date", editedHabit.getDate());
        habitData.put("id", habitId);
        habitData.put("days", editedHabit.getDays());
        habitData.put("public", editedHabit.getPublic());

        // replaces the old fields of the Habit with the new fields, using Habit ID to find document
        setHabitDataInDatabase(userId, habitId, habitData);
    }

    /**
     * Sets the fields of habit belonging the user username with the habit ID id
     * to the ones specified by data
     * @param userId id of user
     * @param id id of habit
     * @param data fields of habit
     */
    private void setHabitDataInDatabase(String userId, String id, HashMap<String, Object> data) {
        // get collection of Habits for a specified user
        FirebaseFirestore _db = FirebaseFirestore.getInstance();
        final CollectionReference _collectionReference = _db.collection("Users")
                .document(userId)
                .collection("Habits");

        // create the new document and add it
        _collectionReference.document(id)
                .set(data)
                .addOnSuccessListener(new SuccessListener(TAG, "Data successfully added."))
                .addOnFailureListener(new FailureListener(TAG, "Data failed to be added."));
    }

    /**
     * This method is responsible for deleting a habit from both locally and the db
     * @param context the current application context
     * @param userId the current user's username
     * @param habitToDelete the habit to delete
     * @param habitPosition the position of the habit to delete
     */
    public void deleteHabit(Context context,
                            String userId,
                            Habit habitToDelete,
                            int habitPosition) {
        // delete locally
        try {
            deleteHabitLocal(habitPosition);
        } catch (IndexOutOfBoundsException e) {
            Log.d("HabitList", "Delete failed, index out of bounds.", e);
        }


        // delete from firebase
        deleteHabitFromDatabase(context, userId, habitToDelete);
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
     * @param userId the current user's id
     * @param habitToDelete the habit to delete
     */
    private void deleteHabitFromDatabase(Context context, String userId, Habit habitToDelete) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // get the habit to delete, then delete it
        db.collection("Users")
          .document(userId)
          .collection("Habits")
          .document(String.valueOf(habitToDelete.getId()))
          .delete()
          .addOnSuccessListener(new SuccessListener(context,
                  "deleteHabit", "Data successfully deleted.",
                  "Habit deleted!"))
                .addOnFailureListener(new FailureListener(context,
                        "deleteHabit", "Data failed to be deleted.",
                                "Something went wrong!"));
    }
}