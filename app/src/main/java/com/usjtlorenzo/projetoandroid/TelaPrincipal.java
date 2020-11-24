package com.usjtlorenzo.projetoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usjtlorenzo.projetoandroid.Adapter.IconeAdapter;
import com.usjtlorenzo.projetoandroid.Adapter.RecyclerAdapter;
import com.usjtlorenzo.projetoandroid.Modelos.Categoria;
import com.usjtlorenzo.projetoandroid.Modelos.IconeItem;
import com.usjtlorenzo.projetoandroid.Modelos.Lembrete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TelaPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerAdapter.OnLembreteItemListener {

    private RecyclerView rv_lembretes;

    private DrawerLayout dl;
    private NavigationView navigationView;
    private String categoria = "Mostrar Todos Lembretes!";
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FloatingActionButton fb_addLembrete;
    private IconeItem iconeItem;
    private IconeItem corItem;
    private IconeItem categoriaItem;
    private RecyclerAdapter meuAdapter;

    private FirebaseUser mUser;
    private DatabaseReference reference;
    private ArrayList<Categoria> minhasCategoria;
    private ArrayList<Lembrete> meusLembretes;
    private ArrayList<String[]> meusIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        getWindow().setBackgroundDrawableResource(R.drawable.telaprincipal);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        minhasCategoria = new ArrayList<>();

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
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        Menu menu = navigationView.getMenu();
        menu.clear();

        minhasCategoria = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("categoria");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                minhasCategoria.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Categoria categoria = dataSnapshot.getValue(Categoria.class);

                    if(!minhasCategoria.contains(categoria)){
                        minhasCategoria.add(categoria);
                    }

                }
                atualizarNaviView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Colocar a função de adicionar categoria
        navigationView.getHeaderView(0).findViewById(R.id.btn_AdcCategoria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(TelaPrincipal.this);
                final View popout = getLayoutInflater().inflate(R.layout.adc_categoria, null);

                // Alimentando spinner de icone
                final ArrayList<IconeItem> iconeList = new ArrayList<>();
                iconeList.add(new IconeItem("Bookmark", R.drawable.ic_bookmark_400));
                iconeList.add(new IconeItem("Document", R.drawable.ic_document_delivery_64));
                iconeList.add(new IconeItem("Scroll", R.drawable.ic_scroll_256));
                iconeList.add(new IconeItem("XML File", R.drawable.ic_xml_file_96));

                Spinner spinnerIcone = popout.findViewById(R.id.escolhaIcone);

                IconeAdapter iconeAdapter = new IconeAdapter(getBaseContext(), iconeList);
                spinnerIcone.setAdapter(iconeAdapter);

                spinnerIcone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        iconeItem = (IconeItem) adapterView.getItemAtPosition(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        iconeItem = iconeList.get(0);
                    }
                });

                popout.findViewById(R.id.fecharAdcCategoria).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                popout.findViewById(R.id.btn_criarCategoria).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText et_nomeCategoria = popout.findViewById(R.id.et_nomeCategoria);
                        String nomeCategoria = et_nomeCategoria.getText().toString();
                        if (!nomeCategoria.equals("")){
                            criarCategoria(mUser.getUid(), nomeCategoria, iconeItem.getNomeIconeCor());
                        }else{
                            Toast.makeText(TelaPrincipal.this, "Verifique se os campos estão vazios", Toast.LENGTH_LONG).show();
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
                Calendar calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int ano = calendar.get(Calendar.YEAR);
                dialogBuilder = new AlertDialog.Builder(TelaPrincipal.this);
                View popout = getLayoutInflater().inflate(R.layout.adc_lembrete, null);
                final EditText et_dataRealizacao = popout.findViewById(R.id.et_dataRealizacao);

                // Alimentando Spinner de cores
                final ArrayList<IconeItem> corList = new ArrayList<>();
                corList.add(new IconeItem("Vermelho", R.drawable.cor_item_vermelho));
                corList.add(new IconeItem("Azul", R.drawable.cor_item_azul));
                corList.add(new IconeItem("Verde", R.drawable.cor_item_verde));
                corList.add(new IconeItem("Amarelo", R.drawable.cor_item_amarelo));

                Spinner spinnerCor = popout.findViewById(R.id.spinnerCor);

                IconeAdapter corAdapter = new IconeAdapter(getBaseContext(), corList);
                spinnerCor.setAdapter(corAdapter);

                spinnerCor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        corItem = (IconeItem) adapterView.getItemAtPosition(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        corItem = corList.get(0);
                    }
                });

                // Alimentando Spinner de categorias no lembrete
                final ArrayList<IconeItem> categoriaList = new ArrayList<>();
                for (Categoria categoria : minhasCategoria){
                    if (categoria.getIcone().equals("Bookmark")){
                        categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_bookmark_400));
                    } else if (categoria.getIcone().equals("Document")) {
                        categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_document_delivery_64));
                    } else if (categoria.getIcone().equals("Scroll")) {
                        categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_scroll_256));
                    } else if (categoria.getIcone().equals("XML File")) {
                        categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_xml_file_96));
                    }
                }

                Spinner spinnerCategoriaL = popout.findViewById(R.id.spinnerCategoriaL);

                IconeAdapter categoriaAdapter = new IconeAdapter(getBaseContext(), categoriaList);
                spinnerCategoriaL.setAdapter(categoriaAdapter);

                spinnerCategoriaL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        categoriaItem = (IconeItem) adapterView.getItemAtPosition(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        categoriaItem = categoriaList.get(0);
                    }
                });

                popout.findViewById(R.id.fecharAdcLembrete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                popout.findViewById(R.id.img_calendario).setOnClickListener(new View.OnClickListener() {
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
                        int newColor = ContextCompat.getColor(view.getContext(), R.color.paleta_bars);
                        datePickerDialog.show();
                        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(newColor);
                        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(newColor);
                    }
                });
                popout.findViewById(R.id.btn_criarLembrete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText et_nomeLembrete = popout.findViewById(R.id.et_nomeLembrete);
                        EditText et_data = popout.findViewById(R.id.et_dataRealizacao);
                        String nomeLembrete = et_nomeLembrete.getText().toString().trim();
                        String data = et_data.getText().toString().trim();
                        String cor = corItem.getNomeIconeCor().trim();
                        String categoriaFinal = categoriaItem.getNomeIconeCor().trim();

                        if (!(TextUtils.isEmpty(nomeLembrete) || TextUtils.isEmpty(data) || TextUtils.isEmpty(cor) || TextUtils.isEmpty(categoriaFinal))){
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date d = calendar.getTime();
                            criarLembrete(mUser.getUid(), nomeLembrete, data, sdf.format(d), cor, categoriaFinal);
                        } else{
                            Toast.makeText(TelaPrincipal.this, "Verifique se todos os campos foram preenchidos corretamente!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialogBuilder.setView(popout);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        });

        // Trabalhando com Recycler View, instanciando e preenchendo.
        atualizarRecyclerView();

    }


    private void criarLembrete(String uid, String nomeLembrete, String data, String dataCriacao, String cor, String categoriaFinal) {
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(uid).child("lembrete");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nome", nomeLembrete);
        hashMap.put("data", data);
        hashMap.put("real_data", dataCriacao);
        hashMap.put("cor", cor);
        hashMap.put("correlacao", categoriaFinal);

        reference.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TelaPrincipal.this, "Lembrete: [" + nomeLembrete + "] adicionado com sucesso", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TelaPrincipal.this, "Erro na criação do lembrete, tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void atualizarRecyclerView(){
        rv_lembretes = findViewById(R.id.rv_lembretes);
        rv_lembretes.setLayoutManager( new LinearLayoutManager(this));

        meusLembretes = new ArrayList<>();
        meusIds = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("lembrete");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                meusLembretes.clear();
                meusIds.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Lembrete lembrete = dataSnapshot.getValue(Lembrete.class);
                    meusIds.add(new String[]{dataSnapshot.getKey(), lembrete.getNome()});
                    if (lembrete.getCorrelacao().equalsIgnoreCase(categoria)){
                        meusLembretes.add(lembrete);
                    }else if(categoria.equalsIgnoreCase("Mostrar Todos Lembretes!")){
                        meusLembretes.add(lembrete);
                    }
                }
                meuAdapter = new RecyclerAdapter(TelaPrincipal.this, meusLembretes, TelaPrincipal.this);
                rv_lembretes.setAdapter(meuAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void criarCategoria(String uid, String nomeCategoria, String iconeItem) {
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(uid).child("categoria");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nome", nomeCategoria);
        hashMap.put("icone", iconeItem);

        reference.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TelaPrincipal.this, "Categoria [" + nomeCategoria + "] adicionada com sucesso", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TelaPrincipal.this, "Erro na criação da categoria, tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
        atualizarNaviView();
    }

    private void atualizarNaviView() {
        navigationView = findViewById(R.id.nav_principal);
        navigationView.setItemIconTintList(null);
        Menu menu = navigationView.getMenu();

        menu.clear();
        menu.add(1, 0, 0, "Mostrar Todos Lembretes!");

        for(Categoria categoria : minhasCategoria){
            if (categoria.getIcone().equals("Bookmark")){
                menu.add(categoria.getNome()).setIcon(R.drawable.ic_bookmark_400);
            } else if (categoria.getIcone().equals("Document")) {
                menu.add(categoria.getNome()).setIcon(R.drawable.ic_document_delivery_64);
            } else if (categoria.getIcone().equals("Scroll")) {
                menu.add(categoria.getNome()).setIcon(R.drawable.ic_scroll_256);
            } else if (categoria.getIcone().equals("XML File")) {
                menu.add(categoria.getNome()).setIcon(R.drawable.ic_xml_file_96);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        categoria = item.getTitle().toString();
        dl.closeDrawer(GravityCompat.START);
        atualizarRecyclerView();
        return true;
    }

    @Override
    public void onLembreteItemClick(final int position) {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        final Lembrete lembrete = meusLembretes.get(position);
        dialogBuilder = new AlertDialog.Builder(TelaPrincipal.this);
        View popout = getLayoutInflater().inflate(R.layout.container_lembrete, null);
        final EditText et_dataRealizacao = popout.findViewById(R.id.dtRealizacaoEditText);
        EditText et_dataCriacao = popout.findViewById(R.id.dtCriacaoEditText);
        EditText et_tarefa = popout.findViewById(R.id.tarefaEditText);

        et_dataRealizacao.setText(lembrete.getData());
        et_dataCriacao.setText(lembrete.getReal_data());
        et_dataCriacao.setFocusable(false);
        et_dataCriacao.setClickable(false);
        et_tarefa.setText(lembrete.getNome());

        final ArrayList<IconeItem> corList = new ArrayList<>();
        corList.add(new IconeItem("Vermelho", R.drawable.cor_item_vermelho));
        corList.add(new IconeItem("Azul", R.drawable.cor_item_azul));
        corList.add(new IconeItem("Verde", R.drawable.cor_item_verde));
        corList.add(new IconeItem("Amarelo", R.drawable.cor_item_amarelo));

        Spinner spinnerCor = popout.findViewById(R.id.spinnerCorAtual);

        IconeAdapter corAdapter = new IconeAdapter(getBaseContext(), corList);
        spinnerCor.setAdapter(corAdapter);
        for (IconeItem item : corList){
            if (item.getNomeIconeCor().equalsIgnoreCase(lembrete.getCor())) {
                spinnerCor.setSelection(corList.indexOf(item));
            }
        }

        spinnerCor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                corItem = (IconeItem) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                for (IconeItem item : corList){
                    if (item.getNomeIconeCor().equalsIgnoreCase(lembrete.getCor())) {
                        corItem = item;
                    }
                }
            }
        });

        final ArrayList<IconeItem> categoriaList = new ArrayList<>();
        for (Categoria categoria : minhasCategoria){
            if (categoria.getIcone().equals("Bookmark")){
                categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_bookmark_400));
            } else if (categoria.getIcone().equals("Document")) {
                categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_document_delivery_64));
            } else if (categoria.getIcone().equals("Scroll")) {
                categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_scroll_256));
            } else if (categoria.getIcone().equals("XML File")) {
                categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_xml_file_96));
            }
        }

        Spinner spinnerCategoriaL = popout.findViewById(R.id.spinnerCategoriaAtual);

        IconeAdapter categoriaAdapter = new IconeAdapter(getBaseContext(), categoriaList);
        spinnerCategoriaL.setAdapter(categoriaAdapter);
        for (IconeItem item : categoriaList){
            if (item.getNomeIconeCor().equalsIgnoreCase(lembrete.getCorrelacao())) {
                spinnerCategoriaL.setSelection(categoriaList.indexOf(item));
            }
        }

        spinnerCategoriaL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoriaItem = (IconeItem) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                for (IconeItem item : categoriaList){
                    if (item.getNomeIconeCor().equalsIgnoreCase(lembrete.getCorrelacao())) {
                        categoriaItem = item;
                    }
                }
            }
        });

        popout.findViewById(R.id.img_calendarioLA).setOnClickListener(new View.OnClickListener() {
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
                int newColor = ContextCompat.getColor(view.getContext(), R.color.paleta_bars);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(newColor);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(newColor);
            }
        });

        popout.findViewById(R.id.fecharBtnContainerL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        popout.findViewById(R.id.btnCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        popout.findViewById(R.id.btnSalvar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("lembrete");

                String nomeLembrete = et_tarefa.getText().toString();
                String data = et_dataRealizacao.getText().toString();
                String dataCriacao = et_dataCriacao.getText().toString();
                String cor = corItem.getNomeIconeCor();
                String categoriaFinal = categoriaItem.getNomeIconeCor();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("nome", nomeLembrete);
                hashMap.put("data", data);
                hashMap.put("real_data", dataCriacao);
                hashMap.put("cor", cor);
                hashMap.put("correlacao", categoriaFinal);

//                FirebaseRecyclerAdapter<Lembrete, RecyclerAdapter.myViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Lembrete, RecyclerAdapter.myViewHolder>() {
//                    @Override
//                    protected void onBindViewHolder(@NonNull RecyclerAdapter.myViewHolder holder, int position, @NonNull Lembrete model) {
//                        String userId = getRef(position).getKey();
//                    }
//
//                    @NonNull
//                    @Override
//                    public RecyclerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        return null;
//                    }
//                };

                String lembreteId = null;
                for (String[] test : meusIds){
                    if (test[1].equalsIgnoreCase(lembrete.getNome())) {
                        lembreteId = test[0];
                    }
                }

                reference.child(lembreteId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TelaPrincipal.this, "Removido", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TelaPrincipal.this, "Erro", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
        popout.findViewById(R.id.btnExcluir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("lembrete");

                String lembreteId = null;
                for (String[] test : meusIds){
                    if (test[1].equalsIgnoreCase(lembrete.getNome())) {
                        lembreteId = test[0];
                    }
                }

                reference.child(lembreteId).removeValue();
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(popout);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}