package com.example.habitsmasher;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;

import com.example.habitsmasher.listeners.FailureListener;
import com.example.habitsmasher.listeners.SuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Class that acts as a container for habit events, allowing for add, edit and delete
 * operations
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
    public void addHabitEventLocally(Date startDate, String comment, Uri pictureUri, String id) {
        // Create habit event and add it to the list
        HabitEvent eventToAdd = new HabitEvent(startDate, comment, id);
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
     * @param addedHabitEvent
     * @param userId Id of the user adding the habit event
     */
    public void addHabitEventToDatabase(HabitEvent addedHabitEvent, String userId, Habit parentHabit) {
        // get collection of specified user
        String eventId = addedHabitEvent.getId();

        // Store data in a hash map
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("date", addedHabitEvent.getDate());
        eventData.put("comment", addedHabitEvent.getComment());
        eventData.put("id", eventId);

        // Set data in database
        setHabitEventDataInDatabase(userId, parentHabit, eventId, eventData);
        addHabitEventLocally(addedHabitEvent);
    }

    /**
     * This method deletes a habit event locally and from the database
     * @param context (Context) The current application context
     * @param userId (String) The current user's username
     * @param parentHabit (Habit) The current habit
     * @param toDelete (HabitEvent) The habit event to delete
     */
    public void deleteHabitEvent(Context context, String userId, Habit parentHabit, HabitEvent toDelete) {
        // Delete locally
        deleteHabitEventLocally(toDelete);

        // Delete from database
        deleteHabitEventFromDatabase(context, userId, parentHabit, toDelete);
    }

    /**
     * This method is responsible for deleting a habit event locally
     * @param toDelete (HabitEvent) the habit event to delete
     */
    public void deleteHabitEventLocally(HabitEvent toDelete) {
        _habitEvents.remove(toDelete);
    }

    /**
     * This method deleted a habit event from the database
     * @param context (Context) The current application context
     * @param userId (String) The current user's username
     * @param parentHabit (Habit) The current habit
     * @param toDelete (HabitEvent) The habit event to delete
     */
    private void deleteHabitEventFromDatabase(Context context, String userId, Habit parentHabit, HabitEvent toDelete) {
        FirebaseFirestore db =  FirebaseFirestore.getInstance();

        // get the document to delete, then delete it
        db.collection("Users")
                .document(userId)
                .collection("Habits")
                .document((parentHabit.getId()))
                .collection("Events")
                .document(toDelete.getId())
                .delete()
                .addOnSuccessListener(new SuccessListener(context,
                        "deleteHabitEvent", "Data successfully deleted.",
                        "Habit Event deleted!"))
                .addOnFailureListener(new FailureListener(context,
                        "deleteHabitEvent", "Data failed to be deleted.",
                        "Something went wrong!"));
        deleteHabitEventImageFromDb(userId, parentHabit, toDelete);
    }

    /**
     * This method is responsible for editing a habit locally
     * @param newComment (String) The edited comment
     * @param newDate (Date) The edited date
     * @param pos (int) The position of the habit in the list
     */
    public void editHabitEventLocally(String newComment, Date newDate, int pos) {
        HabitEvent toEdit = _habitEvents.get(pos);
        toEdit.setComment(newComment);
        toEdit.setDate(newDate);
    }

    /**
     * This method is responsible for editing a habit in the database
     * @param editedHabitEvent (HabitEvent) The edited event
     * @param userId (String) The username of the current user
     * @param userId (String) The id of the current user
     * @param parentHabit (Habit) The current habit
     */
    public void editHabitInDatabase(HabitEvent editedHabitEvent, String userId,
                                    Habit parentHabit, Uri newImage) {
        String toEditId = editedHabitEvent.getId();
        // Create hashmap to hold data
        HashMap<String, Object> habitEventData = new HashMap<>();
        habitEventData.put("comment", editedHabitEvent.getComment());
        habitEventData.put("date", editedHabitEvent.getDate());
        habitEventData.put("id", toEditId);

        // Set edited data in the database
        setHabitEventDataInDatabase(userId,parentHabit, toEditId, habitEventData);

        // Delete old image
        deleteHabitEventImageFromDb(userId, parentHabit, editedHabitEvent);

        // Add new image
        addImageToDatabase(userId, parentHabit, newImage, editedHabitEvent.getId());
    }

    /**
     * This method is responsible for setting habit event data in the database
     * @param userId (String) The current user's id
     * @param parentHabit (Habit) The current habit
     * @param id (String) The id of the habit event for which data is being set
     * @param data The data to set
     */
    private void setHabitEventDataInDatabase(String userId, Habit parentHabit, String id, HashMap<String, Object> data) {
        // Get firestore database reference
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get path to events collection
        final CollectionReference collectionReference = db.collection("Users")
                .document(userId)
                .collection("Habits")
                .document(parentHabit.getId())
                .collection("Events");

        // Set data in database
        collectionReference
                .document(id)
                .set(data)
                .addOnSuccessListener(new SuccessListener(TAG, "Data successfully added."))
                .addOnFailureListener(new FailureListener(TAG, "Data failed to be added."));
    }

    /**
     * @param image The image to add to the database
     * @param id The ID of the habit event corresponding to the image
     */
    public void addImageToDatabase(String userId, Habit parentHabit, Uri image, String id) {
        Uri toAdd = image;

        if (toAdd == null) {
            image = Uri.parse("android.resource://com.example.habitsmasher/drawable/habit_temp_img");

        }
        // Get firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/" + userId + "/" + parentHabit.getId() + "/" + id + "/" + "eventImage");

        ref.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Image uploaded.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Image failed to upload");
                    }
                });
    }

    private void deleteHabitEventImageFromDb(String userId, Habit parentHabit, HabitEvent toDelete) {
        // Get firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/" + userId + "/" + parentHabit.getId() + "/" + toDelete.getId() + "/" + "eventImage");
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Image deleted successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to delete image");
            }
        });
    }
}
