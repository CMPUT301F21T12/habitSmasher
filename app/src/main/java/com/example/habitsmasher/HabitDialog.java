package com.example.habitsmasher;

import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

/**
 * HabitDialog is an abstract class that describes any dialog
 * adding and editing Habits
 * @author Jason Kim
 */
public abstract class HabitDialog extends DialogFragment implements DisplaysErrorMessages {

    // codes for the different error messages displayed by the habit dialog
    public static final int INCORRECT_TITLE = 1;
    public static final int INCORRECT_REASON = 2;
    public static final int INCORRECT_DATE = 3;
    public static final int INCORRECT_DAYS = 4;

    protected EditText _habitTitleEditText;
    protected EditText _habitReasonEditText;
    protected TextView _habitDateTextView;

    /* error text is a text view that has initially no content, and only gets a string value when
      to display an error message */
    protected TextView _errorText;

    @Override
    public void displayErrorMessage(int messageType) {
        switch(messageType) {
            case INCORRECT_TITLE:
                _habitTitleEditText.setError("Incorrect habit title entered");
                _habitTitleEditText.requestFocus();
                break;
            case INCORRECT_REASON:
                _habitReasonEditText.setError("Incorrect habit reason entered");
                _habitReasonEditText.requestFocus();
                break;
            case INCORRECT_DATE:
                _errorText.setText("Please enter a start date");
                break;
            case INCORRECT_DAYS:
                _errorText.setText("Please select a weekly schedule");
                break;
        }
    }
}
