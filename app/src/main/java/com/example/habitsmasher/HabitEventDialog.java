package com.example.habitsmasher;

import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public abstract class HabitEventDialog extends DialogFragment implements DisplaysErrorMessages {
    public static final int INCORRECT_COMMENT = 1;
    public static final int INCORRECT_DATE = 2;

    protected EditText _eventCommentText;
    protected TextView _eventDateText;
    protected TextView _errorText;

    public void displayErrorMessage(int messageType) {
        switch(messageType) {
            case INCORRECT_COMMENT:
                _eventCommentText.setError("Incorrect comment");
                break;
            case INCORRECT_DATE:
                _errorText.setText("Please select a date");
                break;
        }
    }
}
