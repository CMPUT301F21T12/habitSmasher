package com.example.habitsmasher.ui.dashboard;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.habitsmasher.DaysTracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that as a validator that checks if habit input fields are valid
 * when adding or editing a habit
 */
public class HabitValidator {
    private final FragmentActivity _context;

    /**
     * Creates the validator
     * @param context fragment that spawned this validator
     */
    public HabitValidator(FragmentActivity context) {
        _context = context;
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
            showToastMessage("Incorrect habit title entered");
            return false;
        }

        // checking reason length
        if ((habitReason.length() <= 0) || (habitReason.length() > 30)) {
            showToastMessage("Incorrect habit reason entered");
            return false;
        }

        // checking data
        if (parsedDate == null) {
            showToastMessage("Please enter a start date");
            return false;
        }

        // checking days of week
        if (tracker.getDays().isEmpty()){
            showToastMessage("Please select a weekly schedule.");
            return false;
        }

        return true;
    }

    /**
     * This helper method displays a message to the screen
     * Implementation from:
     * https://stackoverflow.com/questions/3875184/cant-create-handler-inside-thread-that-has-not-called-looper-prepare
     * User: Ayaz Alifov
     * Date: January 24, 2016
     * @param message the message to display
     */
    protected void showToastMessage(String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(_context, message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
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
