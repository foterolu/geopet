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
import android.widget.SearchView;
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
import java.util.LinkedHashSet;

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
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CustomListAdapter adapter;
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

       // Create a reference with an initial file path and name



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

                if(item.getItemId() == R.id.nav_chat){
                    startActivity(new Intent(getApplicationContext(),ChatViews.class));
                }
                if(item.getItemId() == R.id.nav_logout){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
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
                                if(document.getData().get("imagePath").getClass() != null ){


                                    images = (ArrayList<String>)  document.getData().get("imagePath");
                                    String userId = (String) document.getData().get("userId");
                                    String contacto = (String) document.getData().get("contacto");
                                    String lat = (String) document.getData().get("lat");
                                    String lon = (String) document.getData().get("long");
                                    String nombre = (String) document.getData().get("nombre");
                                    String raza = (String) document.getData().get("raza");
                                    String usuario = (String) document.getData().get("usuario");

                                    StorageReference pathReference = storageRef.child("Images/" + images.get(0));

                                    //String FirePath = "gs://geopet-9028c.appspot.com/" + "Images/"  +(String) document.getData().get("imagePath");
                                    //System.out.println(FirePath);
                                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            System.out.println("----------------Dentro del OnSuccess----------------");
                                            images = (ArrayList<String>)  document.getData().get("imagePath");
                                            System.out.println(images);
                                            links.add(uri.toString());
                                            list.add(new Card(uri.toString(), (String) document.getData().get("descripcion"),
                                                   images,userId, contacto,lat, lon, nombre, raza, usuario,
                                                    (String) document.getData().get("comuna"), (String) document.getData().get("tipoAnimal")  ));
                                            adapter.notifyDataSetChanged();

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
        adapter = new CustomListAdapter(MainActivity.this,R.layout.activity_main,list);
        mListView.setAdapter(adapter);

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
            case R.id.chats_view:
                startActivity(new Intent(getApplicationContext(),ChatViews.class));
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchView.OnQueryTextListener reader = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                list.clear();
                images.clear();
                adapter.notifyDataSetChanged();
                    /*
                    adapter = new CustomListAdapter(MainActivity.this,R.layout.activity_main,list);
                    mListView.setAdapter(adapter);

                     */
                db.collection("post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            ArrayList<String> links = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getData().get("comuna").toString().contains(query) || query.length() == 0){
                                    System.out.println("-----------------LO CONTIENE O ES 0-------------------");
                                    String image = new String();

                                    images = (ArrayList<String>)  document.getData().get("imagePath");
                                    String userId = (String) document.getData().get("userId");
                                    String contacto = (String) document.getData().get("contacto");
                                    String lat = (String) document.getData().get("lat");
                                    String lon = (String) document.getData().get("lon");
                                    String nombre = (String) document.getData().get("nombre");
                                    String raza = (String) document.getData().get("raza");
                                    String usuario = (String) document.getData().get("usuario");
                                    StorageReference pathReference = storageRef.child("Images/" + images.get(0));

                                    //String FirePath = "gs://geopet-9028c.appspot.com/" + "Images/"  +(String) document.getData().get("imagePath");
                                    //System.out.println(FirePath);

                                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            links.add(uri.toString());
                                            System.out.println(images);
                                            list.add(new Card(uri.toString(), (String) document.getData().get("descripcion"),
                                                    images,userId, contacto,lat, lon, nombre, raza, usuario, (String) document.getData().get("comuna"), (String) document.getData().get("tipoAnimal")));
                                            adapter.notifyDataSetChanged();


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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                list.clear();
                images.clear();
                adapter.notifyDataSetChanged();
                db.collection("post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            ArrayList<String> links = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (query.length() == 0){
                                    System.out.println("-----------------LO CONTIENE O ES 0-------------------");
                                    String image = new String();

                                    images = (ArrayList<String>)  document.getData().get("imagePath");
                                    String userId = (String) document.getData().get("userId");
                                    String contacto = (String) document.getData().get("contacto");
                                    String lat = (String) document.getData().get("lat");
                                    String lon = (String) document.getData().get("lon");
                                    String nombre = (String) document.getData().get("nombre");
                                    String raza = (String) document.getData().get("raza");
                                    String usuario = (String) document.getData().get("usuario");
                                    StorageReference pathReference = storageRef.child("Images/" + images.get(0));

                                    //String FirePath = "gs://geopet-9028c.appspot.com/" + "Images/"  +(String) document.getData().get("imagePath");
                                    //System.out.println(FirePath);

                                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            links.add(uri.toString());
                                            System.out.println(images);
                                            list.add(new Card(uri.toString(), (String) document.getData().get("descripcion"),
                                                    images,userId, contacto,lat, lon, nombre, raza, usuario, (String) document.getData().get("comuna"), (String) document.getData().get("tipoAnimal")));
                                            adapter.notifyDataSetChanged();


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
                    return false;
            }
        };

        MenuItem.OnActionExpandListener onActionExpandListenervar = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        };
        menu.findItem(R.id.action_search).setOnActionExpandListener(onActionExpandListenervar);
        SearchView searchView= (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Ingresa Comuna...");
        searchView.setOnQueryTextListener(reader);
        return true;
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