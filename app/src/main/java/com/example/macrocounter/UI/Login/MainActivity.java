package com.example.macrocounter.UI.Login;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.UserLoged.UserLogedActivity;
import com.example.macrocounter.UI.cifrado.CifradoPropio;
import com.example.macrocounter.UI.cifrado.Transposicion;
import com.example.macrocounter.UI.model.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    MainActivity thisLoginActivity ;
    ProgressBar progressBarLogin;
    EditText editUsername;
    EditText editPass;
    FirebaseFirestore macroDb;
    String userN;
    String pass;
    //comentairio

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.thisLoginActivity=this;

        Button btnSignInRegister = findViewById(R.id.btnLogin);
        progressBarLogin = findViewById(R.id.progress_circular_login);

        editUsername = findViewById(R.id.editUsername);
        editPass = findViewById(R.id.editPassword);

        userN = editUsername.getText().toString();
        pass = editPass.getText().toString();
        macroDb = FirebaseFirestore.getInstance();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getLogingUserStats().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                progressBarLogin.setVisibility(View.GONE);
                if(user!=null)
                    if(validateUser(user)){
                        /*Intent myIntent = new Intent(MainActivity.this, UserLogedActivity.class);
                        startActivity(myIntent);
                        finish();*/
                        try {
                            login(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
               new Thread(new Runnable() {
                    @Override
                    public void run() {
                      //  crearDatos();
                        User user = new User(
                                Transposicion.cifrar( editUsername.getText().toString(),true ),
                                Transposicion.cifrar( editPass.getText().toString(),true )
                        );
                        loginViewModel.getLogingUserStats().postValue(user);
                    }
                }).start();
            }
        });

    }



    CollectionReference mCollecRefUsers;

    public void login(User user) throws Exception {login(user.getUser(),user.getPassword());}


    public void login(final String username, final String password) throws Exception {

        final String userNameDecrypted =  Transposicion.decifrar(username);
        final String passwordDecrypted = Transposicion.decifrar(password);

        progressBarLogin.setVisibility(View.VISIBLE);

        mCollecRefUsers =  macroDb.collection("Usuario");

        mCollecRefUsers.document( userNameDecrypted ).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            /*HashMap<String,Object> vic = (HashMap<String, Object>) documentSnapshot.get(username);*/
                            progressBarLogin.setVisibility(View.GONE);
                            Map<String,Object> vic =documentSnapshot.getData();

                            String userNameDB = null;
                            String passwordDB = null;
                            try {
                                userNameDB = Transposicion.decifrar( (String) vic.get("UserName"));
                                 passwordDB = Transposicion.decifrar ((String) vic.get("Password"));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if(userNameDecrypted.equals(userNameDB)&&passwordDecrypted.equals(passwordDB)){
                                startActivityLoged(userNameDecrypted);

                            }else if(userNameDecrypted.equals(userNameDB)&&!passwordDecrypted.equals(passwordDB) ){

                                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                                builder.setTitle("Wrong password.")
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                builder.create().show();
                            }

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
                                            try {
                                                mCollecRefUsers.document(userNameDecrypted )
                                                        .set(map)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                               startActivityLoged(userNameDecrypted);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(thisLoginActivity);
                                                                builder.setMessage("Try later")
                                                                        .setTitle("Can't creat it");
                                                                builder.show();
                                                            }
                                                        });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
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

    public void startActivityLoged(String username){
        setResult(Activity.RESULT_OK);

        Intent myIntent = new Intent(this, UserLogedActivity.class);
        myIntent.putExtra("UserName", username); //Optional parameters
        MainActivity.this.startActivity(myIntent);
        finish();
    }


    boolean validateUser(User user){
        if(!user.getUser().isEmpty() && !user.getPassword().isEmpty()){
            return true;
        }
        return false;
    }

}