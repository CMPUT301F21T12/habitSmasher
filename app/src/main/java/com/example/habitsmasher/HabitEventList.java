package com.example.habitsmasher;

import android.content.Context;
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
 *
 * @author Julie Pilz, Jason Kim
 */
public class HabitEventList extends ArrayList{
    private ArrayList<HabitEvent> _habitEvents = new ArrayList<>();
    private static final String PATH_TO_DEFAULT_IMG = "android.resource://com.example.habitsmasher/drawable/habit_temp_img";
    private ImageDatabaseHelper _imageDatabaseHelper;

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
     * @param id (String): The id of the habit event to add
     * @param location (String): The location of the habit event
     */
    public void addHabitEventLocally(Date startDate, String comment, String id, String location) {
        // Create habit event and add it to the list
        HabitEvent eventToAdd = new HabitEvent(startDate, comment, id, location);
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
        eventData.put("location", addedHabitEvent.getLocation());
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
     * @param location (String) The location of the habit event
     */
    public void editHabitEventLocally(String newComment, Date newDate, int pos, String location) {
        HabitEvent toEdit = _habitEvents.get(pos);
        toEdit.setComment(newComment);
        toEdit.setDate(newDate);
        toEdit.setLocation(location);
    }

    /**
     * This method is responsible for editing a habit in the database
     * @param editedHabitEvent (HabitEvent) The edited event
     * @param userId (String) The username of the current user
     * @param userId (String) The id of the current user
     * @param parentHabit (Habit) The current habit
     * @param newImage (Uri) The new image
     */
    public void editHabitInDatabase(HabitEvent editedHabitEvent, String userId,
                                    Habit parentHabit, Uri newImage) {
        String toEditId = editedHabitEvent.getId();
        // Create hashmap to hold data
        HashMap<String, Object> habitEventData = new HashMap<>();
        habitEventData.put("comment", editedHabitEvent.getComment());
        habitEventData.put("date", editedHabitEvent.getDate());
        habitEventData.put("id", toEditId);
        habitEventData.put("location", editedHabitEvent.getLocation());
        // Set edited data in the database
        setHabitEventDataInDatabase(userId,parentHabit, toEditId, habitEventData);

        // Add new image
        if (newImage != null) {
            // Delete old image
            deleteHabitEventImageFromDb(userId, parentHabit, editedHabitEvent);

            // Add new image
            addImageToDatabase(userId, parentHabit, newImage, editedHabitEvent.getId());
        }
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
     * Adds an image to the database for a habit event
     * @param userId (String) The id of the current user
     * @param parentHabit (String) The parent habit
     * @param image (Uri) The image to add to the db
     * @param id (String) The id of the habit event
     */
    public void addImageToDatabase(String userId, Habit parentHabit, Uri image, String id) {
        Uri toAdd = image;

        // Get firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference ref = getHabitEventStorageReference(userId, parentHabit.getId(), id, storageReference);

        // If the user didn't select an image, choose the default one
        if (toAdd == null) {
            image = Uri.parse(PATH_TO_DEFAULT_IMG);
        }

        // Add image to database
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

    /**
     * Deletes an image from the db
     * @param userId (String) The id of the current user
     * @param parentHabit (Habit) The parent habit to the event
     * @param toDelete (HabitEvent) The habit event to delete a picture for
     */
    private void deleteHabitEventImageFromDb(String userId, Habit parentHabit, HabitEvent toDelete) {
        // Get firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = getHabitEventStorageReference(userId, parentHabit.getId(), toDelete.getId(), storageReference);
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

    /**
     * Gets the storage reference of a habit event image
     * @param userId (String): The Id of the current user
     * @param habitId (String): The Id of the parent habit event
     * @param eventId (String): The id of the specific habit event
     * @param ref (StorageReference): The parent storage reference
     * @return Reference to the new image location
     */
    private StorageReference getHabitEventStorageReference(String userId, String habitId, String eventId, StorageReference ref) {
        return ref.child("images/" + userId + "/" + habitId + "/" + eventId + "/eventImage");
    }
}
