package com.example.geopet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.geopet.ChatMessage.Chat;
import com.example.geopet.ChatMessage.ChatMessage;
import com.example.geopet.ChatMessage.Chats;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatViews extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private FirebaseDatabase database;
    private DatabaseReference chat_ref;
    private FirebaseAuth fAuth;
    private ListView listOfChats;
    private String userId;
    private Toolbar toolbar;
    private FirebaseListAdapter<Chats> adapter;
    private ArrayList<String> chatKeysUser= new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private ListView ChatsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_views);

        toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Chats");
        ChatsListView= (ListView) findViewById(R.id.list_of_chats);
        listOfChats = (ListView) findViewById(R.id.list_of_chats);
        userId = fAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        database.getReference("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dsp: snapshot.getChildren()){
                    String key = dsp.getKey();


                    if(key.contains(userId)){
                        chatKeysUser.add(key);
                        System.out.println("Chat de usuario");
                        mAdapter = new ArrayAdapter<String>(ChatViews.this, R.layout.message,R.id.show_message_left,chatKeysUser);
                        ChatsListView.setAdapter(mAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        ChatsListView.setOnItemClickListener(this);
        //displayChatMessages();



    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String result = chatKeysUser.get(position);
        Intent intent = new Intent(getApplicationContext(), Chat.class);
        intent.putExtra("chatId",result);
        startActivity(intent);
    }
}