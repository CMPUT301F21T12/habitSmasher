package com.example.habitsmasher;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * UI class that represents and specifies the behaviour of the dialog box
 * spawned when the user wishes to select a certain date
 *
 * Extraction of DatePickerDialog came from the following link:
 *
 * https://stackoverflow.com/a/34589270
 * Post date: January 4, 2016
 */
public class DatePickerDialogFragment extends DialogFragment {

    private static final String TAG = "DatePickerDialogFragment";
    private static final String PATTERN = "dd/MM/yyyy";

    private DatePickerDialog.OnDateSetListener _dateSetListener;

    /**
     * Creates a date picker dialog
     * @param callback listener for setting text of date text field
     */
    public DatePickerDialogFragment(DatePickerDialog.OnDateSetListener callback) {
        _dateSetListener = callback;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // set up the dialog and load elements
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this._dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.getInstance().get(Calendar.MONTH),
                calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
        return datePickerDialog;
    }

    /**
     * Parses a string and returns a Date Object reflecting the date
     * represented by the string, or null if an invalid string input
     * @param string String to be parsed
     * @return date object of date represented in string
     */
    public static Date parseStringToDate(String string) {
        DateFormat dateFormatter = new SimpleDateFormat(PATTERN);
        dateFormatter.setLenient(false);
        try {
            return dateFormatter.parse(string);
        } catch(Exception e) {
            Log.e(TAG, "Exception" + e.getMessage());
            return null;
        }
    }

    /**
     * Takes in a Date object and converts it into a string representation
     * of the date
     * @param date Date object
     * @return string representation of the date
     */
    public static String parseDateToString(Date date) {
        DateFormat dateFormatter = new SimpleDateFormat(PATTERN);
        return dateFormatter.format(date);
    }
}