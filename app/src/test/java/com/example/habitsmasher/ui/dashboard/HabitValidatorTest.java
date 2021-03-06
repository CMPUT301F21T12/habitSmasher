package com.example.habitsmasher.ui.dashboard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.view.View;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.DisplaysErrorMessages;
import com.example.habitsmasher.PublicPrivateButtons;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HabitValidatorTest {
    private static final Locale LOCALE = Locale.CANADA;
    private static final String GOOD_TITLE = "Good title";
    private static final String GOOD_REASON = "Good reason";
    private static final DaysTracker GOOD_TRACKER = new DaysTracker("MO WE FR");
    private static final String LONG_TITLE = "This title is toooooooooooooooooooooo long";
    private static final String LONG_REASON = "This reason is toooooooooooooooooooooooooooooo long";
    private static final String TITLE_20_CHARACTERS = "01234567890123456789";
    private static final String REASON_30_CHARACTERS = "012345678901234567890123456789";
    private static final DaysTracker BAD_TRACKER = new DaysTracker();
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, LOCALE);
    private static final String TODAY = SIMPLE_DATE_FORMAT.format(new Date());

    private static final PublicPrivateButtons GOOD_PRIVACY_BUTTONS = new PublicPrivateButtons(mock(View.class), true);
    private static final PublicPrivateButtons BAD_PRIVACY_BUTTONS = new PublicPrivateButtons(mock(View.class));


    private HabitValidator _validator;

    @Before
    public void setUp() {
        _validator = new HabitValidator(mock(DisplaysErrorMessages.class));
    }

    @Test
    public void isHabitValid_allFieldsValid_expectHabitIsValid() {
        assertTrue(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_emptyTitle_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid("", GOOD_REASON, TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_emptyReason_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(GOOD_TITLE, "", TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_emptyDate_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, "", GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }
    @Test
    public void isHabitValid_emptyDays_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, TODAY, BAD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_titleTooLong_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(LONG_TITLE, GOOD_REASON, TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_reasonTooLong_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(GOOD_TITLE, LONG_REASON, TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_titleExactly20Characters_expectHabitIsValid() {
        assertTrue(_validator.isHabitValid(TITLE_20_CHARACTERS, GOOD_REASON, TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_reasonExactly30Characters_expectHabitIsValid() {
        assertTrue(_validator.isHabitValid(GOOD_TITLE, REASON_30_CHARACTERS, TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_noPrivacyButtonsSelected_expectHabitIsInvalid(){
        assertFalse(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, TODAY, GOOD_TRACKER, BAD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_bothPrivacyButtonsSelected_expectHabitIsInvalid(){
        BAD_PRIVACY_BUTTONS.setButtons(true, true);
        assertFalse(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, TODAY, GOOD_TRACKER, BAD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_publicButtonSelected_expectHabitIsValid(){
        assertTrue(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }

    @Test
    public void isHabitValid_privateButtonSelected_expectHabitIsValid(){
        GOOD_PRIVACY_BUTTONS.setButtons(false, true);
        assertTrue(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, TODAY, GOOD_TRACKER, GOOD_PRIVACY_BUTTONS));
    }
}
