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

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */
public class MainActivityTest {
    private static final String HABIT_TITLE_FIELD = "Habit title";
    private static final String HABIT_REASON_FIELD = "Habit reason";
    private static final String HABIT_LIST_TEXT = "Habit List";
    private static final String WRONG_ACTIVITY_MESSAGE = "Wrong Activity";
    private static final String PROFILE_TEXT = "Profile";
    private static final String HOME_TEXT = "Home";
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final String HABIT_TITLE_ERROR_MESSAGE = "Incorrect habit title entered";
    private static final String HABIT_REASON_ERROR_MESSAGE = "Incorrect habit reason entered";
    private static final String EMPTY_DATE_ERROR_MESSAGE = "Please enter a start date";

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
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // ensure that the app has transitioned to the Habit List screen
        assertTextOnScreen(HABIT_LIST_TEXT);
    }

    /**
     * Navigate to the profile page using the bottom navigation
     */
    @Test
    public void navigateToUserProfile(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(PROFILE_TEXT);
    }

    /**
     * Make sure that the home screen is displayed when the app starts
     */
    @Test
    public void ensureHomeScreenDisplayedOnStart(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(HOME_TEXT);
    }

    @Test
    public void navigateToHabitViewFragment() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        solo.clickOnView(solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitToListTest", "Test Reason", new Date());

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Click on added Habit
        solo.clickOnText(testHabit.getTitle());

        // Check the title is correct
        assertTextOnScreen(testHabit.getTitle());

        // Check that reason is correct
        assertTextOnScreen(testHabit.getReason());

        // Check that the date is correct
        assertTextOnScreen(new SimpleDateFormat(DATE_PATTERN).format(testHabit.getDate()));

        // Click up/back button
        solo.goBack();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);
    }

    @Test
    public void ensureHabitAddedSuccessfully() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        solo.clickOnView(solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitTest", "Test Reason", new Date());

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());
    }

    @Test
    public void ensureHabitAdditionFails_emptyTitle() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        solo.clickOnView(solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitTest", "Test Reason", new Date());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the error message is displayed
        assertTextOnScreen(HABIT_TITLE_ERROR_MESSAGE);

        // Add habit title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());
    }

    @Test
    public void ensureHabitAdditionFails_titleTooLong() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        solo.clickOnView(solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("ExampleHabitTitleThatIsTooLong", "Test Reason", new Date());

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the error message is displayed
        assertTextOnScreen(HABIT_TITLE_ERROR_MESSAGE);

        // Add shorter habit title
        solo.clearEditText(solo.getEditText(testHabit.getTitle()));
        testHabit.setTitle("shorterTitle");
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());
    }

    @Test
    public void ensureHabitAdditionFails_reasonTooLong() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        solo.clickOnView(solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitTest", "AnExampleHabitReasonThatIsTooLong", new Date());

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the error message is displayed
        assertTextOnScreen(HABIT_REASON_ERROR_MESSAGE);

        // Add shorter habit title
        solo.clearEditText(solo.getEditText(testHabit.getReason()));
        testHabit.setReason("shorterReason");
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());
    }

    @Test
    public void ensureHabitAdditionFails_reasonEmpty() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        solo.clickOnView(solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitTest", "", new Date());

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the error message is displayed
        assertTextOnScreen(HABIT_REASON_ERROR_MESSAGE);

        // Add habit reason
        testHabit.setReason("acceptable reason");
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());
    }

    @Test
    public void ensureHabitAdditionFails_dateEmpty() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        solo.clickOnView(solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitTest", "Test Reason", new Date());

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the error message is displayed
        assertTextOnScreen(EMPTY_DATE_ERROR_MESSAGE);

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());
    }

    private void clickConfirmButtonInAddHabitDialogBox() {
        solo.clickOnView(solo.getView(R.id.confirm_habit));
    }

    private void enterCurrentDateInAddHabitDialogBox() {
        solo.clickOnView(solo.getView(R.id.habit_date_selection));
        solo.clickOnText("OK");
    }

    private void setFieldInAddHabitDialogBox(String fieldToSet, String text) {
        solo.enterText(solo.getEditText(fieldToSet), text);
    }

    private void assertTextOnScreen(String text) {
        assertTrue(solo.waitForText(text, 1, 2000));
    }
}
