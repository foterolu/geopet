package com.example.geopet;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createPost extends AppCompatActivity {
    EditText mnombre,mraza,mdescripcion,mcontacto, mTipoAnimal;
    TextView mlatlong;
    AutoCompleteTextView mComuna;
    ListView Post;
    Button ingresar,btnbrowse, btnmap;
    String[] comunas = new String[]{
            "Arica","Camarones","Putre","General Lagos","Iquique","Alto Hospicio","Pozo Almonte",
            "Camiña","Colchane","Huara","Pica","Antofagasta","Mejillones","Sierra Gorda",
            "Taltal","Calama","Ollagüe","San Pedro de Atacama","Tocopilla","María Elena",
            "Copiapó","Caldera","Tierra Amarilla","Chañaral","Diego de Almagro","Vallenar",
            "Alto del Carmen","Freirina","Huasco","La Serena","Coquimbo","Andacollo","La Higuera",
            "Paiguano","Vicuña","Illapel","Canela","Los Vilos","Salamanca","Ovalle","Combarbalá",
            "Monte Patria","Punitaqui","Río Hurtado","Valparaíso","Casablanca","Concón","Juan Fernández",
            "Puchuncaví","Quintero","Viña del Mar","Isla de Pascua","Los Andes","Calle Larga","Rinconada",
            "San Esteban","La Ligua","Cabildo","Papudo","Petorca","Zapallar","Quillota","Calera",
            "Hijuelas","La Cruz","Nogales","San Antonio","Algarrobo","Cartagena","El Quisco",
            "El Tabo","Santo Domingo","San Felipe","Catemu","Llaillay","Panquehue","Putaendo",
            "Santa María","Quilpué","Limache","Olmué","Villa Alemana","Rancagua","Codegua","Coinco",
            "Coltauco","Doñihue","Graneros","Las Cabras","Machalí","Malloa","Mostazal","Olivar",
            "Peumo","Pichidegua","Quinta de Tilcoco","Rengo","Requínoa","San Vicente","Pichilemu","La Estrella",
            "Litueche","Marchihue","Navidad","Paredones","San Fernando","Chépica","Chimbarongo",
            "Lolol","Nancagua","Palmilla","Peralillo","Placilla","Pumanque","Santa Cruz","Talca",
            "Constitución","Curepto","Empedrado","Maule","Pelarco","Pencahue","Río Claro","San Clemente",
            "San Rafael","Cauquenes","Chanco","Pelluhue","Curicó","Hualañé","Licantén","Molina",
            "Rauco","Romeral","Sagrada Familia","Teno","Vichuquén","Linares","Colbún","Longaví",
            "Parral","Retiro","San Javier","Villa Alegre","Yerbas Buenas","Cobquecura","Coelemu","Ninhue",
            "Portezuelo","Quirihue","Ránquil","Treguaco","Bulnes","Chillán Viejo","Chillán","El Carmen",
            "Pemuco","Pinto","Quillón","San Ignacio","Yungay","Coihueco","Ñiquén","San Carlos",
            "San Fabián","San Nicolás","Concepción","Coronel","Chiguayante","Florida","Hualqui",
            "Lota","Penco","San Pedro de la Paz","Santa Juana","Talcahuano","Tomé","Hualpén","Lebu",
            "Arauco","Cañete","Contulmo","Curanilahue","Los Álamos","Tirúa","Los Ángeles","Antuco",
            "Cabrero","Laja","Mulchén","Nacimiento","Negrete","Quilaco","Quilleco","San Rosendo","Santa Bárbara",
            "Tucapel","Yumbel","Alto Biobío","Temuco","Carahue","Cunco","Curarrehue","Freire",
            "Galvarino","Gorbea","Lautaro","Loncoche","Melipeuco","Nueva Imperial","Padre las Casas",
            "Perquenco","Pitrufquén","Pucón","Saavedra","Teodoro Schmidt","Toltén","Vilcún","Villarrica",
            "Cholchol","Angol","Collipulli","Curacautín","Ercilla","Lonquimay","Los Sauces","Lumaco",
            "Purén","Renaico","Traiguén","Victoria","Valdivia","Corral","Lanco","Los Lagos","Máfil",
            "Mariquina","Paillaco","Panguipulli","La Unión","Futrono","Lago Ranco","Río Bueno",
            "Puerto Montt","Calbuco","Cochamó","Fresia","Frutillar","Los Muermos","Llanquihue",
            "Maullín","Puerto Varas","Castro","Ancud","Chonchi","Curaco de Vélez","Dalcahue","Puqueldón",
            "Queilén","Quellón","Quemchi","Quinchao","Osorno","Puerto Octay","Purranque","Puyehue",
            "Río Negro","San Juan de la Costa","San Pablo","Chaitén","Futaleufú","Hualaihué","Palena",
            "Coihaique","Lago Verde","Aisén","Cisnes","Guaitecas","Cochrane","O’Higgins","Tortel",
            "Chile Chico","Río Ibáñez","Punta Arenas","Laguna Blanca","Río Verde","San Gregorio",
            "Cabo de Hornos (Ex Navarino)","Antártica","Porvenir","Primavera","Timaukel","Natales","Torres del Paine",
            "Cerrillos","Cerro Navia","Conchalí","El Bosque","Estación Central","Huechuraba","Independencia",
            "La Cisterna","La Florida","La Granja","La Pintana","La Reina","Las Condes","Lo Barnechea",
            "Lo Espejo","Lo Prado","Macul","Maipú","Ñuñoa","Pedro Aguirre Cerda","Peñalolén","Providencia",
            "Pudahuel","Quilicura","Quinta Normal","Recoleta","Renca","Santiago","San Joaquín","San Miguel",
            "San Ramón","Vitacura","Puente Alto","Pirque","San José de Maipo","Colina","Lampa","Tiltil",
            "San Bernardo","Buin","Calera de Tango","Paine","Melipilla","Alhué","Curacaví","María Pinto",
            "San Pedro","Talagante","El Monte","Isla de Maipo","Padre Hurtado","Peñaflor"};

    List<String> comunaList = new ArrayList<>(Arrays.asList(comunas));

    FirebaseFirestore db;

    ImageView imgview;
    List<Uri> FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog ;
    private Toolbar toolbar;
    String Imageuri;
    int Image_Request_Code = 7;
    int Map_request_code = 1;
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,comunas);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        FilePathUri = new ArrayList<>();;
        mnombre      = findViewById(R.id.name);
        mraza        = findViewById(R.id.raza);
        mdescripcion = findViewById(R.id.description);
        mcontacto    = findViewById(R.id.phone2);
        mlatlong = findViewById(R.id.latlongtext);
        mComuna      = findViewById(R.id.Comuna2);
        mTipoAnimal= findViewById(R.id.tipoAnimal);
        ingresar = findViewById(R.id.añadir);
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
        btnmap = (Button)findViewById(R.id.btnmap);
        progressDialog = new ProgressDialog(createPost.this);
        imgview = findViewById(R.id.image_view);

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        db = FirebaseFirestore.getInstance();
        mComuna.setThreshold(1);
        mComuna.setAdapter(adapter);

        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(createPost.this,MapsActivity.class);
                startActivityForResult(intent,Map_request_code);
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre      =  mnombre.getText().toString().trim();
                String raza        = mraza.getText().toString().trim();
                String descripcion = mdescripcion.getText().toString().trim();
                String contacto    = mcontacto.getText().toString().trim();
                String comuna = mComuna.getText().toString().trim();
                String tipoAnimal= mTipoAnimal.getText().toString().trim();
                String[] x = mlatlong.getText().toString().replaceAll("[()]","").split(",");

                if (!(comunaList.contains(comuna))){
                    mComuna.setError("Comuna valida requerida");
                    return;
                }
                if(x.length != 2){
                    mlatlong.setError("Lugar Requerido");
                    return;
                }
                String lat = x[0];
                String lon = x[1];
                String path = System.currentTimeMillis() + "." + GetFileExtension(FilePathUri.get(0));

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
                String userId = fAuth.getCurrentUser().getUid();

                post.put("nombre", nombre);
                post.put("raza",raza);
                post.put("descripcion",descripcion);
                post.put("contacto",contacto);
                post.put("usuario",user);
                post.put("userId",userId);
                post.put("lat",lat);
                post.put("long",lon);
                post.put("comuna",comuna);
                post.put("tipoAnimal",tipoAnimal);

                UploadImage(post);
            }


        });

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Map_request_code && resultCode == RESULT_OK && data != null){
            TextView text = findViewById(R.id.latlongtext);
            text.setText(data.getStringExtra("value"));
        }


        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null) {
            if(data.getClipData() != null){

                for(int i = 0; i < data.getClipData().getItemCount() ; i++) {
                    System.out.println("------------------------POR ACA-----------------------------------");
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    System.out.println(uri);
                    FilePathUri.add(uri);
                }
                System.out.println(FilePathUri);

            }else {
                try {
                    FilePathUri.add(data.getData());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imgview.setImageBitmap(bitmap);
                }
                catch (IOException e){

                    System.out.println("Error al cargar la imagen");
                }
            }


        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    public void UploadImage(Map<String,Object> post ) {
/*
        if (FilePathUri != null) {

            progressDialog.setTitle("Subiendo  Imagen");
            progressDialog.show();
            Imageuri = System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);
            StorageReference storageReference2 = storageReference.child(Imageuri );
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            post.put("imagePath", Imageuri);
                            String TempImageName = "Remover";
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, Imageuri);
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
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
        else {

            Toast.makeText(createPost.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }

 */
        if (FilePathUri != null) {
            List<String> Imageuri = new ArrayList<>();
            for(int i = 0; i<FilePathUri.size() ; i++){
                progressDialog.setTitle("Subiendo  Imagen");
                progressDialog.show();
                Imageuri.add(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri.get(i)));
                StorageReference storageReference2 = storageReference.child(Imageuri.get(i));
                storageReference2.putFile(FilePathUri.get(i)); //se agrega el archivo i a firestorage

            }


            post.put("imagePath", Imageuri);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

            db.collection("post").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("tagy","Anuncio ingresado");
                    Toast.makeText(createPost.this, "Anuncio Creado", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 5 seconds
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }, 5000);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("tagy","Ingreso Fallido");
                }
            });
        FilePathUri.clear();
        Imageuri.clear();

        }else {

            Toast.makeText(createPost.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    public class uploadinfo {

        public String imageName;
        public String imageURL;
        public uploadinfo(){}

        public uploadinfo(String name, String url) {
            this.imageName = name;
            this.imageURL = url;
        }

        public String getImageName() {
            return imageName;
        }
        public String getImageURL() {
            return imageURL;
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                System.out.println("-------------------PRUEBA--------------------------");
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }






}