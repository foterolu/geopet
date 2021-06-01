package com.example.geopet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Model.Post;

public class createPost extends AppCompatActivity {
    EditText mnombre,mraza,mdescripcion,mcontacto;
    ListView Post;
    Button ingresar;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mnombre      = findViewById(R.id.name);
        mraza        = findViewById(R.id.raza);
        mdescripcion = findViewById(R.id.description);
        mcontacto    = findViewById(R.id.phone2);
        ingresar = findViewById(R.id.añadir);



        db = FirebaseFirestore.getInstance();

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre      =  mnombre.getText().toString().trim();
                String raza        = mraza.getText().toString().trim();
                String descripcion = mdescripcion.getText().toString().trim();
                String contacto    = mcontacto.getText().toString().trim();

                if(TextUtils.isEmpty(contacto)){
                    mcontacto.setError("Número Requerido");
                    return;
                }

                if(TextUtils.isEmpty(nombre)){
                    mnombre.setError("Nombre requerido");
                    return;
                }

                if(TextUtils.isEmpty(descripcion)){
                    mnombre.setError("Descripción requerida");
                    return;
                }

                Map<String,Object> post = new HashMap<>();
                FirebaseAuth fAuth        = FirebaseAuth.getInstance();
                String user = fAuth.getCurrentUser().getEmail();
                post.put("nombre", nombre);
                post.put("raza",raza);
                post.put("descripcion",descripcion);
                post.put("contacto",contacto);
                post.put("usuario",user);

                db.collection("post").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("tagy","Anuncio ingresado");
                        Toast.makeText(createPost.this, "Anuncio Creado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tagy","Ingreso Fallido");
                    }
                });


            }
        });

    }
}