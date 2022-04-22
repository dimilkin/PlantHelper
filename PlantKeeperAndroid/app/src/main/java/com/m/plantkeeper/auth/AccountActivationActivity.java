package com.m.plantkeeper.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.m.plantkeeper.R;

public class AccountActivationActivity extends AppCompatActivity {

    EditText firstNumber, secondNumber, thirdNumber, forthNumber;
    MaterialButton activateButton;
    String first, second, third, forth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activation);

        initializeView();
        setTextListeners();
        activateButton.setOnClickListener( v -> activateProfile());


    }

    private void activateProfile() {

        String code = first + second + third + forth;
        Toast.makeText(this, code, Toast.LENGTH_SHORT).show();

    }

    private void initializeView() {
        firstNumber = findViewById(R.id.activatinFirstNumber);
        secondNumber = findViewById(R.id.activatinSecondNumber);
        thirdNumber = findViewById(R.id.activatinThirdNumber);
        forthNumber = findViewById(R.id.activatinForthNumber);
        activateButton = findViewById(R.id.activateProfileButton);
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
