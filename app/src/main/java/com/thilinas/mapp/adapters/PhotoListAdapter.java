package com.thilinas.mapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thilinas.mapp.R;
import com.thilinas.mapp.models.Photo;

import java.util.List;

/**
 * Created by Thilina on 06-Feb-17.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.MyViewHolder> {

    private List<Photo> photos;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        Button btn;
        ImageButton buttonSetWallpaper;
        ImageView imageView,img_hidden;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img);
            buttonSetWallpaper = (ImageButton)view.findViewById(R.id.set);
            img_hidden = (ImageView) view.findViewById(R.id.img_hidden);
         /*   title = (TextView) view.findViewById(R.id.title);
           *//* genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            btn = (Button) view.findViewById(R.id.btn);*/
        }
    }


    public PhotoListAdapter(List<Photo> games, Context context) {
        this.photos = games;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_photo, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Photo game = photos.get(position);
        final String url = game.getUrl();
        Glide
                .with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_panorama_black_24dp)
                .crossFade()
                .into(holder.imageView);

        holder.buttonSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSetDesktopClickedListener != null) {
                    onSetDesktopClickedListener.onButtonClicked(url);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public interface OnSetDesktopClickedListener {
        void onButtonClicked(String deviceAddress);
    }

    private OnSetDesktopClickedListener onSetDesktopClickedListener;

    public void setOnBluetoothDeviceClickedListener(OnSetDesktopClickedListener l) {
        onSetDesktopClickedListener = l;
    }


}