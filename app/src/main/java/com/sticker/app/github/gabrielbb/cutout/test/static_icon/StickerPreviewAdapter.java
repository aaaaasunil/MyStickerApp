package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.sticker.app.R;
import com.facebook.drawee.view.SimpleDraweeView;
import java.io.File;
import java.io.FileOutputStream;

public class StickerPreviewAdapter extends RecyclerView.Adapter<StickerPreviewViewHolder> {
    private final int cellLimit = 0;
    private final int cellPadding;
    private final int cellSize;
    private final int errorResource;
    private final LayoutInflater layoutInflater;
    @NonNull
    private final StickerPack stickerPack;

    StickerPreviewAdapter(@NonNull LayoutInflater layoutInflater2, int i, int i2, int i3, @NonNull StickerPack stickerPack2) {
        this.cellSize = i2;
        this.cellPadding = i3;
        this.layoutInflater = layoutInflater2;
        this.errorResource = i;
        this.stickerPack = stickerPack2;
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
    }

    @NonNull
    public StickerPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        StickerPreviewViewHolder stickerPreviewViewHolder = new StickerPreviewViewHolder(this.layoutInflater.inflate(R.layout.sticker_image_item, viewGroup, false));
        ViewGroup.LayoutParams layoutParams = stickerPreviewViewHolder.stickerPreviewView.getLayoutParams();
        int i2 = this.cellSize;
        layoutParams.height = i2;
        layoutParams.width = i2;
        stickerPreviewViewHolder.stickerPreviewView.setLayoutParams(layoutParams);
        SimpleDraweeView simpleDraweeView = stickerPreviewViewHolder.stickerPreviewView;
        int i3 = this.cellPadding;
        simpleDraweeView.setPadding(i3, i3, i3, i3);
        return stickerPreviewViewHolder;
    }

    public void onBindViewHolder(@NonNull StickerPreviewViewHolder stickerPreviewViewHolder, int i) {
        final Context context = stickerPreviewViewHolder.stickerPreviewView.getContext();
        Sticker sticker = this.stickerPack.getStickers().get(i);
        stickerPreviewViewHolder.stickerPreviewView.setImageResource(this.errorResource);
        stickerPreviewViewHolder.stickerPreviewView.setImageURI(StickerPackLoader.getStickerAssetUri(this.stickerPack.identifier, this.stickerPack.getStickers().get(i).imageFileName));
        final Uri stickerAssetUri = StickerPackLoader.getStickerAssetUri(this.stickerPack.identifier, this.stickerPack.getStickers().get(i).imageFileName);
        stickerPreviewViewHolder.stickerPreviewView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPreviewAdapter.this.dialogSticker(context, stickerAssetUri);
            }
        });
    }

    public int getItemCount() {
        int size = this.stickerPack.getStickers().size();
        int i = this.cellLimit;
        return i > 0 ? Math.min(size, i) : size;
    }

    public void dialogSticker(final Context context, Uri uri) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemen_3;
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_delete_sticker);
        TextView textView = (TextView) dialog.findViewById(R.id.tvCancel);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.llShare);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView);
        linearLayout.setVisibility(View.VISIBLE);;
        dialog.dismiss();
        ((TextView) dialog.findViewById(R.id.tvOk)).setVisibility(View.GONE);;
        textView.setText("Dismiss");
        ((TextView) dialog.findViewById(R.id.tvText)).setVisibility(View.GONE);;
        ((TextView) dialog.findViewById(R.id.tvTitle)).setText("STICKER");
        Glide.with(context).asBitmap().load(uri).into(imageView);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bitmap access$000 = StickerPreviewAdapter.this.getBitmapFromView(imageView);
                try {
                    File file = new File(context.getExternalCacheDir(), "sticker.png");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    access$000.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    file.setReadable(true, false);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setFlags(268435456);
                    intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                    intent.setType("image/png");
                    context.startActivity(Intent.createChooser(intent, "Share image via"));
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: private */
    public Bitmap getBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Drawable background = view.getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view.draw(canvas);
        return createBitmap;
    }
}
