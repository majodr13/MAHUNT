package com.utch.mahuntproyecto;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);

        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);
        IvPatos = findViewById(R.id.IvPatos);

        miDialog = new Dialog(EscenarioJuego.this);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            UIDS = intent.getString("UID");
            NOMBRES = intent.getString("NOMBRE");
            PATOS = intent.getString("PATO");

            TvNombre.setText(NOMBRES);
            TvContador.setText(PATOS);

            AnchoTv = findViewById(R.id.AnchoTv);
            AltoTv = findViewById(R.id.AltoTv);

            //Pantalla(); Porque esta comentado esto y dentro de un IF
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
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished/1000;
                TvTiempo.setText(segundosRestantes+"S");
            }

            //Cuando se acaba
            public void onFinish() {
                TvTiempo.setText("0S");
                GameOver = true;
                MensajeGameOver();
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
                Toast.makeText(EscenarioJuego.this,"Jugar de nuevo",Toast.LENGTH_SHORT).show();
            }
        });
        IRMENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EscenarioJuego.this,"Menu",Toast.LENGTH_SHORT).show();

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
}


