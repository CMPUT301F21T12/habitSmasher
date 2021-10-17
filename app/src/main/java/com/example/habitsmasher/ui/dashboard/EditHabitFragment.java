package com.example.habitsmasher.ui.dashboard;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class EditHabitFragment extends DialogFragment {
    private EditText _titleText;
    private EditText _reasonText;
    private EditText _dateText;
    private TextView _errorText;

    private Button _okButton;
    private Button _cancelButton;

    // don't want this to be in the front end
    //private FirebaseFirestore _db;
    // need to pass in Habit to be edited
    private Habit _editHabit;
    private int index;

    public EditHabitListener _editHabitListener;


    public interface EditHabitListener {
        public void editHabit(String title, String reason, Date date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // create new dialog box for edit or use add dialog?
        //View view = inflater.inflate(R.id.edit_habit_dialog_box,container,false);
        // TO DO: reference editTexts here
        _titleText.setText(_editHabit.getTitle());
        _reasonText.setText(_editHabit.getReason());
        _dateText.setText(parseDateToString(_editHabit.getDate()));

        //_db = FirebaseFirestore.getInstance();
        //final CollectionReference collectionReference = db.collection("Habits");

        _okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Habit> habitData = new HashMap<>();
                String habitTitle = _titleText.getText().toString();
                String reasonTitle = _reasonText.getText().toString();
                String dateText = _dateText.getText().toString();

                // check if fields are ok, return and display error message if not
                if (habitTitle.length() == 0 || reasonTitle.length() == 0 || dateText.length() == 0) {
                    // set text to display error messages
                    return;
                }
                if (habitTitle.length() > 20){
                    _errorText.setText("Habit title exceeds limit of 20 characters");
                    return;
                }
                if (reasonTitle.length() > 20) {
                    _errorText.setText("");
                    return;
                }
                Date habitDate = parseStringToDate(dateText);
                if (habitDate == null) {
                    _errorText.setText("Illegal date inputted. Ensure data is formatted dd-MM-yyyy");
                    return;
                }
                // update local list and display

                // update database, dont really want this here
                HashMap<String, Habit> habitHashMap = new HashMap<>();
                //habitHashMap.add((habitTitle, new Habit(habitTitle, reasonTitle, )))
                //collectionReference.document(habitTitle).set(habitHashMap);
            }
        });




        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public String parseDateToString(Date date) {
        // replace string constant with a variable
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormatter.format(date);
    }

    public Date parseStringToDate(String string) {
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return dateFormatter.parse(string);
        } catch(Exception e) {
            return null;
        }

    }
}
