package com.example.habitsmasher.listeners;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;

public class SuccessListener implements OnSuccessListener {
    private String _logTag;
    private String _logMsg;

    public SuccessListener(String logTag, String logMsg) {
        _logTag = logTag;
        _logMsg = logMsg;
    }

    @Override
    public void onSuccess(Object o) {
        Log.d(_logTag, _logMsg);
    }
}
