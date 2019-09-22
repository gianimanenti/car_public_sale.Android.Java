package com.moondu.leilao.model.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moondu.leilao.R;
import com.moondu.leilao.commons.utils.MaxHeightRecyclerView;

import java.io.File;
import java.util.List;

public class VideoAdapter extends MaxHeightRecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<String> itens;
    private String path;
    private Context context;

    public VideoAdapter(Context context, String path, List<String> itens) {
        this.itens = itens;
        this.path = path;
        this.context = context;
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

        if (new File(image).exists()) {
            Glide
                    .with(context)
                    .asBitmap()
                    .load(Uri.fromFile(new File(image)))
                    .into(holder.image);
        }
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
