package com.example.geopet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName    = findViewById(R.id.fullname);
        mEmail       = findViewById(R.id.Email);
        mPassword    = findViewById(R.id.password);
        mPhone       = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.login);
        mLoginBtn    = findViewById(R.id.createText);

        fAuth        = FirebaseAuth.getInstance();
        progressBar  = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String name = mFullName.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Requerido");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Contraseña Requerida");
                    return;
                }
                if(password.length() < 6 ){
                    mPassword.setError("Largo de contraseña debe ser mayor a 6 caracteres");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //Ahora se registra el Usuario
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Register.this, "Usuario Creado", Toast.LENGTH_SHORT).show();
                            //flag = true;
                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                /*
                Map<String, Object> user = new HashMap<>();
                user.put("nombre", name);
                user.put("celular", phone);
                user.put("email",email);
                //falta la comuna
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //String Userid =fAuth.getCurrentUser().getUid();
                db.collection("user").document(email).set(user);
                startActivity(new Intent(getApplicationContext(),MainActivity.class)); */



            }

        });


    }

    public void login(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}