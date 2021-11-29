package com.example.habitsmasher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used

 Note: The tests have been verified to run on Pixel 5 API 29
 */
public class MainActivityTest {
    private static final String HABIT_TITLE_FIELD = "Habit title";
    private static final String HABIT_REASON_FIELD = "Habit reason";
    private static final String HABIT_LIST_TEXT = "Habit List";
    private static final String HABIT_EVENT_COMMENT_FIELD = "Enter Comment...";
    private static final String WRONG_ACTIVITY_MESSAGE = "Wrong Activity";
    private static final String PROFILE_TEXT = "Profile";
    private static final boolean PUBLIC_HABIT = true;
    private static final boolean PRIVATE_HABIT = false;
    private static final String DATE_PATTERN = "EEE, d MMM yyyy";
    private static final String HABIT_TITLE_ERROR_MESSAGE = "Incorrect habit title entered";
    private static final String HABIT_REASON_ERROR_MESSAGE = "Incorrect habit reason entered";
    private static final String EMPTY_DATE_ERROR_MESSAGE = "Please enter a start date";
    private static final String NO_DAYS_SELECTED_ERROR_MESSAGE = "Please select a weekly schedule";
    private static final String NO_PRIVACY_BUTTONS_SELECTED_MESSAGE = "Invalid privacy choice";
    private static final String HABIT_EVENT_COMMENT_ERROR_MESSAGE = "Incorrect habit event comment entered";
    private static final String CANNOT_FOLLOW_YOURSELF_MESSAGE = "You cannot follow yourself!";
    private static final String THIS_USERNAME_IS_ALREADY_TAKEN_MESSAGE = "This username is already taken!";
    private static final String ALREADY_REQUESTED_TO_FOLLOW_USER_MESSAGE = "Already requested to follow that user";
    private static final String ALREADY_FOLLOWING_MESSAGE = "Already following that user!";
    private static final String EDIT_BUTTON = "EDIT";
    private static final HabitEventList EMPTY_HABIT_EVENT_LIST = new HabitEventList();
    private static final String DELETE_BUTTON = "DELETE";
    private static final String EDIT_HABIT_DIALOG = "Edit Habit";
    private static final String HABIT_ID = "1";
    private static final String VALID_EMAIL = "validEmail@gmail.com";
    private static final String VALID_USERNAME = "validUsername";
    private static final String VALID_PASSWORD = "validPwd";
    private static final String NEW_USER_ID = "123456789";
    private static final String SIGN_UP_TEXT = "Sign Up";
    private static final String INVALID_EMAIL = "badEmail123";
    private static final String USERNAME_IS_REQUIRED = "Username is required!";
    private static final String EMAIL_IS_REQUIRED = "Email is required!";
    private static final String PASSWORD_IS_REQUIRED = "Password is required!";
    private static final String INVALID_EMAIL_FORMAT = "Invalid email format!";
    private static final String INCORRECT_EMAIL_PASSWORD = "Incorrect email/password";
    private static final String LOGIN_TEXT = "Login";
    private static final String TEST_USER_ID = "TEST";
    private static final String TEST_USER_USERNAME = "TestUser";
    private static final String TEST_USER_REQUEST = "Requested";
    private static final String TEST_USER_FOLLOWED = "Followed";
    private static final String TEST_USER_EMAIL = "test@gmail.com";
    private static final String TEST_USER_PASSWORD = "123456";
    private static final String HABIT_EVENT_TEXT = "Habit Event";
    private static final String HABIT_EVENT_LIST_TEXT = "Habit Event List";
    private static final String FORGOT_PASSWORD_TEXTVIEW = "Forgot Password?";
    private static final String CONFIRM_BUTTON = "Confirm";
    private static final String EMAIL_SENT_MESSAGE = "Email sent, please check your email";
    private static final String UNREGISTERED_EMAIL = "unregisteredEmail@gmail.com";
    private static final String EMAIL_DOES_NOT_EXIST = "Entered email is not registered";
    private static final String EMPTY_USERNAME_ERROR_MESSAGE = "Please enter a username!";
    private static final String FOLLOW = "Follow";
    private static final String INVALID_USERNAME_ERROR_MESSAGE = "Please enter a valid username!";
    private static final String INVALID_USERNAME = "abcdefg";
    private static final String EDIT_HEADER = "EDIT LOCATION";
    private static final String FOLLOWER_USER_ID = "2DovMQknRoSeqVsangKbIU64RY93";
    private static final String FOLLOWER_USER_USERNAME = "follower";
    private static final String FOLLOWER_USER_EMAIL = "follower@gmail.com";
    private static final String FOLLOWER_USER_PASSWORD = "123456";
    private static final Long SAMPLE_SORT_INDEX = 0L;
    private static final String VIEW_PROFILE_USER = "publicTester";
    private ArrayList<String> EMPTY_FOLLOWING_LIST = new ArrayList<>();
    private ArrayList<String> EMPTY_FOLLOWER_LIST = new ArrayList<>();
    private ArrayList<String> EMPTY_REQUEST_LIST = new ArrayList<>();
    private static final String TEST_USER_SUBSTRING = "testUs";

