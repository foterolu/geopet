package com.example.geopet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import Model.Post;

public class createPost extends AppCompatActivity {
    EditText mnombre,mraza,mdescripcion,mcontacto;
    ListView Post;
    Button ingresar,btnbrowse;

    FirebaseFirestore db;

    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog ;

    int Image_Request_Code = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mnombre      = findViewById(R.id.name);
        mraza        = findViewById(R.id.raza);
        mdescripcion = findViewById(R.id.description);
        mcontacto    = findViewById(R.id.phone2);
        ingresar = findViewById(R.id.añadir);
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
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
                post.put("imagePath", path);

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
                UploadImage();


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

    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String TempImageName = "removerEstaWEa";
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
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