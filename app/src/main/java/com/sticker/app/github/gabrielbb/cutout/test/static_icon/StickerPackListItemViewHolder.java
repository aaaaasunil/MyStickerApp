package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sticker.app.R;
import com.google.android.gms.ads.AdView;

class StickerPackListItemViewHolder extends RecyclerView.ViewHolder {
    final AdView adView;
    final ImageView addButton;
    final View container;
    final TextView filesizeView;
    final LinearLayout imageRowView;
    final TextView publisherView;
    final TextView titleView;

    StickerPackListItemViewHolder(View view) {
        super(view);
        this.container = view;
        this.titleView = (TextView) view.findViewById(R.id.sticker_pack_title);
        this.publisherView = (TextView) view.findViewById(R.id.sticker_pack_publisher);
        this.filesizeView = (TextView) view.findViewById(R.id.sticker_pack_filesize);
        this.addButton = (ImageView) view.findViewById(R.id.add_button_on_list);
        this.imageRowView = (LinearLayout) view.findViewById(R.id.sticker_packs_list_item_image_list);
        this.adView = (AdView) view.findViewById(R.id.adView);
    }
}
