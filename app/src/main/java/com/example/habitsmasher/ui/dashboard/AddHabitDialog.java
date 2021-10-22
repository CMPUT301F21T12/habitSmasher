package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Making a dialogfragment from fragment came from the following video:
 * https://www.youtube.com/watch?v=LUV_djRHSEY
 *
 * Name: Mitch Tabian
 * Video Date: December 10, 2017
 */
public class AddHabitDialog extends DialogFragment{

    private static final String TAG = "AddHabitDialog";
    private String DATE_FORMAT = "dd-MM-yyyy";
    private String INCORRECT_TITLE_FORMAT = "Incorrect habit title entered";
    private String INCORRECT_REASON_FORMAT = "Incorrect habit reason entered";
    private String INCORRECT_BLANK_DATE = "Please enter a start date";
    private String INCORRECT_DATE_FORMAT = "Habit start date format: dd-mm-yyyy";

    private HabitListFragment _habitListFragment;
    private EditText _habitTitleEditText;
    private EditText _habitReasonEditText;
    private EditText _habitDateEditText;
    private Button _confirmNewHabit;
    private Button _cancelNewHabit;
    private boolean _invalidDate = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);
        _habitTitleEditText = view.findViewById(R.id.habit_title_edit_text);
        _habitReasonEditText = view.findViewById(R.id.habit_reason_edit_text);
        _habitDateEditText = view.findViewById(R.id.habit_date_edit_text);
        _confirmNewHabit = view.findViewById(R.id.confirm_habit);
        _cancelNewHabit = view.findViewById(R.id.cancel_habit);

        _cancelNewHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                getDialog().dismiss();
            }
        });

        _confirmNewHabit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm");

                String habitTitleInput = _habitTitleEditText.getText().toString();
                String habitReasonInput = _habitReasonEditText.getText().toString();
                String habitDateInput = _habitDateEditText.getText().toString();

                DateFormat inputDateFormatter = new SimpleDateFormat(DATE_FORMAT);
                Date habitDate = null;

                try {
                    inputDateFormatter.setLenient(false);
                    habitDate = inputDateFormatter.parse(habitDateInput);
                } catch (ParseException e) {
                    _invalidDate = true;
                    e.printStackTrace();
                }

                /**
                 * When user input meets the requirements for a proper habit title, habit reason, and habit date,
                 * have the habit be added to the habit list back in dashboard fragment.
                 */
                if (((habitTitleInput.length()>0)&&(habitTitleInput.length()<=20))&&
                        ((habitReasonInput.length()>0)&&(habitReasonInput.length()<=30))&&
                        (!(habitDateInput.equals("")))&&
                        (!(_invalidDate))
                ) {
                    _habitListFragment.addNewHabit(new Habit(habitTitleInput, habitReasonInput, habitDate));
                    _habitListFragment.addHabitToDatabase(habitTitleInput, habitReasonInput, habitDate);
                    getDialog().dismiss();
                } else{

                    //Handle error checking for new habit name/title
                    if ((!(habitTitleInput.length()>0)) || (!(habitTitleInput.length()<=20))){
                        Toast.makeText(getActivity(), INCORRECT_TITLE_FORMAT, Toast.LENGTH_SHORT).show();
                    }

                    //Handle error checking for new habit reason
                    if ((!(habitReasonInput.length()>0)) || (!(habitReasonInput.length()<=30))){
                        Toast.makeText(getActivity(), INCORRECT_REASON_FORMAT, Toast.LENGTH_SHORT).show();
                    }

                    //Handle error checking for new habit date.
                    if ((habitDateInput.equals(""))){
                        Toast.makeText(getActivity(), INCORRECT_BLANK_DATE, Toast.LENGTH_SHORT).show();
                    } else if(_invalidDate==true){
                        Toast.makeText(getActivity(), INCORRECT_DATE_FORMAT, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
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
