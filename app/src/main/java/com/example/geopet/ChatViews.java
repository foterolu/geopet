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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatViews extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private FirebaseDatabase database;
    private FirebaseAuth fAuth;
    private ListView listOfChats;
    private String userId;
    private Toolbar toolbar;

    private ArrayList<String> chatKeysUser= new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private ListView ChatsListView;
    private String otherUser;
    private ArrayList<String> keyChat = new ArrayList<>();

    CustomArrayAdapter customArrayAdapter;
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
                        System.out.println("Chat de usuario");

                        List<String> lol = Arrays.asList(key.split(userId));
                        if(lol != null){
                            System.out.println(lol);
                            if(lol.get(0).isEmpty()){
                                System.out.println("-----------------------0 Null-----------------");
                                otherUser = lol.get(1);
                                System.out.println(otherUser);
                            }
                            else{
                                System.out.println("-----------------------1 Null-----------------");
                                otherUser = lol.get(0);
                            }

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("user").document(otherUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                    String email = task.getResult().getString("email");
                                    keyChat.add(key);
                                    System.out.println(email);
                                    chatKeysUser.add(email);
                                    //mAdapter = new ArrayAdapter<String>(ChatViews.this, R.layout.message,R.id.show_message_left,chatKeysUser);
                                    customArrayAdapter = new CustomArrayAdapter(ChatViews.this,chatKeysUser,key);
                                    ChatsListView.setAdapter(customArrayAdapter);
                                }
                            });
                        }
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
        String result = keyChat.get(position);
        String resultEmail = chatKeysUser.get(position);
        Intent intent = new Intent(getApplicationContext(), Chat.class);
        intent.putExtra("chatId",result);
        intent.putExtra("email",resultEmail);
        startActivity(intent);
    }
}