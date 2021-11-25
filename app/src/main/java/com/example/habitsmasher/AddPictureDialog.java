package com.example.habitsmasher;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.listeners.ClickListenerForCancel;
import com.example.habitsmasher.ui.history.HabitEventListFragment;

public class AddPictureDialog extends DialogFragment {
    private Uri _selectedImage;
    private TextView _header;
    private String TAG;
    private Button _cancelButton;
    private HabitEventDialog _parentDialog;
    private ImageButton _galleryButton;
    private ImageButton _cameraButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view and attach elements
        View view = inflater.inflate(R.layout.add_picture_dialog, container,false);
        initializeUIElements(view);

        // set header
        _header.setText("Select Picture");

        TAG = "AddPictureDialogue";

        // Cancel button
        setCancelButtonListener();

        // Set gallery click button
        setGalleryButtonListener();

        setCameraButtonListener();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Get habit event fragment for later use
            _parentDialog = (HabitEventDialog) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }

    private void initializeUIElements(View view) {
        _header = view.findViewById(R.id.add_picture_header_text);
        _cancelButton = view.findViewById(R.id.select_image_cancel_button);
        _galleryButton = view.findViewById(R.id.gallery_button);
        _cameraButton = view.findViewById(R.id.camera_button);
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
            _parentDialog.setEventImage(_selectedImage);

            // Close dialog
            this.dismiss();
        }
    }

    /**
     * Defines the logic when the cancel button is clicked
     */
    protected void setCancelButtonListener() {
        _cancelButton.setOnClickListener(new ClickListenerForCancel(getDialog(), TAG));
    }

    protected void setGalleryButtonListener() {
        _galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
    }

    protected void setCameraButtonListener() {
        _cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePhoto, 1);
            }
        });
    }
}
