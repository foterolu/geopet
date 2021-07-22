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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class createPost extends AppCompatActivity {
    EditText mnombre,mraza,mdescripcion,mcontacto;
    ListView Post;
    Button ingresar,btnbrowse, btnmap;

    FirebaseFirestore db;

    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog ;
    private Toolbar toolbar;
    String Imageuri;
    int Image_Request_Code = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mnombre      = findViewById(R.id.name);
        mraza        = findViewById(R.id.raza);
        mdescripcion = findViewById(R.id.description);
        mcontacto    = findViewById(R.id.phone2);
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
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fp=new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(fp);
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String nombre      =  mnombre.getText().toString().trim();
                String raza        = mraza.getText().toString().trim();
                String descripcion = mdescripcion.getText().toString().trim();
                String contacto    = mcontacto.getText().toString().trim();
                String path = System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);

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


                UploadImage(post);





            }


        });

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    public void UploadImage(Map<String,Object> post ) {

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






}