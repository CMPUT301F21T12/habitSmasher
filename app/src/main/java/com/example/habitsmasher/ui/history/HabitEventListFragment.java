package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.habitsmasher.HabitEventList;
import com.google.firebase.firestore.FirebaseFirestore;

public class HabitEventListFragment extends Fragment {
    private HabitEventList _habitEventList = new HabitEventList();
    FirebaseFirestore _db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }
}
