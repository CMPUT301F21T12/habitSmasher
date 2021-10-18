package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditHabitFragment extends DialogFragment {
    private EditText _titleText;
    private EditText _reasonText;
    private EditText _dateText;
    private TextView _errorText;

    private Button _okButton;
    private Button _cancelButton;

    // don't want this to be in the front end
    private FirebaseFirestore _db;

    private Habit _editHabit;
    private int _index;
    private EditHabitListener _listener;

    public EditHabitFragment(int _index, Habit _editHabit, EditHabitListener _listener) {
        super();
        this._index = _index;
        this._editHabit = _editHabit;
        this._listener = _listener;
    }

    public interface EditHabitListener {
        public void editHabit(String title, String reason, Date date, int pos);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Used same UI xml as Add Habit, since the dialog box is the same
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);

        // Connecting elements on UI xml to variables
        _titleText = view.findViewById(R.id.habit_title_edit_text);
        _reasonText = view.findViewById(R.id.habit_reason_edit_text);
        _dateText = view.findViewById(R.id.habit_date_edit_text);
        _okButton = view.findViewById(R.id.confirm_habit);
        _cancelButton = view.findViewById(R.id.cancel_habit);

        // presetting text to values of habit
        _titleText.setText(_editHabit.getTitle());
        _reasonText.setText(_editHabit.getReason());
        _dateText.setText(parseDateToString(_editHabit.getDate()));

        /*
        _db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = _db.collection("Habits");
         */

        // when ok is clicked
        _okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HashMap<String, Habit> habitData = new HashMap<>();
                String habitTitle = _titleText.getText().toString();
                String reasonText = _reasonText.getText().toString();
                String dateText = _dateText.getText().toString();

                // check if fields are ok, return and display error message if not
                if (habitTitle.length() == 0 || reasonText.length() == 0 || dateText.length() == 0) {
                    // empty field case
                    return;
                }
                // need to set to constant later
                if (habitTitle.length() > 30){
                    // habit title too long
                    return;
                }
                if (reasonText.length() > 30) {
                    // reason too long
                    return;
                }
                Date habitDate = parseStringToDate(dateText);
                if (habitDate == null) {
                    // date in incorrect format
                    return;
                }
                // update local list and display
                _listener.editHabit(habitTitle, reasonText, habitDate, _index);

                // update database, dont really want this here
                /*
                HashMap<String, Habit> habitHashMap = new HashMap<>();
                habitHashMap.put(habitTitle, new Habit(habitTitle, reasonText, habitDate));
                collectionReference.document(habitTitle)
                        .set(habitHashMap);
                 */
                // close dialog
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

    public String parseDateToString(Date date) {
        // replace string constant with a variable
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormatter.format(date);
    }

    public Date parseStringToDate(String string) {
        // replace string constant
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return dateFormatter.parse(string);
        } catch(Exception e) {
            return null;
        }
    }
}
