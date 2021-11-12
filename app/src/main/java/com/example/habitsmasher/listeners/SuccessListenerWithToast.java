package com.example.habitsmasher.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
/**
 * This is the SuccessListenerWithToast class
 * It implements the OnSuccessListener
 * Its purpose is to perform the onSuccess listener actions
 */
public class SuccessListenerWithToast implements OnSuccessListener {
    private Context _context;
    private String _logTag;
    private String _toastText;
    private String _logMsg;

    public SuccessListenerWithToast(Context context, String logTag, String logMsg, String toastText) {
        _context = context;
        _logTag = logTag;
        _toastText = toastText;
        _logMsg = logMsg;
    }

    @Override
    public void onSuccess(Object o) {
        Log.d(_logTag, _logMsg);
        Toast.makeText(_context, _toastText, Toast.LENGTH_SHORT).show();
    }
}
