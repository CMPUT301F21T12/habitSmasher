package com.example.habitsmasher;

import android.os.Bundle;
import android.util.Patterns;
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
    private FirebaseAuth _auth;

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
        ProgressBar progressBar = view.findViewById(R.id.registration_progress_bar);

        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToLoginScreen();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameInput.setError("Username is required!");
                    usernameInput.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    emailInput.setError("Email is required!");
                    emailInput.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passwordInput.setError("Password is required!");
                    passwordInput.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Invalid email format!");
                    emailInput.requestFocus();
                    return;
                }

                if (password.length() < 3) {
                    passwordInput.setError("Password length must be greater than 3!");
                    passwordInput.requestFocus();
                    return;
                }

                if (password.length() > 15) {
                    passwordInput.setError("Password length must be less than 16!");
                    passwordInput.requestFocus();
                    return;
                }

                if (username.length() > 15) {
                    usernameInput.setError("Username length must be less than 16!");
                    usernameInput.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                _auth.createUserWithEmailAndPassword(email, password)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 User user = new User(_auth.getUid(), username, email, password);

                                 HashMap<String, Object> userData = new HashMap<>();
                                 userData.put("username", user.getUsername());
                                 userData.put("email", user.getEmail());
                                 userData.put("password", user.getPassword());
                                 userData.put("id", user.getId());

                                 FirebaseFirestore.getInstance()
                                                  .collection("Users")
                                                  .document(user.getId())
                                                  .set(userData)
                                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<Void> task) {
                                                          if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "User registered!", Toast.LENGTH_LONG).show();
                                                            progressBar.setVisibility(View.GONE);
                                                            goBackToLoginScreen();
                                                            } else {
                                                                Toast.makeText(getContext(), "Failed to add user, try again!", Toast.LENGTH_LONG).show();
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                      }
                                                  });

                             } else {
                                 Toast.makeText(getContext(), "Failed to register", Toast.LENGTH_LONG).show();
                                 progressBar.setVisibility(View.GONE);
                             }
                         }
                     });
            }
        });


        return view;
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
