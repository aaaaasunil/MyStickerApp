package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.sticker.app.R;
import com.facebook.drawee.view.SimpleDraweeView;

class StickerPreviewViewHolder extends RecyclerView.ViewHolder {
    final SimpleDraweeView stickerPreviewView;

    StickerPreviewViewHolder(View view) {
        super(view);
        this.stickerPreviewView = (SimpleDraweeView) view.findViewById(R.id.sticker_preview);
    }
}
