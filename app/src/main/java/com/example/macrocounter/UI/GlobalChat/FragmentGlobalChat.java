package com.example.macrocounter.UI.GlobalChat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.UserLoged.UserLogedActivity;
import com.example.macrocounter.UI.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FragmentGlobalChat extends AppCompatActivity {

    public static String userName;
    private FirebaseFirestore firebase;
    private CollectionReference chatGlobalCollectionReference;
    private AdapterGlobalChat adapterGlobalChat;
    private ImageButton imgBtn_SendMsg;
    private EditText editText_typeMessage;
    RecyclerView recyclerViewChat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_global_chat);
        overridePendingTransition(R.anim.enter_slide_from_right,R.anim.exit_slide_to_left);
        this.firebase = FirebaseFirestore.getInstance();
        this.chatGlobalCollectionReference = this.firebase.collection("GlobalChat");

        this.recyclerViewChat = findViewById(R.id.recycler_global_chat);
        this.adapterGlobalChat = new AdapterGlobalChat(getCurrentMessages(userName));
        recyclerViewChat.setAdapter(adapterGlobalChat);
        recyclerViewChat.setItemAnimator(new DefaultItemAnimator());
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        userName = intent.getStringExtra("UserName");

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

        imgBtn_SendMsg = findViewById(R.id.btn_sendMessage);
        imgBtn_SendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageToSend = editText_typeMessage.getText().toString();

                if(!messageToSend.isEmpty()){
                    sendMessage(messageToSend);
                    editText_typeMessage.getText().clear();
                }else{
                    editText_typeMessage.setError("Ingrese texto");
                }

            }
        });

        editText_typeMessage = findViewById(R.id.txt_edit_type_message);

        setChatListener();

    }

    public void setChatListener(){
        this.chatGlobalCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w("GlobalChat", "listen:error", e);
                    return;
                }

                String source = (snapshots == null)?"Error": snapshots.getMetadata().hasPendingWrites() ? "Local" : "Server";

                Log.d("PendingWrites ",source);

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    QueryDocumentSnapshot document = dc.getDocument();
                    System.out.println("mamada");
                    switch (dc.getType()) {
                        case ADDED:
                           // Log.d(TAG, "New city: " + dc.getDocument().getData());
                            addMessageToList(document);

                            break;
                        case MODIFIED:
                           Log.d("MODIFIED", "Modified city: " + dc.getDocument().getData());

                            break;
                        case REMOVED:
                            //Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            break;
                    }
                }

            }
        });
    }
    private void addMessageToList(QueryDocumentSnapshot dc){

        String userNameMsg = (String) dc.get("user");
        String msg = (String) dc.get("msg");
        //String firebaseDate = (String) dc.get("date");
        Timestamp messageDate = ((Timestamp) dc.get("date"));
        Date date = messageDate==null?new Date(): messageDate.toDate();
        int indexMsg = messageArrayList.size();
        messageArrayList.add(messageArrayList.size(),new Message(userNameMsg,msg,date));
        adapterGlobalChat.notifyItemChanged(indexMsg);

    }


    public void sendMessage (String message){

        HashMap<String,Object> newMsg = new HashMap<>();
        newMsg.put("date", FieldValue.serverTimestamp());
        newMsg.put("user",userName);
        newMsg.put("msg",message);

        this.chatGlobalCollectionReference.document().set(newMsg).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                recyclerViewChat.scrollToPosition(adapterGlobalChat.getItemCount());
                adapterGlobalChat.notifyItemChanged(adapterGlobalChat.getItemCount());
                recyclerViewChat.scrollToPosition(adapterGlobalChat.getItemCount());

            }
        });

    }
    ArrayList<Message> messageArrayList;

    public ArrayList<Message> getCurrentMessages(final String userName){

        messageArrayList = new ArrayList<>();

        /*this.chatGlobalCollectionReference.orderBy("date", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
        });*/


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