package com.example.habitsmasher.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.DatePickerDialogFragment;
import com.example.habitsmasher.R;

/**
 * Making a dialogfragment from fragment came from the following video:
 * https://www.youtube.com/watch?v=LUV_djRHSEY
 *
 * Name: Mitch Tabian
 * Video Date: December 10, 2017
 *
 * Adding a DatePickerDialog came from the following video:
 * https://www.youtube.com/watch?v=AdTzD96AhE0
 *
 * Name: Mitch Tabian
 * Video Date: March 11, 2019
 */
public class AddHabitDialog extends DialogFragment {

    private static final String TAG = "AddHabitDialog";

    private HabitListFragment _habitListFragment;
    private EditText _habitTitleEditText;
    private EditText _habitReasonEditText;
    private TextView _habitDateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);
        _habitTitleEditText = view.findViewById(R.id.habit_title_edit_text);
        _habitReasonEditText = view.findViewById(R.id.habit_reason_edit_text);
        _habitDateTextView = view.findViewById(R.id.habit_date_selection);
        Button confirmNewHabit = view.findViewById(R.id.confirm_habit);
        Button cancelNewHabit = view.findViewById(R.id.cancel_habit);

        _habitDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        cancelNewHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                getDialog().dismiss();
            }
        });

        confirmNewHabit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm");

                HabitValidator habitValidator = new HabitValidator(getActivity());

                String habitTitle = _habitTitleEditText.getText().toString();
                String habitReason = _habitReasonEditText.getText().toString();
                String habitDate = _habitDateTextView.getText().toString();

                if (habitValidator.isHabitValid(habitTitle,
                                                habitReason,
                                                habitDate)){
                    _habitListFragment.addHabitToDatabase(habitTitle,
                                                          habitReason,
                                                          habitValidator.checkHabitDateValid(habitDate));
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    private void openDatePickerDialog(){
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                int correctedMonth = month + 1;
                String date =  day + "/" + correctedMonth + "/" + year;
                _habitDateTextView.setText(date);
            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            _habitListFragment = (HabitListFragment) getTargetFragment();
        } catch (ClassCastException e){
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }
}