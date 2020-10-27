package com.example.macrocounter.UI.Login;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.model.User;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    EditText editUsername;
    EditText editPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSignInRegister = findViewById(R.id.btnLogin);
        LinearLayout btnSignInGoogle = findViewById(R.id.btnSignInGoogle);
        final ProgressBar progressBarLogin = findViewById(R.id.progress_circular_login);

        editUsername = findViewById(R.id.editUsername);
        editPass = findViewById(R.id.editPassword);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getLogingUserStats().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                progressBarLogin.setVisibility(View.GONE);

            }
        });


        btnSignInRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarLogin.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(validateUser()){
                            loginViewModel.getLogingUserStats().postValue(new User());
                        }
                    }
                }).start();
            }
        });

        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarLogin.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(validateUser()){
                            loginViewModel.getLogingUserStats().postValue(new User());
                        }
                    }
                }).start();
            }
        });


    }


    boolean validateUser(){
        if(!this.editPass.getText().toString().isEmpty() && !this.editUsername.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

}