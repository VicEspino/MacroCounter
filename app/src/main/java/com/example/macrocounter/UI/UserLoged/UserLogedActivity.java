package com.example.macrocounter.UI.UserLoged;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.GlobalChat.FragmentGlobalChat;
import com.example.macrocounter.UI.model.HistorialItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.macrocounter.UI.Login.MainActivity.userN;

public class UserLogedActivity extends AppCompatActivity {

    AdapterHistorial adapterHistorial;
    AppCompatActivity myThis;
    String userName;
    String userAux;
    FirebaseFirestore macroDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loged);
        overridePendingTransition(R.anim.enter_slide_from_right,R.anim.exit_slide_to_left);

         myThis = this;

        userAux=userN;
        macroDb = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        this.userName = intent.getStringExtra("UserName");

        RecyclerView recyclerViewHistorial = findViewById(R.id.recyclerHistory);
        adapterHistorial = new AdapterHistorial(getDataFromServer(userName));
        recyclerViewHistorial.setAdapter(adapterHistorial);
        recyclerViewHistorial.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));



        FloatingActionButton fabAddCalories = findViewById(R.id.fab_addCalories);
        fabAddCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    /*Editing*/
                    final AlertDialog.Builder builder = new AlertDialog.Builder(myThis);
                    View dialogEdit = LayoutInflater.from(myThis).inflate(R.layout.dialog_edit_subred, null);
                    final EditText txtEditHidrato = dialogEdit.findViewById(R.id.txt_edit_node_hidrato);
                    final EditText txtEditProteina = dialogEdit.findViewById(R.id.txt_edit_node_proteina);
                    final EditText txtEditGrasa = dialogEdit.findViewById(R.id.txt_edit_node_grasa);

                    Button buttonOk = dialogEdit.findViewById(R.id.button_ok);
                    Button buttonCancel = dialogEdit.findViewById(R.id.button_cancel);

                    builder.setView(dialogEdit);

                    final AlertDialog alertDialog = builder.create();

                    buttonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(txtEditHidrato.getText().toString().isEmpty())
                                txtEditHidrato.setText(0+"");
                            if(txtEditGrasa.getText().toString().isEmpty())
                                txtEditGrasa.setText(0+"");
                            if(txtEditProteina.getText().toString().isEmpty())
                                txtEditProteina.setText(0+"");

                            int hidrato = Integer.parseInt(String.valueOf(txtEditHidrato.getText()));
                            int proteina = Integer.parseInt(String.valueOf(txtEditProteina.getText()));
                            int grasa = Integer.parseInt(String.valueOf(txtEditGrasa.getText()));

                            int calorias = (hidrato*4)+(proteina*4)+(grasa*9);

                            calRe(userName,adapterHistorial.updateList(calorias));

                            alertDialog.dismiss();
                        }
                    });

                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();

                }
            }
        });

        FloatingActionButton fabChangeChat = findViewById(R.id.fab_chat);
        fabChangeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UserLogedActivity.this, FragmentGlobalChat.class);
                myIntent.putExtra("UserName",userName);
                startActivity(myIntent);
                finish();
            }
        });

    }
    CollectionReference mCollecRefCal;

    private ArrayList<HistorialItem> getDataFromServer(String userName) {

        final ArrayList<HistorialItem> historialItemArrayList = new ArrayList<>();

        mCollecRefCal =  macroDb.collection("Entrada");
        mCollecRefCal.document(userName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Map<String,Object> map =documentSnapshot.getData();


                    for (Map.Entry<String, Object> entry : map.entrySet()) {

                        //todayDate.setTime(Long.parseLong(entry.getKey()));
                        /*thisDate = currentDate.format(todayDate);*/
                        Date parseDate = new Date();
                        try {
                            parseDate = HistorialItem.FORMAT_DATE_ITEM_HISTORIAL.parse(entry.getKey());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        historialItemArrayList.add(
                                 new HistorialItem((int) (long) entry.getValue(), parseDate)
                         );


                    }
                    adapterHistorial.notifyDataSetChanged();

                }
            }
        });



        return historialItemArrayList;
    }


    public void calRe(String username, int calorias){
        mCollecRefCal =  macroDb.collection("Entrada");

        Date todayDate = new Date();
        String thisDate = HistorialItem.FORMAT_DATE_ITEM_HISTORIAL.format(todayDate);

        HashMap<String, Object> map = new HashMap<>();

        map.put(thisDate, calorias);

        macroDb.collection("Entrada").document( username).set(map, SetOptions.merge());
    }




}