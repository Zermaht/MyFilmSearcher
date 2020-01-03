package com.hfad.myfilmsearcher;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class FavoritesFilmsAdapter extends RecyclerView.Adapter<FavoritesFilmsAdapter.FavoritesViewHolder> {

    private String[] filmName;
    private String[] imageUrl;
    private FavoritesListener listener;

    interface FavoritesListener {
        void onLongClick(int position, CardView cardView);
    }

    public FavoritesFilmsAdapter(String[] filmName, String[] imageUrl) {
        this.filmName = filmName;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites, parent, false);

        return new FavoritesViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        String imageURl = imageUrl[position];

        ImageView imageView = cardView.findViewById(R.id.favorites_image);
        Glide.with(cardView.getContext())
                .load(imageUrl[position])
                .centerCrop()
                .into(imageView);
        Log.d("imageURl", imageURl);


        TextView textView = cardView.findViewById(R.id.favorites_name);
        textView.setText(filmName[position]);

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null){
                    listener.onLongClick(position, cardView);
                }
                return true;
            }
        });
    }

    public void setFavoritesListener(FavoritesListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return MainActivity.favorites.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public FavoritesViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }
}
