package com.usjtlorenzo.projetoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class TelaPrincipal extends AppCompatActivity {

    EditText et_texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        et_texto = findViewById(R.id.et_texto);
        et_texto.setText(getIntent().getStringExtra("texto"));
    }
}