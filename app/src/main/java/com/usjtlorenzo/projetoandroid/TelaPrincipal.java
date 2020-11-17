package com.usjtlorenzo.projetoandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.usjtlorenzo.projetoandroid.Adapter.MeuAdapter;

import java.util.Calendar;

public class TelaPrincipal extends AppCompatActivity {

    private RecyclerView rv_lembretes;

    private DrawerLayout dl;
    private NavigationView navigationView;
    private String l1[], l2[], l3[];
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FloatingActionButton fb_addLembrete;
    private EditText et_dataRealizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        getWindow().setBackgroundDrawableResource(R.drawable.telaprincipal);

        dl = findViewById(R.id.drawerLayout);

        // Metodo para abrir a navi view
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(GravityCompat.START);
            }
        });

        // Metodo de logout
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(TelaPrincipal.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        // Trabalhando com o navi view
        navigationView = findViewById(R.id.nav_principal);
        navigationView.setItemIconTintList(null);
        Menu menu = navigationView.getMenu();
        menu.clear();


        // Colocar a função de adicionar categoria
        navigationView.getHeaderView(0).findViewById(R.id.btn_AdcCategoria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(TelaPrincipal.this);
                View popout = getLayoutInflater().inflate(R.layout.adc_categoria, null);
                Spinner spinnerCor = popout.findViewById(R.id.escolhaIcone);
                popout.findViewById(R.id.fecharAdcCategoria).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                popout.findViewById(R.id.btn_criarCategoria).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText et_nome = popout.findViewById(R.id.et_nomeCategoria);
                        String nomeCategoria = et_nome.getText().toString();
                        EditText et_icone = popout.findViewById(R.id.escolhaIcone);
                        String icone = et_icone.getText().toString();
                        if(icone.equalsIgnoreCase("bookmark")){
                            menu.add(nomeCategoria).setIcon(R.drawable.ic_document_delivery_64);
                        }
                    }
                });
                dialogBuilder.setView(popout);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        });

        // Adiocionar lembrete
        fb_addLembrete = findViewById(R.id.fb_addLembrete);
        fb_addLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener;
                Calendar calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int ano = calendar.get(Calendar.YEAR);
                dialogBuilder = new AlertDialog.Builder(TelaPrincipal.this);
                View popout = getLayoutInflater().inflate(R.layout.adc_lembrete, null);
                et_dataRealizacao = popout.findViewById(R.id.et_dataRealizacao);
                popout.findViewById(R.id.fecharAdcLembrete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                popout.findViewById(R.id.et_dataRealizacao).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TelaPrincipal.this, R.style.Theme_meuDateDialog, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                                mes = mes+1;
                                et_dataRealizacao.setText(dia + "/" + mes + "/" + ano);
                            }
                        }, ano, mes, dia
                        );
                        datePickerDialog.show();
                    }
                });
                dialogBuilder.setView(popout);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        });

        // Trabalhando com Recycler View, instanciando e preenchendo.
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

    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}