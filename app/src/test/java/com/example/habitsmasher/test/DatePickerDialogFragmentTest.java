package com.example.habitsmasher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.habitsmasher.DatePickerDialogFragment;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogFragmentTest {
    private Date _dateStringToDate;
    private Date _dateDateToStringToday;
    private Date _dateDateToStringPast;
    private Date _dateDateToStringFuture;
    private String _stringStringToDate;
    private String _stringDateToStringToday;
    private String _stringDateToStringPast;
    private String _stringDateToStringFuture;
    private Calendar _calendarForStringToDate = Calendar.getInstance();
    private Calendar _calendarForDateToStringToday = Calendar.getInstance();
    private Calendar _calendarForDateToStringPast = Calendar.getInstance();
    private Calendar _calendarForDateToStringFuture = Calendar.getInstance();


    @Before
    public void setUp(){

        //For testing parseStringToDate
        //Creating a string to be converted to date for test
        _dateStringToDate = new Date(2021, 9, 30);
        _dateStringToDate.setHours(0);
        _dateStringToDate.setMinutes(0);
        _dateStringToDate.setSeconds(0);
        _calendarForStringToDate.setTime(_dateStringToDate);
        int dayStringToDate = _calendarForStringToDate.get(Calendar.DAY_OF_MONTH);
        int monthStringToDate = _calendarForStringToDate.get(Calendar.MONTH) + 1;
        int yearStringToDate = _calendarForStringToDate.get(Calendar.YEAR);
        _stringStringToDate = dayStringToDate + "/" + monthStringToDate + "/"  + yearStringToDate;



        //For testing parseDateToString
        //Creating a date to be converted to string for test for current date
        _dateDateToStringToday = new Date();
        _calendarForDateToStringToday.setTime(_dateDateToStringToday);
        int dayDateToStringToday = _calendarForDateToStringToday.get(Calendar.DAY_OF_MONTH);
        int monthDateToStringToday = _calendarForDateToStringToday.get(Calendar.MONTH) + 1;
        int yearDateToStringToday = _calendarForDateToStringToday.get(Calendar.YEAR);
        _stringDateToStringToday = dayDateToStringToday + "/" + monthDateToStringToday + "/" + yearDateToStringToday;

        //For testing parseDateToString
        //Creating a date to be converted to string for test for date in the past
        _dateDateToStringPast = new Date(2000, 10, 30);
        _calendarForDateToStringPast.setTime(_dateDateToStringPast);
        int dayDateToStringPast = _calendarForDateToStringPast.get(Calendar.DAY_OF_MONTH);
        int monthDateToStringPast = _calendarForDateToStringPast.get(Calendar.MONTH) + 1;
        int yearDateToStringPast = _calendarForDateToStringPast.get(Calendar.YEAR);
        _stringDateToStringPast = dayDateToStringPast + "/" + monthDateToStringPast + "/" + yearDateToStringPast;


        //For testing parseDateToString
        //Creating a date to be converted to string for test for date in the future
        _dateDateToStringFuture = new Date(2100, 10, 30);
        _calendarForDateToStringFuture.setTime(_dateDateToStringFuture);
        int dayDateToStringFuture = _calendarForDateToStringFuture.get(Calendar.DAY_OF_MONTH);
        int monthDateToStringFuture = _calendarForDateToStringFuture.get(Calendar.MONTH) + 1;
        int yearDateToStringFuture = _calendarForDateToStringFuture.get(Calendar.YEAR);
        _stringDateToStringFuture = dayDateToStringFuture + "/" + monthDateToStringFuture + "/" + yearDateToStringFuture;
    }

    @Test
    public void parseStringToDate_validDateString_expectsStringToBeValidDate() {
        assertEquals(_dateStringToDate, DatePickerDialogFragment.parseStringToDate(_stringStringToDate));
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
        assertEquals(_stringDateToStringToday, DatePickerDialogFragment.parseDateToString(_dateDateToStringToday));
    }

    @Test
    public void parseDateToString_validPastDate_expectsStringFormatOfDate() {
        assertEquals(_stringDateToStringPast, DatePickerDialogFragment.parseDateToString(_dateDateToStringPast));
    }

    @Test
    public void parseDateToString_validFutureDate_expectsStringFormatOfDate() {
        assertEquals(_stringDateToStringFuture, DatePickerDialogFragment.parseDateToString(_dateDateToStringFuture));
    }


}
