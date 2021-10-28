package com.example.habitsmasher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.TextView;

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
    private static final String EDIT_BUTTON = "EDIT";
    private static final String DELETE_BUTTON = "DELETE";
    private static final long HABIT_ID = 1;

    private Solo _solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        _solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
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
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // ensure that the app has transitioned to the Habit List screen
        assertTextOnScreen(HABIT_LIST_TEXT);
    }

    /**
     * Navigate to the profile page using the bottom navigation
     */
    @Test
    public void navigateToUserProfile(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(PROFILE_TEXT);
    }

    /**
     * Make sure that the home screen is displayed when the app starts
     */
    @Test
    public void ensureHomeScreenDisplayedOnStart(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(HOME_TEXT);
    }

    @Test
    public void navigateToHabitViewFragment() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("viewHabitTest", "Test Reason", new Date(), HABIT_ID);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Click on added Habit
        _solo.clickOnText(testHabit.getTitle());

        // Check the title is correct
        assertTextOnScreen(testHabit.getTitle());

        // Check that reason is correct
        assertTextOnScreen(testHabit.getReason());

        // Check that the date is correct
        assertTextOnScreen(new SimpleDateFormat(DATE_PATTERN).format(testHabit.getDate()));

        // Click up/back button
        _solo.goBack();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAddedSuccessfully() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitSuccessTest", "Test Reason", new Date(), HABIT_ID);

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

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAdditionFails_emptyTitle() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitEmptyTitle", "Test Reason", new Date(), HABIT_ID);

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

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAdditionFails_titleTooLong() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("ExampleHabitTitleThatIsTooLong", "Test Reason", new Date(), HABIT_ID);

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
        _solo.clearEditText(_solo.getEditText(testHabit.getTitle()));
        testHabit.setTitle("shorterTitle");
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAdditionFails_reasonTooLong() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitReasonLong", "AnExampleHabitReasonThatIsTooLong", new Date(), HABIT_ID);

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
        _solo.clearEditText(_solo.getEditText(testHabit.getReason()));
        testHabit.setReason("shorterReason");
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAdditionFails_reasonEmpty() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitEmptyReason", "", new Date(), HABIT_ID);

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

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAdditionFails_dateEmpty() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitEmptyDate", "Test Reason", new Date(), HABIT_ID);

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

        deleteTestHabit(testHabit);
    }


    /**
     * Tests edit function is working correctly
     * Testing swipe code found here:
     * URL: https://stackoverflow.com/questions/24664730/writing-a-robotium-test-to-swipe-open-an-item-on-a-swipeable-listview
     * Name: C0D3LIC1OU5
     * Date: July 9, 2014
     */
    @Test
    public void ensureEditIsFunctioning() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("editHabitTest", "Test Reason", new Date(), HABIT_ID);

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

        swipeLeftOnHabit(testHabit);
        _solo.waitForView(R.id.edit_button);
        _solo.clickOnButton(EDIT_BUTTON);
        // clear Edit Text fields
        _solo.clearEditText(0);
        _solo.clearEditText(1);

        Habit testEditHabit = new Habit("editHabitWorked", "testReason1", new Date(), HABIT_ID);
        // enter new values
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testEditHabit.getTitle());
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testEditHabit.getReason());
        enterCurrentDateInAddHabitDialogBox();
        clickConfirmButtonInAddHabitDialogBox();
        assertTextOnScreen(HABIT_LIST_TEXT);
        assertTextOnScreen(testEditHabit.getTitle());

        deleteTestHabit(testEditHabit);
    }

    @Test
    public void ensureHabitDeletedSuccessfully() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("deleteHabitTest", "Test Reason", new Date(), HABIT_ID);

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

        deleteTestHabit(testHabit);
    }

    private void deleteTestHabit(Habit habitToDelete) {
        swipeLeftOnHabit(habitToDelete);
        _solo.waitForView(R.id.delete_button);
        _solo.clickOnButton(DELETE_BUTTON);

        assertFalse(isTextOnScreen(habitToDelete.getTitle()));
    }

    private void swipeLeftOnHabit(Habit habitToDelete) {
        TextView view = _solo.getText(habitToDelete.getTitle());

        // locate row
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int displayWidth = _solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getWidth();

        int fromX = displayWidth - 10;
        int fromY = location[1];
        _solo.drag(fromX, 0, fromY, fromY, 10);
        _solo.drag(fromX, location[0], fromY, fromY, 10);
    }

    private void clickConfirmButtonInAddHabitDialogBox() {
        _solo.clickOnView(_solo.getView(R.id.confirm_habit));
    }

    private void enterCurrentDateInAddHabitDialogBox() {
        _solo.clickOnView(_solo.getView(R.id.habit_date_selection));
        _solo.clickOnText("OK");
    }

    private void setFieldInAddHabitDialogBox(String fieldToSet, String text) {
        _solo.enterText(_solo.getEditText(fieldToSet), text);
    }

    private void assertTextOnScreen(String text) {
        assertTrue(isTextOnScreen(text));
    }

    private boolean isTextOnScreen(String text) {
        return _solo.waitForText(text, 1, 2000);
    }
}
