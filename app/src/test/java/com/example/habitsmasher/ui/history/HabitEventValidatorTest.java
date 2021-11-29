package com.example.habitsmasher.ui.history;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import androidx.fragment.app.FragmentActivity;

import com.example.habitsmasher.DisplaysErrorMessages;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HabitEventValidatorTest {
    private static final Locale LOCALE = Locale.CANADA;
    private static final String VALID_COMMENT = "Valid Comment";
    private static final String INVALID_COMMENT = "I am too long of a comment to be valid";
    private static final String EXACT_LENGTH_COMMENT = "12345678901234567890";
    private static final String EMPTY_STRING = "";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, LOCALE);
    private static final String TODAY = SIMPLE_DATE_FORMAT.format(new Date());

    private HabitEventValidator _validator;

    @Before
    public void setUp() {
        _validator = new HabitEventValidator(mock(DisplaysErrorMessages.class));
    }

    @Test
    public void isHabitEventValid_allFieldsValid_expectHabitEventIsValid() {
        assertTrue(_validator.isHabitEventValid(VALID_COMMENT, TODAY));
    }

    @Test
    public void isHabitEventValid_invalidComment_expectHabitEventIsInvalid() {
        assertFalse(_validator.isHabitEventValid(INVALID_COMMENT, TODAY));
    }

    @Test
    public void isHabitEventValid_emptyDate_expectHabitEventIsInvalid() {
        assertFalse(_validator.isHabitEventValid(EMPTY_STRING, EMPTY_STRING));
    }

    @Test
    public void isHabitEventValid_commentExactly2Chars_expectHabitEventIsInvalid() {
        assertTrue(_validator.isHabitEventValid(EXACT_LENGTH_COMMENT, TODAY));
    }
}
