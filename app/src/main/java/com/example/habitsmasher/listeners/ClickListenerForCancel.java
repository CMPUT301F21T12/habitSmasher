package com.example.habitsmasher.listeners;

import android.app.Dialog;
import android.util.Log;
import android.view.View;

public class ClickListenerForCancel implements View.OnClickListener{
    private Dialog _dialog;
    private String _tag;

    public ClickListenerForCancel(Dialog _dialog, String _tag) {
        this._dialog = _dialog;
        this._tag = _tag;
    }

    @Override
    public void onClick(View view) {
        Log.d(_tag, "Cancel");
        _dialog.dismiss();
    }
}
