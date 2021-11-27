package com.example.habitsmasher;

import com.example.habitsmasher.listeners.ClickListenerForCancelTest;
import com.example.habitsmasher.listeners.ClickListenerForDatePickerTest;
import com.example.habitsmasher.listeners.ClickListenerForDaysOfTheWeekTest;
import com.example.habitsmasher.listeners.FailureListenerTest;
import com.example.habitsmasher.listeners.SuccessListenerTest;
import com.example.habitsmasher.listeners.SwipeListenerTest;
import com.example.habitsmasher.test.UserTest;
import com.example.habitsmasher.test.DatePickerDialogFragmentTest;
import com.example.habitsmasher.ui.dashboard.HabitListTest;
import com.example.habitsmasher.ui.dashboard.HabitValidatorTest;
import com.example.habitsmasher.ui.history.HabitEventListTest;
import com.example.habitsmasher.ui.history.HabitEventValidatorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HabitListTest.class,
                     UserTest.class,
                     HabitValidatorTest.class,
                     HabitEventListTest.class,
                     HabitEventValidatorTest.class,
                     DatePickerDialogFragmentTest.class,
                     UserValidatorTest.class,
                     ClickListenerForCancelTest.class,
                     ClickListenerForDatePickerTest.class,
                     ClickListenerForDaysOfTheWeekTest.class,
                     UserAccountHelperTest.class,
                     FailureListenerTest.class,
                     SuccessListenerTest.class,
                     SwipeListenerTest.class,
                     EmailValidatorTest.class})
public class HabitSmasherTestSuite {
}
