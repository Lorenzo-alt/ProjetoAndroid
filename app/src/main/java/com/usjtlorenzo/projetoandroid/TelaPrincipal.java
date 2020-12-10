package com.usjtlorenzo.projetoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.usjtlorenzo.projetoandroid.Adapter.IconeAdapter;
import com.usjtlorenzo.projetoandroid.Adapter.NaviAdapter;
import com.usjtlorenzo.projetoandroid.Adapter.RecyclerAdapter;
import com.usjtlorenzo.projetoandroid.Modelos.Categoria;
import com.usjtlorenzo.projetoandroid.Modelos.IconeItem;
import com.usjtlorenzo.projetoandroid.Modelos.Lembrete;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TelaPrincipal extends AppCompatActivity implements NaviAdapter.OnCategoriaItemClick, RecyclerAdapter.OnLembreteItemListener, NaviAdapter.OnCategoriaItemLongClick {

    private RecyclerView rv_lembretes;
    private RecyclerView rv_categorias;

    private DrawerLayout dl;
    private NavigationView navigationView;
    private String categoria = "Mostrar Todos Lembretes!";
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FloatingActionButton fb_addLembrete;
    private IconeItem iconeItem;
    private IconeItem corItem;
    private ArrayList<IconeItem> corList = new ArrayList<>();
    private ArrayList<IconeItem> categoriaList = new ArrayList<>();
    private IconeItem categoriaItem;
    private RecyclerAdapter meuAdapter;
    private NaviAdapter meuNaviAdapter;

    private FirebaseUser mUser;
    private DatabaseReference reference;
    private ArrayList<Categoria> minhasCategoria;
    private ArrayList<Lembrete> meusLembretes;
    private ArrayList<Lembrete> todosLembretes;
    private String idLembrete;
    private static final String TAG = "TelaPrincipal";

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

        ConstraintLayout layout_pesquisa = findViewById(R.id.layout_pesquisa);
        final EditText et_pesquisa = findViewById(R.id.et_pesquisar);
        et_pesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString().trim());
            }
        });

        // Metodo de abrir a pesquisa
        findViewById(R.id.ic_pesquisa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pesquisa.getVisibility() == View.GONE) {
                    et_pesquisa.setVisibility(View.VISIBLE);
                    findViewById(R.id.ic_fechar_pesquisa).setVisibility(View.VISIBLE);
                } else{
                    et_pesquisa.setText("");
                    et_pesquisa.setVisibility(View.GONE);
                    findViewById(R.id.ic_fechar_pesquisa).setVisibility(View.GONE);
                }
            }
        });

        // Metodo de fechar pesquisa dentro do layout de pesquisa
        findViewById(R.id.ic_fechar_pesquisa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pesquisa.setText("");
                et_pesquisa.setVisibility(View.GONE);
                findViewById(R.id.ic_fechar_pesquisa).setVisibility(View.GONE);
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
        atualizarNaviView();



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
                iconeList.add(new IconeItem("File", R.drawable.ic_xml_file_96));

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
                        String nomeCategoria = et_nomeCategoria.getText().toString().trim();
                        if (!TextUtils.isEmpty(nomeCategoria)){
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

        // Adicionar lembrete
        fb_addLembrete = findViewById(R.id.fb_addLembrete);
        fb_addLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (minhasCategoria.size() >= 2) {
                    Calendar calendar = Calendar.getInstance();
                    int dia = calendar.get(Calendar.DAY_OF_MONTH);
                    int mes = calendar.get(Calendar.MONTH);
                    int ano = calendar.get(Calendar.YEAR);
                    dialogBuilder = new AlertDialog.Builder(TelaPrincipal.this);
                    View popout = getLayoutInflater().inflate(R.layout.adc_lembrete, null);
                    final EditText et_dataRealizacao = popout.findViewById(R.id.et_dataRealizacao);

                    // Alimentando Spinner de cores
                    Spinner spinnerCor = gerandoSpinnerCor(popout.findViewById(R.id.spinnerCor));

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
                    Spinner spinnerCategoriaL = gerandoSpinnerCategoria(popout.findViewById(R.id.spinnerCategoriaL));

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

                    popout.findViewById(R.id.img_calendario);
                    popout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(TelaPrincipal.this, R.style.Theme_meuDateDialog, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                                    mes = mes + 1;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date dataFormatada = new Date();
                                    try {
                                        dataFormatada = sdf.parse(dia + "/" + mes + "/" + ano);
                                        Date d = calendar.getTime();
                                        String dataAtual = sdf.format(d);
                                        if (!dataFormatada.before(sdf.parse(dataAtual))) {
                                            if (dia < 10 && mes < 10) {
                                                et_dataRealizacao.setText("0" + dia + "/" + "0" + mes + "/" + ano);
                                            } else if(dia < 10) {
                                                et_dataRealizacao.setText("0" + dia + "/" + mes + "/" + ano);
                                            } else if(mes < 10) {
                                                et_dataRealizacao.setText(dia + "/" + "0" + mes + "/" + ano);
                                            }
                                        }else{
                                            Toast.makeText(TelaPrincipal.this, "Não da para voltar no tempo...", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
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
                            String nomeLembrete = et_nomeLembrete.getText().toString().trim();
                            String data = et_dataRealizacao.getText().toString().trim();
                            String cor = corItem.getNomeIconeCor().trim();
                            String categoriaFinal = categoriaItem.getNomeIconeCor().trim();

                            if (data.length()<10){
                                Toast.makeText(TelaPrincipal.this, "Preencha a data corretamente", Toast.LENGTH_LONG).show();
                            }else {
                                if (!(TextUtils.isEmpty(nomeLembrete) || TextUtils.isEmpty(data) || TextUtils.isEmpty(cor) || TextUtils.isEmpty(categoriaFinal))) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                    Date d = calendar.getTime();
                                    criarLembrete(mUser.getUid(), nomeLembrete, data.replace("/", "-"), sdf.format(d), cor, categoriaFinal);
                                } else {
                                    Toast.makeText(TelaPrincipal.this, "Verifique se todos os campos foram preenchidos corretamente!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    dialogBuilder.setView(popout);
                    dialog = dialogBuilder.create();
                    dialog.show();
                }else{
                    Toast.makeText(TelaPrincipal.this, "Não é possivel criar um lembrete sem uma categoria! Crie uma primeiro!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Trabalhando com Recycler View, instanciando e preenchendo.
        atualizarRecyclerView();

    }

    private void filtrar(String text) {
        ArrayList<Lembrete> lembretesFiltrados = new ArrayList<>();

        for (Lembrete item : meusLembretes){
            if (text.equals("/") || text.equals("-")) {
                if (item.getNome().toLowerCase().contains(text.toLowerCase()) || item.getCorrelacao().toLowerCase().contains(text.toLowerCase())) {
                    lembretesFiltrados.add(item);
                }
            } else {
                if (item.getNome().toLowerCase().contains(text.toLowerCase()) || item.getCorrelacao().toLowerCase().contains(text.toLowerCase()) ||
                item.getData().toLowerCase().contains(text.toLowerCase()) || item.getReal_data().toLowerCase().contains(text.toLowerCase())) {
                    lembretesFiltrados.add(item);
                }
            }
        }

        meuAdapter.listaFiltrada(lembretesFiltrados);
    }

    private Spinner gerandoSpinnerCor(Spinner spinnerCorRecebido){
        corList.clear();
        corList.add(new IconeItem("Vermelho", R.drawable.cor_item_vermelho));
        corList.add(new IconeItem("Azul", R.drawable.cor_item_azul));
        corList.add(new IconeItem("Verde", R.drawable.cor_item_verde));
        corList.add(new IconeItem("Amarelo", R.drawable.cor_item_amarelo));

        Spinner spinnerCor = spinnerCorRecebido;

        IconeAdapter corAdapter = new IconeAdapter(getBaseContext(), corList);
        spinnerCor.setAdapter(corAdapter);

        return spinnerCor;
    }
    private Spinner gerandoSpinnerCategoria(Spinner spinnerCategoriaRecebido){
        categoriaList.clear();
        for (Categoria categoria : minhasCategoria){
            if (categoria.getIcone().equals("Bookmark")){
                categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_bookmark_400));
            } else if (categoria.getIcone().equals("Document")) {
                categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_document_delivery_64));
            } else if (categoria.getIcone().equals("Scroll")) {
                categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_scroll_256));
            } else if (categoria.getIcone().equals("File")) {
                categoriaList.add(new IconeItem(categoria.getNome(), R.drawable.ic_xml_file_96));
            }
        }

        Spinner spinnerCategoriaL = spinnerCategoriaRecebido;

        IconeAdapter categoriaAdapter = new IconeAdapter(getBaseContext(), categoriaList);
        spinnerCategoriaL.setAdapter(categoriaAdapter);
        return spinnerCategoriaL;
    }

    private void criarLembrete(String uid, String nomeLembrete, String data, String dataCriacao, String cor, String categoriaFinal) {
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(uid).child("lembrete");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nome", nomeLembrete);
        hashMap.put("data", data);
        hashMap.put("real_data", dataCriacao);
        hashMap.put("cor", cor);
        hashMap.put("correlacao", categoriaFinal);

        boolean resp = true;
        for (Lembrete temp1 : meusLembretes){
            if (resp == true) {
                if (temp1.getNome().equalsIgnoreCase(nomeLembrete)){
                    resp = false;
                } else{
                    resp = true;
                }
            }
        }

        if (resp == true) {

            reference.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(TelaPrincipal.this, "Lembrete: [ " + nomeLembrete + " ] adicionado com sucesso", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TelaPrincipal.this, "Erro na criação do lembrete, tente novamente!", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(this, "Já existe lembrete com esse nome", Toast.LENGTH_SHORT).show();
        }
    }

    public void atualizarRecyclerView(){
        rv_lembretes = findViewById(R.id.rv_lembretes);
        rv_lembretes.setLayoutManager( new LinearLayoutManager(this));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        meusLembretes = new ArrayList<>();
        todosLembretes = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("lembrete");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                meusLembretes.clear();
                todosLembretes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Lembrete lembrete = dataSnapshot.getValue(Lembrete.class);
                    if (lembrete.getCorrelacao().equalsIgnoreCase(categoria)){
                        meusLembretes.add(lembrete);
                    }else if(categoria.equalsIgnoreCase("Mostrar Todos Lembretes!")){
                        meusLembretes.add(lembrete);
                    }
                    todosLembretes.add(lembrete);
                }
                Lembrete temp = null;
                    for (int i = 0; i < meusLembretes.size(); i++){
                        for (int j = i+1; j<meusLembretes.size(); j++){
                            try {
                                Date d1 = sdf.parse(meusLembretes.get(i).getData());
                                Date d2 = sdf.parse(meusLembretes.get(j).getData());
                                if (d1.compareTo(d2) > 0){
                                    temp = meusLembretes.get(i);
                                    meusLembretes.set(i, meusLembretes.get(j));
                                    meusLembretes.set(j, temp);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
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

        boolean resp = true;
        for (Categoria temp : minhasCategoria){
            if (resp == true) {
                if (temp.getNome().equalsIgnoreCase(nomeCategoria)){
                    resp = false;
                } else{
                    resp = true;
                }
            }
        }

        if (resp == true) {
            reference.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(TelaPrincipal.this, "Categoria [ " + nomeCategoria + " ] adicionada com sucesso", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TelaPrincipal.this, "Erro na criação da categoria, tente novamente!", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(this, "Já existe uma categoria com esse nome!", Toast.LENGTH_SHORT).show();
        }
        atualizarNaviView();
    }

    private void atualizarNaviView() {
        navigationView = findViewById(R.id.nav_principal);
        rv_categorias = findViewById(R.id.rv_categorias);
        rv_categorias.setLayoutManager( new LinearLayoutManager(this));

        minhasCategoria = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("categoria");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                minhasCategoria.clear();
                Categoria categoriaTodos = new Categoria("Mostrar Todos Lembretes!", "null");
                if (!minhasCategoria.contains(categoriaTodos)){
                    minhasCategoria.add(0, categoriaTodos);
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Categoria categoria = dataSnapshot.getValue(Categoria.class);

                    if(!minhasCategoria.contains(categoria)){
                        minhasCategoria.add(categoria);
                    }

                }
                meuNaviAdapter = new NaviAdapter(minhasCategoria, TelaPrincipal.this, TelaPrincipal.this, TelaPrincipal.this);
                rv_categorias.setAdapter(meuNaviAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


    @Override
    public void onCategoriaItemClick(int position) {
        final Categoria categoriaAtual = minhasCategoria.get(position);
        categoria = categoriaAtual.getNome();
        dl.closeDrawer(GravityCompat.START);
        atualizarRecyclerView();
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

        et_dataRealizacao.setText(lembrete.getData().replace("-", "/"));
        et_dataCriacao.setText(lembrete.getReal_data().replace("-", "/"));
        et_dataCriacao.setFocusable(false);
        et_dataCriacao.setClickable(false);
        et_tarefa.setText(lembrete.getNome());

        Spinner spinnerCor = gerandoSpinnerCor(popout.findViewById(R.id.spinnerCorAtual));

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

        Spinner spinnerCategoriaL = gerandoSpinnerCategoria(popout.findViewById(R.id.spinnerCategoriaAtual));
                
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
                        mes = mes + 1;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date dataFormatada = new Date();
                        try {
                            dataFormatada = sdf.parse(dia + "/" + mes + "/" + ano);
                            Date d = calendar.getTime();
                            String dataAtual = sdf.format(d);
                            if (!dataFormatada.before(sdf.parse(dataAtual))) {
                                if (dia < 10 && mes < 10) {
                                    et_dataRealizacao.setText("0" + dia + "/" + "0" + mes + "/" + ano);
                                } else if(dia < 10) {
                                    et_dataRealizacao.setText("0" + dia + "/" + mes + "/" + ano);
                                } else if(mes < 10) {
                                    et_dataRealizacao.setText(dia + "/" + "0" + mes + "/" + ano);
                                }
                            }else{
                                Toast.makeText(TelaPrincipal.this, "Não da para voltar no tempo...", Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
                idLembrete = null;
                reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("lembrete");
                Query mQueryUpdate = reference.orderByChild("nome").equalTo(lembrete.getNome());

                String nomeLembrete = et_tarefa.getText().toString();
                String data = et_dataRealizacao.getText().toString();
                String dataCriacao = et_dataCriacao.getText().toString();
                String cor = corItem.getNomeIconeCor();
                String categoriaFinal = categoriaItem.getNomeIconeCor();

                if (data.length()<10){
                    Toast.makeText(TelaPrincipal.this, "Preencha a data corretamente", Toast.LENGTH_LONG).show();
                }else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("nome", nomeLembrete);
                    hashMap.put("data", data.replace("/", "-"));
                    hashMap.put("real_data", dataCriacao.replace("/", "-"));
                    hashMap.put("cor", cor);
                    hashMap.put("correlacao", categoriaFinal);

                    mQueryUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                idLembrete = dataSnapshot.getKey();
                                reference.child(idLembrete).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(TelaPrincipal.this, "Atualizado!!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TelaPrincipal.this, "Erro", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.dismiss();
                }
            }
        });

        popout.findViewById(R.id.btnExcluir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TelaPrincipal.this);
                builder.setMessage("Deseja mesmo excluir o lembrete: [ " + lembrete.getNome() + " ] ?").setTitle("Tem certeza disso ?").setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogDelete, int which) {
                        reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("lembrete");
                        Query mQueryDelete = reference.orderByChild("nome").equalTo(lembrete.getNome());

                        mQueryDelete.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    idLembrete = dataSnapshot.getKey();
                                    Lembrete atualLembrete = dataSnapshot.getValue(Lembrete.class);
                                    if (atualLembrete.getNome().equalsIgnoreCase(lembrete.getNome()) && atualLembrete.getCorrelacao().equalsIgnoreCase(lembrete.getCorrelacao())){
                                        reference.child(idLembrete).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(TelaPrincipal.this, "Removido!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TelaPrincipal.this, "Erro", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        dialogDelete.dismiss();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogDelete, int which) {
                        dialogDelete.dismiss();
                    }
                });

                AlertDialog dialogDelete = builder.create();
                dialogDelete.show();
                int newColor = ContextCompat.getColor(view.getContext(), R.color.paleta_bars);
                dialogDelete.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(newColor);
                dialogDelete.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(newColor);
            }
        });
        dialogBuilder.setView(popout);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onCategoriaItemLongClick(final int position) {
        final Categoria categoria = minhasCategoria.get(position);
        if (!categoria.getNome().equals("Mostrar Todos Lembretes!")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TelaPrincipal.this);
            builder.setMessage("Deseja mesmo excluir a categoria: [ " + categoria.getNome() + " ] ? Isso excluirá todos os lembretes relacionados a ela").setTitle("Tem certeza disso ?").setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogDelete, int which) {
                    reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("categoria");
                    Query mQueryDelete = reference.orderByChild("nome").equalTo(categoria.getNome());
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mUser.getUid()).child("lembrete");
                    Query mQueryLDelete = reference1.orderByChild("correlacao").equalTo(categoria.getNome());

                    mQueryDelete.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                final String idCategoria = dataSnapshot.getKey();
                                Categoria categoriaAtual = dataSnapshot.getValue(Categoria.class);
                                if (categoriaAtual.getNome().equalsIgnoreCase(categoria.getNome()) && categoriaAtual.getIcone().equalsIgnoreCase(categoria.getIcone())) {
                                    reference.child(idCategoria).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(TelaPrincipal.this, "Removido!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(TelaPrincipal.this, "Erro", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    mQueryLDelete.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                final String idLembrete = dataSnapshot.getKey();
                                Lembrete lembreteAtual = dataSnapshot.getValue(Lembrete.class);
                                if (lembreteAtual.getCorrelacao().equalsIgnoreCase(categoria.getNome())) {
                                    reference1.child(idLembrete).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialogDelete.dismiss();
                }
            });
            builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogDelete, int which) {
                    dialogDelete.dismiss();
                }
            });
            AlertDialog dialogDelete = builder.create();
            dialogDelete.show();
            int newColor = ContextCompat.getColor(TelaPrincipal.this, R.color.paleta_bars);
            dialogDelete.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(newColor);
            dialogDelete.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(newColor);
        }
    }
}