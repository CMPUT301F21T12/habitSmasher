package com.example.habitsmasher.ui.dashboard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.DatePickerDialogFragment;
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;


public class EditHabitFragment extends DialogFragment{
    private EditText _titleText;
    private EditText _reasonText;
    private TextView _dateText;

    private final Habit _editHabit;
    private final int _index;
    private final HabitListFragment _listener;
    // viewHolder of edited Habit
    private final HabitItemAdapter.HabitViewHolder _viewHolder;

    public EditHabitFragment(int index, Habit editHabit, HabitListFragment listener,
                                HabitItemAdapter.HabitViewHolder habitViewHolder) {
        _index = index;
        _editHabit = editHabit;
        _listener = listener;
        _viewHolder = habitViewHolder;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Used same UI xml as Add Habit, since the dialog box is the same
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);

        // Connecting elements on UI xml to variables
        TextView header = view.findViewById(R.id.add_habit_header);
        Button confirmButton = view.findViewById(R.id.confirm_habit);
        Button cancelButton = view.findViewById(R.id.cancel_habit);
        _titleText = view.findViewById(R.id.habit_title_edit_text);
        _reasonText = view.findViewById(R.id.habit_reason_edit_text);
        _dateText = view.findViewById(R.id.habit_date_selection);

        // Get header from resource file and set it
        header.setText(getResources().getString(R.string.edit_habit));

        // presetting text to values of habit
        _titleText.setText(_editHabit.getTitle());
        _reasonText.setText(_editHabit.getReason());
        _dateText.setText(DatePickerDialogFragment.parseDateToString(_editHabit.getDate()));

        _dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        // when ok is clicked
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HashMap<String, Habit> habitData = new HashMap<>();
                String habitTitle = _titleText.getText().toString();
                String reasonText = _reasonText.getText().toString();
                String dateText = _dateText.getText().toString();

                HabitValidator habitValidator = new HabitValidator(getActivity());

                if (!habitValidator.isHabitValid(habitTitle, reasonText, dateText)) {
                    return;
                }

                // update local list and display
                _listener.updateAfterEdit(habitTitle, reasonText, DatePickerDialogFragment.parseStringToDate(dateText), _index, _viewHolder);

                getDialog().dismiss();
            }
        });

        // when cancel button is clicked
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
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
        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
    }
}
