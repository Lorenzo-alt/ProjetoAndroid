package com.usjtlorenzo.projetoandroid.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;
import com.usjtlorenzo.projetoandroid.Modelos.Categoria;

import java.util.ArrayList;

public class CategoriaAdapter extends ArrayAdapter<Categoria> {
    public CategoriaAdapter(Context context, ArrayList<Categoria> categorias){
        super(context, 0, categorias);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
