package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sticker.app.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import java.util.List;

public class StickerPackListAdapter extends RecyclerView.Adapter<StickerPackListItemViewHolder> {
    private int maxNumberOfStickersInARow;
    private int minMarginBetweenImages;
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
        stickerPackListItemViewHolder.publisherView.setText(stickerPack.publisher);
        stickerPackListItemViewHolder.filesizeView.setText(Formatter.formatShortFileSize(context, stickerPack.getTotalSize()));
        MobileAds.initialize(context, "ca-app-pub-2044501287137385~8630720099");
        stickerPackListItemViewHolder.adView.setVisibility(View.GONE);;
        if (i == 1) {
            stickerPackListItemViewHolder.adView.loadAd(new AdRequest.Builder().build());
            stickerPackListItemViewHolder.adView.setVisibility(View.VISIBLE);;
        } else if (i == 3) {
            stickerPackListItemViewHolder.adView.loadAd(new AdRequest.Builder().build());
            stickerPackListItemViewHolder.adView.setVisibility(View.VISIBLE);;
        } else if (i == 5) {
            stickerPackListItemViewHolder.adView.loadAd(new AdRequest.Builder().build());
            stickerPackListItemViewHolder.adView.setVisibility(View.VISIBLE);;
        }
        stickerPackListItemViewHolder.adView.setAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {
                Log.d("ErroreAdd", String.valueOf(i));
            }
        });
        stickerPackListItemViewHolder.titleView.setText(stickerPack.name);
        stickerPackListItemViewHolder.container.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                StickerPackListAdapter.lambda$onBindViewHolder$0(stickerPack, view);
            }
        });
        stickerPackListItemViewHolder.imageRowView.removeAllViews();
        Log.e(">>>>", String.valueOf(4));
        for (int i2 = 0; i2 < 4; i2++) {
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.sticker_pack_list_item_image, stickerPackListItemViewHolder.imageRowView, false);
            simpleDraweeView.setImageURI(StickerPackLoader.getStickerAssetUri(stickerPack.identifier, stickerPack.getStickers().get(i2).imageFileName));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) simpleDraweeView.getLayoutParams();
            int i3 = (this.minMarginBetweenImages - layoutParams.leftMargin) - layoutParams.rightMargin;
            if (i2 != 3 && i3 > 0) {
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin + i3, layoutParams.bottomMargin);
                simpleDraweeView.setLayoutParams(layoutParams);
            }
            stickerPackListItemViewHolder.imageRowView.addView(simpleDraweeView);
        }
        setAddButtonAppearance(stickerPackListItemViewHolder.addButton, stickerPack);
    }

    static /* synthetic */ void lambda$onBindViewHolder$0(StickerPack stickerPack, View view) {
        Intent intent = new Intent(view.getContext(), StickerPackDetailsActivity.class);
        intent.putExtra("show_up_button", true);
        intent.putExtra("sticker_pack", (Parcelable) stickerPack);
        view.getContext().startActivity(intent);
    }

    private void setAddButtonAppearance(ImageView imageView, StickerPack stickerPack) {
        if (stickerPack.getIsWhitelisted()) {
            imageView.setImageResource(R.drawable.sticker_3rdparty_added);
            imageView.setClickable(false);
            imageView.setOnClickListener((View.OnClickListener) null);
            setBackground(imageView, (Drawable) null);
        }
    }

    private void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public int getItemCount() {
        return this.stickerPacks.size();
    }

    /* access modifiers changed from: package-private */
    public void setImageRowSpec(int i, int i2) {
        this.minMarginBetweenImages = i2;
        if (this.maxNumberOfStickersInARow != i) {
            this.maxNumberOfStickersInARow = i;
            notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public void setStickerPackList(List<StickerPack> list) {
        this.stickerPacks = list;
    }
}
