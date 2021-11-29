package com.example.habitsmasher;
import static android.content.ContentValues.TAG;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * This class handles fetching images from the database
 */
public class ImageDatabaseHelper {
    // Firebase variables
    private FirebaseStorage _storage = FirebaseStorage.getInstance();
    private StorageReference _storageReference = _storage.getReference();

    // Max download megabyte size
    private final long FIVE_MEGABYTES = 1024 * 1024 * 5;

    // Has to be global in order to be accessed in onSuccess method
    private Bitmap _returnImage;

    /**
     * Default constructor
     */
    public ImageDatabaseHelper () {
    }

    /**
     * Fetches images from the database. Implemented recursively to account for possibility that the related entity
     * such as user or habit event hasn't finished adding to the database yet
     * @param viewHolder (ImageView): The image view for which to set the image
     * @param reference (StorageReference): The reference to upload the image to
     * @return The fetched image
     */
    public Bitmap fetchImagesFromDB(@NonNull ImageView viewHolder, StorageReference reference) {
        // Get Image from database
        reference.getBytes(FIVE_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // If successful, store bitmap of new image and set in view holder
                _returnImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewHolder.setImageBitmap(_returnImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get image");

                // If the image hasn't finished uploading to the db yet, try again
                fetchImagesFromDB(viewHolder, reference);

            }
        });

        return _returnImage;
    }

    /**
     * Deletes an image from the database
     * @param reference (StorageReference) The location of the image to delete in the database
     */
    public void deleteImageFromDatabase(StorageReference reference) {
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Image deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to delete image.");
            }});
    }

    /**
     * Adds an image to the database
     * @param reference (StorageReference) The location of the new image in the database
     * @param toUpload (Uri) The image to add
     */
    public void addImageToDatabase(StorageReference reference, Uri toUpload) {
        reference.putFile(toUpload).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Image uploaded.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Image failed to upload");
            }
        });
    }


    /**
     * Gets the storage reference of a habit event image
     * @param userId (String): The Id of the current user
     * @param habitId (String): The Id of the parent habit event
     * @param eventId (String): The id of the specific habit event
     * @return Reference to the new image location
     */
    public StorageReference getHabitEventStorageReference(String userId, String habitId, String eventId) {
        return _storageReference.child("images/" + userId + "/" + habitId + "/" + eventId + "/eventImage");
    }

    /**
     * Gets the storage reference of a user image
     * @param userId (String): The Id of the current user
     * @return Reference to the new image location
     */
    public StorageReference getUserStorageReference(String userId) {
        return _storageReference.child("images/" + userId + "/" + "userImage");
    }
}
