package com.usjtlorenzo.projetoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaRegistrar extends AppCompatActivity {

    private Button bt_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_registrar);
        getWindow().setBackgroundDrawableResource(R.drawable.backgroundregistrar);

        bt_registrar = findViewById(R.id.bt_registrar);

        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}