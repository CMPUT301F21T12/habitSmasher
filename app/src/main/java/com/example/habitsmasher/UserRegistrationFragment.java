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

    private void createNewUserWithEmailAndPassword(String email, String password, String username) {
        _auth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                         User user = new User(_auth.getUid(),
                                              username,
                                              email,
                                              password);

                         addNewUserToDatabase(user);

                     } else {
                         showMessage(FAILED_TO_REGISTER_MESSAGE);
                     }
                     _progressBar.setVisibility(View.GONE);
                 }
             });
    }

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

    private void showMessage(String userRegisteredMessage) {
        Toast.makeText(getContext(),
                       userRegisteredMessage, Toast.LENGTH_LONG).show();
    }

    @NonNull
    private HashMap<String, Object> buildUserDataMap(User user) {
        HashMap<String, Object> userData = new HashMap<>();

        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("password", user.getPassword());
        userData.put("id", user.getId());

        return userData;
    }

    private void setClickListenerForBackToLoginButton(Button backToLoginButton) {
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToLoginScreen();
            }
        });
    }

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
