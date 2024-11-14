package com.utch.mahuntproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Registro extends AppCompatActivity {
    FirebaseAuth auth;
    EditText correoEt, passEt, nombreEt;
    TextView fechaTxt;
    Button Registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        correoEt = findViewById(R.id.correoEt);
        passEt = findViewById(R.id.passEt);
        nombreEt = findViewById(R.id.nombreEt);
        fechaTxt = findViewById(R.id.fechaTxt);
        Registrar = findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance();


        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String StringFecha = fecha.format(date);
        fechaTxt.setText(StringFecha);

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = correoEt.getText().toString();
                String password = passEt.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    correoEt.setError("Correo no es valido");
                    correoEt.setFocusable(true);
                } else if (password.length()<6) {
                    passEt.setError("La contraseña debe ser mayor a 6");
                    correoEt.setFocusable(true);

                }else {
                    RegistrarJugador(email,password);
                }

            }
        });

    }
//Metodo para registrar un jugador

private void RegistrarJugador(String email, String password) {
    auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user= auth.getCurrentUser();

                        int contador =0;

                        assert user !=null;
                        String uidString= user.getUid();
                        String correoString= correoEt.getText().toString();
                        String passString=passEt.getText().toString();
                        String nombreString= nombreEt.getText().toString();
                        String fechaString= fechaTxt.getText().toString();

                        HashMap<Object,Object>DatosJUGADOR = new HashMap<>();

                        DatosJUGADOR.put("Uid",uidString);
                        DatosJUGADOR.put("Email",correoString);
                        DatosJUGADOR.put("Password",passString);
                        DatosJUGADOR.put("Nombres",nombreString);
                        DatosJUGADOR.put("Fecha",fechaString);
                        DatosJUGADOR.put("Patos",contador); //Zombies por Patos

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Mahunt");//nombre de la base de datos
                        reference.child(uidString).setValue(DatosJUGADOR);
                        startActivity(new Intent(Registro.this,Menu.class));
                        Toast.makeText(Registro.this, "USUARIO REGISTRADO EXITOSAMENTE",Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(Registro.this, "HA OCURRIDO UN ERROR " , Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
}


}