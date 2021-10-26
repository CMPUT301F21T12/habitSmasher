package com.example.habitsmasher;

import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // ensure that the app has transitioned to the Habit List screen
        assertTrue(solo.waitForText("Habit List", 1, 2000));
    }

    /**
     * Navigate to the profile page using the bottom navigation
     */
    @Test
    public void navigateToUserProfile(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Notifications screen
        assertTrue(solo.waitForText("Profile", 1, 2000));
    }

    /**
     * Make sure that the home screen is displayed when the app starts
     */
    @Test
    public void ensureHomeScreenDisplayedOnStart(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // ensure that the app has transitioned to the Notifications screen
        assertTrue(solo.waitForText("Home", 1, 2000));
    }

    @Test
    public void navigateToHabitViewFragment() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        solo.clickOnView(solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitToListTest", "Test Reason", new Date());

        // Format the date
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        // Enter title
        solo.enterText(solo.getEditText("Habit title"), testHabit.getTitle());

        // Enter reason
        solo.enterText(solo.getEditText("Habit reason"), testHabit.getReason());

        // Enter date
        solo.enterText(solo.getEditText("dd-mm-yyyy"), simpleDateFormat.format(testHabit.getDate()));

        // Click confirm
        solo.clickOnView(solo.getView(R.id.confirm_habit));

        // Click on added Habit
        solo.clickOnText(testHabit.getTitle());

        // Check that the viewHabitFragment has loaded
        assertTrue(solo.waitForText("HabitViewFragment", 1, 2000));

        // Check that reason is correct
        assertTrue(solo.waitForText(testHabit.getReason(), 1, 2000));

        // Check that the date is correct
        assertTrue(solo.waitForText(simpleDateFormat.format(testHabit.getDate()), 1, 2000));

        // Click up/back button
        solo.goBack();

        // Check that the current fragment is the habit list
        assertTrue(solo.waitForText("Habit List", 1, 2000));


    }
}
