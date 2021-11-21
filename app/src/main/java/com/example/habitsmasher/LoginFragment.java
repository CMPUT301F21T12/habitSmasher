package com.example.habitsmasher;

import static android.view.View.GONE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class holds the front-end elements relating to the Login screen
 * Author: Rudy Patel
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private static final String INCORRECT_EMAIL_PASSWORD_MESSAGE = "Incorrect email/password";
    private static final String LOGIN_SUCCESSFUL_MESSAGE = "Login successful!";
    private static final String USERNAME_SHARED_PREF_TAG = "username";
    private static final String USER_ID_SHARED_PREF_TAG = "userId";
    private static final String USER_PASSWORD_SHARED_PREF_TAG = "password";
    private static final String USER_EMAIL_SHARED_PREF_TAG = "email";

    private final LoginFragment _fragment = this;
    private FirebaseAuth _auth;
    private ProgressBar _progressBar;
    private View _bottomNav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_login, container, false);

        _auth = FirebaseAuth.getInstance();

        // remove bottom nav bar from login screen
        _bottomNav = getActivity().findViewById(R.id.nav_view);
        _bottomNav.setVisibility(GONE);

        View actionBar = getActivity().findViewById(R.id.action_bar);
        actionBar.setBackgroundColor(getResources().getColor(R.color.habit_list_text));

        // getting UI elements
        Button loginButton = view.findViewById(R.id.login_button);
        EditText emailInput = view.findViewById(R.id.login_email);
        EditText passwordInput = view.findViewById(R.id.login_password);
        TextView forgotPassword = view.findViewById(R.id.login_forgot_password);
        Button registerButton = view.findViewById(R.id.login_signup_button);
        _progressBar = view.findViewById(R.id.login_progress_bar);

        setClickListenerForLoginButton(loginButton, emailInput, passwordInput);

        setClickListenerForRegisterButton(registerButton);

        setClickListenerForForgotPasswordButton(forgotPassword);

        return view;
    }

    /**
     * This method sets the click listener for the login button, along with calling the associated
     * validators and setting/hiding UI related elements
     * @param loginButton the login button
     * @param emailInput the email input box
     * @param passwordInput the password input box
     */
    private void setClickListenerForLoginButton(Button loginButton, EditText emailInput, EditText passwordInput) {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserValidator validator = new UserValidator(emailInput, passwordInput);

                // if user data is not valid
                if (!validator.isLoginValid(emailInput.getText().toString().trim(),
                                            passwordInput.getText().toString().trim())) {
                    validator.showLoginErrors();  // show errors on the front-end
                    return;
                }

                _progressBar.setVisibility(View.VISIBLE);

                signInUserWithEmailAndPassword(validator.getValidEmailForLogin(),
                                               validator.getValidPasswordForLogin());
            }
        });
    }

    /**
     * This method sets the click listener for the register button and routes the screen to the
     * user sign up page
     * @param registerButton the register button
     */
    private void setClickListenerForRegisterButton(Button registerButton) {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToFragmentWithAction(R.id.action_navigation_login_to_UserRegistrationFragment);
            }
        });
    }

    /**
     * This method is responsible for using Firebase Auth to sign in a user given an email/pwd
     * @param email the user email
     * @param password the user password
     */
    private void signInUserWithEmailAndPassword(String email, String password) {
        PasswordEncrypt passwordEncrypt = new PasswordEncrypt();
        password = passwordEncrypt.encrypt(password);
        _auth.signInWithEmailAndPassword(email, password)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // if log in successful, redirect to home page
                    showMessage(LOGIN_SUCCESSFUL_MESSAGE);

                    DocumentReference userRef = getCurrentUserDocumentReference();
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);

                            saveUserInformation(user);

                            navigateToFragmentWithAction(R.id.action_navigation_login_to_ProfileFragment);
                        }
                    });
                } else {
                    showMessage(INCORRECT_EMAIL_PASSWORD_MESSAGE);
                }
                _progressBar.setVisibility(View.GONE);
                _bottomNav.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * This method listens for when the forgot password text view is clicked
     * @param forgotPasswordButton the reset password text view
     */
    public void setClickListenerForForgotPasswordButton(TextView forgotPasswordButton) {
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgotPasswordDialog();
            }
        });
    }

    /**
     * This is a helper method that opens the forgot password dialog.
     */
    protected void openForgotPasswordDialog() {
        ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
        forgotPasswordDialog.setCancelable(true);
        forgotPasswordDialog.setTargetFragment(this, 1);
        forgotPasswordDialog.show(getFragmentManager(), "ForgotPasswordDialog");
    }

    /**
     * This method is responsible for switching the application context to a new fragment
     * @param actionId the action corresponding to the routing of the new fragment
     */
    private void navigateToFragmentWithAction(int actionId) {
        NavController controller = NavHostFragment.findNavController(_fragment);
        controller.navigate(actionId);
    }

    /**
     * This method saves the logged in user's information to be shared/accessed throughout fragments
     * @param user the user logged in
     */
    private void saveUserInformation(User user) {
        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(USERNAME_SHARED_PREF_TAG, user.getUsername());
        editor.putString(USER_ID_SHARED_PREF_TAG, user.getId());
        editor.putString(USER_PASSWORD_SHARED_PREF_TAG, user.getPassword());
        editor.putString(USER_EMAIL_SHARED_PREF_TAG, user.getEmail());

        editor.apply();
    }

    /**
     * This method gets the document reference from firebase of the current user
     * @return document reference from firebase of the current user
     */
    @NonNull
    private DocumentReference getCurrentUserDocumentReference() {
        DocumentReference userRef = FirebaseFirestore.getInstance()
                                                     .collection("Users")
                                                     .document(FirebaseAuth.getInstance()
                                                                           .getCurrentUser()
                                                                           .getUid());
        return userRef;
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
