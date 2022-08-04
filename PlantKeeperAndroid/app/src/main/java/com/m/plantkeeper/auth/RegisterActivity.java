package com.m.plantkeeper.auth;

import static com.m.plantkeeper.Constants.EXTRA_USER_EMAIL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.m.plantkeeper.R;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.services.HashingService;
import com.m.plantkeeper.services.impl.HashingServiceImpl;
import com.m.plantkeeper.viewmodels.AuthActivityViewModel;

import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField, repeatPaswordField;
    private Button signUpBtn;
    private TextView iHaveAnAccountText;
    private CircularProgressIndicator progressIndicator;
    private Navigation navigation;
    private AuthActivityViewModel authViewModel;
    private ActivityResultLauncher<Intent> activityForResultLauncher;
    private HashingService hashingService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpUiElements();
        setupActivityForResultLauncher();
        hashingService = new HashingServiceImpl();

        signUpBtn.setOnClickListener(view -> {
            try {
                registerNewUser();
            } catch (NoSuchAlgorithmException e) {
                Log.e("HASHING ERROR : ", e.getMessage());
                e.printStackTrace();
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
        iHaveAnAccountText.setOnClickListener(view -> navigateToLoginScreen());
    }

    private void registerNewUser() throws NoSuchAlgorithmException {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String repeatedPassword = repeatPaswordField.getText().toString();
        try {
            verifyString(email);
            verifyString(password);
            verifyString(repeatedPassword);
            matchPasswords(password, repeatedPassword);
        } catch (InputMismatchException exception) {
            return;
        }
        showProgressIndicator();
        String hashedPass = hashingService.hashData(password);
        RegistrationInfo registrationInfo = new RegistrationInfo(email, hashedPass);
        registerUserInfo(registrationInfo);
    }

    private void registerUserInfo(RegistrationInfo registrationInfod) {
        authViewModel.registerNewUser(registrationInfod);
        authViewModel.isUserRegistered().observe(this, successfulRegistration -> {
            if (successfulRegistration) {
                Toast.makeText(this, "Registaration Successful! Please confirm email!", Toast.LENGTH_LONG).show();
                requestProfileActivation(registrationInfod.getEmail());
            } else {
                Toast.makeText(this, "Invalid mail! Registration Failed!", Toast.LENGTH_SHORT).show();
                hideProgressIndicator();
            }
        });
    }
    private void requestProfileActivation(String userMail) {
        Intent intent = new Intent(this, AccountActivationActivity.class);
        intent.putExtra(EXTRA_USER_EMAIL, userMail);
        activityForResultLauncher.launch(intent);
    }

    private void setupActivityForResultLauncher() {
        activityForResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                navigateToLoginScreen();
            }
        });
    }
    private void navigateToLoginScreen() {
        navigation.navigateToActivity(this, LoginActivity.class);
    }

    private void setUpUiElements() {
        navigation = new NavigationProviderImpl();
        authViewModel = new ViewModelProvider(this).get(AuthActivityViewModel.class);
        emailField = findViewById(R.id.registrationEmail);
        passwordField = findViewById(R.id.registrationPassword);
        repeatPaswordField = findViewById(R.id.repeatRegistrationPassword);
        signUpBtn = findViewById(R.id.completeRegistrationButton);
        iHaveAnAccountText = findViewById(R.id.haveAnAccountField);
        progressIndicator = findViewById(R.id.registerProgressIndicator);
        progressIndicator.hide();
    }

    private void showProgressIndicator() {
        emailField.setVisibility(View.INVISIBLE);
        passwordField.setVisibility(View.INVISIBLE);
        repeatPaswordField.setVisibility(View.INVISIBLE);
        signUpBtn.setClickable(false);
        progressIndicator.show();
    }

    private void hideProgressIndicator() {
        emailField.setVisibility(View.VISIBLE);
        passwordField.setVisibility(View.VISIBLE);
        repeatPaswordField.setVisibility(View.VISIBLE);
        signUpBtn.setClickable(true);
        progressIndicator.hide();
    }


    private void verifyString(String entry) {
        if (null == entry || entry.trim().isEmpty()) {
            Toast.makeText(this, "Please provide valid mail and passwords", Toast.LENGTH_SHORT).show();
            throw new InputMismatchException();
        }
    }

    private void matchPasswords(String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            throw new InputMismatchException();
        }
    }
}