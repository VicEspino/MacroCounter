package com.example.macrocounter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.macrocounter.UI.Login.LoginViewModel;
import com.example.macrocounter.UI.UserLoged.UserLogedActivity;
import com.example.macrocounter.UI.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    MainActivity thisLoginActivity ;
    ProgressBar progressBarLogin;
    EditText editUsername;
    EditText editPass;
    FirebaseFirestore MacroDb;
    String userN;
    String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.thisLoginActivity=this;

        Button btnSignInRegister = findViewById(R.id.btnLogin);
        LinearLayout btnSignInGoogle = findViewById(R.id.btnSignInGoogle);
        progressBarLogin = findViewById(R.id.progress_circular_login);

        editUsername = findViewById(R.id.editUsername);
        editPass = findViewById(R.id.editPassword);

       userN = editUsername.getText().toString();
        pass = editPass.getText().toString();

      //  FirebaseFirestore db = FirebaseFirestore.getInstance();
        MacroDb = FirebaseFirestore.getInstance();





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
                if(editUsername.getText().toString().isEmpty()){
                    editUsername.setError("This field is required.");
                    progressBarLogin.setVisibility(View.GONE);

                    return;
                }
                if(editPass.getText().toString().isEmpty()){
                    editPass.setError("This field is required.");
                    progressBarLogin.setVisibility(View.GONE);
                    return;
                }

                login(
                        editUsername.getText().toString(),
                        editPass.getText().toString()
                );

               /* new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                          //  crearDatos();
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        loginViewModel.getLogingUserStats().postValue(new User(editUsername.getText().toString(),editPass.getText().toString()));

                    }
                }).start();*/
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



    CollectionReference mCollecRefUsers;

    public void login(final String username, final String password) {

        progressBarLogin.setVisibility(View.VISIBLE);

        mCollecRefUsers =  MacroDb.collection("Usuario");

        mCollecRefUsers.document(username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            /*HashMap<String,Object> vic = (HashMap<String, Object>) documentSnapshot.get(username);*/
                            progressBarLogin.setVisibility(View.GONE);
                            Map<String,Object> vic =documentSnapshot.getData();

                            if(username.equals(vic.get("UserName"))&&password.equals(vic.get("Password"))){
                                //startActivityVic(username);

                            }else if(username.equals(vic.get("UserName"))&&!password.equals(vic.get("Password"))){
                                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                                builder.setTitle("Wrong password.")
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                builder.create().show();
                            }


                            System.out.println("asd");
                        }else{
                            progressBarLogin.setVisibility(View.GONE);
                            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                            builder.setMessage("Would you like to create it?")
                                    .setTitle("User doesn't exists")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            /*Invocar firebase y crear*/
                                            HashMap<String, Object> map = new HashMap<>();

                                            map.put("UserName",username);
                                            map.put("Password",password);

                                            /* db.collection("vlsm").document("users").*/
                                            mCollecRefUsers.document(username)
                                                    .set(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                           // startActivityVic(username);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                                                            builder.setMessage("Try later")
                                                                    .setTitle("Can't creat it");
                                                            builder.show();
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton("No",new  DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            /*ignore*/


                                        }
                                    });

                            builder.create().show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBarLogin.setVisibility(View.GONE);
                        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                        builder.setMessage("Can't get the data from the server")
                                .setTitle("Please try later.")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                        builder.create().show();
                    }
                });


    }

   /* public void startActivityVic(String username){
        setResult(Activity.RESULT_OK);

        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
        myIntent.putExtra("Username", username); //Optional parameters
        MainActivity.this.startActivity(myIntent);

        finish();
    }*/



   /* private void crearDatos(){
        Map<String,Object> map = new HashMap<>();
        map.put("UserName", userN);
        map.put("Pasword", pass);
        macroccounter.collection("Usuario").document().set(map);
    }*/


    boolean validateUser(User user){
        if(!user.getUser().isEmpty() && !user.getPassword().isEmpty()){
            return true;
        }
        return false;
    }

}