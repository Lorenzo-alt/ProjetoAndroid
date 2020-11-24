package com.usjtlorenzo.projetoandroid.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.usjtlorenzo.projetoandroid.Modelos.IconeItem;
import com.usjtlorenzo.projetoandroid.Modelos.Lembrete;
import com.usjtlorenzo.projetoandroid.R;

import java.security.AccessController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

//public class MeuAdapter extends FirebaseRecyclerAdapter<Lembrete,MeuAdapter.myViewHolder> {
//
//    FirebaseRecyclerOptions<Lembrete> options;
//    String categoria;
//
//    public MeuAdapter(@NonNull FirebaseRecyclerOptions<Lembrete> options, String categoria) {
//        super(options);
//        this.options = options;
//        this.categoria = categoria;
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Lembrete model) {
//        if (categoria.equalsIgnoreCase("todos")) {
//            gerandoLembrete(holder, position, model);
//        } else if (categoria.equalsIgnoreCase(model.getCategoria())){
//            gerandoLembrete(holder, position, model);
//        }
//    }
//
//    public void gerandoLembrete(@NonNull myViewHolder holder, int position, @NonNull Lembrete model){
//        holder.tv_tarefa.setText(model.getNomeLembrete());
//        holder.tv_dtRealizacao.setText(model.getDataRealizacao());
//        holder.tv_dia.setText(model.getDataRealizacao().substring(0, 2));
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        GregorianCalendar gc = new GregorianCalendar();
//        try {
//            gc.setTime(sdf.parse(model.getDataRealizacao()));
//            switch (gc.get(Calendar.DAY_OF_WEEK)) {
//                case Calendar.SUNDAY:
//                    holder.tv_diaS.setText("DOM");
//                    break;
//                case Calendar.MONDAY:
//                    holder.tv_diaS.setText("SEG");
//                    break;
//                case Calendar.TUESDAY:
//                    holder.tv_diaS.setText("TER");
//                    break;
//                case Calendar.WEDNESDAY:
//                    holder.tv_diaS.setText("QUA");
//                    break;
//                case Calendar.THURSDAY:
//                    holder.tv_diaS.setText("QUI");
//                    break;
//                case Calendar.FRIDAY:
//                    holder.tv_diaS.setText("SEX");
//                    break;
//                case Calendar.SATURDAY:
//                    holder.tv_diaS.setText("SAB");
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (model.getCor().equalsIgnoreCase("Vermelho")) {
//            holder.img_boxLinha.setBackgroundResource(R.drawable.dr_caixa_linha_vermelho);
//        } else if (model.getCor().equalsIgnoreCase("Verde")) {
//            holder.img_boxLinha.setBackgroundResource(R.drawable.dr_caixa_linha_verde);
//        } else if (model.getCor().equalsIgnoreCase("Amarelo")) {
//            holder.img_boxLinha.setBackgroundResource(R.drawable.dr_caixa_linha_amarelo);
//        }
//    }
//    @NonNull
//    @Override
//    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.minha_linha,parent,false);
//        return new myViewHolder(view);
//    }
//
//    public class myViewHolder extends RecyclerView.ViewHolder{
//        ImageView img_boxLinha, img_sinalLinha;
//        TextView tv_tarefa, tv_dtRealizacao, tv_dia, tv_diaS;
//
//
//        public myViewHolder(@NonNull View itemView) {
//            super(itemView);
//            img_boxLinha = itemView.findViewById(R.id.img_boxLinha);
//            tv_tarefa = itemView.findViewById(R.id.tv_tarefa);
//            tv_dtRealizacao = itemView.findViewById(R.id.tv_dtRealizacao);
//            tv_dia = itemView.findViewById(R.id.tv_dia);
//            tv_diaS = itemView.findViewById(R.id.tv_diaS);
//        }
//    }
//
////    private String tarefasList[], dataCadastroList[], dataRealizacaoList[], corList[], categoriaList[];
////    private Context ct;
////
////    public MeuAdapter(Context ct, String l1[], String l2[], String l3[]){
////        this.ct = ct;
////        this.t = l1;
////        this.l2 = l2;
////        this.l3 = l3;
////    }
////
////    @NonNull
////    @Override
////    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        LayoutInflater inflater = LayoutInflater.from(ct);
////        View view = inflater.inflate(R.layout.minha_linha, parent,false);
////        return new MeuViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
////        holder.tv_tarefa.setText(l1[position]);
////        holder.tv_dtCadastro.setText(l2[position]);
////        holder.tv_dtRealizacao.setText(l3[position]);
////    }
////
////    @Override
////    public int getItemCount() {
////        return l1.length;
////    }
////
////    public class MeuViewHolder extends RecyclerView.ViewHolder{
////        TextView tv_tarefa, tv_dtCadastro, tv_dtRealizacao;
////
////        public MeuViewHolder(@NonNull View itemView) {
////            super(itemView);
////            tv_tarefa = itemView.findViewById(R.id.tv_tarefa);
////            tv_dtCadastro = itemView.findViewById(R.id.tv_dtCadastro);
////            tv_dtRealizacao = itemView.findViewById(R.id.tv_dtRealizacao);
////        }
////    }
//}
