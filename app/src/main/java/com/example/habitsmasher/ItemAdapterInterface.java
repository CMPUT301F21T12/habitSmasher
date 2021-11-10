package com.example.habitsmasher;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface ItemAdapterInterface <A, B> {
    A onCreateViewHolder(@NonNull ViewGroup parent, int viewType);
    void onBindViewHolder(@NonNull A holder, int position, @NonNull B habit);
    int getItemCount();

}