package com.example.habitsmasher;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

/**
 * This class holds the front-end elements related to the user sign up page
 * Author: Rudy Patel
 */
public class UserRegistrationFragment extends Fragment {
    private static final String USER_REGISTERED_MESSAGE = "User registered!";
    private static final String FAILED_TO_ADD_USER_MESSAGE = "Failed to add user, try again!";
    private static final String FAILED_TO_REGISTER_MESSAGE = "Failed to register with this username/email";
    private static final String TAG = "UserRegistrationFragment";
    private static final String USERS_COLLECTION_PATH = "Users";
    private static final String USERNAME_FIELD = "username";
    private static final String THIS_USERNAME_IS_ALREADY_TAKEN_MESSAGE = "This username is already taken!";
    private static final String PATH_TO_DEFAULT_USER_IMG = "android.resource://com.example.habitsmasher/drawable/placeholder_profile_picture";

    private FirebaseAuth _auth;
    private ProgressBar _progressBar;
    protected ImageView _profilePictureView;
    protected Uri _selectedImage;
    private UserAccountHelper _userAccountHelper;

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
        _profilePictureView = view.findViewById(R.id.registration_image);

        _userAccountHelper = new UserAccountHelper(getContext(), this);

        setClickListenerForBackToLoginButton(backToLoginButton);

        setClickListenerForRegisterButton(emailInput, passwordInput, usernameInput, registerButton);

        setImageViewListener();

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
                _progressBar.setVisibility(View.VISIBLE);

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

                checkForUniqueUsername(isUsernameTaken -> {
                    if (isUsernameTaken) {
                        Log.d(TAG, "This username already exists");

                        usernameInput.setError(THIS_USERNAME_IS_ALREADY_TAKEN_MESSAGE);
                        usernameInput.requestFocus();

                        _progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d(TAG, "Username is unique!");

                        createNewUserWithEmailAndPassword(validator.getValidEmailForSignUp(),
                                                          validator.getValidPasswordForSignUp(),
                                                          validator.getValidUsernameForSignUp());
                    }
                }, usernameInput.getText().toString().trim());
            }
        });
    }

    /**
     * This method checks if the username specified is already taken
     * @param firestoreCallback an instance of the firestore callback to ensure the operation completes
     * @param username the username to check
     */
    private void checkForUniqueUsername(FirestoreCallback firestoreCallback, String username) {
        Log.d(TAG, "Fetching users");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollectionRef = db.collection(USERS_COLLECTION_PATH);

        usersCollectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()) {
                if (document.get(USERNAME_FIELD).toString().equals(username)) {
                    Log.d(TAG, "Username taken: " + document.get(USERNAME_FIELD).toString());

                    firestoreCallback.onCallback(true);  // if username is taken, set the flag
                    return;
                } else {
                    Log.d(TAG, "Username does not equal: " + document.get(USERNAME_FIELD).toString());
                }
            }
            Log.d(TAG, "Finished fetching users, username does not exist");
            firestoreCallback.onCallback(false);  // if username is not taken, set the flag to false
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
        String encryptedPassword = password;
        _auth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                         User user = new User(_auth.getUid(),
                                              username,
                                              email,
                                 encryptedPassword);

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
                         .set(_userAccountHelper.buildUserDataMap(user))
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
        addUserImageToDatabase(user.getId());
    }

    /**
     * @param userId The ID of the user for which the picture is uploaded
     */
    public void addUserImageToDatabase(String userId) {
        Uri toUpload = _selectedImage;

        if (toUpload == null) {
            toUpload = Uri.parse(PATH_TO_DEFAULT_USER_IMG);
        }

        // Get firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/" + userId + "/" + "userImage");

        ref.putFile(toUpload)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Image uploaded.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Image failed to upload");
                        addUserImageToDatabase(userId);
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

    /**
     * Not touching this when refactoring until images are fully implemented for habit events
     * Reference: https://stackoverflow.com/questions/10165302/dialog-to-pick-image-from-gallery-or-from-camera
     * Override onActivityResult to handle when user has selected image
     * @param requestCode
     * @param resultCode
     * @param imageReturnedIntent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            // Set selected picture
            _selectedImage = imageReturnedIntent.getData();
            _profilePictureView.setImageURI(_selectedImage);
        }
    }

    protected void setImageViewListener() {
        _profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open gallery to let user pick photo
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
    }
}
