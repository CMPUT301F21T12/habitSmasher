package com.example.habitsmasher.ui.dashboard;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.DisplaysErrorMessages;
import com.example.habitsmasher.HabitDialog;
import com.example.habitsmasher.PublicPrivateButtons;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that as a validator that checks if habit input fields are valid
 * when adding or editing a habit
 */
public class HabitValidator {

    /* takes in a fragment that can display error messages, this allows it
    to pass in fragments from Habit and HabitEvent (since habitEventValidator
    extends this class) */
    protected final DisplaysErrorMessages _fragment;

    private final int MINIMUM_LENGTH = 0;
    private final int MAXIMUM_HABIT_TITLE_LENGTH = 20;
    private final int MAXIMUM_HABIT_REASON_LENGTH = 30;

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
     * @param privacyButtons
     * @return true, if habit is valid, false otherwise
     */
    public boolean isHabitValid(String habitTitle,
                                String habitReason,
                                String habitDate,
                                DaysTracker tracker, PublicPrivateButtons privacyButtons) {
        Date parsedDate = checkHabitDateValid(habitDate);

        // checking title length
        if ((habitTitle.length() <= MINIMUM_LENGTH) ||
                (habitTitle.length() > MAXIMUM_HABIT_TITLE_LENGTH)) {
            _fragment.displayErrorMessage(HabitDialog.INCORRECT_TITLE);
            return false;
        }

        // checking reason length
        if ((habitReason.length() <= MINIMUM_LENGTH) ||
                (habitReason.length() > MAXIMUM_HABIT_REASON_LENGTH)) {
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

        // check that only one button is pressed, not both or neither
        if (!(privacyButtons.isHabitPrivate() || privacyButtons.isHabitPublic()) ||
                (privacyButtons.isHabitPrivate() && privacyButtons.isHabitPublic())){
            _fragment.displayErrorMessage(HabitDialog.INCORRECT_PRIVACY);
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
