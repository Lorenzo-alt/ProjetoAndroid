package com.usjtlorenzo.projetoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button bt_login;
    private TextView tv_registrar;
    private EditText loginEditText;
    private EditText senhaEditText;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Verificando se ja ocorreu um login, salvando login mesmo se fechar o app
        if (firebaseUser != null){
            Intent i = new Intent(getApplicationContext(), TelaPrincipal.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.backgroundnovo);

        // Recuperar os campos  de login e senha e inicializar Firebase Auth
        loginEditText = findViewById(R.id.loginEditText);
        senhaEditText = findViewById(R.id.senhaEditText);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        bt_login = findViewById(R.id.bt_login);
        tv_registrar = findViewById(R.id.tv_registrar);

        // Criando o metodo de login para o evento de click no bt_login
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginEditText.getEditableText().toString();
                String senha = senhaEditText.getEditableText().toString();
                if (TextUtils.isEmpty(login) || TextUtils.isEmpty(senha)) {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(login, senha).addOnSuccessListener((result) -> {
                        startActivity(new Intent(getApplicationContext(), TelaPrincipal.class));
                        finish();
                    }).addOnFailureListener((exception) -> {
                        exception.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Login invalido!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        // Metodo para iniciar tela de registro
        tv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TelaRegistrar.class));
            }
        });
    }
}