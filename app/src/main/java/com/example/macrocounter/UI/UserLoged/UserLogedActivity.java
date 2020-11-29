package com.example.macrocounter.UI.UserLoged;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.model.HistorialItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserLogedActivity extends AppCompatActivity {

    AdapterHistorial adapterHistorial;
    AppCompatActivity myThis;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loged);

         myThis = this;


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

    private ArrayList<HistorialItem> getDataFromServer(String userName) {

        ArrayList<HistorialItem> historialItemArrayList = new ArrayList<>();

        //call firebase for the data


        return historialItemArrayList;
    }
}