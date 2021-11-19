package com.example.habitsmasher.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class hosts the unit tests for the ClickListenerForDatePicker listener
 *
 * The reference for testing listeners is the following url:
 * URL: https://stackoverflow.com/questions/18913827/how-to-test-listener-interface-is-called-within-android-unit-tests/19050763
 */
public class ClickListenerForDatePickerTest {
    private ClickListenerForDatePicker _clickListenerForDatePicker;

    @Before
    public void setUp() {
        _clickListenerForDatePicker = Mockito.spy(new ClickListenerForDatePicker
                (mock(FragmentManager.class), mock(TextView.class)));
    }

    @Test
    public void clickListenerForDatePicker_validInput_expectNoErrors(){
        View mockView = mock(View.class);
        _clickListenerForDatePicker.onClick(mockView);
        //Here we can verify whether the methods "onClick" executed
        verify(_clickListenerForDatePicker, times(1)).onClick(mockView);
    }
}
