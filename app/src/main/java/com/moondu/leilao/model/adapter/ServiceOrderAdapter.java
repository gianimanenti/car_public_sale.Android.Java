package com.moondu.leilao.model.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moondu.leilao.R;
import com.moondu.leilao.model.entity.ServiceOrder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.text.Html.fromHtml;

public class ServiceOrderAdapter extends RecyclerView.Adapter<ServiceOrderAdapter.ServiceOrdermentoViewHolder> {

    private List<ServiceOrder> ServiceOrderList;
    private List<ServiceOrder> copyList = new ArrayList<ServiceOrder>();

    public ServiceOrderAdapter(List<ServiceOrder> ServiceOrderList) {
        this.ServiceOrderList = ServiceOrderList;
        copyList.addAll(ServiceOrderList);
    }

    public void filter(String queryText) {

        if (queryText != null) {
            ServiceOrderList.clear();

            for (ServiceOrder to : copyList) {
                String propriedade = to.getIdentification() != null ? to.getIdentification().toLowerCase() : "";
                String proprietario = to.getDriver() != null ? to.getDriver().toLowerCase() : "";
                String endereco = to.getLocation() != null ? to.getLocation().toLowerCase() : "";

                if (propriedade.contains(queryText.toLowerCase()) ||
                        proprietario.contains(queryText.toLowerCase()) ||
                        endereco.contains(queryText.toLowerCase())) {
                    ServiceOrderList.add(to);
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public ServiceOrderAdapter.ServiceOrdermentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_order, parent, false);

        return new ServiceOrdermentoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ServiceOrderAdapter.ServiceOrdermentoViewHolder holder, int position) {
        ServiceOrder ServiceOrdermento = ServiceOrderList.get(position);

        if (ServiceOrdermento.getIdentification() != null)
            holder.title.setText(fromHtml("<b>" + ServiceOrdermento.getIdentification() + "</b>"));

        if (ServiceOrdermento.getDate() != null)
            holder.date.setText(fromHtml("<b>" + formatDate(ServiceOrdermento.getDate()) + "</b>"));
        else
            holder.date.setVisibility(View.GONE);

        if (ServiceOrdermento.getLocation() != null)
            holder.veiculo.setText(fromHtml("<b>Véiculo: </b>" +
                    ServiceOrdermento.getVehicleType() + " / " + ServiceOrdermento.getModel()));

        if (ServiceOrdermento.getDriver() != null)
            holder.proprietario.setText(fromHtml("<b>Proprietário: </b>" +
                    ServiceOrdermento.getDriver()));
        else
            holder.proprietario.setText(fromHtml("<b>Condutor: </b> -"));

        holder.endereco.setText(fromHtml("<b>Local: </b>" + ServiceOrdermento.getLocation()));
    }

    public static String formatDate(Date value) {
        if (value != null) {
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

            return sf.format(value);
        }

        return null;
    }

    @Override
    public int getItemCount() {
        if (ServiceOrderList != null) {
            return ServiceOrderList.size();
        }
        return 0;
    }

    public ServiceOrder getItem(int position) {
        return ServiceOrderList.get(position);
    }

    public class ServiceOrdermentoViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, veiculo, proprietario, endereco;

        public ServiceOrdermentoViewHolder(View view) {
            super(view);
            veiculo = (TextView) view.findViewById(R.id.item_veiculo);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            proprietario = (TextView) view.findViewById(R.id.item_motorista);
            endereco = (TextView) view.findViewById(R.id.item_local);
        }
    }
}
