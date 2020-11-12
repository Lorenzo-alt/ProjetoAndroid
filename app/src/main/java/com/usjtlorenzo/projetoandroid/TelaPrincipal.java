package com.usjtlorenzo.projetoandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

import static com.usjtlorenzo.projetoandroid.R.drawable.backgroundnovo;

public class TelaPrincipal extends AppCompatActivity {

    private RecyclerView rv_lembretes;

    private DrawerLayout dl;
    private NavigationView navigationView;
    private String l1[], l2[], l3[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        getWindow().setBackgroundDrawableResource(R.drawable.telaprincipal);

        dl = findViewById(R.id.drawerLayout);
        findViewById(R.id.logotb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(GravityCompat.START);
            }
        });

        navigationView = findViewById(R.id.nav_principal);
        navigationView.setItemIconTintList(null);

        rv_lembretes = findViewById(R.id.rv_lembretes);

        l1 = getResources().getStringArray(R.array.tarefas);
        l2 = getResources().getStringArray(R.array.datas_cadastro);
        l3 = getResources().getStringArray(R.array.datas_previsao);

        MeuAdapter meuAdapter = new MeuAdapter(this, l1, l2, l3);
        rv_lembretes.setAdapter(meuAdapter);
        rv_lembretes.setLayoutManager( new LinearLayoutManager(this));
        rv_lembretes.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL){
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
    }
}