package com.example.habitsmasher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * This class holds the front-end elements related to the user sign up page
 * Author: Rudy Patel
 */
public class UserRegistrationFragment extends Fragment {
    private static final String USER_REGISTERED_MESSAGE = "User registered!";
    private static final String FAILED_TO_ADD_USER_MESSAGE = "Failed to add user, try again!";
    private static final String FAILED_TO_REGISTER_MESSAGE = "Failed to register";

    private FirebaseAuth _auth;
    private ProgressBar _progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_registration, container, false);

        _auth = FirebaseAuth.getInstance();

        // get front-end elements
        EditText emailInput = view.findViewById(R.id.registration_email);
        EditText passwordInput = view.findViewById(R.id.registration_password);
        EditText usernameInput = view.findViewById(R.id.registration_username);
        Button registerButton = view.findViewById(R.id.registration_signup_button);
        Button backToLoginButton = view.findViewById(R.id.registration_go_back_to_login_button);
        _progressBar = view.findViewById(R.id.registration_progress_bar);

        setClickListenerForBackToLoginButton(backToLoginButton);

        setClickListenerForRegisterButton(emailInput, passwordInput, usernameInput, registerButton);

        return view;
    }

    /**
     * This method sets the click listener for the register button, along with routing to the
     * associated validators to process the input data
     * @param emailInput the email input box
     * @param passwordInput the password input box
     * @param usernameInput the username input box
     * @param registerButton the register button
     */
    private void setClickListenerForRegisterButton(EditText emailInput,
                                                   EditText passwordInput,
                                                   EditText usernameInput,
                                                   Button registerButton) {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserValidator validator = new UserValidator(usernameInput,
                                                            emailInput,
                                                            passwordInput);

                // if user data is not valid
                if (!validator.isNewUserValid(usernameInput.getText().toString().trim(),
                                              emailInput.getText().toString().trim(),
                                              passwordInput.getText().toString().trim())) {
                    validator.showSignUpErrors();  // show errors on the front-end
                    return;
                }

                _progressBar.setVisibility(View.VISIBLE);

                createNewUserWithEmailAndPassword(validator.getValidEmailForSignUp(),
                                                  validator.getValidPasswordForSignUp(),
                                                  validator.getValidUsernameForSignUp());
            }
        });
    }

    /**
     * This method is responsible for using Firebase Auth to create a new user entry in the database
     * with the provided username, email, and password
     * @param email the user's email
     * @param password the user's password
     * @param username the user's username
     */
    private void createNewUserWithEmailAndPassword(String email, String password, String username) {
        PasswordEncrypt passwordEncrypt = new PasswordEncrypt();
        password = passwordEncrypt.encrypt(password);
        String finalPassword = password;
        _auth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                         User user = new User(_auth.getUid(),
                                              username,
                                              email,
                                 finalPassword);

                         addNewUserToDatabase(user);

                     } else {
                         showMessage(FAILED_TO_REGISTER_MESSAGE);
                     }
                     _progressBar.setVisibility(View.GONE);
                 }
             });
    }

    /**
     * This method adds a new user to the Firestore database
     * @param user the new user to add
     */
    private void addNewUserToDatabase(User user) {
        FirebaseFirestore.getInstance()
                         .collection("Users")
                         .document(user.getId())
                         .set(buildUserDataMap(user))
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if (task.isSuccessful()) {
                                     showMessage(USER_REGISTERED_MESSAGE);
                                     goBackToLoginScreen();
                                   } else {
                                     showMessage(FAILED_TO_ADD_USER_MESSAGE);
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
                       message, Toast.LENGTH_LONG).show();
    }

    /**
     * This helper method builds up the user data to insert into the database
     * @param user the user data to build up
     * @return a hashmap of key/value pairs of the user data
     */
    @NonNull
    private HashMap<String, Object> buildUserDataMap(User user) {
        HashMap<String, Object> userData = new HashMap<>();

        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("password", user.getPassword());
        userData.put("id", user.getId());

        return userData;
    }

    /**
     * This method sets the click listener for the "go back" button and routes to the new fragment
     * @param backToLoginButton
     */
    private void setClickListenerForBackToLoginButton(Button backToLoginButton) {
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToLoginScreen();
            }
        });
    }

    /**
     * This helper method is responsible for routing back to the LoginFragment
     */
    private void goBackToLoginScreen() {
        NavController controller = NavHostFragment.findNavController(UserRegistrationFragment.this);
        controller.navigate(R.id.action_navigation_registration_to_UserLoginFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        androidx.appcompat.app.ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.show();
    }
}
