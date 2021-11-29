package com.example.habitsmasher.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
/**
 * This is the SuccessListener class
 * It implements the OnSuccessListener
 * Its purpose is to perform the onSuccess listener actions
 *
 * @author Kaden Dreger
 */
public class SuccessListener implements OnSuccessListener {
    private String _toastText;
    private Context _context;
    private String _logTag;
    private String _logMsg;

    public SuccessListener(String logTag, String logMsg) {
        _logTag = logTag;
        _logMsg = logMsg;
    }

    public SuccessListener(Context context, String logTag, String logMsg, String toastText) {
        _context = context;
        _logTag = logTag;
        _toastText = toastText;
        _logMsg = logMsg;
    }

    @Override
    public void onSuccess(Object o) {
        Log.d(_logTag, _logMsg);
        if(_toastText != null) {
            try {
                Toast.makeText(_context, _toastText, Toast.LENGTH_SHORT).show();
            } catch (NullPointerException error) {
                // Expected in unit test
                Log.d("SuccessListener", "Success Listener NPE." + error.toString());
            };
        }
    }
}
