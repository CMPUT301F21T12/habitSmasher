package com.example.habitsmasher;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class holds the front-end elements relating to the Login screen
 * @author Rudy Patel, Jacob Nguyen
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
    private static final int RC_SIGN_IN = 123;
    private static final String GOOGLE_USER_PWD = "GoogleUserNoPwd";
    private ArrayList<String> EMPTY_FOLLOWING_LIST = new ArrayList<>();
    private ArrayList<String> EMPTY_FOLLOWER_LIST = new ArrayList<>();
    private ArrayList<String> EMPTY_REQUEST_LIST = new ArrayList<>();

    private final LoginFragment _fragment = this;
    private FirebaseAuth _auth;
    private ProgressBar _progressBar;
    private View _bottomNav;
    private GoogleSignInClient _googleSignInClient;
    private UserAccountHelper _userAccountHelper;

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

        _userAccountHelper = new UserAccountHelper(getContext(), this);

        setClickListenerForLoginButton(loginButton, emailInput, passwordInput);

        setClickListenerForRegisterButton(registerButton);

        setClickListenerForForgotPasswordButton(forgotPassword);

        createGoogleSignInRequest();

        setClickListenerForGoogleSignInButton(view);

        return view;
    }

    /**
     * This method sets the click listener for the google sign up button
     * @param view the current view
     */
    private void setClickListenerForGoogleSignInButton(View view) {
        Button googleSignInButton = view.findViewById(R.id.google_login_button);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    /**
     * This method builds the google sign in request using the app's SHA1 token
     */
    private void createGoogleSignInRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        _googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    /**
     * This method uses the google sign in client and launches the sign in activity to select the
     * user's google account
     */
    private void signInWithGoogle() {
        Intent signInIntent = _googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * This method is triggered when the google sign in activity is finished and a result is returned.
     * This method is also responsible for signing in the user using the specified intent
     * @param requestCode the code of the given request
     * @param resultCode the code of the resulting result
     * @param data the intent data passed down from the google sign in activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                showMessage("Sign in failed", Toast.LENGTH_SHORT);
            }
        }
    }

    /**
     * This helper method uses the GoogleAuthProvider with the given id credential to sign in
     * using Firebase
     * @param idToken the id token of the associated credential
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        _auth.signInWithCredential(credential)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                         // if log in successful, redirect to home page
                         showMessage(LOGIN_SUCCESSFUL_MESSAGE, Toast.LENGTH_SHORT);

                         DocumentReference userRef = getCurrentUserDocumentReference();
                         userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(
                                         getContext());

                                 User googleUser = new User(user.getId(),
                                                            user.getDisplayName()
                                                                .replaceAll("\\s+",""),
                                                            user.getEmail(),
                                                            GOOGLE_USER_PWD,
                                                            EMPTY_FOLLOWER_LIST,
                                                            EMPTY_FOLLOWING_LIST,
                                                            EMPTY_REQUEST_LIST);

                                 // once the user is successfully retrieved, add them to the db
                                 addUserToDatabaseIfUserDoesNotExist(googleUser);

                                 // save their information to be used globally throughout the app
                                 saveUserInformation(googleUser);

                                 // transition to the home screen
                                 navigateToFragmentWithAction(R.id.action_navigation_login_to_HomeFragment);
                             }
                         });
                     } else {
                         showMessage(INCORRECT_EMAIL_PASSWORD_MESSAGE, Toast.LENGTH_LONG);
                     }
                     _progressBar.setVisibility(View.GONE);
                     _bottomNav.setVisibility(View.VISIBLE);
                 }
             });
    }

    /**
     * This helper method adds the google user to the database if they don't already exist
     * @param googleUser the user to add
     */
    private void addUserToDatabaseIfUserDoesNotExist(User googleUser) {
        Log.d(TAG, "addUserToDatabaseIfUserDoesNotExist: entry");
        FirebaseFirestore.getInstance()
                         .collection("Users")
                         .document(googleUser.getId()).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d(TAG, "addUserToDatabaseIfUserDoesNotExist: checking if user exists");
                        if (!task.getResult().exists()) {
                            Log.d(TAG, "addUserToDatabaseIfUserDoesNotExist: user does not exist, adding to db");
                            // if the user does not exist already, add them to the db collection
                            _userAccountHelper.addNewUserToDatabase(googleUser);
                        }
                    }
                });
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
        _auth.signInWithEmailAndPassword(email, password)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // if log in successful, redirect to home page
                    showMessage(LOGIN_SUCCESSFUL_MESSAGE, Toast.LENGTH_SHORT);

                    DocumentReference userRef = getCurrentUserDocumentReference();
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);

                            saveUserInformation(user);

                            navigateToFragmentWithAction(R.id.action_navigation_login_to_HomeFragment);
                        }
                    });
                } else {
                    showMessage(INCORRECT_EMAIL_PASSWORD_MESSAGE, Toast.LENGTH_LONG);
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
     * @param length the length of the message
     */
    private void showMessage(String message, int length) {
        Toast.makeText(getContext(),
                       message,
                       length).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        // note:  not working currently, but this is what we need to do if we want to persist
        // the login session

//        FirebaseUser currentUser = _auth.getCurrentUser();
//        if (currentUser != null) {
//            navigateToFragmentWithAction(R.id.action_navigation_login_to_HomeFragment);
//        }
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
