package com.example.geopet;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createPost extends AppCompatActivity {
    EditText mnombre,mraza,mdescripcion,mcontacto;
    TextView mlatlong;
    ListView Post;
    Button ingresar,btnbrowse, btnmap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

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
        ingresar = findViewById(R.id.añadir);
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
        btnmap = (Button)findViewById(R.id.btnmap);
        progressDialog = new ProgressDialog(createPost.this);
        imgview = findViewById(R.id.image_view);

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        db = FirebaseFirestore.getInstance();

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
                String[] x = mlatlong.getText().toString().replaceAll("[()]","").split(",");
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
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
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