package com.example.habitsmasher;

import android.util.Patterns;
import android.widget.EditText;

import androidx.core.util.PatternsCompat;

public class UserValidator {
    private static final String USERNAME_IS_REQUIRED_MESSAGE = "Username is required!";
    private static final String USERNAME_LENGTH_MUST_BE_LESS_THAN_16_MESSAGE = "Username length must be less than 16!";
    private static final String EMAIL_IS_REQUIRED_MESSAGE = "Email is required!";
    private static final String PASSWORD_IS_REQUIRED_MESSAGE = "Password is required!";
    private static final String INVALID_EMAIL_FORMAT_MESSAGE = "Invalid email format!";
    private static final String PASSWORD_LENGTH_MUST_BE_GREATER_THAN_6_MESSAGE = "Password length must be greater than 6!";
    private static final String PASSWORD_LENGTH_MUST_BE_LESS_THAN_16_MESSAGE = "Password length must be less than 16!";
    private static final int UPPER_CHARACTER_LIMIT = 15;
    private static final int LOWER_CHARACTER_LIMIT = 6;

    private EditText _usernameInput;
    private EditText _emailInput;
    private EditText _passwordInput;

    String _username;
    String _email;
    String _password;

    // for testing
    public UserValidator() {}

    // for sign up
    public UserValidator(EditText usernameInput,
                         EditText emailInput,
                         EditText passwordInput) {
        _usernameInput = usernameInput;
        _emailInput = emailInput;
        _passwordInput = passwordInput;

        // save text contents from EditText fields
        _email = _emailInput.getText().toString().trim();
        _password = _passwordInput.getText().toString().trim();
        _username = _usernameInput.getText().toString().trim();
    }

    // for log in
    public UserValidator(EditText emailInput,
                         EditText passwordInput) {
        _usernameInput = null;
        _emailInput = emailInput;
        _passwordInput = passwordInput;

        // save text contents from EditText fields
        _email = _emailInput.getText().toString().trim();
        _password = _passwordInput.getText().toString().trim();
        _username = "";  // since there is no username for logging in, empty
    }

    //for forgot password
    public UserValidator(EditText emailInput) {
        _emailInput = emailInput;

        _email = _emailInput.getText().toString().trim();
    }

    public String getValidUsernameForSignUp() {
        if (isNewUserValid()) {
            return _username;
        }
        return "";
    }
    public String getValidEmailForSignUp() {
        if (isNewUserValid()) {
            return _email;
        }
        return "";
    }
    public String getValidPasswordForSignUp() {
        if (isNewUserValid()) {
            return _password;
        }
        return "";
    }

    public String getValidEmailForLogin() {
        if (isLoginValid()) {
            return _email;
        }
        return "";
    }
    public String getValidPasswordForLogin() {
        if (isLoginValid()) {
            return _password;
        }
        return "";
    }

    private boolean isNewUserValid() {
        return isNewUserValid(_username, _email, _password);
    }

    private boolean isLoginValid() {
        return isLoginValid(_email, _password);
    }

    public boolean isNewUserValid(String username, String email, String password) {
        return !(username.isEmpty() | username.length() > UPPER_CHARACTER_LIMIT | !isLoginValid(email, password));
    }

    public boolean isLoginValid(String email, String password) {
        return !(email.isEmpty() | password.isEmpty() |
                !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() |
                password.length() < LOWER_CHARACTER_LIMIT |
                password.length() > UPPER_CHARACTER_LIMIT);
    }

    public boolean isEmailValid(String email) {
        return !(email.isEmpty() | !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches());
    }


    public void showSignUpErrors() {
        if (_username.isEmpty()) {
            _usernameInput.setError(USERNAME_IS_REQUIRED_MESSAGE);
            _usernameInput.requestFocus();
            return;
        }

        if (_username.length() > UPPER_CHARACTER_LIMIT) {
            _usernameInput.setError(USERNAME_LENGTH_MUST_BE_LESS_THAN_16_MESSAGE);
            _usernameInput.requestFocus();
            return;
        }

        showLoginErrors();
    }

    public void showLoginErrors() {
        if (_email.isEmpty()) {
            _emailInput.setError(EMAIL_IS_REQUIRED_MESSAGE);
            _emailInput.requestFocus();
            return;
        }
        if (_password.isEmpty()) {
            _passwordInput.setError(PASSWORD_IS_REQUIRED_MESSAGE);
            _passwordInput.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            _emailInput.setError(INVALID_EMAIL_FORMAT_MESSAGE);
            _emailInput.requestFocus();
            return;
        }

        if (_password.length() < LOWER_CHARACTER_LIMIT) {
            _passwordInput.setError(PASSWORD_LENGTH_MUST_BE_GREATER_THAN_6_MESSAGE);
            _passwordInput.requestFocus();
            return;
        }

        if (_password.length() > UPPER_CHARACTER_LIMIT) {
            _passwordInput.setError(PASSWORD_LENGTH_MUST_BE_LESS_THAN_16_MESSAGE);
            _passwordInput.requestFocus();
            return;
        }
    }
}
