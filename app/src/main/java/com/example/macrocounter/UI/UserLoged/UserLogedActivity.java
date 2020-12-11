package com.example.macrocounter.UI.UserLoged;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.cifrado.CifradoPropio;
import com.example.macrocounter.UI.model.HistorialItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
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

                            int hidrato = Integer.parseInt(String.valueOf(txtEditHidrato.getText()));
                            int proteina = Integer.parseInt(String.valueOf(txtEditProteina.getText()));
                            int grasa = Integer.parseInt(String.valueOf(txtEditGrasa.getText()));

                            int calorias = (hidrato*4)+(proteina*4)+(grasa*9);
                          //  Toast.makeText(getApplicationContext(),Integer.toString(calorias),Toast.LENGTH_SHORT).show();


                                adapterHistorial.updateList(calorias);


                            //connect firebase to update value and set the value in the label caloriesAmount

                            CalRe(userName,calorias);

                            /*currentProject.getListNodos().get(position).setNodesAmount(Integer.parseInt(txtEditAmount.getText().toString()));
                            currentProject.getListNodos().get(position).setSubredDescriptcion(txtEditAmount.getText().toString());
                            currentProject.recalculateNodesRange();

                            adapterSubRedesProject.notifyDataSetChanged();*/

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

    }
    CollectionReference mCollecRefCal;

    private ArrayList<HistorialItem> getDataFromServer(String userName) {

        final ArrayList<HistorialItem> historialItemArrayList = new ArrayList<>();

        //call firebase for the data
        mCollecRefCal =  macroDb.collection("Entrada");
        mCollecRefCal.document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Map<String,Object> map =documentSnapshot.getData();

                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MMMM/yyyy");
                    Date todayDate = new Date();
                    String thisDate = currentDate.format(todayDate);

                    for (Map.Entry<String, Object> entry : map.entrySet()) {

                        todayDate.setTime(Long.parseLong(entry.getKey()));
                        thisDate = currentDate.format(todayDate);
                        historialItemArrayList.add (new HistorialItem(Integer.parseInt((String) entry.getValue()),thisDate));


                    }

                }
            }
        });



        return historialItemArrayList;
    }


    public void CalRe(String username, int calorias){
        mCollecRefCal =  macroDb.collection("Entrada");

      //  HashMap<String, Object> map2 = new HashMap<>();
      //  map2.put(map);

        HashMap<String, Object> map = new HashMap<>();

     //   map.put("id",username);
        map.put(new Date().getTime()+"", calorias);
     //   map.put("CalorieAmount",calorias);

      //  macroDb.collection("Entrada").document(username).set(map);

        macroDb.collection("Entrada").document( username).set(map, SetOptions.merge());
    }




}