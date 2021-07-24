package com.example.geopet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class SinglePost extends AppCompatActivity {
    private Toolbar toolbar;
    TextView descripcion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        descripcion= (TextView) findViewById(R.id.descripcionId);

        Bundle cardSent= getIntent().getExtras();
        Card card=null;
        if (cardSent!=null){
            card= (Card) cardSent.getSerializable("card");
            descripcion.setText(card.getTitle());
        }
    }
}

