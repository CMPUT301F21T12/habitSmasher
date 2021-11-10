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
        _progressBar = view.findViewById(R.id.login_progress_bar);

        setClickListenerForLoginButton(loginButton, emailInput, passwordInput);

        setClickListenerForRegisterButton(view);

        return view;
    }

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

    private void setClickListenerForRegisterButton(View view) {
        View registerButton = view.findViewById(R.id.login_signup_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToFragmentWithAction(R.id.action_navigation_login_to_UserRegistrationFragment);
            }
        });
    }

    private void signInUserWithEmailAndPassword(String email, String password) {
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

    private void navigateToFragmentWithAction(int actionId) {
        NavController controller = NavHostFragment.findNavController(_fragment);
        controller.navigate(actionId);
    }

    private void saveUserInformation(User user) {
        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(USERNAME_SHARED_PREF_TAG, user.getUsername());
        editor.putString(USER_ID_SHARED_PREF_TAG, user.getId());
        editor.putString(USER_PASSWORD_SHARED_PREF_TAG, user.getPassword());
        editor.putString(USER_EMAIL_SHARED_PREF_TAG, user.getEmail());

        editor.apply();
    }

    @NonNull
    private DocumentReference getCurrentUserDocumentReference() {
        DocumentReference userRef = FirebaseFirestore.getInstance()
                                                     .collection("Users")
                                                     .document(FirebaseAuth.getInstance()
                                                                           .getCurrentUser()
                                                                           .getUid());
        return userRef;
    }

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
