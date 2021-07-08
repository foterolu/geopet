package com.example.geopet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Create a reference with an initial file path and name


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        setContentView(R.layout.listview_layout);
        mListView = (ListView) findViewById(R.id.listView);
        ArrayList<Card> list = new ArrayList<>();
        db.collection("post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            ArrayList<String> links = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("images/" + (String) document.getData().get("imagePath"));
                                StorageReference pathReference = storageRef.child("Images/" + (String) document.getData().get("imagePath"));
                                //String FirePath = "gs://geopet-9028c.appspot.com/" + "Images/"  +(String) document.getData().get("imagePath");
                                //System.out.println(FirePath);

                                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        links.add(uri.toString());
                                        list.add(new Card(uri.toString(), (String) document.getData().get("descripcion")));
                                        System.out.println(links);
                                        CustomListAdapter adapter = new CustomListAdapter(MainActivity.this,R.layout.activity_main,list);
                                        mListView.setAdapter(adapter);
                                    }

                                });
                                System.out.println(links);

                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();

    }


    public void crearPost(View view) {
        startActivity(new Intent(getApplicationContext(),createPost.class));
    }
}