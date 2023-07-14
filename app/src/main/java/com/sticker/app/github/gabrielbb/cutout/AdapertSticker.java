package com.sticker.app.github.gabrielbb.cutout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sticker.app.R;

import java.util.ArrayList;
import org.apache.commons.lang3.StringEscapeUtils;

public class AdapertSticker extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    ArrayList<String> data;
    interfaceClick interfaceClick;

    interface interfaceClick {
        void image(String str);
    }

    public AdapertSticker(Activity activity2, ArrayList<String> arrayList, interfaceClick interfaceclick) {
        this.activity = activity2;
        this.data = arrayList;
        this.interfaceClick = interfaceclick;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_sticker, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.ivImage.setText(StringEscapeUtils.unescapeJava(this.data.get(i)));
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdapertSticker.this.interfaceClick.image(StringEscapeUtils.unescapeJava(AdapertSticker.this.data.get(i)));
            }
        });
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

    }

    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ivImage;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.ivImage = (TextView) view.findViewById(R.id.ivImage);
        }
    }
}
