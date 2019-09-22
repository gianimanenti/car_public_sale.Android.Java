package com.moondu.leilao.model.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moondu.leilao.R;
import com.moondu.leilao.commons.utils.MaxHeightRecyclerView;
import com.moondu.leilao.view.fragment.checkin.CarEvent;

import java.util.List;

public class TipoOcorrenciaAdapter extends MaxHeightRecyclerView.Adapter<TipoOcorrenciaAdapter.ViewHolder> {

    private List<String> itens;
    private CarEvent fragment;

    public TipoOcorrenciaAdapter(CarEvent fragment, List<String> itens) {
        this.itens = itens;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tipo_ocorrencia, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String item = itens.get(position);

        if (position % 2 == 0) {
            holder.ll.setBackgroundColor(Color.parseColor("#f1f1f1"));
        } else {
            holder.ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.nota.setText(item);

        holder.btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (itens != null)
            return itens.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nota;
        Button btExcluir;
        LinearLayout ll;

        public ViewHolder(View view) {
            super(view);

            nota = view.findViewById(R.id.tvNome);
            btExcluir = view.findViewById(R.id.btnExcluir);
            ll = view.findViewById(R.id.ll_itens);
        }
    }
}
