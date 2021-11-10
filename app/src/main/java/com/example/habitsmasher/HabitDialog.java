package com.example.habitsmasher;

import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public abstract class HabitDialog extends DialogFragment implements DisplaysErrorMessages {
    public static final int INCORRECT_TITLE = 1;
    public static final int INCORRECT_REASON = 2;
    public static final int INCORRECT_DATE = 3;
    public static final int INCORRECT_DAYS = 4;

    protected EditText _habitTitleEditText;
    protected EditText _habitReasonEditText;
    protected TextView _habitDateTextView;
    protected TextView _daysOfWeekText;

    public void displayErrorMessage(int messageType) {
        switch(messageType) {
            case INCORRECT_TITLE:
                _habitTitleEditText.setError("Incorrect habit title entered");
                _habitTitleEditText.requestFocus();
                break;
            case INCORRECT_REASON:
                _habitReasonEditText.setError("Incorrect habit reason entered");
                break;
            case INCORRECT_DATE:
                _habitDateTextView.setError("Please enter a start date");
                _habitDateTextView.requestFocus();
                break;
            case INCORRECT_DAYS:
                _daysOfWeekText.setError("Please select a weekly schedule");
                break;
        }
    }
}
