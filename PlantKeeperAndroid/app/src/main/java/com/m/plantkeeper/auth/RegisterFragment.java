package com.m.plantkeeper.auth;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.m.plantkeeper.R;
import com.m.plantkeeper.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    EditText emailEditText, passwordEditText, repeatPasswordEditText;
    Button registrationButton;
    TextView iHaveAnAccountText;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRegisterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);

        emailEditText = dataBinding.registrationEmail;
        passwordEditText = dataBinding.registrationPassword;
        repeatPasswordEditText = dataBinding.repeatRegistrationPassword;
        registrationButton = dataBinding.completeRegistrationButton;
        iHaveAnAccountText = dataBinding.haveAnAccountField;


        registrationButton.setOnClickListener( b-> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String repeatedPassword = repeatPasswordEditText.getText().toString();
            if (email.trim().length() == 0 ){
                Toast.makeText(getContext(), "Incorrect mail", Toast.LENGTH_SHORT).show();
            }else if (!repeatedPassword.equals(password)) {
                Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            }else {
//                ((AuthActivity) getActivity()).registerNewUser(email, password);
                Toast.makeText(getContext(), "Registration successful! Please confirm email", Toast.LENGTH_LONG).show();
                navigateToLoginFragment();
            }
        });

        iHaveAnAccountText.setOnClickListener( v-> {
            navigateToLoginFragment();
        });


        return dataBinding.getRoot();
    }

    private void navigateToLoginFragment(){
//        ((FragmentNavigation) getActivity()).navigateToFragment(new LoginFrament(), false);
    }
}