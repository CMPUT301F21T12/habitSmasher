package com.example.habitsmasher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.habitsmasher.DatePickerDialogFragment;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogFragmentTest {
    private Date _testDate;
    private String _testString;
    private Calendar _testCalendar = Calendar.getInstance();

    @Test
    public void parseStringToDate_validDateString_expectsStringToBeValidDate() {
        _testDate = new Date(2021, 9, 30);
        _testDate.setHours(0);
        _testDate.setMinutes(0);
        _testDate.setSeconds(0);
        _testCalendar.setTime(_testDate);
        int dayStringToDate = _testCalendar.get(Calendar.DAY_OF_MONTH);
        int monthStringToDate = _testCalendar.get(Calendar.MONTH) + 1;
        int yearStringToDate = _testCalendar.get(Calendar.YEAR);
        _testString = dayStringToDate + "/" + monthStringToDate + "/"  + yearStringToDate;

        assertEquals(_testDate, DatePickerDialogFragment.parseStringToDate(_testString));
    }

    @Test
    public void parseStringToDate_invalidDateString_expectsStringToBeInvalidDate() {
        String invalidEmptyStringToDate = "";
        String invalidDayStringToDate = "32/10/2021";
        String invalidDayLeapYearStringToDate = "29/2/2021";
        String invalidMonthStringToDate = "15/13/2021";
        String invalidYearStringToDate = "30/10/-2021";
        String invalidEmptyDayStringToDate = "/10/2021";
        String invalidEmptyMonthStringToDate = "30//2021";
        String invalidEmptyYearStringToDate = "30/10/";
        String invalidEmptyDayEmptyMonthStringToDate = "//2021";
        String invalidEmptyDayEmptyYearStringToDate = "/10/";
        String invalidEmptyMonthEmptyYearStringToDate = "30//";

        assertNull(DatePickerDialogFragment.parseStringToDate(invalidEmptyStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidDayStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidDayLeapYearStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidMonthStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidYearStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidEmptyDayStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidEmptyMonthStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidEmptyYearStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidEmptyDayEmptyMonthStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidEmptyDayEmptyYearStringToDate));
        assertNull(DatePickerDialogFragment.parseStringToDate(invalidEmptyMonthEmptyYearStringToDate));
    }

    @Test
    public void parseDateToString_validTodayDate_expectsStringFormatOfDate() {
        _testDate = new Date();
        _testCalendar.setTime(_testDate);
        int dayDateToStringToday = _testCalendar.get(Calendar.DAY_OF_MONTH);
        int monthDateToStringToday = _testCalendar.get(Calendar.MONTH) + 1;
        int yearDateToStringToday = _testCalendar.get(Calendar.YEAR);
        if (dayDateToStringToday >= 1 && dayDateToStringToday <= 9) {
            _testString = "0"+dayDateToStringToday + "/" + monthDateToStringToday + "/" + yearDateToStringToday;
        } else {
            _testString = dayDateToStringToday + "/" + monthDateToStringToday + "/" + yearDateToStringToday;
        }
        assertEquals(_testString, DatePickerDialogFragment.parseDateToString(_testDate));
    }

    @Test
    public void parseDateToString_validPastDate_expectsStringFormatOfDate() {
        _testDate = new Date(2000, 10, 30);
        _testCalendar.setTime(_testDate);
        int dayDateToStringPast = _testCalendar.get(Calendar.DAY_OF_MONTH);
        int monthDateToStringPast = _testCalendar.get(Calendar.MONTH) + 1;
        int yearDateToStringPast = _testCalendar.get(Calendar.YEAR);
        _testString = dayDateToStringPast + "/" + monthDateToStringPast + "/" + yearDateToStringPast;
        assertEquals(_testString, DatePickerDialogFragment.parseDateToString(_testDate));
    }

    @Test
    public void parseDateToString_validFutureDate_expectsStringFormatOfDate() {
        _testDate = new Date(2100, 10, 30);
        _testCalendar.setTime(_testDate);
        int dayDateToStringFuture = _testCalendar.get(Calendar.DAY_OF_MONTH);
        int monthDateToStringFuture = _testCalendar.get(Calendar.MONTH) + 1;
        int yearDateToStringFuture = _testCalendar.get(Calendar.YEAR);
        _testString = dayDateToStringFuture + "/" + monthDateToStringFuture + "/" + yearDateToStringFuture;
        assertEquals(_testString, DatePickerDialogFragment.parseDateToString(_testDate));
    }


}
