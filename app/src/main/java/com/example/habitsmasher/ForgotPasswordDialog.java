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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;


/**
 * This class is the dialog that opens when the user forgets their password.
 * @author Jacob Nguyen
 */
public class ForgotPasswordDialog extends DialogFragment {

    private static final String EMAIL_SENT_MESSAGE = "Email sent, please check your email";
    private static final String EMAIL_DOES_NOT_EXIST = "Entered email is not registered";

    FirebaseAuth _auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_user_password_dialog, container, false);

        _auth = FirebaseAuth.getInstance();

        EditText forgotPasswordEdittext = view.findViewById(R.id.reset_password_dialog);
        Button forgotPasswordButton = view.findViewById(R.id.reset_password_button);

        setClickListenerForConfirmButton(forgotPasswordEdittext, forgotPasswordButton);

        return view;
    }

    /**
     * This method listens for when the user presses the confirm button to get an email sent to them to reset their email.
     * @param emailInput email inputted by user
     * @param confirmButton button user presses to get email sent to them
     */
    private void setClickListenerForConfirmButton(EditText emailInput, Button confirmButton) {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailValidator emailValidator = new EmailValidator(emailInput);
                if (!emailValidator.isEmailValid(emailInput.getText().toString().trim())) {
                    emailValidator.showForgotPasswordErrors();
                } else {
                    //Checking whether the entered email exists within the database came from the following link:
                    //https://stackoverflow.com/a/56949945
                    //Author username: J.Dragon
                    _auth.fetchSignInMethodsForEmail(emailInput.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                    boolean nonExistingUser = task.getResult().getSignInMethods().isEmpty();
                                    
                                    if (nonExistingUser) {
                                        emailInput.setError(EMAIL_DOES_NOT_EXIST);
                                        emailInput.requestFocus();
                                    } else {
                                        _auth.sendPasswordResetEmail(emailInput.getText().toString().trim());
                                        showMessage(EMAIL_SENT_MESSAGE);
                                        getDialog().dismiss();
                                    }
                                }
                            });
                }
            }
        });
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
