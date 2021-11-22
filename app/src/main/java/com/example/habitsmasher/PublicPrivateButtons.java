package com.example.habitsmasher;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.tabs.TabLayout;

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
        // get the buttons
        _publicButton = view.findViewById(R.id.public_button);
        _privateButton = view.findViewById(R.id.private_button);

    }

    /**
     * This constructor is used whenever a habit is being viewed after creation, such as in {@link com.example.habitsmasher.ui.dashboard.EditHabitDialog}.
     * @param view The view that has the buttons.
     * @param isPublic Whether the habit is public or not.
     */
    public PublicPrivateButtons(View view, boolean isPublic){
        // get the buttons
        _publicButton = view.findViewById(R.id.public_button);
        _privateButton = view.findViewById(R.id.private_button);

        Log.d(TAG, String.valueOf(isPublic));

        // if its public, click the public button. if not, click the private button
        if (isPublic){
            _publicButton.performClick();
            _publicSelected = true;
            _privateSelected = false;
        }
        else{
            _privateButton.performClick();
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
        if (_publicSelected) return true;
        else return false;
    }

    /**
     * Returns whether the private button is selected or not
     * @return Status of the private button
     */
    public boolean isHabitPrivate(){
        if (_privateSelected) return true;
        else return false;
    }


}
