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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Making a dialogfragment from fragment came from the following video:
 * https://www.youtube.com/watch?v=LUV_djRHSEY
 *
 * Name: Mitch Tabian
 * Video Date: December 10, 2017
 */
public class AddHabitDialog extends DialogFragment{

    private static final String TAG = "AddHabitDialog";
    FirebaseFirestore db;

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

        db = FirebaseFirestore.getInstance();
        /**
         * Name of database collection.
         */
        final CollectionReference collectionReference = db.collection("Habits");

        /**
         * Closes Dialog box if user presses the cancel button.
         */
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
                /**
                 * Gets user input and saves to string variables.
                 */
                String habitTitleInput = habitTitleEditText.getText().toString();
                String habitReasonInput = habitReasonEditText.getText().toString();
                String habitDateInput = habitDateEditText.getText().toString();
                SimpleDateFormat inputDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                Date habitDate = null;
                try {
                    habitDate = inputDateFormatter.parse(habitDateInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /**
                 * Makes sure user does not enter blank fields,
                 * and is capped on the number of characters for title and reason.
                 */
                HashMap<String, Habit> firebaseData = new HashMap<>();
                Boolean properTitle = false;
                Boolean properReason = false;
                Boolean properDate = false;
                if ((habitTitleInput.length()>0)&&(habitTitleInput.length()<=20)){
                    properTitle = true;
                }
                if((habitReasonInput.length()>0)&&(habitReasonInput.length()<=30)){
                    properReason = true;
                }
                if(!(habitDateInput.equals(""))){
                    properDate = true;
                }

                /**
                 * When user input meets the requirements for a proper habit title, habit reason, and habit date,
                 * have the habit be added to the habit list back in dashboard fragment.
                 *
                 * Add the habit to our database.
                 * Name of document in database will be the title of habit inputted by user.
                 */

                if ((properTitle==true)&&(properReason==true)&&(properDate==true)){
                    _listener.addNewHabit(habitTitleInput, habitReasonInput, habitDate);
                    firebaseData.put(habitTitleInput, new Habit(habitTitleInput, habitReasonInput, habitDate));
                    collectionReference
                            .document(habitTitleInput)
                            .set(firebaseData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Data successfully added.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Data failed to be added." + e.toString());
                                }
                            });
                    getDialog().dismiss();
                } else{
                    if (properTitle==false){
                        habitTitleEditText.getText().clear();
                        Toast.makeText(getActivity(), "Please enter a name for your habit (20 characters max).", Toast.LENGTH_SHORT).show();
                    }
                    if (properReason==false){
                        habitReasonEditText.getText().clear();
                        Toast.makeText(getActivity(), "Please enter a reason for your habit (30 characters max).", Toast.LENGTH_SHORT).show();
                    }
                    if (properDate==false){
                        habitDateEditText.getText().clear();
                        Toast.makeText(getActivity(), "Habit start date format: dd-mm-yyyy", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Please make sure all field are correctly entered.", Toast.LENGTH_SHORT).show();
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
}
