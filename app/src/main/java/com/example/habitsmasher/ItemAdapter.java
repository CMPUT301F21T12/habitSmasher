package com.example.habitsmasher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

/**
 * Abstract class for adapters to use.
 * @author Jacob Nguyen, Jason Kim
 */
public abstract class ItemAdapter<A, B extends RecyclerView.ViewHolder> extends FirestoreRecyclerAdapter<A, B> {
    public ObservableSnapshotArray<A> _snapshots;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ItemAdapter(@NonNull FirestoreRecyclerOptions<A> options) {
        super(options);
        _snapshots = options.getSnapshots();
    }

    public int getItemCount() {
        return _snapshots.size();
    }

    /**
     * This updates the options for the item adapter, this also updates the snapshots
     * @param options the new options
     */
    @Override
    public void updateOptions(@NonNull FirestoreRecyclerOptions<A> options) {
        super.updateOptions(options);
        _snapshots = options.getSnapshots();
    }
}