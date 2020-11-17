package com.usjtlorenzo.projetoandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usjtlorenzo.projetoandroid.R;

public class MeuAdapter extends RecyclerView.Adapter<MeuAdapter.MeuViewHolder> {

    private String l1[], l2[], l3[];
    private Context ct;

    public MeuAdapter(Context ct, String l1[], String l2[], String l3[]){
        this.ct = ct;
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.minha_linha, parent,false);
        return new MeuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        holder.tv_tarefa.setText(l1[position]);
        holder.tv_dtCadastro.setText(l2[position]);
        holder.tv_dtRealizacao.setText(l3[position]);
    }

    @Override
    public int getItemCount() {
        return l1.length;
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        TextView tv_tarefa, tv_dtCadastro, tv_dtRealizacao;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tarefa = itemView.findViewById(R.id.tv_tarefa);
            tv_dtCadastro = itemView.findViewById(R.id.tv_dtCadastro);
            tv_dtRealizacao = itemView.findViewById(R.id.tv_dtRealizacao);
        }
    }
}
