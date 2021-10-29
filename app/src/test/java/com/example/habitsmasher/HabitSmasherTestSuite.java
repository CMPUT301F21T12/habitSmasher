package com.example.habitsmasher;

import com.example.habitsmasher.test.UserTest;
import com.example.habitsmasher.ui.dashboard.HabitListTest;
import com.example.habitsmasher.ui.dashboard.HabitValidatorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HabitListTest.class,
                     UserTest.class,
                     HabitValidatorTest.class})
public class HabitSmasherTestSuite {
}
