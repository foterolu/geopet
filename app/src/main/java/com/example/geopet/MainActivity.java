package com.example.geopet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "MainActivity";
    private final ArrayList<Card> list=new ArrayList<>();
    private Toolbar toolbar;
    private ListView mListView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private  FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<String> images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions();
            System.out.println("----------------------VAMOS ACAAA 2 -------------------------");

        }
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
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


        //Se cargan datos a la vista, referencia a firestore
        db.collection("post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            ArrayList<String> links = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String image = new String();
                                System.out.println(document.getData().get("imagePath").getClass());
                                if(document.getData().get("imagePath").getClass() == image.getClass() ){
                                    image = (String) document.getData().get("imagePath");
                                    String userId = (String) document.getData().get("userId");
                                    images.add(image);
                                    //System.out.println("images/" + (String) document.getData().get("imagePath"));
                                    StorageReference pathReference = storageRef.child("Images/" + document.getData().get("imagePath"));
                                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            links.add(uri.toString());
                                            list.add(new Card(uri.toString(), (String) document.getData().get("descripcion"),images,userId, (String) document.getData().get("contacto"),(String) document.getData().get("lat"), (String) document.getData().get("lon"), (String) document.getData().get("nombre"), (String) document.getData().get("raza"), (String) document.getData().get("usuario")  ));
                                            System.out.println(links);
                                            CustomListAdapter adapter = new CustomListAdapter(MainActivity.this,R.layout.activity_main,list);
                                            mListView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                            images.clear();
                                        }
                                    });

                                }else{
                                    images = (ArrayList<String>)  document.getData().get("imagePath");
                                    String userId = (String) document.getData().get("userId");
                                    String contacto = (String) document.getData().get("contacto");
                                    String lat = (String) document.getData().get("lat");
                                    String lon = (String) document.getData().get("lon");
                                    String nombre = (String) document.getData().get("nombre");
                                    String raza = (String) document.getData().get("raza");
                                    String usuario = (String) document.getData().get("usuario");
                                    System.out.println(images);
                                    StorageReference pathReference = storageRef.child("Images/" + images.get(0));
                                    System.out.println("Images/" + images.get(0));
                                    //String FirePath = "gs://geopet-9028c.appspot.com/" + "Images/"  +(String) document.getData().get("imagePath");
                                    //System.out.println(FirePath);
                                    System.out.println("----------------Dentro del For----------------");
                                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            System.out.println("----------------Dentro del OnSuccess----------------");
                                            System.out.println(uri.toString());
                                            links.add(uri.toString());
                                            list.add(new Card(uri.toString(), (String) document.getData().get("descripcion"),
                                                   images,userId, contacto,lat, lon, nombre, raza, usuario  ));
                                            CustomListAdapter adapter = new CustomListAdapter(MainActivity.this,R.layout.activity_main,list);
                                            adapter.notifyDataSetChanged();
                                            mListView.setAdapter(adapter);

                                            //System.out.println(links);

                                        }
                                    });
                                        //System.out.println(links);
                                    //error cuando no hay foto
                                }

                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        mListView.deferNotifyDataSetChanged();
        mListView.setOnItemClickListener(this);


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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        System.out.println("----------------Item Clickeado-------------------------");
        System.out.println(list.get(position).getUris());
        Card card= list.get(position);
        Intent intent= new Intent(MainActivity.this, SinglePost.class);
        Bundle bundle= new Bundle();
        bundle.putSerializable("card", card);
        intent.putExtras(bundle);

        startActivity(intent);




    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
    }
}