package com.example.habitsmasher;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.listeners.ClickListenerForCancel;

public class DeleteUserDialog extends DialogFragment {
    // UI elements
    private TextView _header;
    private Button _cancelButton;
    private Button _confirmButton;
    private final static String TAG = "ConfirmDeleteUserDialog";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view and attach elements
        View view = inflater.inflate(R.layout.confirm_delete_user_dialog, container, false);
        initializeUIElements(view);

        // Set header
        _header.setText("Delete User");

        // Set Cancel Button
        setCancelButtonListener();

        // Set confirm button
        setConfirmButton();

        return view;
    }

    private void initializeUIElements(View view) {
        _header = view.findViewById(R.id.delete_user_dialog_header_text);
        _cancelButton = view.findViewById(R.id.delete_user_cancel_button);
        _confirmButton = view.findViewById(R.id.delete_user_confirm_button);
    }

    /**
     * Defines the logic when the cancel button is clicked
     */
    protected void setCancelButtonListener() {
        _cancelButton.setOnClickListener(new ClickListenerForCancel(getDialog(), TAG));
    }

    protected void setConfirmButton() {
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm clicked");
                getDialog().dismiss();
            }
        });
    }

}
