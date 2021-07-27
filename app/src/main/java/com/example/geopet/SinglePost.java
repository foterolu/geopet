package com.example.geopet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.geopet.ChatMessage.Chat;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SinglePost extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "SinglePost";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Toolbar toolbar;
    Button botonMapa;
    GridView picsGridView;
    String username;
    ArrayList<String> fotos, storagePhotos=new ArrayList<String>();
    String lat, contacto;
    String lon;
    TextView nombrePublicacion, descripcion, raza, comuna, tipoAnimal, tvTipoAnimal, tvRaza, tvComuna;
    TextView userID; //SOLO DE PRUEBA, LUEGO REMOVER
    Button mbtn;
    String postUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);



        ImageSlider imageSlider=findViewById(R.id.fotosSlider);
        List<SlideModel> slideModels= new ArrayList<>();





        nombrePublicacion= (TextView) findViewById(R.id.nombrePublicacionId);
        descripcion= (TextView) findViewById(R.id.descripcionId);
        mbtn = findViewById(R.id.chatButton);
        raza= findViewById(R.id.razaId);
        comuna=findViewById(R.id.comunaId);
        tipoAnimal=findViewById(R.id.tipoAnimalId);
        botonMapa=findViewById(R.id.mapButton);

        tvTipoAnimal=findViewById(R.id.tvTipoAnimal);
        tvTipoAnimal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pets2, 0, 0, 0);
        tvRaza=findViewById(R.id.tvRaza);
        tvRaza.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_raza, 0, 0, 0);
        tvComuna=findViewById(R.id.tvComuna);
        tvComuna.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_place, 0, 0, 0);





        Bundle cardSent= getIntent().getExtras();
        Card card=null;
        if (cardSent!=null){
            card= (Card) cardSent.getSerializable("card");
            username = card.getUsername();
            nombrePublicacion.setText(card.getNombrePublicacion());
            descripcion.setText(card.getDescripcion());
            contacto=card.getContacto()+'"';
            raza.setText(card.getRaza());
            comuna.setText(card.getComuna());
            tipoAnimal.setText(card.getTipoAnimal());
            fotos=card.getUris();
            lat=card.getLat();
            lon=card.getLon();
            System.out.println("uris enviadas:  "+fotos);




            //userID.setText(card.getUserId());
            //username=card.getUsername();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(card.getNombrePublicacion());


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
                    intent.putExtra("email",username);
                    startActivity(intent);

                }


            }
        });

        //Se cargan datos a la vista, referencia a firestore

        for (int i=0; i<fotos.size(); i=i+1) {
            int finalI = i;
            db.collection("post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        System.out.println("FOTOS PREPARADAS::::::::::");
                        System.out.println("Images/" + fotos.get(finalI));
                        StorageReference pathReference = storageRef.child("Images/" + fotos.get(finalI));
                        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println("on success single post"+uri.toString());
                                storagePhotos.add(uri.toString());
                                slideModels.add(new SlideModel(uri.toString()));
                                imageSlider.setImageList(slideModels,true);
                                System.out.println("MAMASITA XD :    "+storagePhotos);


                                //adapter = new CustomListAdapter(MainActivity.this,R.layout.activity_main,list);
                                //mListView.setAdapter(adapter);
                                //adapter.notifyDataSetChanged();
                                    }
                                });
                    } else {
                        Log.d(TAG, "Error getting photos: ", task.getException());
                    }
                }

            });

        }


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

    public void desplegarMapa(View view) {
        // Do something in response to button click
        System.out.println("Latitud: "+lat + "Longitud: "+lon);
        double lat1=Double.parseDouble(lat);
        double lon1=Double.parseDouble(lon);
        LatLng latLng = new LatLng(lat1, lon1);

        System.out.println(lat+"funcionara et wea latitu xd");
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat1, lon1);
        System.out.println(uri);
        /*
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);

         */

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + lat1  + ">,<" + lon1 + ">?q=<" + lat1  + ">,<" + lon1+ ">(" + "Visto" + ")"));
        startActivity(intent);
    }


    //ID para OBTENER CHAT
    private String setOneToOneChat(String uid1, String uid2){
        if(uid1.compareTo(uid2)<0){
            return uid1 + uid2;
        }else{
            return uid2 + uid1;
        }

    }


    public void callContact(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        System.out.println(contacto);
        i.setData(Uri.parse("tel:"+contacto));
        startActivity(i);
    }
}

