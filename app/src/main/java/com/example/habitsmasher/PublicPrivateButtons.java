package com.example.habitsmasher;

import android.view.View;
import android.widget.Button;

import java.util.HashMap;

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

        // set the public button as the default button.
        _publicButton.performClick();
        _publicSelected = true;
        _privateSelected = false;

        // set the listeners
        setClickListeners();
    }

    /**
     * This constructor is used whenever a habit is being viewed after creation, such as in {@link com.example.habitsmasher.ui.dashboard.EditHabitFragment}.
     * @param view The view that has the buttons.
     * @param isPublic Whether the habit is public or not.
     */
    public PublicPrivateButtons(View view, boolean isPublic){
        // get the buttons
        _publicButton = view.findViewById(R.id.public_button);
        _privateButton = view.findViewById(R.id.private_button);

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

        // set the listeners
        setClickListeners();
    }

    private void setClickListeners(){
        // on click listeners and logic behind the buttons switching
        // TODO: Check interactions when the selected button is selected again
        // preferably make it unselectable

        _publicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if we check the public box, uncheck the private box
                if (_privateSelected){
                    _privateButton.performClick();
                    _privateSelected = false;
                }
            }
        });

        _privateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if we check the private box, uncheck the public box
                if (_publicSelected){
                    _publicButton.performClick();
                    _publicSelected = false;
                }
            }
        });
    }

    /**
     * Returns whether the public button is selected or not. Logically, if the public button is
     * selected the private button is unselected, and vice versa.
     * @return
     */
    public boolean isPublic(){
        if (_publicSelected) return true;
        else return false;
    }


}
