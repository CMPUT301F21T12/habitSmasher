package com.example.habitsmasher;

import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

/**
 * HabitEventDialog is an abstract class that describes any dialog
 * involving adding and editing Habit Events
 */
public abstract class HabitEventDialog extends DialogFragment implements DisplaysErrorMessages {
    public static final int INCORRECT_COMMENT = 1;
    public static final int INCORRECT_DATE = 2;

    protected EditText _eventCommentText;
    protected TextView _eventDateText;
    protected TextView _errorText;

    @Override
    public void displayErrorMessage(int messageType) {
        switch(messageType) {
            case INCORRECT_COMMENT:
                _eventCommentText.setText("Incorrect habit event comment entered");
                break;
            case INCORRECT_DATE:
                _errorText.setText("Please enter a start date");
                break;
        }
    }
}
