package com.example.habitsmasher.listeners;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.habitsmasher.DatePickerDialogFragment;

public class ClickListenerForDatePicker implements View.OnClickListener{
    private FragmentManager _fragmentManager;
    private TextView _dateText;

    public ClickListenerForDatePicker(FragmentManager fragmentManager, TextView dateText) {
        _fragmentManager = fragmentManager;
        _dateText = dateText;
    }

    @Override
    public void onClick(View view) {
        openDatePickerDialog();
    }

    /**
     * Opens the calendar dialog used for date selection
     */
    private void openDatePickerDialog() {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialog.OnDateSetListener() {
            /**
             * Sets the text of the date select view to reflect selected date
             * @param view
             * @param year year of selected date
             * @param month month of selected date (integer from 0 to 11)
             * @param day day of month of selected date
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                int correctedMonth = month + 1;
                String date = day + "/" + correctedMonth + "/" + year;
                _dateText.setText(date);
            }
        });
        datePickerDialogFragment.show(_fragmentManager, "DatePickerDialogFragment");
    }
}
