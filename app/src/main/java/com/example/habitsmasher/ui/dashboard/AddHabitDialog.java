package com.example.habitsmasher.ui.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;

import java.util.Date;

public class AddHabitDialog extends DialogFragment{

    private static final String TAG = "AddHabitDialog";

    public  interface HabitDialogListener {
        void addNewHabit(String title, String reason, Date date);
    }

    public HabitDialogListener _listener;

    /**
     * Initializing EditTexts and dialog listener.
     */
    private EditText habitTitleEditText;
    private EditText habitReasonEditText;
    private EditText habitDateEditText;
    private Button confirmNewHabit;
    private Button cancelNewHabit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);
        habitTitleEditText = view.findViewById(R.id.habit_title_edittext);
        habitReasonEditText = view.findViewById(R.id.habit_reason_edittext);
        habitDateEditText = view.findViewById(R.id.habit_date_edittext);
        confirmNewHabit = view.findViewById(R.id.confirmHabit);
        cancelNewHabit = view.findViewById(R.id.cancelHabit);



        cancelNewHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                getDialog().dismiss();
            }
        });
        confirmNewHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm");
                String habitTitleInput = habitTitleEditText.getText().toString();
                String habitReasonInput = habitReasonEditText.getText().toString();
                Date habitDateInput = (Date) habitDateEditText.getText();
                if (!(habitTitleInput.equals("") && habitReasonInput.equals("") && habitDateInput.equals(""))){
                    _listener.addNewHabit(habitTitleInput, habitReasonInput, habitDateInput);
                }
                getDialog().dismiss();
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
}
