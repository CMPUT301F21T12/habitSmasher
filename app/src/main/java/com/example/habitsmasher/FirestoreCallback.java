package com.example.habitsmasher;

/**
 * This interface implements a custom version of a Firestore Callback. Since Firestore operations
 * are asynchronous, we can use this interface and override the onCallback method to ensure the
 * operations are complete before moving forward.
 *
 * @author Rudy Patel
 */
public interface FirestoreCallback {
    void onCallback(Boolean flag);
}
