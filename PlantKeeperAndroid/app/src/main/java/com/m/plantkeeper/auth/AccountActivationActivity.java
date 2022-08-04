package com.m.plantkeeper.auth;

import static com.m.plantkeeper.Constants.EXTRA_USER_EMAIL;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.m.plantkeeper.R;
import com.m.plantkeeper.models.dtos.AccountActivationDto;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.viewmodels.AuthActivityViewModel;

public class AccountActivationActivity extends AppCompatActivity {

    private LinearLayoutCompat numbersFields;
    private EditText firstNumber, secondNumber, thirdNumber, forthNumber;
    private MaterialButton activateButton;
    private CircularProgressIndicator progressIndicator;
    private String first, second, third, forth;
    private AuthActivityViewModel viewModel;
    private Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activation);

        initializeView();
        setTextListeners();
        Intent intent = getIntent();
        String email = intent.getStringExtra(EXTRA_USER_EMAIL);
        activateButton.setOnClickListener(v -> activateProfile(email));
    }

    private void activateProfile(String email) {
        showProgressIndicator();
        String code = first + second + third + forth;
        AccountActivationDto activationDto = new AccountActivationDto();
        activationDto.setActivationCode(code);
        activationDto.setUserEmail(email);
        viewModel.activateNewUser(activationDto);
        viewModel.isUserActivated().observe(this, successfulActivation -> {
            if (successfulActivation) {
                Toast.makeText(this, "Activation Successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                setResult(200, intent);
                finish();
                navigation.navigateToActivity(this, LoginActivity.class);
            } else {
                Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
                hideProgressIndicator();
            }
        });
    }

    private void initializeView() {
        firstNumber = findViewById(R.id.activatinFirstNumber);
        secondNumber = findViewById(R.id.activatinSecondNumber);
        thirdNumber = findViewById(R.id.activatinThirdNumber);
        forthNumber = findViewById(R.id.activatinForthNumber);
        activateButton = findViewById(R.id.activateProfileButton);
        numbersFields = findViewById(R.id.numbersHolder);
        progressIndicator = findViewById(R.id.activationProgressIndicator);
        viewModel = new ViewModelProvider(this).get(AuthActivityViewModel.class);
        progressIndicator.hide();
        navigation = new NavigationProviderImpl();
    }

    private void showProgressIndicator() {
        numbersFields.setVisibility(View.INVISIBLE);
        activateButton.setClickable(false);
        progressIndicator.show();
    }

    private void hideProgressIndicator() {
        numbersFields.setVisibility(View.VISIBLE);
        activateButton.setClickable(true);
        progressIndicator.hide();
    }

    @Override
    public void onBackPressed() {
        navigation.navigateToActivity(this, LoginActivity.class);
    }

    private void setTextListeners() {
        firstNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (firstNumber.getText().toString().length() == 1) {
                    first = firstNumber.getText().toString();
                    secondNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (secondNumber.getText().toString().length() == 1) {
                    second = secondNumber.getText().toString();
                    thirdNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        thirdNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (thirdNumber.getText().toString().length() == 1) {
                    third = thirdNumber.getText().toString();
                    forthNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forthNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (forthNumber.getText().toString().length() == 1) {
                    forth = forthNumber.getText().toString();
                    activateButton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
