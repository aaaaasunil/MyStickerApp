package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sticker.app.R;

public class StickerPackListItemViewHolder extends RecyclerView.ViewHolder {
    ImageView addButton;
    View container;
    TextView filesizeView;
    LinearLayout imageRowView;
    TextView publisherView;
    ImageView shareButton;
    TextView titleView;
    TextView tvNoSticker;

    StickerPackListItemViewHolder(View view) {
        super(view);
        this.container = view;
        this.titleView = (TextView) view.findViewById(R.id.sticker_pack_title);
        this.tvNoSticker = (TextView) view.findViewById(R.id.tvNoSticker);
        this.publisherView = (TextView) view.findViewById(R.id.sticker_pack_publisher);
        this.filesizeView = (TextView) view.findViewById(R.id.sticker_pack_filesize);
        this.addButton = (ImageView) view.findViewById(R.id.add_button_on_list);
        this.shareButton = (ImageView) view.findViewById(R.id.export_button_on_list);
        this.imageRowView = (LinearLayout) view.findViewById(R.id.sticker_packs_list_item_image_list);
    }
}
