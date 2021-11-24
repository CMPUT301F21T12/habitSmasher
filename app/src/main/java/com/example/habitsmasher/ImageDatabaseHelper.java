package com.example.habitsmasher;
import static android.content.ContentValues.TAG;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * This class handles fetching images from the database
 */
public class ImageDatabaseHelper {
    // Firebase variables
    private FirebaseStorage _storage = FirebaseStorage.getInstance();
    private StorageReference _storageReference = _storage.getReference();

    // Max download megabyte size
    private final long ONE_MEGABYTE = 1024 * 1024;

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
        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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
