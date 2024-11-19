package com.utch.mahuntproyecto;

import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);

        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);
        IvPatos = findViewById(R.id.IvPatos);

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
            }
        }.start();
    }
}


