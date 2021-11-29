package com.example.habitsmasher;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.listeners.ClickListenerForCancel;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This dialog allows user to either select an image from their gallery or take it with their camera
 *
 * @author Julie Pilz
 */
public class AddPictureDialog extends DialogFragment {
    private String TAG;

    // The chosen image
    private Uri _selectedImage;

    // UI elements
    private TextView _header;
    private Button _cancelButton;
    private PictureSelectionUser _parentDialog;
    private ImageButton _galleryButton;
    private ImageButton _cameraButton;

    // Constants
    private static final String IMAGE_DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String FILE_EXTENSION = ".jpg";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view and attach elements
        View view = inflater.inflate(R.layout.add_picture_dialog, container,false);
        initializeUIElements(view);

        // Set header
        _header.setText("Select Picture");

        // Set tag
        TAG = "AddPictureDialogue";

        // Set Cancel button
        setCancelButtonListener();

        // Set gallery and camera buttons
        setGalleryButtonListener();
        setCameraButtonListener();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Get parent fragment to send image back to
            _parentDialog = (PictureSelectionUser) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }

    /**
     * Initialize the UI elements of this dialog
     * @param view
     */
    private void initializeUIElements(View view) {
        _header = view.findViewById(R.id.add_picture_header_text);
        _cancelButton = view.findViewById(R.id.select_image_cancel_button);
        _galleryButton = view.findViewById(R.id.gallery_button);
        _cameraButton = view.findViewById(R.id.camera_button);
    }

    /**
     * Reference: https://stackoverflow.com/questions/10165302/dialog-to-pick-image-from-gallery-or-from-camera
     * Override onActivityResult to handle when user has selected image
     * @param requestCode
     * @param resultCode
     * @param imageReturnedIntent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode){
            case 1:
                // If the user selects the image from the gallery
                if (resultCode == RESULT_OK) {
                    // Set selected picture
                    _selectedImage = imageReturnedIntent.getData();
                    _parentDialog.setImage(_selectedImage);

                    // Close dialog
                    this.dismiss();
                }
                break;
            case 2:
                // If the user takes the image it will already be stored in _selectedImage
                if (resultCode == RESULT_OK) {
                    // Set selected picture
                    _parentDialog.setImage(_selectedImage);

                    // Close dialog
                    this.dismiss();
                }
                break;
        }
    }

    /**
     * Defines the logic when the cancel button is clicked
     */
    protected void setCancelButtonListener() {
        _cancelButton.setOnClickListener(new ClickListenerForCancel(getDialog(), TAG));
    }

    /**
     * Defines the logic when the gallery button is clicked
     */
    protected void setGalleryButtonListener() {
        _galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
    }

    /**
     * Defines the logic when the camera button is clicked
     */
    protected void setCameraButtonListener() {
        _cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    /**
     * Method that handles the event related to handling the camera
     * Reference: https://developer.android.com/training/camera/photobasics#java
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(TAG, "Failed to process image");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                _selectedImage = FileProvider.getUriForFile(getContext(),
                        "com.example.habitsmasher.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, _selectedImage);
                startActivityForResult(takePictureIntent, 2);
            }
        }
    }

    /**
     * Creates a temporary image file when the camera is used
     * Reference: https://developer.android.com/training/camera/photobasics#java
     * @return The created image
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(IMAGE_DATE_FORMAT).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                FILE_EXTENSION,         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }
}
