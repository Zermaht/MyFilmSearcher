package com.hfad.myfilmsearcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.myfilmsearcher.roomDB.FilmEntity;

import java.util.List;

public class CaptionedImageAdapter extends RecyclerView.Adapter<CaptionedImageAdapter.ViewHolder> {
    private Listener listener;
    private List<FilmEntity> films;
    private LayoutInflater inflater;

    interface Listener {
        void onClick(int position, CardView cardView);
        void onLongClick(int position, CardView cardView);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) inflater.inflate(R.layout.card_view, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.info_image);
        TextView textView = cardView.findViewById(R.id.info_film);
        textView.setText(films.get(position).getName());

        Glide.with(cardView.getContext())
                .load(films.get(position).getImgUrl())
                .into(imageView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position, cardView);
                }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLongClick(position, cardView);
                }
                return true;
            }
        });
    }

    public CaptionedImageAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    void setFilms(List<FilmEntity> film) {
        films = film;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (films != null) {
            return films.size();
        } else return 0;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}
