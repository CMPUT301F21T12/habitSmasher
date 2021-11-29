package com.example.habitsmasher.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
/**
 * This is the FailureListener class
 * It implements the OnFailureListener
 * Its purpose is to perform the onFailure listener actions
 *
 * @author Kaden Dreger
 */
public class FailureListener implements OnFailureListener {
    private String _toastText;
    private Context _context;
    private String _logTag;
    private String _logMsg;

    public FailureListener(String logTag, String logMsg) {
        _logTag = logTag;
        _logMsg = logMsg;
    }

    public FailureListener(Context context, String logTag, String logMsg, String toastText) {
        _logTag = logTag;
        _logMsg = logMsg;
        _context = context;
        _toastText = toastText;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(_logTag, _logMsg + e.toString());
        if(_toastText != null) {
            try {
                Toast.makeText(_context, _toastText, Toast.LENGTH_SHORT).show();
            } catch (NullPointerException error) {
                // Expected in unit test
                Log.d("FailureListener", "Failure Listener NPE." + error.toString());
            };
        }
    }
}
