package com.example.habitsmasher;

import android.util.Patterns;
import android.widget.EditText;

import androidx.core.util.PatternsCompat;


/**
 * This class validates whether a email is of the correct format.
 * This class is based of the UserValidator class.
 *
 * @author Jacob Nguyen
 */
public class EmailValidator {
    private static final String EMAIL_IS_REQUIRED_MESSAGE = "Email is required!";
    private static final String INVALID_EMAIL_FORMAT_MESSAGE = "Invalid email format!";

    private EditText _emailInput;

    String _email;

    //For Forgot Password
    public EmailValidator(EditText emailInput) {
        _emailInput = emailInput;

        _email = _emailInput.getText().toString().trim();
    }

    //Empty Constructor used for tests and using in user validator
    public EmailValidator() { }

    public boolean isEmailValid(String email) {
        return !(email.isEmpty() | !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches());
    }

    public void showForgotPasswordErrors() {
        if (_email.isEmpty()) {
            _emailInput.setError(EMAIL_IS_REQUIRED_MESSAGE);
            _emailInput.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            _emailInput.setError(INVALID_EMAIL_FORMAT_MESSAGE);
            _emailInput.requestFocus();
            return;
        }
    }
}
