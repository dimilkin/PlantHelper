package com.m.plantkeeper.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.m.plantkeeper.MainActivity;
import com.m.plantkeeper.R;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.services.HashingService;
import com.m.plantkeeper.services.impl.HashingServiceImpl;
import com.m.plantkeeper.viewmodels.AuthActivityViewModel;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button signinBtn, signUpBtn;
    private LinearLayout buttonsGroup;
    private TextView forgotPasswordText, signInError;
    private Navigation navigation;
    private AuthActivityViewModel authViewModel;
    private HashingService hashingService;
    private CircularProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();
        navigation = new NavigationProviderImpl();
        authViewModel = new ViewModelProvider(this).get(AuthActivityViewModel.class);
        hashingService = new HashingServiceImpl();
        signinBtn.setOnClickListener(view -> {
            try {
                signinBtn.setActivated(false);
                buttonsGroup.setVisibility(View.GONE);
                progressIndicator.show();
                authenticate();
            } catch (NoSuchAlgorithmException e) {
                signinBtn.setActivated(true);
                buttonsGroup.setVisibility(View.VISIBLE);
                progressIndicator.hide();
            }
        });
        signUpBtn.setOnClickListener(view -> navigateToSignUpScreen());
    }

    private void navigateToSignUpScreen() {
        navigation.navigateToActivity(LoginActivity.this, RegisterActivity.class);
    }

    private void authenticate() throws NoSuchAlgorithmException {
        signInError.setVisibility(View.GONE);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String hashedPassword = hashingService.hashData(password);
        authViewModel.authenticate(email, hashedPassword);
        authViewModel.isUserAuthenticated().observe(this, this::navigateToMainActivity);
        authViewModel.getIsAuthenticationLoading().observe(this, isLoading ->{
            if (isLoading != null){
                if (isLoading){
                    progressIndicator.show();
                    buttonsGroup.setVisibility(View.GONE);
                }
                if (!isLoading){
                    progressIndicator.hide();
                    buttonsGroup.setVisibility(View.VISIBLE);
                }
            }
        });
        authViewModel.getIsAuthenticationError().observe(this, isError -> {
            if (isError != null && isError){
                signInError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void navigateToMainActivity(boolean isAuthenticated) {
        if (isAuthenticated) {
            signInError.setVisibility(View.GONE);
            navigation.navigateToActivity(this, MainActivity.class);
        }
    }

    private void initializeView(){
        emailField = findViewById(R.id.email_text_edit);
        passwordField = findViewById(R.id.password_text_edit);
        signinBtn = findViewById(R.id.signInButton);
        signUpBtn = findViewById(R.id.signUpButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        buttonsGroup = findViewById(R.id.buttonsGroupLayout);
        progressIndicator = findViewById(R.id.loginProgressIndicator);
        progressIndicator.hide();
        signInError = findViewById(R.id.signInError);
        signInError.setVisibility(View.GONE);
    }
}