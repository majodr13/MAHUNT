package com.utch.mahuntproyecto;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Random;

public class EscenarioJuego extends AppCompatActivity {
    private String UIDS, NOMBRES, PATOS;
    private TextView TvContador, TvNombre, TvTiempo;
    private ImageView IvPatos;

    TextView AnchoTv, AltoTv;

    int AnchoPantalla;
    int AltoPantalla;

    Random aleatorio;

    boolean GameOver = false;
    Dialog miDialog;

    int contador = 0;

    FirebaseAuth  firebaseAuth;//video 16
    FirebaseUser user;//video 16
    FirebaseDatabase firebaseDatabase;//video 16
    DatabaseReference Mahunt;//video 16


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);

        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);
        IvPatos = findViewById(R.id.IvPatos);

        miDialog = new Dialog(EscenarioJuego.this);

        firebaseAuth = FirebaseAuth.getInstance(); //video 16
        user = firebaseAuth.getCurrentUser(); //video 16
        firebaseDatabase = FirebaseDatabase.getInstance();//video 16
        Mahunt= firebaseDatabase.getReference("mahunt");//video 16

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            UIDS = intent.getString("UID");
            NOMBRES = intent.getString("NOMBRE");
            PATOS = intent.getString("PATO");

            TvNombre.setText(NOMBRES);
            TvContador.setText(PATOS);

            AnchoTv = findViewById(R.id.AnchoTv);
            AltoTv = findViewById(R.id.AltoTv);

            Pantalla();// Porque esta comentado esto y dentro de un IF
            CuentaAtras();
        }

        IvPatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!GameOver ) {
                    contador++;
                    TvContador.setText(String.valueOf(contador));
                    IvPatos.setImageResource(R.drawable.patoconmira);

                    // NOS PERMITE EJECUTAR
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // AQUI SE EJECUTA
                            IvPatos.setImageResource(R.drawable.patoconmira);
                            Movimiento();
                        }
                    }, 500);
                }

            }
        });
    }

    //OBTENER TAMAÑO DE PANTALLA
    private void Pantalla() {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        AnchoPantalla = point.x;
        AltoPantalla = point.y;

        String ANCHOS = String.valueOf(AnchoPantalla);
        String ALTOS = String.valueOf(AltoPantalla);

        AnchoTv.setText(ANCHOS);
        AltoTv.setText(ALTOS);

        aleatorio = new Random();
    }

    private  void Movimiento(){
        int min =0;

        //Maximo que se puede mover en  X
        int MaximoX = AnchoPantalla-IvPatos.getWidth();
        //Maximo que se puede mover en  Y
        int MaximoY = AltoPantalla-IvPatos.getHeight();

        int randomX= aleatorio.nextInt(((MaximoX-min)+1)+min);
        int randomY= aleatorio.nextInt(((MaximoY-min)+1)+min);

        IvPatos.setX(randomX);
        IvPatos.setY(randomY);

    }
    private void CuentaAtras(){
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished/1000;
                TvTiempo.setText(segundosRestantes+"S");
            }

            //Cuando se acaba
            public void onFinish() {
                TvTiempo.setText("0S");
                GameOver = true;
                MensajeGameOver();
                GuardarResultados("Patos",contador);
            }
        }.start();
    }

    private void MensajeGameOver() {
        String ubicacion = "fuentes/letra.TTF";
        Typeface typeface= Typeface.createFromAsset(EscenarioJuego.this.getAssets(),ubicacion);

        TextView SeacaboTXT, HasmatadoTXT, NumeroTXT;
        Button JUGARDENUEVO, IRMENU, Puntajes;

        miDialog.setContentView(R.layout.gameover);

        SeacaboTXT=miDialog.findViewById(R.id.SeacaboTXT);
        HasmatadoTXT=miDialog.findViewById(R.id.HasmatadoTXT);
        NumeroTXT=miDialog.findViewById(R.id.NumeroTXT);

        JUGARDENUEVO=miDialog.findViewById(R.id.JUGARDENUEVO);
        IRMENU=miDialog.findViewById(R.id.IRMENU);
        Puntajes=miDialog.findViewById(R.id.Puntajes);

        String patos = String.valueOf(contador);
        NumeroTXT.setText(patos);

        SeacaboTXT.setTypeface(typeface);
        HasmatadoTXT.setTypeface(typeface);
        NumeroTXT.setTypeface(typeface);

        JUGARDENUEVO.setTypeface(typeface);
        IRMENU.setTypeface(typeface);
        Puntajes.setTypeface(typeface);

        JUGARDENUEVO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contador=0;
                miDialog.dismiss();
                TvContador.setText("0");
                GameOver=false;
                CuentaAtras();
                Movimiento();

            }
        });
        IRMENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EscenarioJuego.this,Menu.class));
            }
        });
        Puntajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EscenarioJuego.this,"Puntajes",Toast.LENGTH_SHORT).show();

            }
        });

        miDialog.show();

    }

    private void GuardarResultados(String key, int patos) {
        if (user == null || user.getUid() == null) {
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // UID del usuario autenticado
        String userUid = user.getUid();

        // Referencia a Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Busca el documento donde el campo "Uid" coincide con la UID del usuario
        db.collection("mahunt")
                .whereEqualTo("Uid", userUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Obtiene el primer documento encontrado
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();

                                // Actualiza el campo "Patos" en el documento
                                db.collection("mahunt")
                                        .document(documentId)
                                        .update(key, patos)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(EscenarioJuego.this, "El puntaje se actualizó correctamente", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(EscenarioJuego.this, "Error al actualizar el puntaje: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                break; // Sale del bucle después de encontrar y actualizar el documento
                            }
                        } else {
                            Toast.makeText(EscenarioJuego.this, "No se encontró un documento con este UID", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EscenarioJuego.this, "Error al buscar el documento: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}


