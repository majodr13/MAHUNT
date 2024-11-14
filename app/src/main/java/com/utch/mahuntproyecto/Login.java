package com.utch.mahuntproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText correoLogin, passLogin;
    Button BtnLogin;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        correoLogin = findViewById(R.id.correoLogin);
        passLogin = findViewById(R.id.passLogin);
        BtnLogin = findViewById(R.id.BtnLogin);
        auth = FirebaseAuth.getInstance();

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = correoLogin.getText().toString();
                String pass = passLogin.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    correoLogin.setError("Correo no es valido");
                    correoLogin.setFocusable(true);
                } else if (pass.length() < 6) {
                    passLogin.setError("La contraseña debe ser mayor a 6 caracteres");
                    passLogin.setFocusable(true);
                } else {
                    LogeoDeJugador(email, pass);
                }
            }
        });
    }

    private void LogeoDeJugador(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(Login.this, Menu.class));
                            assert user != null;
                            Toast.makeText(Login.this,"BIENVENIDO(A)" + user.getEmail(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}