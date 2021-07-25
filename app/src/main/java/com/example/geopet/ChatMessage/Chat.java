package com.example.geopet.ChatMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geopet.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

public class Chat extends AppCompatActivity {
    private FirebaseListAdapter<ChatMessage> adapter;
    private FirebaseAuth fAuth;
    private String chatId;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fAuth        = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        chatId = intent.getStringExtra("chatId");
        Toast.makeText(this,
                chatId,
                Toast.LENGTH_LONG)
                .show();


        displayChatMessages();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                System.out.println("-------------------AcaaaUWU----------------");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference chat_ref = database.getReference("chats");
                FirebaseAuth fAuth        = FirebaseAuth.getInstance();

                ChatMessage message = new ChatMessage(input.getText().toString(),userId);
                chat_ref.child(chatId).push().setValue(message);
            /* FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );*/
                // Clear the input
                input.setText("");
            }
        });

    }


    private void displayChatMessages() {
        Query query = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
        ListView listOfMessages  = (ListView)findViewById(R.id.list_of_messages);
        FirebaseListOptions options = new FirebaseListOptions.Builder<ChatMessage>().
                setQuery(query,ChatMessage.class).setLayout(R.layout.message).build();




        adapter = new FirebaseListAdapter<ChatMessage>(options){
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                if(model.getMessageUser().compareTo(userId) == 0) {
                    TextView messageText = (TextView) v.findViewById(R.id.message_user);
                    //TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                    messageText.setGravity(Gravity.RIGHT);
                    messageText.setBackground(v.getResources().getDrawable(R.drawable.background_right));

                    // Set their text
                    messageText.setText(model.getMessageText());

                    messageText.setTextColor(Color.WHITE);
                    // Format the date before showing it
                    //messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                      //      model.getMessageTime()));
                }else{

                    TextView messageText = (TextView) v.findViewById(R.id.message_user);
                    //TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                    messageText.setGravity(Gravity.LEFT);
                    messageText.setBackground(v.getResources().getDrawable(R.drawable.background_left));

                    // Set their text
                    messageText.setTextColor(Color.BLACK);
                    messageText.setText(model.getMessageText());


                    // Format the date before showing it
                    //messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                     //       model.getMessageTime()));
                }
            }
        };

        listOfMessages.setAdapter(adapter);
        adapter.startListening();



    }
}