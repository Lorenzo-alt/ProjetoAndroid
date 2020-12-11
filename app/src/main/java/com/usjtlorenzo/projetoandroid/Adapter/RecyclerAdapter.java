package com.usjtlorenzo.projetoandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usjtlorenzo.projetoandroid.Modelos.Lembrete;
import com.usjtlorenzo.projetoandroid.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.myViewHolder>{

    private ArrayList<Lembrete> lembretes;
    private Context context;
    private OnLembreteItemListener mOnLembreteItemListener;

    public RecyclerAdapter(Context context, ArrayList<Lembrete> lembretes, OnLembreteItemListener onLembreteItemListener) {
        this.lembretes = lembretes;
        this.context = context;
        this.mOnLembreteItemListener = onLembreteItemListener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.minha_linha, parent,false);
        return new myViewHolder(view, mOnLembreteItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Lembrete model = lembretes.get(position);
            holder.tv_tarefa.setText(model.getNome());
            holder.tv_dtRealizacao.setText(model.getData().replace("-", "/"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            GregorianCalendar gc = new GregorianCalendar();
            try {
                gc.setTime(sdf.parse(model.getData()));
                holder.tv_dia.setText((gc.get(Calendar.DAY_OF_MONTH)) + "");
                switch (gc.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.SUNDAY:
                        holder.tv_diaS.setText("DOM");
                        break;
                    case Calendar.MONDAY:
                        holder.tv_diaS.setText("SEG");
                        break;
                    case Calendar.TUESDAY:
                        holder.tv_diaS.setText("TER");
                        break;
                    case Calendar.WEDNESDAY:
                        holder.tv_diaS.setText("QUA");
                        break;
                    case Calendar.THURSDAY:
                        holder.tv_diaS.setText("QUI");
                        break;
                    case Calendar.FRIDAY:
                        holder.tv_diaS.setText("SEX");
                        break;
                    case Calendar.SATURDAY:
                        holder.tv_diaS.setText("SAB");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (model.getCor().equalsIgnoreCase("Vermelho")) {
                holder.img_boxLinha.setBackgroundResource(R.drawable.dr_caixa_linha_vermelho);
            } else if (model.getCor().equalsIgnoreCase("Verde")) {
                holder.img_boxLinha.setBackgroundResource(R.drawable.dr_caixa_linha_verde);
            } else if (model.getCor().equalsIgnoreCase("Amarelo")) {
                holder.img_boxLinha.setBackgroundResource(R.drawable.dr_caixa_linha_amarelo);
            } else if (model.getCor().equalsIgnoreCase("Azul")){
                holder.img_boxLinha.setBackgroundResource(R.drawable.dr_caixa_linha_azul);
            }
    }

    @Override
    public int getItemCount() {
        return lembretes.size();
    }

    public void listaFiltrada(ArrayList<Lembrete> lembretesFiltrados){
        lembretes = lembretesFiltrados;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_boxLinha;
        TextView tv_tarefa, tv_dtRealizacao, tv_dia, tv_diaS;
        OnLembreteItemListener onLembreteItemListener;


        public myViewHolder(@NonNull View itemView, OnLembreteItemListener onLembreteItemListener) {
            super(itemView);
            img_boxLinha = itemView.findViewById(R.id.img_boxLinha);
            tv_tarefa = itemView.findViewById(R.id.tv_tarefa);
            tv_dtRealizacao = itemView.findViewById(R.id.tv_dtRealizacao);
            tv_dia = itemView.findViewById(R.id.tv_dia);
            tv_diaS = itemView.findViewById(R.id.tv_diaS);

            this.onLembreteItemListener = onLembreteItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onLembreteItemListener.onLembreteItemClick(getAdapterPosition());
        }
    }

    public interface OnLembreteItemListener{
        void onLembreteItemClick(int position);
    }
}
