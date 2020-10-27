package com.example.macrocounter.UI.Login;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.UserLoged.UserLogedActivity;
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

                if(user!=null)
                    if(validateUser(user)){
                        Intent myIntent = new Intent(MainActivity.this, UserLogedActivity.class);
                        startActivity(myIntent);
                        finish();
                    }

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

                        loginViewModel.getLogingUserStats().postValue(new User(editUsername.getText().toString(),editPass.getText().toString()));

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
                        loginViewModel.getLogingUserStats().postValue(new User(editUsername.getText().toString(),editPass.getText().toString()));

                    }
                }).start();
            }
        });


    }


    boolean validateUser(User user){
        if(!user.getUser().isEmpty() && !user.getPassword().isEmpty()){
            return true;
        }
        return false;
    }

}