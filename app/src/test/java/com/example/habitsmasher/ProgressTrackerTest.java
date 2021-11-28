package com.example.habitsmasher;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.Date;
import java.util.UUID;

public class ProgressTrackerTest {
    private ProgressTracker _progressTracker;
    private Habit _parentHabit;
    private static final String SAMPLE_DAYS_OF_THE_WEEK = "MO WE FR";
    private static final Date SAMPLE_DATE_NOT_IN_DAYS_OF_WEEK = new Date(2021, 11, 27);
    private static final Date SAMPLE_DATE_IN_DAYS_OF_WEEK = new Date(2021, 11, 26);
    private static final Date SECOND_SAMPLE_DATE_IN_DAYS_OF_WEEK = new Date(2021, 11, 25);
    private static final Date THIRD_SAMPLE_DATE_IN_DAYS_OF_WEEK = new Date(2021, 11, 24);
    private static final Date SAMPLE_START_DATE = new Date(2021, 11, 20);
    private static final String SAMPLE_HABIT_TITLE = "Test Habit";
    private static final String SAMPLE_HABIT_REASON = "Test Reason";
    private final Date DATE_TODAY = new Date();
    private static final Boolean PUBLIC_HABIT = true;
    private static final String SAMPLE_HABIT_ID = UUID.randomUUID().toString();
    private static final String SAMPLE_HABIT_EVENT_ID = UUID.randomUUID().toString();
    private HabitEventList EMPTY_HABIT_EVENT_LIST = new HabitEventList();
    private static final String ALL_DAYS_OF_WEEK = "MO TU WE TH FR SA SU";
    private static final String SAMPLE_EVENT_COMMENT = "Test Comment";


    @Before
    public void setUp() {
        _parentHabit = new Habit(SAMPLE_HABIT_TITLE, SAMPLE_HABIT_REASON, DATE_TODAY, SAMPLE_DAYS_OF_THE_WEEK, PUBLIC_HABIT, SAMPLE_HABIT_ID, EMPTY_HABIT_EVENT_LIST);
    }

    @Test
    public void calculateProgress_noViableDays_expect0() {
        // Create date that is not viable
        _parentHabit.setDate(SAMPLE_DATE_NOT_IN_DAYS_OF_WEEK);

        // Create progress tracker
        _progressTracker = new ProgressTracker(_parentHabit);
        assertEquals(0, (int) _progressTracker.calculateProgressPercentage());

    }

    @Test
    public void calculateProgress_noEventsOnDays_expect0() {
        // Set date that is viable, but do not add events
        _parentHabit.setDate(SAMPLE_DATE_IN_DAYS_OF_WEEK);

        // Create progress tracker
        _progressTracker = new ProgressTracker(_parentHabit);

        assertEquals(0, (int) _progressTracker.calculateProgressPercentage());
    }

    @Test
    public void calculateProgress_eventOnStartDate_expectNotZero() {
        // Set start date to be on a viable day
        _parentHabit = new Habit(SAMPLE_HABIT_TITLE, SAMPLE_HABIT_REASON, SAMPLE_DATE_IN_DAYS_OF_WEEK, ALL_DAYS_OF_WEEK, PUBLIC_HABIT, SAMPLE_HABIT_ID, EMPTY_HABIT_EVENT_LIST);
        _progressTracker = new ProgressTracker(_parentHabit);

        // Add habit event on the same day
        HabitEvent onStartDate = new HabitEvent(SAMPLE_DATE_IN_DAYS_OF_WEEK, SAMPLE_EVENT_COMMENT, SAMPLE_HABIT_EVENT_ID);
        _parentHabit.getHabitEvents().addHabitEventLocally(onStartDate);

        assertFalse(_progressTracker.calculateProgressPercentage() == 0.0);
    }

    @Test
    public void calculateProgress_eventOnEndDate_expectNotZero() {
        // Set start date to be on a viable day
        _parentHabit = new Habit(SAMPLE_HABIT_TITLE, SAMPLE_HABIT_REASON, DATE_TODAY, ALL_DAYS_OF_WEEK, PUBLIC_HABIT, SAMPLE_HABIT_ID, EMPTY_HABIT_EVENT_LIST);
        _progressTracker = new ProgressTracker(_parentHabit);

        // Add habit event on the same day
        HabitEvent onStartDate = new HabitEvent(DATE_TODAY, SAMPLE_EVENT_COMMENT, SAMPLE_HABIT_EVENT_ID);
        _parentHabit.getHabitEvents().addHabitEventLocally(onStartDate);

        assertFalse(_progressTracker.calculateProgressPercentage() == 0.0);
    }

    @Test
    public void calculateProgress_severalEvents_expectNotZero() {
        // Set start date to be on a viable day
        _parentHabit = new Habit(SAMPLE_HABIT_TITLE, SAMPLE_HABIT_REASON, SAMPLE_START_DATE, ALL_DAYS_OF_WEEK, PUBLIC_HABIT, SAMPLE_HABIT_ID, EMPTY_HABIT_EVENT_LIST);
        _progressTracker = new ProgressTracker(_parentHabit);

        // Add habit events on viable days
        HabitEvent event1 = new HabitEvent(SAMPLE_DATE_IN_DAYS_OF_WEEK, SAMPLE_EVENT_COMMENT, SAMPLE_HABIT_EVENT_ID);
        HabitEvent event2 = new HabitEvent(SECOND_SAMPLE_DATE_IN_DAYS_OF_WEEK, SAMPLE_EVENT_COMMENT, SAMPLE_HABIT_EVENT_ID);
        HabitEvent event3 = new HabitEvent(THIRD_SAMPLE_DATE_IN_DAYS_OF_WEEK, SAMPLE_EVENT_COMMENT, SAMPLE_HABIT_EVENT_ID);
        _parentHabit.getHabitEvents().addHabitEventLocally(event1);
        _parentHabit.getHabitEvents().addHabitEventLocally(event2);
        _parentHabit.getHabitEvents().addHabitEventLocally(event3);

        assertFalse(_progressTracker.calculateProgressPercentage() == 0.0);
    }

}
