package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
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
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.R;

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
public class AddHabitDialog extends DialogFragment implements HabitDialogListener{

    private static final String TAG = "AddHabitDialog";

    HabitDialogListener _listener;

    private EditText _habitTitleEditText;
    private EditText _habitReasonEditText;
    private EditText _habitDateEditText;
    private Button _confirmNewHabit;
    private Button _cancelNewHabit;
    private String DATE_FORMAT = "dd-MM-yyyy";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);
        _habitTitleEditText = view.findViewById(R.id.habit_title_edittext);
        _habitReasonEditText = view.findViewById(R.id.habit_reason_edittext);
        _habitDateEditText = view.findViewById(R.id.habit_date_edittext);
        _confirmNewHabit = view.findViewById(R.id.confirmHabit);
        _cancelNewHabit = view.findViewById(R.id.cancelHabit);

        _cancelNewHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                getDialog().dismiss();
            }
        });

        _confirmNewHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm");
                String habitTitleInput = _habitTitleEditText.getText().toString();
                String habitReasonInput = _habitReasonEditText.getText().toString();
                String habitDateInput = _habitDateEditText.getText().toString();
                SimpleDateFormat inputDateFormatter = new SimpleDateFormat(DATE_FORMAT);

                Date habitDate = null;
                try {
                    habitDate = inputDateFormatter.parse(habitDateInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                /**
                 * When user input meets the requirements for a proper habit title, habit reason, and habit date,
                 * have the habit be added to the habit list back in dashboard fragment.
                 */
                if (((habitTitleInput.length()>0)&&(habitTitleInput.length()<=20))&&
                        ((habitReasonInput.length()>0)&&(habitReasonInput.length()<=30))&&
                        (!(habitDateInput.equals("")))) {
                    _listener.addNewHabit(habitTitleInput, habitReasonInput, habitDate);
                    getDialog().dismiss();
                } else{

                    //Handle error checking for new habit name/title
                    if (!(habitTitleInput.length()>0)){
                        Toast.makeText(getActivity(), "Please enter a name for your habit", Toast.LENGTH_SHORT).show();
                    } else if (!(habitTitleInput.length()<=20)){
                        _habitTitleEditText.getText().clear();
                        Toast.makeText(getActivity(), "Habit name is too long", Toast.LENGTH_SHORT).show();
                    }

                    //Handle error checking for new habit reason
                    if (!(habitReasonInput.length()>0)){
                        Toast.makeText(getActivity(), "Please enter a reason for your habit", Toast.LENGTH_SHORT).show();
                    } else if (!(habitReasonInput.length()<=30)){
                        _habitReasonEditText.getText().clear();
                        Toast.makeText(getActivity(), "Habit reason is too long", Toast.LENGTH_SHORT).show();
                    }

                    //Handle error checking for new habit date.
                    if ((habitDateInput.equals(""))){
                        _habitDateEditText.getText().clear();
                        Toast.makeText(getActivity(), "Habit start date format: dd-mm-yyyy", Toast.LENGTH_SHORT).show();
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
            _listener = (HabitDialogListener) getTargetFragment();
        } catch (ClassCastException e){
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }

    @Override
    public void addNewHabit(String habitTitleInput, String habitReasonInput, Date habitDate) {    }
}
