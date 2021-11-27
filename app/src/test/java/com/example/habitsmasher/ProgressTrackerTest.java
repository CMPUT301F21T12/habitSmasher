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

    @Before
    public void setUp() {
        _parentHabit = new Habit("Test Habit", "Test Reason", new Date(), SAMPLE_DAYS_OF_THE_WEEK, true, UUID.randomUUID().toString(), new HabitEventList());
    }

    @Test
    public void calculateProgress_noViableDays_expect0() {
        // Create date that is not viable
        Date notInDays = new Date(2021, 11, 27);
        _parentHabit.setDate(notInDays);

        // Create progress tracker
        _progressTracker = new ProgressTracker(_parentHabit);
        assertEquals(0, (int) _progressTracker.calculateProgressPercentage());

    }

    @Test
    public void calculateProgress_noEventsOnDays_expect0() {
        // Set date that is viable, but do not add events
        Date notInDays = new Date(2021, 11, 26);
        _parentHabit.setDate(notInDays);

        // Create progress tracker
        _progressTracker = new ProgressTracker(_parentHabit);

        assertEquals(0, (int) _progressTracker.calculateProgressPercentage());
    }

    @Test
    public void calculateProgress_eventOnStartDate_expectNotZero() {
        // Set start date to be on a viable day
        Date startDateInDays = new Date(2021, 11, 22);
        _parentHabit = new Habit("Test Habit", "Test Reason", startDateInDays, "MO TE WE TH FR SA SU", true, UUID.randomUUID().toString(), new HabitEventList());
        _progressTracker = new ProgressTracker(_parentHabit);

        // Add habit event on the same day
        HabitEvent onStartDate = new HabitEvent(startDateInDays, "Comment", UUID.randomUUID().toString());
        _parentHabit.getHabitEvents().addHabitEventLocally(onStartDate);

        assertFalse(_progressTracker.calculateProgressPercentage() == 0.0);
    }

    @Test
    public void calculateProgress_eventOnEndDate_expectNotZero() {
        // Set start date to be on a viable day
        Date today = new Date();
        _parentHabit = new Habit("Test Habit", "Test Reason", today, "MO TE WE TH FR SA SU", true, UUID.randomUUID().toString(), new HabitEventList());
        _progressTracker = new ProgressTracker(_parentHabit);

        // Add habit event on the same day
        HabitEvent onStartDate = new HabitEvent(today, "Comment", UUID.randomUUID().toString());
        _parentHabit.getHabitEvents().addHabitEventLocally(onStartDate);

        assertFalse(_progressTracker.calculateProgressPercentage() == 0.0);
    }

    @Test
    public void calculateProgress_severalEvents_expectNotZero() {
        // Set start date to be on a viable day
        Date today = new Date();
        _parentHabit = new Habit("Test Habit", "Test Reason", today, "MO TE WE TH FR SA SU", true, UUID.randomUUID().toString(), new HabitEventList());
        _progressTracker = new ProgressTracker(_parentHabit);

        // Add habit event on the same day
        HabitEvent event1 = new HabitEvent(new Date(2021, 11, 22), "Comment", UUID.randomUUID().toString());
        HabitEvent event2 = new HabitEvent(new Date(2021, 11, 23), "Comment", UUID.randomUUID().toString());
        HabitEvent event3 = new HabitEvent(new Date(2021, 11, 24), "Comment", UUID.randomUUID().toString());
        _parentHabit.getHabitEvents().addHabitEventLocally(event1);
        _parentHabit.getHabitEvents().addHabitEventLocally(event2);
        _parentHabit.getHabitEvents().addHabitEventLocally(event3);

        assertFalse(_progressTracker.calculateProgressPercentage() == 0.0);
    }

}
