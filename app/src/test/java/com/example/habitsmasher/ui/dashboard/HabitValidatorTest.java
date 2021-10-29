package com.example.habitsmasher.ui.dashboard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import androidx.fragment.app.FragmentActivity;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HabitValidatorTest {
    private static final Locale LOCALE = Locale.CANADA;
    private static final String GOOD_TITLE = "Good title";
    private static final String GOOD_REASON = "Good reason";
    private static final String LONG_TITLE = "This title is toooooooooooooooooooooo long";
    private static final String LONG_REASON = "This reason is toooooooooooooooooooooooooooooo long";
    private static final String TITLE_20_CHARACTERS = "01234567890123456789";
    private static final String REASON_30_CHARACTERS = "012345678901234567890123456789";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, LOCALE);
    private static final String TODAY = SIMPLE_DATE_FORMAT.format(new Date());

    private HabitValidator _validator;

    @Before
    public void setUp() {
        _validator = new HabitValidator(mock(FragmentActivity.class));
    }

    @Test
    public void isHabitValid_allFieldsValid_expectHabitIsValid() {
        assertTrue(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, TODAY));
    }

    @Test
    public void isHabitValid_emptyTitle_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid("", GOOD_REASON, TODAY));
    }

    @Test
    public void isHabitValid_emptyReason_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(GOOD_TITLE, "", TODAY));
    }

    @Test
    public void isHabitValid_emptyDate_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(GOOD_TITLE, GOOD_REASON, ""));
    }

    @Test
    public void isHabitValid_titleTooLong_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(LONG_TITLE, GOOD_REASON, TODAY));
    }

    @Test
    public void isHabitValid_reasonTooLong_expectHabitIsInvalid() {
        assertFalse(_validator.isHabitValid(GOOD_TITLE, LONG_REASON, TODAY));
    }

    @Test
    public void isHabitValid_titleExactly20Characters_expectHabitIsValid() {
        assertTrue(_validator.isHabitValid(TITLE_20_CHARACTERS, GOOD_REASON, TODAY));
    }

    @Test
    public void isHabitValid_reasonExactly30Characters_expectHabitIsValid() {
        assertTrue(_validator.isHabitValid(GOOD_TITLE, REASON_30_CHARACTERS, TODAY));
    }
}
