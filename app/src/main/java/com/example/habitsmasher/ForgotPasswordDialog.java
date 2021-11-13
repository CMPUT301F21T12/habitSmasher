package com.example.habitsmasher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;


/**
 * This class is the dialog that opens when the user forgets their password.
 * @author Jacob Nguyen
 */
public class ForgotPasswordDialog extends DialogFragment {

    private static final String EMAIL_SENT_MESSAGE = "Email sent, please check your email";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_user_password_dialog, container, false);

        EditText forgotPasswordEdittext = view.findViewById(R.id.reset_password_dialog);
        Button forgotPasswordButton = view.findViewById(R.id.reset_password_next);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserValidator userValidator = new UserValidator(forgotPasswordEdittext);
                if (!userValidator.isEmailValid(forgotPasswordEdittext.getText().toString().trim())) {
                    userValidator.showLoginErrors();
                } else {
                    auth.sendPasswordResetEmail(forgotPasswordEdittext.getText().toString().trim());
                    showMessage(EMAIL_SENT_MESSAGE);
                    getDialog().dismiss();
                }
            }
        });
        return view;
    }

    /**
     * This helper method shows a toast message to the screen
     * @param message message to display
     */
    private void showMessage(String message) {
        Toast.makeText(getContext(),
                message,
                Toast.LENGTH_LONG).show();
    }
}
