package com.utch.mahuntproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int DURACION_SPLASH=3000;
        /* Handler, ejecuta lineas de codigo en un tiempo determinado*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Esto es lo que ejecuta
                Intent intent = new Intent(Splash.this, Menu.class);
                startActivity(intent);
                //Hasta aqui
            };
            //Esto es lo que ejecutara
        },DURACION_SPLASH);
    }
}
