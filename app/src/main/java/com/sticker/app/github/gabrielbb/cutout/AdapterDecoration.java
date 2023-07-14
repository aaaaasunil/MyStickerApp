package com.sticker.app.github.gabrielbb.cutout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sticker.app.R;

import java.util.ArrayList;

public class AdapterDecoration extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    ArrayList<Integer> data;
    interfaceClick interfaceClick;

    interface interfaceClick {
        void image(int i);
    }

    public AdapterDecoration(Activity activity2, ArrayList<Integer> arrayList, interfaceClick interfaceclick) {
        this.activity = activity2;
        this.data = arrayList;
        this.interfaceClick = interfaceclick;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_decoration, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.ivImage.setImageResource(this.data.get(i).intValue());
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdapterDecoration.this.interfaceClick.image(AdapterDecoration.this.data.get(i).intValue());
            }
        });
    }

    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.ivImage = (ImageView) view.findViewById(R.id.ivImage);
        }
    }
}
