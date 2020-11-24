package com.usjtlorenzo.projetoandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.usjtlorenzo.projetoandroid.Modelos.IconeItem;
import com.usjtlorenzo.projetoandroid.R;

import java.util.ArrayList;

public class IconeAdapter extends ArrayAdapter<IconeItem> {

    public IconeAdapter(Context context, ArrayList<IconeItem> iconeList){
        super(context, 0, iconeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return iniciarView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return iniciarView(position, convertView, parent);
    }

    private View iniciarView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.linha_spinner_icone_cor, parent, false
            );
        }

        ImageView img_iconeCor = convertView.findViewById(R.id.iconeCor);
        TextView tv_nomeIconeCor = convertView.findViewById(R.id.nomeIconeCor);

        IconeItem iconeItem = getItem(position);

        if (iconeItem != null) {
            img_iconeCor.setImageResource(iconeItem.getIconeCor());
            tv_nomeIconeCor.setText(iconeItem.getNomeIconeCor());
        }
        return convertView;
    }
}
