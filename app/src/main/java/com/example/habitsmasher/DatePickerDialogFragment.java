package com.example.habitsmasher;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DatePickerDialogFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener _dateSetListener;

    public DatePickerDialogFragment(DatePickerDialog.OnDateSetListener callback) {
        _dateSetListener = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this._dateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
        return datePickerDialog;
    }
}