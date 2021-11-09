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


    // needed for dialogs spawned from this fragment
    private final LoginFragment _fragment = this;
    private String _usernameToSend = "";
    private FirebaseAuth _auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_login, container, false);
        _auth = FirebaseAuth.getInstance();

        // remove bottom nav bar and action bar
        View bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(GONE);

        View actionBar = getActivity().findViewById(R.id.action_bar);
        actionBar.setBackgroundColor(getResources().getColor(R.color.habit_list_text));

        Button loginButton = view.findViewById(R.id.login_button);
        EditText emailInput = view.findViewById(R.id.login_email);
        EditText passwordInput = view.findViewById(R.id.login_password);
        TextView forgotPassword = view.findViewById(R.id.login_forgot_password);
        ProgressBar progressBar = view.findViewById(R.id.login_progress_bar);

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

                progressBar.setVisibility(View.VISIBLE);
                _auth.signInWithEmailAndPassword(validator.getValidEmailForLogin(),
                                                 validator.getValidPasswordForLogin())
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // redirect to home page with user data
                            Toast.makeText(getContext(),
                                           "Login successful!",
                                           Toast.LENGTH_LONG).show();

                            bottomNav.setVisibility(View.VISIBLE);

                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DocumentReference userRef = FirebaseFirestore.getInstance()
                                                                         .collection("Users")
                                                                         .document(userID);
                            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user = documentSnapshot.toObject(User.class);

                                    SharedPreferences sharedPref = getContext().getSharedPreferences("user data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("username", user.getUsername());
                                    editor.putString("userId", user.getId());
                                    editor.putString("password", user.getPassword());
                                    editor.putString("email", user.getEmail());
                                    editor.apply();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", user.getUsername());

                                    NavController controller = NavHostFragment.findNavController(_fragment);
                                    controller.navigate(R.id.action_navigation_login_to_ProfileFragment, bundle);
                                }
                            });
                        } else {
                            Toast.makeText(getContext(),
                                           "Incorrect email/password",
                                           Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        View registerButton = view.findViewById(R.id.login_signup_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = NavHostFragment.findNavController(LoginFragment.this);
                controller.navigate(R.id.action_navigation_login_to_UserRegistrationFragment);
            }
        });

        return view;
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
