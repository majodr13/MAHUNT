package com.utch.mahuntproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    TextView MiPuntuaciontxt, uid, correo, nombre, Menutxt;
    Button CerrarSesion, JugarBtn, PuntuacionesBtn, AcercaDeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        auth = FirebaseAuth.getInstance(); //Cambios video 5
        user = auth.getCurrentUser(); //Cambios video 5

        MiPuntuaciontxt= findViewById(R.id.MiPuntuaciontxt);
        uid= findViewById(R.id.uid);
        correo= findViewById(R.id.correo);
        nombre= findViewById(R.id.nombre);
        Menutxt= findViewById(R.id.Menutxt);


        CerrarSesion= findViewById(R.id.CerrarSesion);//Cambio video 6
        JugarBtn= findViewById(R.id.JugarBtn);
        PuntuacionesBtn= findViewById(R.id.PuntuacionesBtn);
        AcercaDeBtn= findViewById(R.id.AcercaDeBtn);

        JugarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Jugar", Toast.LENGTH_SHORT).show();
            }
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
}