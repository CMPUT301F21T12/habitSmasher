package com.example.habitsmasher.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Dialog;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class hosts the unit tests for the ClickListenerForCancel listener
 *
 * The reference for testing listeners is the following url:
 * URL: https://stackoverflow.com/questions/18913827/how-to-test-listener-interface-is-called-within-android-unit-tests/19050763
 */
public class ClickListenerForCancelTest {
    private static final String TAG = "Example tag";

    private ClickListenerForCancel _clickListenerForCancel;

    @Before
    public void setUp() {
        _clickListenerForCancel = Mockito.spy(new ClickListenerForCancel(mock(Dialog.class), TAG));
    }

    @Test
    public void clickListenerForCancel_validInput_expectNoErrors(){
        View mockView = mock(View.class);
        _clickListenerForCancel.onClick(mockView);
        //Here we can verify whether the methods "onClick" executed
        verify(_clickListenerForCancel, times(1)).onClick(mockView);
    }
}
