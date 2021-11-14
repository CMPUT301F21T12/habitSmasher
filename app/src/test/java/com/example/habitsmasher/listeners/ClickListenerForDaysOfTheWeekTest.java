package com.example.habitsmasher.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.view.View;

import com.example.habitsmasher.DaysTracker;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class hosts the unit tests for the ClickListenerForDaysOfTheWeek listener
 *
 * The reference for testing listeners is the following url:
 * URL: https://stackoverflow.com/questions/18913827/how-to-test-listener-interface-is-called-within-android-unit-tests/19050763
 */
public class ClickListenerForDaysOfTheWeekTest {
    private static final String DAY = "MO";

    private ClickListenerForDaysOfTheWeek _clickListenerForDaysOfTheWeek;

    @Before
    public void setUp() {
        _clickListenerForDaysOfTheWeek = Mockito.spy(new ClickListenerForDaysOfTheWeek
                (mock(DaysTracker.class), DAY));
    }

    @Test
    public void clickListenerForDaysOfTheWeek_validInput_expectNoErrors(){
        View mockView = mock(View.class);
        _clickListenerForDaysOfTheWeek.onClick(mockView);
        //Here we can verify whether the methods "onClick" executed
        verify(_clickListenerForDaysOfTheWeek, times(1)).onClick(mockView);
    }
}
