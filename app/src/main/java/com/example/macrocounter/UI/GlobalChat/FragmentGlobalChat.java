package com.example.macrocounter.UI.GlobalChat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.UserLoged.UserLogedActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentGlobalChat extends AppCompatActivity {

    private String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_global_chat);
        overridePendingTransition(R.anim.enter_slide_from_right,R.anim.exit_slide_to_left);

        Intent intent = getIntent();
        this.userName = intent.getStringExtra("UserName");

        FloatingActionButton fabChangeHistory = findViewById(R.id.fab_history);
        fabChangeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FragmentGlobalChat.this, UserLogedActivity.class);
                myIntent.putExtra("UserName", userName);
                startActivity(myIntent);
                finish();
            }
        });
    }

}