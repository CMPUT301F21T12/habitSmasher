package com.example.habitsmasher;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Abstract class that specifies the behaviour of any fragment
 * using a RecyclerView to display a list of objects of type T
 * stored in a Firebase database. This displayed list supports
 * adding, editing, deleting and viewing the details of items
 * from the list
 * @author Jason Kim
 */
public abstract class ListFragment<T> extends Fragment {


    /* once the ItemAdapter interface is complete, we can add this for
     * even greater abstraction
     */
    /*
    protected void ItemAdapter _itemAdapter;
    protected void setItemAdapter(ItemAdapter itemAdapter) {
        _itemAdapter = itemAdapter
    }

    @Override
    public void onStart() {
        super();
        _itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super();
        _itemAdapter.stopListening();
    }
     */
    protected FirebaseFirestore _db = FirebaseFirestore.getInstance();

    /**
     * Queries the Firebase database and returns a query that
     * corresponds to the list that is to be displayed
     * by this fragment
     * @return
     */
    protected abstract Query getListFromFirebase();

    /**
     * Populates the list being displayed so it
     * matches the list in the database
     * @param query
     */
    protected abstract void populateList(Query query);

    /**
     * Opens the dialog box to add an object to the list in question
     */
    protected abstract void openAddDialogBox();

    // add after swipe is fixed for habit events
    //public abstract void openEditDialogBox();

    // add after view is implemented for habit events
    //protected abstract void openViewWindowForItem();

    /**
     * Initializes the Recycler View the fragment is using to display
     * the list of objects
     * @param layoutManager
     * @param view
     */
    protected abstract void initializeRecyclerView(LinearLayoutManager layoutManager, View view);

    /**
     * Update list fragment after an object is added to the list
     */
    public abstract void updateListAfterAdd(T addedObject);

    /**
     * Update list fragment after an object is edited in the list
     */
    public abstract void updateListAfterEdit(T editedObject, int pos);

    // add after swipe is fixed in habit events
    //public abstract void updateListAfterDelete(int pos);
}
