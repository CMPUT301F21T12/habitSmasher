package com.example.habitsmasher;

import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */
public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }
    
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Navigate to the habit list using the bottom navigation
     */
    @Test
    public void navigateToHabitList(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnText("Habit List");

        // ensure that the app has transitioned to the Habit List screen
        assertTrue(solo.waitForText("Habit List", 2, 2000));
    }

    /**
     * Navigate to the profile page using the bottom navigation
     */
    @Test
    public void navigateToUserProfile(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        solo.clickOnText("Profile");

        // ensure that the app has transitioned to the Notifications screen
        assertTrue(solo.waitForText("Profile", 2, 2000));
    }

    /**
     * Make sure that the home screen is displayed when the app starts
     */
    @Test
    public void ensureHomeScreenDisplayedOnStart(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // ensure that the app has transitioned to the Notifications screen
        assertTrue(solo.waitForText("Home", 2, 2000));
    }
}
