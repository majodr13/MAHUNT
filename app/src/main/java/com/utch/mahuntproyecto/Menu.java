package com.utch.mahuntproyecto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase; //Video 9
    DatabaseReference Mahunt; // Video 9
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    TextView MiPuntuaciontxt, uid, correo, nombre, Menutxt, Patos, fecha;
    Button CerrarSesion, JugarBtn, PuntuacionesBtn, AcercaDeBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        auth = FirebaseAuth.getInstance(); //Cambios video 5
        user = auth.getCurrentUser(); //Cambios video 5

        //UBICACIÓN
        String ubicacion = "fuentes/letra.TTF";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(), ubicacion);

        firebaseDatabase = FirebaseDatabase.getInstance();
        Mahunt = firebaseDatabase.getReference("mahunt");


        MiPuntuaciontxt= findViewById(R.id.MiPuntuaciontxt);
        Patos = findViewById(R.id.Patos);
        uid= findViewById(R.id.uid);
        correo= findViewById(R.id.correo);
        fecha = findViewById(R.id.fecha);
        nombre= findViewById(R.id.nombre);
        Menutxt= findViewById(R.id.Menutxt);


        CerrarSesion= findViewById(R.id.CerrarSesion);//Cambio video 6
        JugarBtn= findViewById(R.id.JugarBtn);
        PuntuacionesBtn= findViewById(R.id.PuntuacionesBtn);
        AcercaDeBtn= findViewById(R.id.AcercaDeBtn);

        MiPuntuaciontxt.setTypeface(Tf);
        uid.setTypeface(Tf);
        correo.setTypeface(Tf);
        nombre.setTypeface(Tf);
        Menutxt.setTypeface(Tf);

        CerrarSesion.setTypeface(Tf);
        JugarBtn.setTypeface(Tf);
        PuntuacionesBtn.setTypeface(Tf);
        AcercaDeBtn.setTypeface(Tf);

        JugarBtn.setOnClickListener(view -> {
            Toast.makeText(Menu.this, "JUGAR", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Menu.this, EscenarioJuego.class);

            String Uids = uid.getText().toString();
            String NombreS = nombre.getText().toString();
            String PatosS = Patos.getText().toString();

            intent.putExtra("UID", Uids);
            intent.putExtra("NOMBRE", NombreS);
            intent.putExtra("PATO", PatosS);

            startActivity(intent);
            Toast.makeText(Menu.this, "ENVIANDO PARÁMETROS", Toast.LENGTH_SHORT).show();
        });

        PuntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Puntuaciones", Toast.LENGTH_SHORT).show();

            }
        });

        AcercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Acerca de", Toast.LENGTH_SHORT).show();

            }
        });



        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarSesion();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //ESTE MÉTODO SE EJECUTA CUANDO SE ABRE EL MINIJUEGO
    @Override
    protected void  onStart(){
        UsuarioLogeado();
        super.onStart();
    }

    // MÉTODO COMPRUEBA SI EL JUGADOR HA INICIADO SESIÓN
    //Cambios video 5
    private void UsuarioLogeado(){
        if (user != null){
            //Consulta();
            Toast.makeText(this, "Jugador en linea", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(Menu.this, MainActivity.class));
            finish();
        }
    }
//Metodo cerrar cesion video 6
    private  void CerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this,MainActivity.class));
        Toast.makeText(this, "Cerrado de sesion exitosamente", Toast.LENGTH_SHORT).show();
    }

    //Consulta video 9
    private void Consulta() {

        // Accede a la colección "mahunt" y busca el documento por el correo del usuario
        db.collection("mahunt")
                .whereEqualTo("Email", user.getEmail()) // Filtra por correo electrónico
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Extrae los datos
                                    String patosString = document.getString("Patos");
                                    String uidString = document.getString("Uid");
                                    String emailString = document.getString("Email");
                                    String nombreString = document.getString("Nombres");
                                    String fechaString = document.getString("Fecha");

                                    // Muestra los datos en los TextView
                                    Patos.setText(patosString);
                                    uid.setText(uidString);
                                    correo.setText(emailString);
                                    nombre.setText(nombreString);
                                    fecha.setText(fechaString);

                                    Toast.makeText(Menu.this, "Datos cargados correctamente", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Menu.this, "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Menu.this, "Error al consultar datos: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}