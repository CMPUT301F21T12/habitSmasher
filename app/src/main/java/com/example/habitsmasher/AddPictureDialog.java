package com.example.habitsmasher;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.listeners.ClickListenerForCancel;

public class AddPictureDialog extends DialogFragment {
    private Uri _selectedImage;
    protected TextView _header;
    protected String TAG;
    protected Button _cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view and attach elements
        View view = inflater.inflate(R.layout.add_picture_dialog, container,false);

        // set header
        _header.setText("Select Picture");

        TAG = "AddPictureDialogue";

        // Cancel button
        setCancelButtonListener();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void initializeUIElements(View view) {
        _header = view.findViewById(R.id.add_picture_header_text);
        _cancelButton = view.findViewById(R.id.select_image_cancel_button);
    }

    /**
     * Not touching this when refactoring until images are fully implemented for habit events
     * Reference: https://stackoverflow.com/questions/10165302/dialog-to-pick-image-from-gallery-or-from-camera
     * Override onActivityResult to handle when user has selected image
     * @param requestCode
     * @param resultCode
     * @param imageReturnedIntent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            // Set selected picture
            _selectedImage = imageReturnedIntent.getData();

            // TODO: Send selected picture
        }
    }

    /**
     * Defines the logic when the cancel button is clicked
     */
    protected void setCancelButtonListener() {
        _cancelButton.setOnClickListener(new ClickListenerForCancel(getDialog(), TAG));
    }
}
