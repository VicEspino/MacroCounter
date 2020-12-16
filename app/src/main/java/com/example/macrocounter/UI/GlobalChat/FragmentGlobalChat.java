package com.example.macrocounter.UI.GlobalChat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.UserLoged.UserLogedActivity;
import com.example.macrocounter.UI.model.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentGlobalChat extends AppCompatActivity {

    private String userName;
    private FirebaseFirestore firebase;
    private CollectionReference chatGlobalCollectionReference;
    private AdapterGlobalChat adapterGlobalChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_global_chat);
        overridePendingTransition(R.anim.enter_slide_from_right,R.anim.exit_slide_to_left);
        this.firebase = FirebaseFirestore.getInstance();
        this.chatGlobalCollectionReference = this.firebase.collection("GlobalChat");

        RecyclerView recyclerViewChat = findViewById(R.id.recycler_global_chat);
        this.adapterGlobalChat = new AdapterGlobalChat(getCurrentMessages(userName));
        recyclerViewChat.setAdapter(adapterGlobalChat);
        recyclerViewChat.setItemAnimator(new DefaultItemAnimator());
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

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

    public ArrayList<Message> getCurrentMessages(final String userName){

        final ArrayList<Message> messageArrayList = new ArrayList<>();

        this.chatGlobalCollectionReference.orderBy("date", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                for(DocumentSnapshot currentDocumentSnapshot : documents){
                    String userNameMsg = (String) currentDocumentSnapshot.get("user");
                    String msg = (String) currentDocumentSnapshot.get("msg");
                    //String firebaseDate = (String) currentDocumentSnapshot.get("date");
                    Date messageDate = ((Timestamp) currentDocumentSnapshot.get("date")).toDate();

                    messageArrayList.add(new Message(userName,msg,messageDate));

                }

                adapterGlobalChat.notifyDataSetChanged();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //do something
            }
        });


          /*      addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Map<String,Object> map =documentSnapshot.getData();

                   *//* SimpleDateFormat currentDate = new SimpleDateFormat("dd/MMMM/yyyy");
                    Date todayDate = new Date();*//*
                    *//*String thisDate = currentDate.format(todayDate);*//*

                    for (Map.Entry<String, Object> entry : map.entrySet()) {

                        messageArrayList.add(
                                new Message(entry.getKey(), (String) entry.getValue())
                        );


                    }
                    //messageArrayList.notifyDataSetChanged();

                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //do something xd
            }
        });
*/

        return messageArrayList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.exit_slide_to_left,R.anim.enter_slide_from_right);

    }
}