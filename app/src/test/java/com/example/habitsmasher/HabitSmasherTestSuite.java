package com.example.habitsmasher;

import com.example.habitsmasher.test.UserUnitTest;
import com.example.habitsmasher.ui.dashboard.HabitListTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HabitListTest.class, UserUnitTest.class})
public class HabitSmasherTestSuite {
}
