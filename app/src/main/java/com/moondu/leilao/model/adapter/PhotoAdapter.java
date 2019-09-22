package com.moondu.leilao.model.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.moondu.leilao.R;
import com.moondu.leilao.commons.utils.MaxHeightRecyclerView;

import java.io.File;
import java.util.List;

public class PhotoAdapter extends MaxHeightRecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<String> itens;
    private String path;

    public PhotoAdapter(String path, List<String> itens) {
        this.itens = itens;
        this.path = path;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String item = itens.get(position);

        String image = path + "/" + item;

        if (new File(image).exists())
            holder.image.setImageBitmap(BitmapFactory.decodeFile(image));
    }

    @Override
    public int getItemCount() {
        if (itens != null)
            return itens.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        RelativeLayout ll;

        public ViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.image);
            ll = view.findViewById(R.id.ll_itens);
        }
    }
}
