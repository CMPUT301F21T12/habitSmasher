package com.example.habitsmasher.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.habitsmasher.ListFragment;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FollowListFragment extends ListFragment<User> {

    private String _followType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get context
        Context context = getContext();

        if(getArguments() != null){
            Bundle arguments = getArguments();
            _followType = arguments.getString("FollowType");
        }

        // Inflate habit event list view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        View view = inflater.inflate(R.layout.fragment_follow_list, container, false);

        // Set header title
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(_followType + " List");

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    @Override
    protected Query getListFromFirebase() {
        return null;
    }

    @Override
    protected void populateList(Query query) {

    }

    @Override
    protected void openAddDialogBox() {

    }

    @Override
    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {

    }

    @Override
    public void updateListAfterAdd(User addedObject) {

    }

    @Override
    public void updateListAfterEdit(User editedObject, int pos) {

    }
}
