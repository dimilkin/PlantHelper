package com.m.plantkeeper.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.m.plantkeeper.MainActivity;
import com.m.plantkeeper.R;
import com.m.plantkeeper.models.AuthCredentials;
import com.m.plantkeeper.models.AuthResponse;
import com.m.plantkeeper.models.RegistrationInfo;
import com.m.plantkeeper.navigation.Navigation;
import com.m.plantkeeper.navigation.NavigationProviderImpl;
import com.m.plantkeeper.services.AuthService;
import com.m.plantkeeper.services.impl.AuthServiceImpl;
import com.m.plantkeeper.viewmodels.AuthActivityViewModel;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private AuthActivityViewModel authActivityViewModel;
    private Navigation navigationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        navigationProvider = new NavigationProviderImpl();
        ViewModelProvider.Factory factory = new ViewModelProvider.NewInstanceFactory();
        authActivityViewModel = new ViewModelProvider(this, factory).get(AuthActivityViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.authContainer, new LoginFrament())
                    .commit();
        }
    }

    public void authenticateUser(String email, String password) {
        SharedPreferences preferences = getSharedPreferences("CarSafe", Context.MODE_PRIVATE);
        authActivityViewModel.authenticate(email, password);

        authActivityViewModel.credentials.observe(this, authCredentials ->
                preferences.edit()
                        .putString("AUTHTOKEN", authCredentials.getUserToken())
                        .putInt("USERID", authCredentials.getUserId()).apply());

    }


    public void registerNewUser(String email, String password) {
        RegistrationInfo registrationInfo = new RegistrationInfo();
        registrationInfo.setEmail(email);
        registrationInfo.setPassword(password);
    }
}