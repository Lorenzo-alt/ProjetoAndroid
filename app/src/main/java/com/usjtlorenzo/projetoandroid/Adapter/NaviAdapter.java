package com.usjtlorenzo.projetoandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usjtlorenzo.projetoandroid.Modelos.Categoria;
import com.usjtlorenzo.projetoandroid.Modelos.IconeItem;
import com.usjtlorenzo.projetoandroid.R;

import java.util.ArrayList;

public class NaviAdapter extends RecyclerView.Adapter<NaviAdapter.myViewHolder>{

    private ArrayList<Categoria> categorias;
    private Context context;
    private OnCategoriaItemClick mOnCategoriaItemClick;
    private OnCategoriaItemLongClick mOnCategoriaItemLongClick;

    public NaviAdapter(ArrayList<Categoria> categorias, Context context, OnCategoriaItemClick onCategoriaItemClick, OnCategoriaItemLongClick onCategoriaItemLongClick) {
        this.categorias = categorias;
        this.context = context;
        this.mOnCategoriaItemClick = onCategoriaItemClick;
        this.mOnCategoriaItemLongClick = onCategoriaItemLongClick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.linha_naviview, parent,false);
        return new NaviAdapter.myViewHolder(view, mOnCategoriaItemClick, mOnCategoriaItemLongClick);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Categoria model = categorias.get(position);
        holder.txt_nomeCategoria.setText(model.getNome());
        if (model.getIcone().equals("Bookmark")){
            holder.img_icone.setImageResource(R.drawable.ic_bookmark_400);
        } else if (model.getIcone().equals("Document")) {
            holder.img_icone.setImageResource(R.drawable.ic_document_delivery_64);
        } else if (model.getIcone().equals("Scroll")) {
            holder.img_icone.setImageResource(R.drawable.ic_scroll_256);
        } else if (model.getIcone().equals("File")) {
            holder.img_icone.setImageResource(R.drawable.ic_xml_file_96);
        } else if (model.getIcone().equals("null")){
            holder.img_icone.setImageResource(R.drawable.ic_autorenew_24);
            holder.img_icone.setColorFilter(R.color.black);
        }
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView img_icone;
        TextView txt_nomeCategoria;
        OnCategoriaItemClick onCategoriaItemClick;
        OnCategoriaItemLongClick onCategoriaItemLongClick;

        public myViewHolder(@NonNull View itemView, OnCategoriaItemClick onCategoriaItemClick, OnCategoriaItemLongClick onCategoriaItemLongClick) {
            super(itemView);
            img_icone = itemView.findViewById(R.id.iconeCorN);
            txt_nomeCategoria = itemView.findViewById(R.id.nomeIconeCorN);

            this.onCategoriaItemClick = onCategoriaItemClick;
            this.onCategoriaItemLongClick = onCategoriaItemLongClick;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCategoriaItemClick.onCategoriaItemClick(getAdapterPosition());
        }
        @Override
        public boolean onLongClick(View v) {
            onCategoriaItemLongClick.onCategoriaItemLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnCategoriaItemClick{
        void onCategoriaItemClick(int position);
    }
    public interface OnCategoriaItemLongClick{
        void onCategoriaItemLongClick(int position);
    }
}
