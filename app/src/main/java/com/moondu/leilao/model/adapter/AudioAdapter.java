package com.moondu.leilao.model.adapter;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moondu.leilao.R;
import com.moondu.leilao.commons.utils.MaxHeightRecyclerView;

import java.io.IOException;
import java.util.List;

public class AudioAdapter extends MaxHeightRecyclerView.Adapter<AudioAdapter.ViewHolder> {

    private List<String> itens;
    private String path;

    public AudioAdapter(String path, List<String> itens) {
        this.itens = itens;
        this.path = path;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_audio, parent, false);

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

        holder.indice.setText(String.valueOf(position + 1));
        holder.nome.setText(item);

        holder.stopBtn.setVisibility(View.GONE);

        MediaPlayer mp = new MediaPlayer();

        holder.stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.stopBtn.setVisibility(View.GONE);
                holder.startBtn.setVisibility(View.VISIBLE);

                if (mp.isPlaying()) {
                    mp.stop();
                }
            }
        });

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.startBtn.setVisibility(View.GONE);
                holder.stopBtn.setVisibility(View.VISIBLE);

                if (mp.isPlaying()) {
                    mp.stop();
                }

                try {
                    String filePath = path + "/" + item;
                    mp.reset();
                    mp.setDataSource(filePath);
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mp.start();
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
        TextView indice;
        TextView nome;
        ImageView stopBtn;
        ImageView startBtn;
        RelativeLayout ll;

        public ViewHolder(View view) {
            super(view);

            indice = view.findViewById(R.id.idxItem);
            nome = view.findViewById(R.id.tvNome);
            stopBtn = view.findViewById(R.id.mcStop);
            startBtn = view.findViewById(R.id.mcPlay);
            ll = view.findViewById(R.id.ll_itens);
        }
    }
}
