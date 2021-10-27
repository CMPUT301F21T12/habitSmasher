package com.example.habitsmasher.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditHabitFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private static final String HEADER = "Edit Dialog";
    private static final String PATTERN = "dd/MM/yyyy";
    private final String INCORRECT_TITLE_FORMAT = "Incorrect habit title entered";
    private final String INCORRECT_REASON_FORMAT = "Incorrect habit reason entered";
    private final String INCORRECT_BLANK_DATE = "Please enter a start date";
    private EditText _titleText;
    private EditText _reasonText;
    private TextView _dateText;
    private TextView _header;

    private Button _confirmButton;
    private Button _cancelButton;

    private Habit _editHabit;
    private int _index;
    private HabitListFragment _listener;
    // viewHolder of edited Habit
    private HabitItemAdapter.HabitViewHolder _viewHolder;

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
        _header = view.findViewById(R.id.add_habit_header);
        _titleText = view.findViewById(R.id.habit_title_edit_text);
        _reasonText = view.findViewById(R.id.habit_reason_edit_text);
        _dateText = view.findViewById(R.id.habit_date_selection);
        _confirmButton = view.findViewById(R.id.confirm_habit);
        _cancelButton = view.findViewById(R.id.cancel_habit);

        _header.setText(HEADER);

        // presetting text to values of habit
        _titleText.setText(_editHabit.getTitle());
        _reasonText.setText(_editHabit.getReason());
        _dateText.setText(parseDateToString(_editHabit.getDate()));

        _dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        // when ok is clicked
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HashMap<String, Habit> habitData = new HashMap<>();
                String habitTitle = _titleText.getText().toString();
                String reasonText = _reasonText.getText().toString();
                String dateText = _dateText.getText().toString();

                // need to set to constant later
                if (habitTitle.length() > 30 || habitTitle.length() == 0){
                    Toast.makeText(getActivity(), INCORRECT_TITLE_FORMAT, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (reasonText.length() > 30 || reasonText.length() == 0) {
                    Toast.makeText(getActivity(), INCORRECT_REASON_FORMAT, Toast.LENGTH_SHORT).show();
                    return;
                }
                Date habitDate = parseStringToDate(dateText);
                if (habitDate == null) {
                    Toast.makeText(getActivity(), INCORRECT_BLANK_DATE, Toast.LENGTH_SHORT).show();
                    return;
                }
                // update local list and display
                _listener.updateAfterEdit(habitTitle, reasonText, habitDate, _index, _viewHolder);

                getDialog().dismiss();
            }
        });

        // when cancel button is clicked
        _cancelButton.setOnClickListener(new View.OnClickListener() {
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

    /**
     * Sets the text of the date select view to reflect selected date
     * @param view
     * @param year year of selected date
     * @param month month of selected date (integer from 0 to 11)
     * @param day day of month of selected date
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //1 is added to the month we get from the DatePickerDialog
        // because DatePickerDialog returns values between 0 and 11,
        // which is not really helpful for users.
        int correctedMonth = month + 1;
        String date = day + "/" + correctedMonth + "/" + year;
        _dateText.setText(date);
    }

    /**
     * Takes in a Date object and converts it into a string representation
     * of the date
     * @param date Date object
     * @return string representation of the date
     */
    private String parseDateToString(Date date) {
        DateFormat dateFormatter = new SimpleDateFormat(PATTERN);
        return dateFormatter.format(date);
    }

    /**
     * Parses a string and returns a Date Object reflecting the date
     * represented by the string, or null if an invalid string input
     * @param string String to be parsed
     * @return date object of date represented in string
     */
    private Date parseStringToDate(String string) {
        DateFormat dateFormatter = new SimpleDateFormat(PATTERN);
        dateFormatter.setLenient(false);
        try {
            return dateFormatter.parse(string);
        } catch(Exception e) {
            return null;
        }
    }
}
