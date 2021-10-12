package com.example.habitsmasher.ui.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;

import java.util.Date;

public class AddHabitDialog extends DialogFragment {
    private EditText habitTitleEditText;
    private EditText habitReasonEditText;
    private EditText habitDateEditText;
    private HabitDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_habit_dialog_box, null);
        builder.setView(view)
                .setTitle("Add a Habit")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String habitTitleInput = habitTitleEditText.getText().toString();
                        String habitReasonInput = habitReasonEditText.getText().toString();
                        Date habitDateInput = (Date) habitDateEditText.getText();
                        listener.applyChange(habitTitleInput, habitReasonInput, habitDateInput);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {                    }
                });

        habitTitleEditText = view.findViewById(R.id.habit_title_edittext);
        habitReasonEditText = view.findViewById(R.id.habit_reason_edittext);
        habitDateEditText = view.findViewById(R.id.habit_date_edittext);
        return builder.create();
    }

    public void onAttach(Context context){
        super.onAttach(context);
        try {
            listener = (HabitDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement ExampleDialogListener");
        }
    }

    public interface HabitDialogListener{
        void applyChange(String Title, String Reason, Date date);
    }


}
