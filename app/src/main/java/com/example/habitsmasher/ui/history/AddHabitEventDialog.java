package com.example.habitsmasher.ui.history;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddHabitEventDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AddHabitEventDialog";

    private final String INCORRECT_COMMENT_FORMAT = "Incorrect comment entered";
    private final String DATE_FORMAT = "dd/MM/yyyy";
    private final String INCORRECT_BLANK_DATE = "Please enter a start date";

    private String _username;

    private HabitEventListFragment _habitEventListFragment;
    private EditText _eventCommentText;
    private TextView _eventDateText;
    private Button _cancelNewEvent;
    private Button _confirmNewEvent;
    private ImageView _eventPictureView;
    private Uri _selectedImage;

    private String _eventCommentInput;
    private Date _eventDate;

    public AddHabitEventDialog(String username) {
        _username = username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_event_dialog, container, false);
        _eventCommentText = view.findViewById(R.id.add_habit_event_comment);
        _eventDateText = view.findViewById(R.id.habit_event_date_selection);
        _cancelNewEvent = view.findViewById(R.id.cancel_habit_event);
        _confirmNewEvent = view.findViewById(R.id.confirm_habit_event);
        _eventPictureView = view.findViewById(R.id.habit_event_add_photo);

        _eventDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        _cancelNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                getDialog().dismiss();
            }
        });

        _confirmNewEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm");
                if (checkNewHabitEventIsValid()) {
                    Log.d(TAG, _habitEventListFragment.toString());
                    HabitEvent newEvent = new HabitEvent(_eventDate, _eventCommentInput, _selectedImage);
                    _habitEventListFragment.addNewHabitEvent(newEvent);
                    _habitEventListFragment.addHabitEventToDatabase(newEvent.getStartDate(), newEvent.getComment(), newEvent.getId(), newEvent.getPictureUri(), _username);
                    getDialog().dismiss();
                }

            }
        });

        _eventPictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
            case 1:
                if (resultCode == RESULT_OK) {
                    _selectedImage = imageReturnedIntent.getData();
                    _eventPictureView.setImageURI(_selectedImage);
                }
                break;
        }

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
        _eventDateText.setText(date);
    }

    private boolean checkNewHabitEventIsValid() {
        boolean invalidDate = false;

        _eventCommentInput = _eventCommentText.getText().toString();

        DateFormat inputDateFormatter = new SimpleDateFormat(DATE_FORMAT);
        _eventDate = null;

        try {
            inputDateFormatter.setLenient(false);
            _eventDate = inputDateFormatter.parse(_eventDateText.getText().toString());
        } catch (ParseException e) {
            invalidDate = true;
            e.printStackTrace();
        }

        if ((_eventCommentInput.length() >0) && (_eventCommentInput.length() <= 20) && !invalidDate) {
            return true;
        } else {
            if (invalidDate) {
                Toast.makeText(getActivity(), INCORRECT_BLANK_DATE, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), INCORRECT_COMMENT_FORMAT, Toast.LENGTH_SHORT).show();
            }
            // TODO: IMAGE CHECKING
            return false;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            _habitEventListFragment = (HabitEventListFragment) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }
}
