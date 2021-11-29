package com.example.habitsmasher;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * This class holds the public/private buttons to set the privacy for a given habit
 *
 * @author Cameron Matthew
 */
public class PublicPrivateButtons {
    /**
     * This class handles the logic behind the public and private buttons, which are used to
     * set a habit to either public or private.
     *
     * @author Cameron Matthew
     * @version 1.0
     */

    private Button _publicButton;
    private Button _privateButton;
    private boolean _publicSelected;
    private boolean _privateSelected;

    /**
     * This constructor is the default constructor, and is used in {@link com.example.habitsmasher.ui.dashboard.AddHabitDialog}.
     * @param view The view that has the buttons.
     */
    public PublicPrivateButtons(View view){
        // get the buttons if they exist
        try {
            _publicButton = view.findViewById(R.id.public_button);
            _privateButton = view.findViewById(R.id.private_button);
        }
        catch (Exception e){
            Log.e(TAG, "PublicPrivateButtons: Buttons Not Found!", e);
        }
    }

    /**
     * This constructor is used whenever a habit is being viewed after creation, such as in {@link com.example.habitsmasher.ui.dashboard.EditHabitDialog}.
     * @param view The view that has the buttons.
     * @param isPublic Whether the habit is public or not.
     */
    public PublicPrivateButtons(View view, boolean isPublic){
        // get the buttons if they exist
        try {
            _publicButton = view.findViewById(R.id.public_button);
            _privateButton = view.findViewById(R.id.private_button);
        }
        catch (Exception e){
            Log.e(TAG, "PublicPrivateButtons: Buttons Not Found!", e);
        }

        // if its public, click the public button. if not, click the private button
        if (isPublic){
            // Catch error when buttons are not loaded
            try {
                _publicButton.performClick();
            }
            catch (Exception e){
                Log.e(TAG, "PublicPrivateButtons: Buttons Not Found!", e);
            }

            _publicSelected = true;
            _privateSelected = false;
        }

        else{
            // Catch error when buttons are not loaded
            try {
                _privateButton.performClick();
            }
            catch (Exception e){
                Log.e(TAG, "PublicPrivateButtons: Buttons Not Found!", e);
            }

            _publicSelected = false;
            _privateSelected = true;
        }

    }

    /**
     * Sets the onClick listeners for both buttons. This also sets the logic behind the buttons in which
     * only one button may be clicked at a time.
     */
    public void setClickListeners(){
        // track whether the button is selected or not
        _publicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if we check the public box, uncheck the private box
                if (_publicSelected){
                    _publicSelected = false;
                }
                else {
                    _publicSelected = true;
                }
            }
        });

        // track whether the button is selected or not
        _privateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_privateSelected){
                    _privateSelected = false;
                }
                else{
                    _privateSelected = true;
                }
            }
        });
    }

    /**
     * Returns whether the public button is selected or not.
     * @return Status of the public button
     */
    public boolean isHabitPublic(){
        return _publicSelected;
    }

    /**
     * Returns whether the private button is selected or not
     * @return Status of the private button
     */
    public boolean isHabitPrivate(){
        return _privateSelected;
    }

    /**
     * Sets both buttons to whatever state given.
     * @param publicButton the new public button state
     * @param privateButton the new private button state
     */
    public void setButtons(boolean publicButton, boolean privateButton){
        // This is actually a method I'm using for unit testing the validator
        _publicSelected = publicButton;
        _privateSelected = privateButton;
    }


}
