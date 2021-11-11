package com.example.habitsmasher;

import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

/**
 * HabitEventDialog is an abstract class that describes any dialog
 * involving adding and editing Habit Events
 * @author Jason Kim
 */
public abstract class HabitEventDialog extends DialogFragment implements DisplaysErrorMessages {

    // codes for the different error messages that are displayed by the habit event dialog
    public static final int INCORRECT_COMMENT = 1;
    public static final int INCORRECT_DATE = 2;

    protected EditText _eventCommentText;
    protected TextView _eventDateText;

    /* error text is a text view that has initially no content, and only gets a string value when
      to display an error message */
    protected TextView _errorText;

    @Override
    public void displayErrorMessage(int messageType) {
        switch(messageType) {
            case INCORRECT_COMMENT:
                /* even though the comment field is an EditText, we use errorText to
                display the error for aesthetic purposes */
                _errorText.setText("Incorrect habit event comment entered");
                break;
            case INCORRECT_DATE:
                _errorText.setText("Please enter a start date");
                break;
        }
    }
}
