package com.m.plantkeeper.auth;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m.plantkeeper.R;
import com.m.plantkeeper.databinding.FragmentLoginFramentBinding;

public class LoginFrament extends Fragment {

    TextInputLayout passwordTextInput, emailTextImput;
    TextInputEditText passwordTextEdit, emailTextEdit;
    MaterialButton logInButton, registerButton;

    public LoginFrament() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLoginFramentBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_frament, container, false);

        passwordTextEdit = dataBinding.passwordTextEdit;
        passwordTextInput = dataBinding.passwordTextInput;
        emailTextEdit = dataBinding.emailTextEdit;
        emailTextImput = dataBinding.emailTextInput;
        logInButton = dataBinding.logInButton;
        registerButton = dataBinding.registerButton;

//        registerButton.setOnClickListener(view -> {
//            ((FragmentNavigation) getActivity()).navigateToFragment(new RegisterFragment(), false);
//        });

        logInButton.setOnClickListener(view -> {
            if (showError(passwordTextEdit.getText())) {
                emailTextEdit.setError("Wrong Email");
                passwordTextInput.setError("or Wrong password");
            } else {
                emailTextEdit.setError(null);
                passwordTextInput.setError(null);
                String email = emailTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
//                ((AuthActivity)getActivity()).sendCredentials(email, password);
            }
        });


        passwordTextEdit.setOnKeyListener((view1, i, keyEvent) -> {
            if (!showError(passwordTextEdit.getText())) {
                passwordTextInput.setError(null); //Clear the error
            }
            return false;
        });

        emailTextEdit.setOnKeyListener((view1, i, keyEvent) -> {
            if (!showError(emailTextEdit.getText())) {
                emailTextEdit.setError(null); //Clear the error
            }
            return false;
        });


        return dataBinding.getRoot();
    }


    private boolean showError(@Nullable Editable text) {
        return false;
    }
}