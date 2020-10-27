package com.example.macrocounter.UI.UserLoged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.macrocounter.R;

public class UserLogedActivity extends AppCompatActivity {

    AdapterHistorial adapterHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loged);

        RecyclerView recyclerViewHistorial = findViewById(R.id.recyclerHistory);
        adapterHistorial = new AdapterHistorial();
        recyclerViewHistorial.setAdapter(adapterHistorial);
        recyclerViewHistorial.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));
    }
}