    private Solo _solo;
    private User _testUser = new User(TEST_USER_ID, TEST_USER_USERNAME, TEST_USER_EMAIL,
                                      TEST_USER_PASSWORD, EMPTY_FOLLOWER_LIST, EMPTY_FOLLOWING_LIST,
                                      EMPTY_REQUEST_LIST);
    private User _follower = new User(FOLLOWER_USER_ID, FOLLOWER_USER_USERNAME, FOLLOWER_USER_EMAIL,
                                        FOLLOWER_USER_PASSWORD, EMPTY_FOLLOWER_LIST, EMPTY_FOLLOWING_LIST,
                                        EMPTY_REQUEST_LIST);

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
        logInTestUser();

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
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(PROFILE_TEXT);
    }

    @Test
    public void navigateToUserProfile_logOut(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Profile screen
        assertTextOnScreen(PROFILE_TEXT);

        // click on log out button
        _solo.clickOnView(_solo.getView(R.id.logout_button));

        // ensure login button is displayed
        assertTextOnScreen("Login");
    }

    @Test
    public void requestToFollowUserWithEmptyUsername(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Profile screen
        assertTextOnScreen(PROFILE_TEXT);

        // click follow user search button
        _solo.clickOnView(_solo.getView(R.id.follow_user_search_button));

        // click follow button
        _solo.clickOnButton(FOLLOW);

        // ensure proper error message displayed
        assertTextOnScreen(EMPTY_USERNAME_ERROR_MESSAGE);
    }

    @Test
    public void requestTofollowYourself_followFails(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Profile screen
        assertTextOnScreen(PROFILE_TEXT);

        // click follow user search button
        _solo.clickOnView(_solo.getView(R.id.follow_user_search_button));

        // enter an invalid username
        _solo.enterText(_solo.getEditText("Username"), TEST_USER_USERNAME);

        // click follow button
        _solo.clickOnButton(FOLLOW);

        // ensure proper error message displayed
        assertTextOnScreen(CANNOT_FOLLOW_YOURSELF_MESSAGE);
    }

    @Test
    public void requestToFollowUserWithInvalidUsername(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Profile screen
        assertTextOnScreen(PROFILE_TEXT);

        // click follow user search button
        _solo.clickOnView(_solo.getView(R.id.follow_user_search_button));

        // enter an invalid username
        _solo.enterText(_solo.getEditText("Username"), INVALID_USERNAME);

        // click follow button
        _solo.clickOnButton(FOLLOW);

        // ensure proper error message displayed
        assertTextOnScreen(INVALID_USERNAME_ERROR_MESSAGE);
    }

    @Test
    public void requestToFollowUserAlreadyRequested(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Profile screen
        assertTextOnScreen(PROFILE_TEXT);

        // click follow user search button
        _solo.clickOnView(_solo.getView(R.id.follow_user_search_button));

        // enter username of user already followed
        _solo.enterText(_solo.getEditText("Username"), TEST_USER_REQUEST);

        // click follow button
        _solo.clickOnButton(FOLLOW);

        // ensure proper error message displayed
        assertTextOnScreen(ALREADY_REQUESTED_TO_FOLLOW_USER_MESSAGE);
    }

    @Test
    public void requestToFollowUserAlreadyFollowed(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Profile screen
        assertTextOnScreen(PROFILE_TEXT);

        // click follow user search button
        _solo.clickOnView(_solo.getView(R.id.follow_user_search_button));

        // enter username of user already followed
        _solo.enterText(_solo.getEditText("Username"), TEST_USER_FOLLOWED);

        // click follow button
        _solo.clickOnButton(FOLLOW);

        // ensure proper error message displayed
        assertTextOnScreen(ALREADY_FOLLOWING_MESSAGE);
    }

    @Test
    public void navigateToHabitViewFragment() {
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("viewHabitTest", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

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

        // Check that the days are correct
        assertCorrectDays();

        //check the privacy is right
        assertCorrectPrivacy(testHabit.getPublic(), PUBLIC_HABIT);

        // Click up/back button
        _solo.goBack();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAddedSuccessfully() {
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitSuccessTest", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitOnHomeScreen(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("habitHomeScreenTest", "Test Reason", new Date(), getCurrentDay(), PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select today
        setCurrentDayInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        // Go to home screen
        _solo.clickOnView(_solo.getView(R.id.navigation_home));

        // Assert that the habit is there
        assertTextOnScreen(testHabit.getTitle());

        // Go back to list tab
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // Delete the habit
        deleteTestHabit(testHabit);

    }

    @Test
    public void ensureHabitNotOnHomeScreen(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        String notToday;
        if (getCurrentDay().equals("WE")){
            notToday = "TH";
        }
        else{
            notToday = "WE";
        }

        // Create test habit
        Habit testHabit = new Habit("noHomeScreenTest", "Test Reason", new Date(), notToday, PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setNonCurrentDayInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        // Go to home screen
        _solo.clickOnView(_solo.getView(R.id.navigation_home));

        // Assert that the habit is NOT there
        assertTextNotOnScreen(testHabit.getTitle());

        // Go back to list tab
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // Delete the habit
        deleteTestHabit(testHabit);

    }

    @Test
    public void ensureHabitAdditionFails_emptyTitle() {
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitEmptyTitle", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

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
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("ExampleHabitTitleThatIsTooLong", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

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
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitReasonLong", "AnExampleHabitReasonThatIsTooLong", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

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
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitEmptyReason", "", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

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
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitEmptyDate", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

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

    @Test
    public void ensureHabitAdditionFails_NoDays(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("addHabitNoDays", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check for toast message
        assertTextOnScreen(NO_DAYS_SELECTED_ERROR_MESSAGE);

        // Select days
        setDaysInAddHabitDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();


        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        deleteTestHabit(testHabit);

    }

    @Test
    public void ensurePublicHabitAdditionCorrect(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);


        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create public test habit
        Habit testHabit = new Habit("publicHabitCheck", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Select Public (public by default, leaving single fragment for consistancy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        //get the habit we just made
        _solo.clickOnText(testHabit.getTitle());

        // assert that the correct buttons are selected, not just the underlying habit
        CheckBox publicBox = (CheckBox) _solo.getView(R.id.public_button);
        CheckBox privateBox = (CheckBox) _solo.getView(R.id.private_button);
        assertTrue(publicBox.isChecked());
        assertFalse(privateBox.isChecked());

        _solo.goBack();

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensurePrivateHabitAdditionCorrect(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);


        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create public test habit
        Habit testHabit = new Habit("privateHabitCheck", "Test Reason", new Date(), "MO WE FR", PRIVATE_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // select private
        setPrivacyInAddHabitDialogBox(PRIVATE_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        //get the habit we just made
        _solo.clickOnText(testHabit.getTitle());

        //get the checkboxes to check
        CheckBox publicBox = (CheckBox) _solo.getView(R.id.public_button);
        CheckBox privateBox = (CheckBox) _solo.getView(R.id.private_button);

        // assert that the correct buttons are selected, not just the underlying habit
        assertTrue(privateBox.isChecked());
        assertFalse(publicBox.isChecked());

        _solo.goBack();

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAdditionFails_NoPrivacyButtonsSelected(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);


        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create public test habit
        Habit testHabit = new Habit("noPrivacyHabitCheck", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Click confirm (no privacy buttons clicked)
        clickConfirmButtonInAddHabitDialogBox();

        // check for the error message
        assertTextOnScreen(NO_PRIVACY_BUTTONS_SELECTED_MESSAGE);

        // Select Public
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        //get the habit we just made
        _solo.clickOnText(testHabit.getTitle());

        // assert that the correct buttons are selected, not just the underlying habit
        CheckBox publicBox = (CheckBox) _solo.getView(R.id.public_button);
        CheckBox privateBox = (CheckBox) _solo.getView(R.id.private_button);
        assertTrue(publicBox.isChecked());
        assertFalse(privateBox.isChecked());

        _solo.goBack();

        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitAdditionFails_BothPrivacyButtonsSelected(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);


        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create public test habit
        Habit testHabit = new Habit("bothPrivacyCheck", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Select both public and private
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);
        setPrivacyInAddHabitDialogBox(PRIVATE_HABIT);

        // Click confirm (Both buttons clicked)
        clickConfirmButtonInAddHabitDialogBox();

        // check for the error message
        assertTextOnScreen(NO_PRIVACY_BUTTONS_SELECTED_MESSAGE);

        // deselect private box
        setPrivacyInAddHabitDialogBox(PRIVATE_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        //get the habit we just made
        _solo.clickOnText(testHabit.getTitle());

        // assert that the correct buttons are selected, not just the underlying habit
        CheckBox publicBox = (CheckBox) _solo.getView(R.id.public_button);
        CheckBox privateBox = (CheckBox) _solo.getView(R.id.private_button);
        assertTrue(publicBox.isChecked());
        assertFalse(privateBox.isChecked());

        _solo.goBack();

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
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("editHabitTest", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        swipeLeftOnHabit(testHabit);
        _solo.waitForView(R.id.edit_habit_event_button);
        _solo.clickOnButton(EDIT_BUTTON);

        // wait for edit habit dialog to spawn after edit is clicked
        _solo.waitForText(EDIT_HABIT_DIALOG, 1, 5000);

        // clear Edit Text fields
        _solo.clearEditText(0);
        _solo.clearEditText(1);

        Habit testEditHabit = new Habit("editHabitWorked", "testReason1", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);
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
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("deleteHabitTest", "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        deleteTestHabit(testHabit);
    }

    @Test
    public void navigateToHabitEventsList() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("ViewEventsTest");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View addHabitEventButton = _solo.getView(R.id.add_habit_event_fab);
        assertNotNull(addHabitEventButton);

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventAddedSuccessfullyWithoutLocation() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("AddEventNoLocTest");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventAddedSuccessfullyWithLocation() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("AddEventWithLocation");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click on enter location button
        clickOnAddLocation();

        // need timeout since google map snippet doesnt load immedatiely
        _solo.sleep(5000);

        // click confirm for map dialog box
        clickOnConfirmMapDialog();

        // Assert that location header has changed
        assertTextOnScreen(EDIT_HEADER);

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventAddedSuccessfullyFromHomeScreenWithoutLocation(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("habitEventNoLoc", "Test Reason", new Date(), getCurrentDay(), PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select today
        setCurrentDayInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        // Go to home screen
        _solo.clickOnView(_solo.getView(R.id.navigation_home));

        // Assert that the habit is there
        assertTextOnScreen(testHabit.getTitle());

        // Click on the add event button
        swipeLeftOnHabit(testHabit);
        _solo.clickOnView(_solo.getView(R.id.home_add_event_button));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();

        // Go back to list tab
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // Delete the habit
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventAddedSuccessfullyFromHomeScreenWithLocation(){
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit("homeScreenEventLoc", "Test Reason", new Date(), getCurrentDay(), PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select today
        setCurrentDayInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_LIST_TEXT);

        // Ensure added Habit is present in the list
        assertTextOnScreen(testHabit.getTitle());

        // Go to home screen
        _solo.clickOnView(_solo.getView(R.id.navigation_home));

        // Assert that the habit is there
        assertTextOnScreen(testHabit.getTitle());

        // Click on the add event button
        swipeLeftOnHabit(testHabit);
        _solo.clickOnView(_solo.getView(R.id.home_add_event_button));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // click on add location button
        clickOnAddLocation();

        // Needed so map snippet can load
        _solo.sleep(5000);

        // confirm map dialog button
        clickOnConfirmMapDialog();

        // assert header changed
        assertTextOnScreen(EDIT_HEADER);

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();

        // Go back to list tab
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // Delete the habit
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventFails_emptyComment() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("emptyComEventTest");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that the error message is displayed
        assertTextOnScreen(HABIT_EVENT_COMMENT_ERROR_MESSAGE);

        // Add habit comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventFails_commentTooLong() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("LongCommentEventTest");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment that is too long, and too many characters", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that the error message is displayed
        assertTextOnScreen(HABIT_EVENT_COMMENT_ERROR_MESSAGE);

        // Change habit comment
        _solo.clearEditText(_solo.getEditText(testEvent.getComment()));
        testEvent.setComment("ShorterComment");
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventFails_dateEmpty() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("EmptyDateEventTest");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that the error message is displayed
        assertTextOnScreen(EMPTY_DATE_ERROR_MESSAGE);

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventDeletedSuccessfully() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("DeleteEventTest");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Test deleting the habit event
        deleteTestHabitEvent(testEvent);

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventEditedSuccessfully_changeDate() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("EditDateEvent");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Click on edit button
        swipeLeftOnHabitEvent(testEvent);
        _solo.waitForView(R.id.edit_habit_event_button);
        _solo.clickOnButton(EDIT_BUTTON);

        // Check that dialog opens
        assertTextOnScreen("Edit Habit Event");
        enterCurrentDateInAddHabitEventDialogBox();
        clickConfirmButtonInAddHabitEventDialogBox();

        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventEditedSuccessfully_changeComment() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("EditCommentEvent");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Click on edit button
        swipeLeftOnHabitEvent(testEvent);
        _solo.waitForView(R.id.edit_habit_event_button);
        _solo.clickOnButton(EDIT_BUTTON);

        // Check that dialog opens
        assertTextOnScreen("Edit Habit Event");

        // Change comment
        _solo.clearEditText(_solo.getEditText(testEvent.getComment()));
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, "New comment!");
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that new comment is displayed
        assertTextOnScreen("New comment!");

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventEditedSuccessfully_changeLocation() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("EditLocationEvent");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Click on edit button
        swipeLeftOnHabitEvent(testEvent);
        _solo.waitForView(R.id.edit_habit_event_button);
        _solo.clickOnButton(EDIT_BUTTON);

        // Check that dialog opens
        assertTextOnScreen("Edit Habit Event");

        // Click on location button
        clickOnAddLocation();

        // Wait for map snippets to load
        _solo.sleep(5000);

        clickOnConfirmMapDialog();

        assertTextOnScreen(EDIT_HEADER);

        clickConfirmButtonInAddHabitEventDialogBox();

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void ensureHabitEventEditedSuccessfully_changeLocationWhenLocationAlreadySelected() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("EditLocationNewEvent");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click on location button
        clickOnAddLocation();

        // Wait for map snippets to load
        _solo.sleep(5000);

        clickOnConfirmMapDialog();

        assertTextOnScreen(EDIT_HEADER);

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Click on edit button
        swipeLeftOnHabitEvent(testEvent);
        _solo.waitForView(R.id.edit_habit_event_button);
        _solo.clickOnButton(EDIT_BUTTON);

        // Check that dialog opens
        assertTextOnScreen("Edit Habit Event");

        // click on edit location button
        clickOnEditLocation();

        // Wait for map snippets to load
        _solo.sleep(5000);

        clickOnConfirmMapDialog();

        assertTextOnScreen(EDIT_HEADER);

        clickConfirmButtonInAddHabitEventDialogBox();

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void navigateToHabitEventViewFragment() {
        logInTestUser();

        // Navigate to view habit
        Habit testHabit = goToViewHabit("viewEventTest");

        // Click on history button
        _solo.clickOnView(_solo.getView(R.id.eventHistoryButton));

        // Check that habit event list fragment is displaying
        View fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on add habit event floating action button to add habit event
        _solo.clickOnView(_solo.getView(R.id.add_habit_event_fab));

        // Create test habit event
        HabitEvent testEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        // Enter comment
        setFieldInAddHabitDialogBox(HABIT_EVENT_COMMENT_FIELD, testEvent.getComment());

        // Enter date
        enterCurrentDateInAddHabitEventDialogBox();

        // Click confirm
        clickConfirmButtonInAddHabitEventDialogBox();

        // Check that habit event list fragment is displaying
        fragment = _solo.getView(R.id.habit_event_list_container);
        assertNotNull(fragment);

        // Click on habit event
        _solo.clickOnText(testEvent.getComment());

        // Ensure that event page is present
        assertTextOnScreen(HABIT_EVENT_TEXT);

        // Check that Comment is correct
        assertTextOnScreen(testEvent.getComment());

        // Check that the date is correct
        assertTextOnScreen(new SimpleDateFormat(DATE_PATTERN).format(testEvent.getDate()));

        // Click up/back button
        _solo.goBack();

        // Check that the current fragment is the habit list
        assertTextOnScreen(HABIT_EVENT_LIST_TEXT);

        // Ensure added habit event is present in the list
        assertTextOnScreen(testEvent.getComment());

        // Go back and delete habit
        _solo.goBack();
        _solo.goBack();
        deleteTestHabit(testHabit);
    }

    @Test
    public void navigateToFollower(){
        logInTestUser();

        // go to the profile
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // click on the followers button
        _solo.clickOnView(_solo.getView(R.id.number_followers));

        // click on the test user
        _solo.clickOnText(VIEW_PROFILE_USER);

        // check that their username is on the screen
        assertTextOnScreen(VIEW_PROFILE_USER);

        //assert that their public habit is on the screen
        assertTextOnScreen("Public Habit");

        //assert that their private habit is not on the screen
        assertTextNotOnScreen("Private Habit");

        // click on the public habit
        _solo.clickOnText("Public Habit");

        // assert that the habit name and privacy are correct
        assertTextOnScreen("Public Habit");
        CheckBox publicBox = (CheckBox) _solo.getView(R.id.public_button);
        CheckBox privateBox = (CheckBox) _solo.getView(R.id.private_button);
        assertTrue(publicBox.isChecked());
        assertFalse(privateBox.isChecked());



    }

    @Test
    public void navigateToFollowing(){
        logInTestUser();

        // go to the profile
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // click on the followers button
        _solo.clickOnView(_solo.getView(R.id.number_following));

        // click on the test user
        _solo.clickOnText(VIEW_PROFILE_USER);

        // check that their username is on the screen
        assertTextOnScreen(VIEW_PROFILE_USER);

        //assert that their public habit is on the screen
        assertTextOnScreen("Public Habit");

        //assert that their private habit is not on the screen
        assertTextNotOnScreen("Private Habit");

        // click on the public habit
        _solo.clickOnText("Public Habit");

        // assert that the habit name and privacy are correct
        assertTextOnScreen("Public Habit");
        CheckBox publicBox = (CheckBox) _solo.getView(R.id.public_button);
        CheckBox privateBox = (CheckBox) _solo.getView(R.id.private_button);
        assertTrue(publicBox.isChecked());
        assertFalse(privateBox.isChecked());

    }

    @Test
    public void signUpNewUser_emptyUsername_signUpFails() {
        User newUser = new User(NEW_USER_ID, "", VALID_EMAIL, VALID_PASSWORD, EMPTY_FOLLOWER_LIST,
                                EMPTY_FOLLOWING_LIST, EMPTY_REQUEST_LIST);

        _solo.clickOnButton(SIGN_UP_TEXT);

        _solo.enterText(_solo.getEditText("Username"), newUser.getUsername());
        _solo.enterText(_solo.getEditText("Email"), newUser.getEmail());
        _solo.enterText(_solo.getEditText("Password"), newUser.getPassword());

        _solo.clickOnButton(SIGN_UP_TEXT);

        assertTextOnScreen(USERNAME_IS_REQUIRED);
    }

    @Test
    public void signUpNewUser_emptyEmail_signUpFails() {
        User newUser = new User(NEW_USER_ID, VALID_USERNAME, "", VALID_PASSWORD,
                                EMPTY_FOLLOWER_LIST, EMPTY_FOLLOWING_LIST,
                                EMPTY_REQUEST_LIST);

        _solo.clickOnButton(SIGN_UP_TEXT);

        _solo.enterText(_solo.getEditText("Username"), newUser.getUsername());
        _solo.enterText(_solo.getEditText("Email"), newUser.getEmail());
        _solo.enterText(_solo.getEditText("Password"), newUser.getPassword());

        _solo.clickOnButton(SIGN_UP_TEXT);

        assertTextOnScreen(EMAIL_IS_REQUIRED);
    }

    @Test
    public void signUpNewUser_emptyPassword_signUpFails() {
        User newUser = new User(NEW_USER_ID, VALID_USERNAME, VALID_EMAIL, "",
                                EMPTY_FOLLOWER_LIST, EMPTY_FOLLOWING_LIST,
                                EMPTY_REQUEST_LIST);

        _solo.clickOnButton(SIGN_UP_TEXT);

        _solo.enterText(_solo.getEditText("Username"), newUser.getUsername());
        _solo.enterText(_solo.getEditText("Email"), newUser.getEmail());
        _solo.enterText(_solo.getEditText("Password"), newUser.getPassword());

        _solo.clickOnButton(SIGN_UP_TEXT);

        assertTextOnScreen(PASSWORD_IS_REQUIRED);
    }

    @Test
    public void signUpNewUser_usernameExists_emailExists_signUpFails() {
        User newUser = new User(TEST_USER_ID, TEST_USER_USERNAME, TEST_USER_EMAIL, VALID_PASSWORD,
                                EMPTY_FOLLOWER_LIST, EMPTY_FOLLOWING_LIST,
                                EMPTY_REQUEST_LIST);

        _solo.clickOnButton(SIGN_UP_TEXT);

        _solo.enterText(_solo.getEditText("Username"), newUser.getUsername());
        _solo.enterText(_solo.getEditText("Email"), newUser.getEmail());
        _solo.enterText(_solo.getEditText("Password"), newUser.getPassword());

        _solo.clickOnButton(SIGN_UP_TEXT);

        assertTextOnScreen(THIS_USERNAME_IS_ALREADY_TAKEN_MESSAGE);
    }

    @Test
    public void signUpNewUser_usernameExists_newEmail_signUpFails() {
        User newUser = new User(TEST_USER_ID, TEST_USER_USERNAME, "newemail@gmail.com", VALID_PASSWORD,
                                EMPTY_FOLLOWER_LIST, EMPTY_FOLLOWING_LIST,
                                EMPTY_REQUEST_LIST);

        _solo.clickOnButton(SIGN_UP_TEXT);

        _solo.enterText(_solo.getEditText("Username"), newUser.getUsername());
        _solo.enterText(_solo.getEditText("Email"), newUser.getEmail());
        _solo.enterText(_solo.getEditText("Password"), newUser.getPassword());

        _solo.clickOnButton(SIGN_UP_TEXT);

        assertTextOnScreen(THIS_USERNAME_IS_ALREADY_TAKEN_MESSAGE);
    }

    @Test
    public void loginTestUser_invalidEmail_loginFails() {
        _solo.enterText(_solo.getEditText("Email"), INVALID_EMAIL);
        _solo.enterText(_solo.getEditText("Password"), _testUser.getPassword());

        _solo.clickOnButton(LOGIN_TEXT);

        assertTextOnScreen(INVALID_EMAIL_FORMAT);
    }

    @Test
    public void loginTestUser_emptyEmail_loginFails() {
        _solo.enterText(_solo.getEditText("Email"), "");
        _solo.enterText(_solo.getEditText("Password"), _testUser.getPassword());

        _solo.clickOnButton(LOGIN_TEXT);

        assertTextOnScreen(EMAIL_IS_REQUIRED);
    }

    @Test
    public void loginTestUser_invalidPassword_loginFails() {
        _solo.enterText(_solo.getEditText("Email"), _testUser.getEmail());
        _solo.enterText(_solo.getEditText("Password"), "wrongPassword");

        _solo.clickOnButton(LOGIN_TEXT);

        assertTextOnScreen(INCORRECT_EMAIL_PASSWORD);
    }

    @Test
    public void loginTestUser_emptyPassword_loginFails() {
        _solo.enterText(_solo.getEditText("Email"), _testUser.getEmail());
        _solo.enterText(_solo.getEditText("Password"), "");

        _solo.clickOnButton(LOGIN_TEXT);

        assertTextOnScreen(PASSWORD_IS_REQUIRED);
    }

    //Accessing edit text came from the following link:
    //https://stackoverflow.com/a/40652532
    //Author username: Motsane M
    @Test
    public void forgotPassword_emptyEmail_emailNotSent() {
        _solo.clickOnText(FORGOT_PASSWORD_TEXTVIEW);
        EditText emailEditText = (EditText) _solo.getView(R.id.reset_password_dialog);
        _solo.enterText(emailEditText, "");

        _solo.clickOnButton(CONFIRM_BUTTON);
        assertTextOnScreen(EMAIL_IS_REQUIRED);
    }

    @Test
    public void forgotPassword_invalidEmail_emailNotSent() {
        _solo.clickOnText(FORGOT_PASSWORD_TEXTVIEW);
        EditText emailEditText = (EditText) _solo.getView(R.id.reset_password_dialog);
        _solo.enterText(emailEditText, INVALID_EMAIL);

        _solo.clickOnButton(CONFIRM_BUTTON);

        assertTextOnScreen(INVALID_EMAIL_FORMAT);
    }

    @Test
    public void forgotPassword_validEmailRegistered_emailSent() {
        _solo.clickOnText(FORGOT_PASSWORD_TEXTVIEW);
        EditText emailEditText = (EditText) _solo.getView(R.id.reset_password_dialog);
        _solo.enterText(emailEditText, TEST_USER_EMAIL);

        _solo.clickOnButton(CONFIRM_BUTTON);

        assertTextOnScreen(EMAIL_SENT_MESSAGE);
    }

    @Test
    public void forgotPassword_validEmailNotRegistered_emailNotSent() {
        _solo.clickOnText(FORGOT_PASSWORD_TEXTVIEW);
        EditText emailEditText = (EditText) _solo.getView(R.id.reset_password_dialog);
        _solo.enterText(emailEditText, UNREGISTERED_EMAIL);

        _solo.clickOnButton(CONFIRM_BUTTON);

        assertTextOnScreen(EMAIL_DOES_NOT_EXIST);
    }

    @Test
    public void autoCompleteTextView_PartOfValidUsername_usernameSelected() {
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Profile screen
        assertTextOnScreen(PROFILE_TEXT);

        // click follow user search button
        _solo.clickOnView(_solo.getView(R.id.follow_user_search_button));

        // click on the auto complete text view
        _solo.clickOnView(_solo.getView(R.id.auto_complete_text_view));

        // enter part of the users username we wish to follow
        _solo.enterText(_solo.getEditText("Username"), TEST_USER_SUBSTRING);

        // click on the user name that we wish to follow
        // Click on text does not work for item in autoCompleteTextView
        // so I decided to go with click on screen at a specific location
        _solo.clickOnScreen(540, 1170);

        _solo.clickOnButton(FOLLOW);
        assertTextOnScreen(CANNOT_FOLLOW_YOURSELF_MESSAGE);

    }

    /**
     * Ensure unfollow works
     */
    @Test
    public void ensureUnfollowWorks(){
        // log in to follower user
        logInFollowerUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(PROFILE_TEXT);

        // click follow user search button
        _solo.clickOnView(_solo.getView(R.id.follow_user_search_button));

        // enter username of test user
        _solo.enterText(_solo.getEditText("Username"), TEST_USER_USERNAME);

        // click follow button
        _solo.clickOnButton(FOLLOW);

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(PROFILE_TEXT);

        // click on log out button
        _solo.clickOnView(_solo.getView(R.id.logout_button));

        // ensure HabitSmasher is displayed
        assertTextOnScreen("HabitSmasher");

        // login to test user
        logInTestUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // press notifications button
        _solo.clickOnView(_solo.getView(R.id.notifications_button));

        // swipe on follow request
        swipeLeftOnFollowRequest(_follower);

        // accept follow request
        _solo.clickOnView(_solo.getView(R.id.accept_request_button));

        _solo.goBack();

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(PROFILE_TEXT);

        // make sure number of followers is 1
        TextView followers = (TextView) _solo.getView(R.id.number_followers);
        assertEquals("1", followers.getText().toString());

        // click on followers button
        _solo.clickOnView(followers);

        // check that the test user is being followed
        assertTextOnScreen(FOLLOWER_USER_USERNAME);

        _solo.goBack();

        // click on log out button
        _solo.clickOnView(_solo.getView(R.id.logout_button));

        // ensure HabitSmasher is displayed
        assertTextOnScreen("HabitSmasher");

        // log in to follower user
        logInFollowerUser();

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Notifications tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_notifications));

        // ensure that the app has transitioned to the Notifications screen
        assertTextOnScreen(PROFILE_TEXT);

        // make sure number of following is 1
        TextView following = (TextView) _solo.getView(R.id.number_following);
        assertEquals("1", following.getText().toString());

        // click on following button
        _solo.clickOnView(following);

        // check that the test user is being followed
        assertTextOnScreen(TEST_USER_USERNAME);

        // swipe on followed user
        swipeLeftOnFollowing(_testUser);

        // unfollow user
        _solo.clickOnView(_solo.getView(R.id.unfollow_button));

        // make sure unfollowed user is gone
        assertTextNotOnScreen(TEST_USER_USERNAME);

        _solo.goBack();

        // make sure number of following is 0
        following = (TextView) _solo.getView(R.id.number_following);
        assertEquals("0", following.getText().toString());
    }

    private void deleteTestHabit(Habit habitToDelete) {
        swipeLeftOnHabit(habitToDelete);
        _solo.waitForView(R.id.delete_habit_event_button);
        _solo.clickOnButton(DELETE_BUTTON);

        assertFalse(isTextOnScreen(habitToDelete.getTitle()));
    }

    private void deleteTestHabitEvent(HabitEvent eventToDelete) {
        swipeLeftOnHabitEvent(eventToDelete);


        _solo.waitForView(R.id.delete_habit_event_button);


        _solo.clickOnButton(DELETE_BUTTON);

        assertFalse(isTextOnScreen(eventToDelete.getComment()));
    }

    private void swipeLeftOnHabit(Habit habitToDelete) {
        TextView view = _solo.getText(habitToDelete.getTitle());

        // locate row
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        // larger padding from righthand side of screen to ensure swipe functions
        int displayWidth = _solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getWidth();
        int fromX = displayWidth - 100;
        int fromY = location[1];

        // 0 so Robotium swipes to leftmost side of screen
        _solo.drag(fromX, 0, fromY, fromY, 10);
    }

    private void swipeLeftOnHabitEvent(HabitEvent eventToDelete) {
        TextView view = _solo.getText(eventToDelete.getComment());

        // locate row
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        // larger padding from righthand side of screen to ensure swipe functions
        int displayWidth = _solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getWidth();
        int fromX = displayWidth - 100;
        int fromY = location[1];

        // 0 so Robotium swipes to leftmost side of screen
        _solo.drag(fromX, 0, fromY, fromY, 10);
    }

    private void swipeLeftOnFollowing(User userToUnfollow) {
        TextView view = _solo.getText(userToUnfollow.getUsername());

        // locate row
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        // larger padding from righthand side of screen to ensure swipe functions
        int displayWidth = _solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getWidth();
        int fromX = displayWidth - 100;
        int fromY = location[1];

        // 0 so Robotium swipes to leftmost side of screen
        _solo.drag(fromX, 0, fromY, fromY, 10);
    }

    private void swipeLeftOnFollowRequest(User userWithRequest) {
        TextView view = _solo.getText(userWithRequest.getUsername());

        // locate row
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        // larger padding from righthand side of screen to ensure swipe functions
        int displayWidth = _solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getWidth();
        int fromX = displayWidth - 100;
        int fromY = location[1];

        // 0 so Robotium swipes to leftmost side of screen
        _solo.drag(fromX, 0, fromY, fromY, 10);
    }


    private Habit goToViewHabit(String testName) {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        _solo.assertCurrentActivity(WRONG_ACTIVITY_MESSAGE, MainActivity.class);

        // click on the Habit List tab in the bottom navigation bar
        _solo.clickOnView(_solo.getView(R.id.navigation_dashboard));

        // click on add habit floating action button to add habit
        _solo.clickOnView(_solo.getView(R.id.add_habit_fab));

        // Create test habit
        Habit testHabit = new Habit(testName, "Test Reason", new Date(), "MO WE FR", PUBLIC_HABIT, HABIT_ID, EMPTY_HABIT_EVENT_LIST, SAMPLE_SORT_INDEX);

        // Enter title
        setFieldInAddHabitDialogBox(HABIT_TITLE_FIELD, testHabit.getTitle());

        // Enter reason
        setFieldInAddHabitDialogBox(HABIT_REASON_FIELD, testHabit.getReason());

        // Enter date
        enterCurrentDateInAddHabitDialogBox();

        // Select days
        setDaysInAddHabitDialogBox();

        // Set the privacy
        setPrivacyInAddHabitDialogBox(PUBLIC_HABIT);

        // Click confirm
        clickConfirmButtonInAddHabitDialogBox();

        // Click on added Habit
        _solo.clickOnText(testHabit.getTitle());

        return testHabit;
    }

    private void clickConfirmButtonInAddHabitDialogBox() {
        _solo.clickOnView(_solo.getView(R.id.confirm_habit));
    }

    private void clickConfirmButtonInAddHabitEventDialogBox() {
        _solo.clickOnView(_solo.getView(R.id.confirm_habit_event));
    }

    private void enterCurrentDateInAddHabitDialogBox() {
        _solo.clickOnView(_solo.getView(R.id.habit_date_selection));
        _solo.clickOnText("OK");
    }

    private void enterCurrentDateInAddHabitEventDialogBox() {
        _solo.clickOnView(_solo.getView(R.id.habit_event_date_selection));
        _solo.clickOnText("OK");
    }

    private void setFieldInAddHabitDialogBox(String fieldToSet, String text) {
        _solo.enterText(_solo.getEditText(fieldToSet), text);
    }

    private void setDaysInAddHabitDialogBox(){
        _solo.clickOnView(_solo.getView(R.id.monday_button));
        _solo.clickOnView(_solo.getView(R.id.wednesday_button));
        _solo.clickOnView(_solo.getView(R.id.friday_button));
    }

    private void setCurrentDayInAddHabitDialogBox(){
        String currentDay = getCurrentDay();

        if (currentDay.equals("MO")){
            _solo.clickOnView(_solo.getView(R.id.monday_button));
        }
        else if (currentDay.equals("TU")){
            _solo.clickOnView(_solo.getView(R.id.tuesday_button));
        }
        else if (currentDay.equals("WE")){
            _solo.clickOnView(_solo.getView(R.id.wednesday_button));
        }
        else if (currentDay.equals("TH")){
            _solo.clickOnView(_solo.getView(R.id.thursday_button));
        }
        else if (currentDay.equals("FR")){
            _solo.clickOnView(_solo.getView(R.id.friday_button));
        }
        else if (currentDay.equals("SA")){
            _solo.clickOnView(_solo.getView(R.id.saturday_button));
        }
        else if (currentDay.equals("SU")){
            _solo.clickOnView(_solo.getView(R.id.sunday_button));
        }
    }

    private void setNonCurrentDayInAddHabitDialogBox(){
        if (getCurrentDay().equals("WE")){
            _solo.clickOnView(_solo.getView(R.id.thursday_button));
        }
        else{
            _solo.clickOnView(_solo.getView(R.id.wednesday_button));
        }
    }

    private void setPrivacyInAddHabitDialogBox(boolean privacy){
        if (privacy) {
            _solo.clickOnView(_solo.getView(R.id.public_button));
        } else {
            _solo.clickOnView(_solo.getView(R.id.private_button));
        }
    }

    private String getCurrentDay() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getDayOfWeek().toString().toUpperCase().substring(0, 2);
    }

    private void checkForToastMessage(String errorMessage) {
        assertTrue(_solo.searchText(errorMessage));
    }

    private void assertTextOnScreen(String text) {
        assertTrue(isTextOnScreen(text));
    }

    private void assertTextNotOnScreen(String text){ assertFalse(isTextOnScreen(text));}

    private boolean isTextOnScreen(String text) {
        return _solo.waitForText(text, 1, 2000);
    }

    private void assertCorrectDays(){
        // I'm not entirely sure how to check the state of a button nor get the drawable without context, so
        // my workaround checks the drawable of the buttons compared to each other, which change based on whether they're checked.
        assertNotEquals(_solo.getView(R.id.tuesday_button).getBackground(),_solo.getView(R.id.monday_button).getBackground());
        assertNotEquals(_solo.getView(R.id.wednesday_button).getBackground(), _solo.getView(R.id.thursday_button).getBackground());
        assertNotEquals(_solo.getView(R.id.friday_button).getBackground(), _solo.getView(R.id.saturday_button).getBackground());
        assertNotEquals(_solo.getView(R.id.sunday_button).getBackground(), _solo.getView(R.id.monday_button).getBackground());

        // If you include these lines, you'll see that each button has the same background but different hex code, meaning
        // Different states. This gives the difference between states.
        //Log.d("Monday Background", _solo.getView(R.id.monday_button).getBackground().toString());
        //Log.d("Tuesday Background", _solo.getView(R.id.tuesday_button).getBackground().toString());
    }

    private void assertCorrectPrivacy(boolean privacy, boolean expected_Privacy){
        //assert the habit is correctly labeled as private or public
        assertEquals(privacy, expected_Privacy);
    }

    private void logInTestUser() {
        _solo.enterText(_solo.getEditText("Email"), _testUser.getEmail());
        _solo.enterText(_solo.getEditText("Password"), _testUser.getPassword());
        _solo.clickOnButton(LOGIN_TEXT);
        // Wait for Profile fragment to load
        _solo.waitForFragmentById(R.id.navigation_notifications, 4000);
    }

    private void logInFollowerUser() {
        _solo.enterText(_solo.getEditText("Email"), FOLLOWER_USER_EMAIL);
        _solo.enterText(_solo.getEditText("Password"), FOLLOWER_USER_PASSWORD);
        _solo.clickOnButton(LOGIN_TEXT);
        // Wait for Profile fragment to load
        _solo.waitForFragmentById(R.id.navigation_notifications, 4000);
    }

    private void clickOnAddLocation() {
        _solo.clickOnView(_solo.getView(R.id.add_location_button));
    }

    private void clickOnConfirmMapDialog() {
        _solo.clickOnView(_solo.getView(R.id.confirm_map));
    }

    private void clickOnEditLocation() {
        _solo.clickOnView(_solo.getView(R.id.edit_location_button));
    }
}
