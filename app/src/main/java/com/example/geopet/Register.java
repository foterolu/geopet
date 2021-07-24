package com.example.geopet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    AutoCompleteTextView mComuna;
    Boolean flag = false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,comunas);

        setContentView(R.layout.activity_register);
        mFullName    = findViewById(R.id.fullname);
        mEmail       = findViewById(R.id.Email);
        mPassword    = findViewById(R.id.password);
        mPhone       = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.login);
        mLoginBtn    = findViewById(R.id.createText);
        mComuna      = findViewById(R.id.Comuna);
        fAuth        = FirebaseAuth.getInstance();
        progressBar  = findViewById(R.id.progressBar);

        mComuna.setThreshold(1);
        mComuna.setAdapter(adapter);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String name = mFullName.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String comuna = mComuna.getText().toString().trim();

                if (!(comunaList.contains(comuna))){
                    mComuna.setError("Comuna valida requerida");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Requerido");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Contraseña Requerida");
                    return;
                }
                if(password.length() < 6 ){
                    mPassword.setError("Largo de contraseña debe ser mayor a 6 caracteres");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //Ahora se registra el Usuario
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Register.this, "Usuario Creado", Toast.LENGTH_SHORT).show();
                            //flag = true;
                            Map<String, Object> user = new HashMap<>();
                            user.put("nombre", name);
                            user.put("celular", phone);
                            user.put("email",email);
                            user.put("comuna",comuna);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            //String Userid =fAuth.getCurrentUser().getUid();
                            db.collection("user").document(email).set(user);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });






            }

        });


    }

    public void login(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}