package com.m.plantkeeper.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.m.plantkeeper.R;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.viewmodels.AuthActivityViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField, repeatPaswordField;
    private Button signUpBtn;
    private TextView iHaveAnAccountText;
    private Navigation navigation;
    private AuthActivityViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = findViewById(R.id.registrationEmail);
        passwordField = findViewById(R.id.registrationPassword);
        repeatPaswordField = findViewById(R.id.repeatRegistrationPassword);
        signUpBtn = findViewById(R.id.completeRegistrationButton);
        iHaveAnAccountText = findViewById(R.id.haveAnAccountField);
        navigation = new NavigationProviderImpl();
        authViewModel = new ViewModelProvider(this).get(AuthActivityViewModel.class);

        signUpBtn.setOnClickListener(view -> registerNewUser());
        iHaveAnAccountText.setOnClickListener(view -> navigateToLoginScreen());
    }

    private void registerNewUser() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String repeatedPassword = repeatPaswordField.getText().toString();
        RegistrationInfo registrationInfo = createUserInfo(email, password, repeatedPassword);
        registerUserInfo(registrationInfo);
    }

    private void registerUserInfo(RegistrationInfo registrationInfod) {
        authViewModel.registerNewUser(registrationInfod);
        authViewModel.isUserRegistered().observe(this, successfulRegistration -> {
            if (successfulRegistration) {
                Toast.makeText(this, "Registaration Successful! Please confirm email!", Toast.LENGTH_LONG).show();
                navigateToLoginScreen();
            } else {
                Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RegistrationInfo createUserInfo(String email, String password, String repeatedPassword) {
        if (email.trim().length() == 0 || password.trim().length() == 0 || repeatedPassword.trim().length() == 0) {
            Toast.makeText(this, "Please provide email and password", Toast.LENGTH_SHORT).show();
        }
        if (!password.equals(repeatedPassword)) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
        }
        return new RegistrationInfo(email, password);
    }

    private void navigateToLoginScreen() {
        navigation.navigateToActivity(this, LoginActivity.class);
    }

    private void navigateToAccountActivation() {
        navigation.navigateToActivity(this, AccountActivationActivity.class);
    }
}