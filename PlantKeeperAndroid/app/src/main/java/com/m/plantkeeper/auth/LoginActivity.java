package com.m.plantkeeper.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.m.plantkeeper.MainActivity;
import com.m.plantkeeper.R;
import com.m.plantkeeper.models.AuthCredentials;
import com.m.plantkeeper.models.AuthResponse;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.services.AuthService;
import com.m.plantkeeper.services.impl.AuthServiceImpl;
import com.m.plantkeeper.ui.PlantsListFragment;
import com.m.plantkeeper.viewmodels.AuthActivityViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button signinBtn, signUpBtn;
    private TextView forgotPasswordText;
    private Navigation navigation;
    private AuthActivityViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.email_text_edit);
        passwordField = findViewById(R.id.password_text_edit);
        signinBtn = findViewById(R.id.signInButton);
        signUpBtn = findViewById(R.id.signUpButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        navigation = new NavigationProviderImpl();
        authViewModel = new ViewModelProvider(this).get(AuthActivityViewModel.class);


        signinBtn.setOnClickListener(view -> authenticate());
        signUpBtn.setOnClickListener(view -> navigateToSignUpScreen());
    }

    private void navigateToSignUpScreen() {
        navigation.navigateToActivity(LoginActivity.this, RegisterActivity.class);
    }


    private void authenticate() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        authViewModel.authenticate(email, password);
        authViewModel.isUserAuthenticated().observe(this, this::getCredentials);

    }

    private void getCredentials(boolean isAuthenticated) {
        if (isAuthenticated) {
            authViewModel.getCredentials().observe(this, credentials -> {
                SharedPreferences preferences = getSharedPreferences("CarSafe", Context.MODE_PRIVATE);
                preferences.edit().putString("AUTHTOKEN", credentials.getUserToken()).putInt("USERID", credentials.getUserId()).apply();
            });
            navigation.navigateToActivity(this, MainActivity.class);
        } else {
            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
        }
    }
}