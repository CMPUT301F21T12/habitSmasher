package com.example.habitsmasher.ui.history;

import com.example.habitsmasher.DisplaysErrorMessages;
import com.example.habitsmasher.HabitEventDialog;
import com.example.habitsmasher.ui.dashboard.HabitValidator;

import java.util.Date;

/**
 * Habit Event Validator, extends off habit validator to also add input validation
 * for habit events
 *
 * @author Julie Pilz, Jason Kim
 */
public class HabitEventValidator extends HabitValidator {
    /**
     * Default constructor
     * @param fragment The parent fragment
     */
    public HabitEventValidator(DisplaysErrorMessages fragment) {
        super(fragment);
    }

    private final int MINIMUM_COMMENT_LENGTH = 0;
    private final int MAXIMUM_COMMENT_LENGTH = 20;

    /**
     * Checks if a habit event is valid
     * @param habitEventComment (String) The comment of the new habit event
     * @param habitEventDate (String) The date of the new habit event
     * @return True or False depending on validity
     */
    public boolean isHabitEventValid(String habitEventComment, String habitEventDate) {
        Date parsedDate = checkHabitDateValid(habitEventDate);

        // check that the comment conforms to requirements
        if (habitEventComment.length() > MAXIMUM_COMMENT_LENGTH) {
            _fragment.displayErrorMessage(HabitEventDialog.INCORRECT_COMMENT);
            return false;
        }

        // check that a date was selected
        if (parsedDate == null) {
            _fragment.displayErrorMessage(HabitEventDialog.INCORRECT_DATE);
            return false;
        }

        // if all checks pass return true
        return true;
    }
}
