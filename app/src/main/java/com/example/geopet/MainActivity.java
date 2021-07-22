package com.example.geopet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.geopet.ui.gallery.GalleryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private ListView mListView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private  FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        System.out.println("-------------------PRUEBA--------------------------");

       // Create a reference with an initial file path and name

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        /*Se incializa la app bar  */
        setContentView(R.layout.nav_activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Se incializa el drawer */
        drawerLayout = findViewById(R.id.drawer_layout);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);//icono de drawer

        navigationView = findViewById(R.id.nav_view);

        //establecer evento onClick al navigationView
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if(item.getItemId() == R.id.nav_cuenta){

                    startActivity(new Intent(getApplicationContext(),createPost.class));
                }
                return false;
            }
        });



        /*Se cargan las publicaciones en el dashboard */
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                Toast.makeText(this, "boton busqueda", Toast.LENGTH_SHORT ).show();
                return true;




        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



}