package com.example.habitsmasher.listeners;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;

public class FailureListener implements OnFailureListener {
    private String _logTag;
    private String _logMsg;

    public FailureListener(String logTag, String logMsg) {
        _logTag = logTag;
        _logMsg = logMsg;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(_logTag, _logMsg + e.toString());
    }
}
