package com.example.geopet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geopet.ChatMessage.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SinglePost extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar toolbar;
    String username;
    ArrayList<String> fotos;
    String lat, lon;
    TextView nombrePublicacion, descripcion, raza, contacto, comuna, tipoAnimal;
    TextView userID; //SOLO DE PRUEBA, LUEGO REMOVER
    Button mbtn;
    String postUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        nombrePublicacion= (TextView) findViewById(R.id.nombrePublicacionId);
        descripcion= (TextView) findViewById(R.id.descripcionId);
        contacto= (TextView) findViewById(R.id.contactoID);
        mbtn = findViewById(R.id.chatButton);
        raza= findViewById(R.id.razaId);
        comuna=findViewById(R.id.comunaId);
        tipoAnimal=findViewById(R.id.tipoAnimalId);



        Bundle cardSent= getIntent().getExtras();
        Card card=null;
        if (cardSent!=null){
            card= (Card) cardSent.getSerializable("card");
            System.out.println("lolardo Xd" +card.getNombrePublicacion());
            nombrePublicacion.setText(card.getNombrePublicacion());
            descripcion.setText(card.getDescripcion());
            contacto.setText("Contacto: "+card.getContacto());
            raza.setText("Raza animal: "+card.getRaza());
            comuna.setText("Comuna: "+card.getComuna());
            tipoAnimal.setText("Tipo animal encontrado: "+card.getTipoAnimal());



            //userID.setText(card.getUserId());
            //username=card.getUsername();
        }
        String postUserId = card.getUserId();
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth fAuth        = FirebaseAuth.getInstance();
                String userId = fAuth.getCurrentUser().getUid();
                if(postUserId.compareTo(userId) == 0){
                    Toast.makeText(SinglePost.this, "Eres el dueño de la publicación", Toast.LENGTH_SHORT ).show();
                }else{
                    String result = setOneToOneChat(userId,postUserId);
                    Intent intent = new Intent(getApplicationContext(), Chat.class);
                    intent.putExtra("chatId",result);
                    startActivity(intent);

                }


            }
        });






    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        /*
        System.out.println("----------------Item Clickeado-------------------------");
        System.out.println(list.get(position).getUris());
        Toast.makeText(this, list.get(position).getTitle(), Toast.LENGTH_SHORT ).show();
        Card card= list.get(position);
        Intent intent= new Intent(MainActivity.this, SinglePost.class);
        Bundle bundle= new Bundle();
        bundle.putSerializable("card", card);
        intent.putExtras(bundle);

        startActivity(intent);
        */



    }


    //ID para OBTENER CHAT
    private String setOneToOneChat(String uid1, String uid2){
        if(uid1.compareTo(uid2)<0){
            return uid1 + uid2;
        }else{
            return uid2 + uid1;
        }

    }


}

