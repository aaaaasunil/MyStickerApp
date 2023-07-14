package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sticker.app.R;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

public class StickerPackListAdapter extends RecyclerView.Adapter<StickerPackListItemViewHolder> {
    private int maxNumberOfStickersInARow;
    @NonNull
    private final OnAddButtonClickedListener onAddButtonClickedListener;
    @NonNull
    private List<StickerPack> stickerPacks;

    public interface OnAddButtonClickedListener {
        void onAddButtonClicked(StickerPack stickerPack);
    }

    StickerPackListAdapter(@NonNull List<StickerPack> list, @NonNull OnAddButtonClickedListener onAddButtonClickedListener2) {
        this.stickerPacks = list;
        this.onAddButtonClickedListener = onAddButtonClickedListener2;
    }

    @NonNull
    public StickerPackListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StickerPackListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticker_packs_list_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull StickerPackListItemViewHolder stickerPackListItemViewHolder, int i) {
        StickerPack stickerPack = this.stickerPacks.get(i);
        Context context = stickerPackListItemViewHolder.publisherView.getContext();
        TextView textView = stickerPackListItemViewHolder.publisherView;
        textView.setText("By " + stickerPack.publisher);
        stickerPackListItemViewHolder.filesizeView.setText(Formatter.formatShortFileSize(context, stickerPack.getTotalSize()));
        stickerPackListItemViewHolder.titleView.setText(stickerPack.name);
        stickerPackListItemViewHolder.container.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                StickerPackListAdapter.lambda$onBindViewHolder$0(stickerPack, view);
            }
        });
        if (stickerPack.getStickers().size() == 0) {
            stickerPackListItemViewHolder.tvNoSticker.setVisibility(View.VISIBLE);;
        } else {
            stickerPackListItemViewHolder.tvNoSticker.setVisibility(View.GONE);;
        }
        stickerPackListItemViewHolder.imageRowView.removeAllViews();
        int min = Math.min(this.maxNumberOfStickersInARow, stickerPack.getStickers().size());
        Log.e(">>>>", String.valueOf(min));
        for (int i2 = 0; i2 < min; i2++) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.sticker_pack_list_item_image, stickerPackListItemViewHolder.imageRowView, false);
            simpleDraweeView.setImageURI(stickerPack.getSticker(i2).getUri());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) simpleDraweeView.getLayoutParams();
            int measuredWidth = (((stickerPackListItemViewHolder.imageRowView.getMeasuredWidth() - (this.maxNumberOfStickersInARow * stickerPackListItemViewHolder.imageRowView.getContext().getResources().getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size))) / (this.maxNumberOfStickersInARow - 1)) - layoutParams.leftMargin) - layoutParams.rightMargin;
            if (i2 != min - 1 && measuredWidth > 0) {
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin + measuredWidth, layoutParams.bottomMargin);
                simpleDraweeView.setLayoutParams(layoutParams);
            }
            stickerPackListItemViewHolder.imageRowView.addView(simpleDraweeView);
        }
        setAddButtonAppearance(stickerPackListItemViewHolder.addButton, stickerPack);
    }

    static /* synthetic */ void lambda$onBindViewHolder$0(StickerPack stickerPack, View view) {
        Intent intent = new Intent(view.getContext(), StickerPackDetailsActivity.class);
        intent.putExtra("show_up_button", true);
        intent.putExtra("sticker_pack", stickerPack.identifier);
        view.getContext().startActivity(intent);
    }

    private void setAddButtonAppearance(ImageView imageView, StickerPack stickerPack) {
        if (stickerPack.getIsWhitelisted()) {
            imageView.setClickable(false);
            imageView.setOnClickListener((View.OnClickListener) null);
            imageView.setBackgroundDrawable((Drawable) null);
            return;
        }
        imageView.setImageResource(R.drawable.ic_close);
        imageView.setOnClickListener(new View.OnClickListener() {
//            private final /* synthetic */ StickerPack f$1;
//
//            {
//                this.f$1 = r2;
//            }

            public final void onClick(View view) {
                StickerPackListAdapter.this.onAddButtonClickedListener.onAddButtonClicked(stickerPack);
            }
        });
        TypedValue typedValue = new TypedValue();
        imageView.getContext().getTheme().resolveAttribute(16843534, typedValue, true);
        imageView.setBackgroundResource(typedValue.resourceId);
    }

    public int getItemCount() {
        return this.stickerPacks.size();
    }

    /* access modifiers changed from: package-private */
    public void setMaxNumberOfStickersInARow(int i) {
        if (this.maxNumberOfStickersInARow != i) {
            this.maxNumberOfStickersInARow = i;
            notifyDataSetChanged();
        }
    }
}
