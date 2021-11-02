package com.example.habitsmasher;

import com.example.habitsmasher.test.DatePickerDialogFragmentTest;
import com.example.habitsmasher.test.UserUnitTest;
import com.example.habitsmasher.ui.dashboard.HabitListTest;
import com.example.habitsmasher.ui.dashboard.HabitValidatorTest;
import com.example.habitsmasher.ui.history.HabitEventListTest;
import com.example.habitsmasher.ui.history.HabitEventValidatorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HabitListTest.class,
                     UserUnitTest.class,
                     HabitValidatorTest.class,
                     HabitEventListTest.class,
                     HabitEventValidatorTest.class,
        DatePickerDialogFragmentTest.class})
public class HabitSmasherTestSuite {
}
