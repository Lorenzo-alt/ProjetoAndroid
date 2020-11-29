package com.usjtlorenzo.projetoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.HashBiMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TelaRegistrar extends AppCompatActivity {

    private EditText loginNovoUsuarioText;
    private EditText senhaNovoUsuarioText;
    private EditText senhaConfirmar;
    private EditText username;
    private FirebaseAuth mAuth;
    private Button bt_registrar;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_registrar);
        getWindow().setBackgroundDrawableResource(R.drawable.backgroundregistrar);

        loginNovoUsuarioText = findViewById(R.id.loginNovoUsuarioText);
        senhaNovoUsuarioText = findViewById(R.id.senhaNovoUsuarioText);
        senhaConfirmar = findViewById(R.id.senhaConfirmar);
        username = findViewById(R.id.username);
        bt_registrar = findViewById(R.id.bt_registrar);
        //Firebase Auth, Database
        mAuth = FirebaseAuth.getInstance();


        // Registrando Usuário
        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginNovoUsuarioText.getText().toString();
                String senha = senhaNovoUsuarioText.getText().toString();
                String senha2 = senhaConfirmar.getText().toString();
                String user = username.getText().toString();

                if (TextUtils.isEmpty(login) || TextUtils.isEmpty(senha) || TextUtils.isEmpty(senha2) || TextUtils.isEmpty(user)) {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    if (senha.equals(senha2)) {
                        Registrar(user, login, senha);
                    } else {
                        Toast.makeText(getApplicationContext(), "Insira senhas iguais", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void Registrar(String user, String login, String senha) {
        mAuth.createUserWithEmailAndPassword(login, senha).addOnSuccessListener((result) -> {
                            Toast.makeText(getApplicationContext(), "Usuário Cadastrado!", Toast.LENGTH_SHORT).show();
                            finish();
                    Intent i = new Intent(TelaRegistrar.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
        }).addOnFailureListener((exception) -> {
            exception.printStackTrace();
            Toast.makeText(getApplicationContext(), "Impossivel realizar registro, verifique todos os campos!", Toast.LENGTH_SHORT).show();
        });
    }
}