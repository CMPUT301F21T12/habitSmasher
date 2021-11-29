package com.example.habitsmasher;

import android.content.Context;
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
import com.example.habitsmasher.ui.profile.ProfileFragment;

/**
 * This class handles the dialog for deleting a user
 */
public class DeleteUserDialog extends DialogFragment {
    // UI elements
    private TextView _header;
    private Button _cancelButton;
    private Button _confirmButton;
    private ProfileFragment _profileFragment;
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

    /**
     * Initialize the UI elements for the dialog
     * @param view (View) The dialog view
     */
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

    /**
     * Defines the logic when the confirm button is clicked
     */
    protected void setConfirmButton() {
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete a user and close the dialog
                _profileFragment.deleteUser();
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Get habit event fragment for later use
            _profileFragment = (ProfileFragment) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }

}
