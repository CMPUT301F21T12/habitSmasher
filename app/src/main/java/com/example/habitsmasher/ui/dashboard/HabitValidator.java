package com.example.habitsmasher.ui.dashboard;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.DisplaysErrorMessages;
import com.example.habitsmasher.HabitDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that as a validator that checks if habit input fields are valid
 * when adding or editing a habit
 */
public class HabitValidator {
    protected final DisplaysErrorMessages _fragment;

    /**
     * Creates the validator
     * @param fragment that spawned this validator
     */
    public HabitValidator(DisplaysErrorMessages fragment) {
        _fragment = fragment;
    }

    /**
     * This method checks if a habit with the specified fields are valid
     * @param habitTitle the habit title
     * @param habitReason the habit reason
     * @param habitDate the habit date
     * @param tracker the tracker for days of the week
     * @return true, if habit is valid, false otherwise
     */
    public boolean isHabitValid(String habitTitle,
                                String habitReason,
                                String habitDate,
                                DaysTracker tracker) {
        Date parsedDate = checkHabitDateValid(habitDate);

        // checking title length
        if ((habitTitle.length() <= 0) || (habitTitle.length() > 20)) {
            _fragment.displayErrorMessage(HabitDialog.INCORRECT_TITLE);
            return false;
        }

        // checking reason length
        if ((habitReason.length() <= 0) || (habitReason.length() > 30)) {
            _fragment.displayErrorMessage(HabitDialog.INCORRECT_REASON);
            return false;
        }

        // check that a date was picked
        if (parsedDate == null) {
            _fragment.displayErrorMessage(HabitDialog.INCORRECT_DATE);
            return false;
        }

        // check that at least one day of the week was selected
        if (tracker.getDays().isEmpty()){
            _fragment.displayErrorMessage(HabitDialog.INCORRECT_DAYS);
            return false;
        }

        // return true if all checks pass
        return true;
    }


    /**
     * This helper method parses a given date in string form
     * @param date the habit date
     * @return correctly parsed date, null otherwise
     */
    public Date checkHabitDateValid(String date) {
        Date parsedDate;
        DateFormat inputDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            inputDateFormatter.setLenient(false);
            parsedDate = inputDateFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return parsedDate;
    }
}
