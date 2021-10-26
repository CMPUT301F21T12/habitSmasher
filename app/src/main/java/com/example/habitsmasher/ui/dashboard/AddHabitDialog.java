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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
public class AddHabitDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "AddHabitDialog";
    private final String DATE_FORMAT = "dd/MM/yyyy";
    private final String INCORRECT_TITLE_FORMAT = "Incorrect habit title entered";
    private final String INCORRECT_REASON_FORMAT = "Incorrect habit reason entered";
    private final String INCORRECT_BLANK_DATE = "Please enter a start date";

    private HabitListFragment _habitListFragment;
    private EditText _habitTitleEditText;
    private EditText _habitReasonEditText;
    private TextView _habitDateTextView;
    private String _habitTitleInput;
    private String _habitReasonInput;
    private Date _habitDate;
    private final String _username;

    public AddHabitDialog(String username) {
        _username = username;
    }

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
                if (checkNewHabitIsValid()){
                    _habitListFragment.addNewHabit(new Habit(_habitTitleInput, _habitReasonInput, _habitDate, new HabitEventList()));
                    _habitListFragment.addHabitToDatabase(_habitTitleInput, _habitReasonInput, _habitDate, _username);
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    private void openDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //1 is added to the month we get from the DatePickerDialog
        // because DatePickerDialog returns values between 0 and 11,
        // which is not really helpful for users.
        int correctedMonth = month + 1;
        String date = day + "/" + correctedMonth + "/" + year;
        _habitDateTextView.setText(date);
    }

    /**
     * Returns boolean so habit can be added to habit list or not.
     * @return true if all fields are entered correctly, false otherwise.
     */
    private boolean checkNewHabitIsValid(){
        boolean invalidDate = false;
        _habitTitleInput = _habitTitleEditText.getText().toString();
        _habitReasonInput = _habitReasonEditText.getText().toString();

        DateFormat inputDateFormatter = new SimpleDateFormat(DATE_FORMAT);
        _habitDate = null;

        try {
            inputDateFormatter.setLenient(false);
            _habitDate = inputDateFormatter.parse(_habitDateTextView.getText().toString());
        } catch (ParseException e) {
            invalidDate = true;
            e.printStackTrace();
        }

        /**
         * When user input meets the requirements for a proper habit title, habit reason, and habit date,
         * have the habit be added to the habit list back in dashboard fragment.
         */
        if (
                ((_habitTitleInput.length()>0)&&(_habitTitleInput.length()<=20))&&
                ((_habitReasonInput.length()>0)&&(_habitReasonInput.length()<=30))&&
                (!invalidDate)
        ) {
            return true;
        } else {

            //Handle error checking for new habit name/title
            if ((!(_habitTitleInput.length()>0)) || (!(_habitTitleInput.length()<=20))){
                Toast.makeText(getActivity(), INCORRECT_TITLE_FORMAT, Toast.LENGTH_SHORT).show();
            }

            //Handle error checking for new habit reason
            if ((!(_habitReasonInput.length()>0)) || (!(_habitReasonInput.length()<=30))){
                Toast.makeText(getActivity(), INCORRECT_REASON_FORMAT, Toast.LENGTH_SHORT).show();
            }

            //Handle error checking for new habit date.
            if (invalidDate){
                Toast.makeText(getActivity(), INCORRECT_BLANK_DATE, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
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