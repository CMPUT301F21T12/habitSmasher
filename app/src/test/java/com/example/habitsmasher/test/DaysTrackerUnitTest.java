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
    public void testSetupStringInput(){
        _stringTracker = new DaysTracker("MO TU WE TH");
        assertTrue(_stringTracker.getMonday());
        assertTrue(_stringTracker.getTuesday());
        assertTrue(_stringTracker.getWednesday());
        assertTrue(_stringTracker.getThursday());
        assertFalse(_stringTracker.getFriday());
        assertFalse(_stringTracker.getSaturday());
        assertFalse(_stringTracker.getSunday());
    }
    @Test
    public void testSetupNoStringInput(){
        _stringTracker = new DaysTracker("");
        assertTrue(allFalse(_stringTracker));
    }

    @Test
    public void testSetupRandomString(){
        _stringTracker = new DaysTracker("hob bobby monday tuesday we friday sa sunday funday");
        assertTrue(_stringTracker.getWednesday());
        assertTrue(_stringTracker.getSaturday());
        assertFalse(_stringTracker.getMonday());
        assertFalse(_stringTracker.getTuesday());
        assertFalse(_stringTracker.getFriday());
        assertFalse(_stringTracker.getSunday());
    }

    @Test
    public void testInit(){
        //tests the initialization
        assertEquals(true, allFalse(_tracker));
    }

    @Test
    public void testSetDay(){
        //sake of clarity, gonna assume if monday works every other day works too
        _tracker.setMonday(true);
        assertTrue(_tracker.getMonday());
        assertFalse(allFalse(_tracker));
    }

    //test sampling of days
    @Test
    public void testGetDays(){
        _tracker.setMonday(true);
        _tracker.setWednesday(true);
        _tracker.setFriday(true);

        assertEquals("MO WE FR", _tracker.getDays());
    }

    //test all days
    @Test
    public void testGetAllDays(){
        _tracker.setTrue();

        assertEquals("MO TU WE TH FR SA SU", _tracker.getDays());
    }

    //test no days
    @Test
    public void testNoDays(){
        _tracker.setFalse();

        assertEquals("", _tracker.getDays());
    }

    //test changing day twice
    @Test
    public void testDoubleChange(){
        _tracker.setTuesday(true);
        assertTrue(_tracker.getTuesday());

        _tracker.setTuesday(false);
        assertFalse(_tracker.getTuesday());
    }

    //test changing to true then back to false
    @Test
    public void testChangeTrueAndBack(){
        _tracker.setTrue();
        assertEquals("MO TU WE TH FR SA SU", _tracker.getDays());

        _tracker.setFalse();
        assertEquals("", _tracker.getDays());
    }


    private Boolean allFalse(DaysTracker tracker){
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

}
