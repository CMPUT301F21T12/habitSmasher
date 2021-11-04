package com.example.habitsmasher.test;

import com.example.habitsmasher.DaysTracker;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DaysTrackerUnitTest {

    private DaysTracker _tracker;
    private DaysTracker _stringTracker;

    @Before
    public void setupTracker(){

        _tracker = new DaysTracker();
    }

    @Test
    public void constructor_withAssortedDays_expectMoTuWeThAreTrue(){
        _stringTracker = new DaysTracker("MO TU WE TH");
        // Monday Tuesday Wednesday Thursday should be true.
        assertTrue(_stringTracker.getMonday());
        assertTrue(_stringTracker.getTuesday());
        assertTrue(_stringTracker.getWednesday());
        assertTrue(_stringTracker.getThursday());
        assertFalse(_stringTracker.getFriday());
        assertFalse(_stringTracker.getSaturday());
        assertFalse(_stringTracker.getSunday());
    }
    @Test
    public void constructor_withEmptyDaysString_expectAllDaysAreFalse(){
        _stringTracker = new DaysTracker("");
        assertTrue(areAllDaysUnselected(_stringTracker));
    }

    @Test
    public void constructor_randomStringWithDays_expectWeAndFrAreTrue(){
        _stringTracker = new DaysTracker("hob bobby monday tuesday we friday sa sunday funday");
        assertTrue(_stringTracker.getWednesday());
        assertTrue(_stringTracker.getSaturday());
        assertFalse(_stringTracker.getMonday());
        assertFalse(_stringTracker.getTuesday());
        assertFalse(_stringTracker.getFriday());
        assertFalse(_stringTracker.getSunday());
    }

    @Test
    public void constructor_noStringInput_expectAllDaysFalse(){
        //tests the initialization
        assertEquals(true, areAllDaysUnselected(_tracker));
    }

    @Test
    public void setMonday_setMondayTrue_MoTrueAndAllOthersFalse(){
        //sake of clarity, gonna assume if monday works every other day works too
        _tracker.setMonday(true);
        assertTrue(_tracker.getMonday());
        assertFalse(areAllDaysUnselected(_tracker));
    }

    //test sampling of days
    @Test
    public void getDays_getMoWeFr_MoWeFrAreTrue(){
        _tracker.setMonday(true);
        _tracker.setWednesday(true);
        _tracker.setFriday(true);

        assertEquals("MO WE FR", _tracker.getDays());
    }

    //test all days
    @Test
    public void getDays_setAllAsSelected_expectAllToBeTrue(){
        _tracker.setTrue();

        assertTrue(areAllDaysSelected(_tracker));
    }

    //test no days
    @Test
    public void getDays_setAllToFalse_expectAllDaysAreFalse(){
        _tracker.setFalse();

        assertTrue(areAllDaysUnselected(_tracker));
    }

    //test changing day twice
    @Test
    public void setDay_setTuesdayToTrueThenFalse_expectTuesdayIsTrueThenFalse(){
        _tracker.setTuesday(true);
        assertTrue(_tracker.getTuesday());

        _tracker.setTuesday(false);
        assertFalse(_tracker.getTuesday());
    }

    @Test
    public void setDays_withStringOfDays_expectFrSaSuAreTrue(){
        _tracker.setDays("FR SA SU");

        assertTrue(_tracker.getFriday());
        assertTrue(_tracker.getSaturday());
        assertTrue(_tracker.getSunday());
    }

    @Test
    public void setDays_setAllDaysAsSelectedWithStringInput_expectAllDaysSelected(){
        _tracker.setDays("MO TU WE TH FR SA SU");

        assertTrue(areAllDaysSelected(_tracker));
    }

    //test changing to true then back to false
    @Test
    public void setDay_setAllDaysToTrueThenFalse_expectAllDaysAreTrueThenAllFalse(){
        _tracker.setTrue();
        assertTrue(areAllDaysSelected(_tracker));

        _tracker.setFalse();
        assertTrue(areAllDaysUnselected(_tracker));
    }


    private Boolean areAllDaysUnselected(DaysTracker tracker){
        if (
                tracker.getMonday() || tracker.getTuesday() || tracker.getWednesday() ||
                tracker.getThursday() || tracker.getFriday() || tracker.getSaturday() ||
                tracker.getSunday()){
            return false;
        }
        else {
            return true;
        }
    }

    private Boolean areAllDaysSelected(DaysTracker tracker){
        if (tracker.getMonday() && tracker.getTuesday() &&
                tracker.getWednesday() && tracker.getThursday() && tracker.getFriday()
                && tracker.getSaturday() && tracker.getSunday()){
            return true;
        } else {
            return false;
        }
    }

}
