package com.example.habitsmasher.listeners;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
/**
 * This is the ClickListenerForCancel class
 * It implements the View.OnClickListener
 * Its purpose is to perform the onClick listener actions
 *
 * @author Kaden Dreger
 */
public class ClickListenerForCancel implements View.OnClickListener{
    private Dialog _dialog;
    private String _tag;

    public ClickListenerForCancel(Dialog dialog, String tag) {
        _dialog = dialog;
        _tag = tag;
    }

    @Override
    public void onClick(View view) {
        Log.d(_tag, "Cancel");
        _dialog.dismiss();
    }
}
