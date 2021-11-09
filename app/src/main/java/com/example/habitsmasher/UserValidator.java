package com.example.habitsmasher;

import android.util.Patterns;
import android.widget.EditText;

import androidx.core.util.PatternsCompat;

public class UserValidator {
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

        _email = _emailInput.getText().toString().trim();
        _password = _passwordInput.getText().toString().trim();
        _username = "";
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
        return !(username.isEmpty() | username.length() > 15 | !isLoginValid(email, password));
    }

    public boolean isLoginValid(String email, String password) {
        return !(email.isEmpty() | password.isEmpty() |
                !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() |
                password.length() < 6 |
                password.length() > 15);
    }


    public void showSignUpErrors() {
        if (_username.isEmpty()) {
            _usernameInput.setError("Username is required!");
            _usernameInput.requestFocus();
            return;
        }

        if (_username.length() > 15) {
            _usernameInput.setError("Username length must be less than 16!");
            _usernameInput.requestFocus();
            return;
        }

        showLoginErrors();
    }

    public void showLoginErrors() {
        if (_email.isEmpty()) {
            _emailInput.setError("Email is required!");
            _emailInput.requestFocus();
            return;
        }
        if (_password.isEmpty()) {
            _passwordInput.setError("Password is required!");
            _passwordInput.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            _emailInput.setError("Invalid email format!");
            _emailInput.requestFocus();
            return;
        }

        if (_password.length() < 6) {
            _passwordInput.setError("Password length must be greater than 6!");
            _passwordInput.requestFocus();
            return;
        }

        if (_password.length() > 15) {
            _passwordInput.setError("Password length must be less than 16!");
            _passwordInput.requestFocus();
            return;
        }
    }
}
