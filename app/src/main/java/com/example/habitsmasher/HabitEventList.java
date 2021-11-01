package com.example.habitsmasher;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * This is the Habit Event List Class.
 * It's purpose is to store habit events
 */
public class HabitEventList extends ArrayList{
    private ArrayList<HabitEvent> _habitEvents = new ArrayList<>();

    /**
     * Gets the list of habit events
     * @return habitEvents: The list of habit events
     */
    public ArrayList<HabitEvent> getHabitEvents() {
        return _habitEvents;
    }

    /**
     * Creates a new habit event and add its to the habit event list
     * @param startDate (Date): The start date of the habit event to add
     * @param comment (String): The comment of the habit event to add
     * @param pictureUri (String): The URL of the picture of the habit event to add
     */
    public void addHabitEventLocally(Date startDate, String comment, Uri pictureUri, UUID id) {
        // Create habit event and add it to the list
        HabitEvent eventToAdd = new HabitEvent(startDate, comment, pictureUri, id);
        _habitEvents.add(eventToAdd);
    }

    /**
     * Adds a habit event to the habit event list
     * @param eventToAdd (HabitEvent): The event to add to the habit event list
     */
    public void addHabitEventLocally(HabitEvent eventToAdd) {
        // Add event to list
        _habitEvents.add(eventToAdd);
    }

    /**
     * Add a new habit event to the database
     * @param date Date of the habit event
     * @param comment Comment of the new habit event
     * @param id ID of the new habit event
     * @param pictureUri Picture of the new habit event
     * @param username Username of the user adding the habit event
     */
    public void addHabitEventToDatabase(Date date, String comment, UUID id, Uri pictureUri, String username, Habit parentHabbit) {
        // get collection of specified user
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get reference to habit events collection
        final CollectionReference collectionReference = db.collection("Users")
                .document(username)
                .collection("Habits")
                .document(Long.toString(parentHabbit.getHabitId()))
                .collection("Events");

        // Store data in a hash map
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("date", date);
        eventData.put("comment", comment);
        eventData.put("id", id.toString());

        // Set data in database
        setHabitEventDataInDatabase(db, username, parentHabbit, id.toString(), eventData);
    }

    /**
     * This method deletes a habit event locally and from the database
     * @param context (Context) The current application context
     * @param username (String) The current user's username
     * @param parentHabit (Habit) The current habit
     * @param toDelete (HabitEvent) The habit event to delete
     */
    public void deleteHabitEvent(Context context, String username, Habit parentHabit, HabitEvent toDelete) {
        // Delete locally
        deleteHabitEventLocally(toDelete);

        // Delete from database
        deleteHabitEventFromDatabase(context, username, parentHabit, toDelete);
    }

    /**
     * This method is responsible for deleting a habit event locally
     * @param toDelete (HabitEvent) the habit event to delete
     */
    private void deleteHabitEventLocally(HabitEvent toDelete) {
        _habitEvents.remove(toDelete);
    }

    /**
     * This method deleted a habit event from the database
     * @param context (Context) The current application context
     * @param username (String) The current user's username
     * @param parentHabit (Habit) The current habit
     * @param toDelete (HabitEvent) The habit event to delete
     */
    private void deleteHabitEventFromDatabase(Context context, String username, Habit parentHabit, HabitEvent toDelete) {
        FirebaseFirestore db =  FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(username)
                .collection("Habits")
                .document(Long.toString(parentHabit.getHabitId()))
                .collection("Events")
                .document(toDelete.getId().toString())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("deleteHabitEvent", "Data successfully deleted.");
                        Toast.makeText(context, "Habit Event deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("deleteHabitEvent", "Data failed to be deleted.");
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setHabitEventDataInDatabase(FirebaseFirestore db, String username, Habit parentHabit, String id, HashMap<String, Object> data) {
        final CollectionReference collectionReference = db.collection("Users")
                .document(username)
                .collection("Habits")
                .document(Long.toString(parentHabit.getHabitId()))
                .collection("Events");

        // Set data in database
        collectionReference
                .document(id.toString())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    // Handle success
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data successfully added.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // Handle failure
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added." + e.toString());
                    }
                });
    }
}
