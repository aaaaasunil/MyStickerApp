package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.app.Activity;
import android.app.Application;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.sticker.app.R;
import com.facebook.drawee.view.SimpleDraweeView;
import java.io.File;
import java.io.FileOutputStream;

public class StickerPreviewAdapter extends RecyclerView.Adapter<StickerPreviewViewHolder> {
    private int cellLimit = 0;
    private int cellPadding;
    private final int cellSize;
    private final int errorResource;
    private final LayoutInflater layoutInflater;
    /* access modifiers changed from: private */
    @NonNull
    public StickerPack stickerPack;

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
        StickerPreviewViewHolder stickerPreviewViewHolder = new StickerPreviewViewHolder(this.layoutInflater.inflate(R.layout.sticker_image, viewGroup, false));
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
        final Sticker sticker = this.stickerPack.getSticker(i);
        final Context context = stickerPreviewViewHolder.stickerPreviewView.getContext();
        stickerPreviewViewHolder.stickerPreviewView.setImageResource(this.errorResource);
        stickerPreviewViewHolder.stickerPreviewView.setImageURI(sticker.getUri());
        stickerPreviewViewHolder.stickerPreviewView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPreviewAdapter stickerPreviewAdapter = StickerPreviewAdapter.this;
                stickerPreviewAdapter.dialogDeleteSticker(context, sticker, context);
            }
        });
    }

    public int getItemCount() {
        int size = this.stickerPack.getStickers().size();
        int i = this.cellLimit;
        return i > 0 ? Math.min(size, i) : size;
    }

    public void dialogDeleteSticker(final Context context, final Sticker sticker, final Context context2) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemen_3;
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_delete_sticker);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView);
        imageView.setBackgroundColor(0);
        ImageView imageView2 = (ImageView) dialog.findViewById(R.id.ivShare);
        imageView2.setVisibility(View.VISIBLE);
        Glide.with(context).asBitmap().load(sticker.getUri()).into(imageView);
        ((TextView) dialog.findViewById(R.id.tvOk)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StickerPreviewAdapter.this.stickerPack.getStickers().size() > 3 || !WhitelistCheck.isWhitelisted(context2, StickerPreviewAdapter.this.stickerPack.getIdentifier())) {
                    dialog.dismiss();
                    StickerPreviewAdapter.this.stickerPack.deleteSticker(sticker);
                    Activity activity = (Activity) context2;
                    activity.finish();
                    activity.startActivity(activity.getIntent());
                    Toast.makeText(context2, "Sticker Pack deleted",  Toast.LENGTH_SHORT).show();
                    return;
                }
                StickerPreviewAdapter.this.dialogAddWhatAppSticker(context2);
            }
        });
        ((TextView) dialog.findViewById(R.id.tvCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bitmap access$100 = StickerPreviewAdapter.this.getBitmapFromView(imageView);
                try {
                    File file = new File(context.getExternalCacheDir(), "sticker.png");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    access$100.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    access$100.setHasAlpha(true);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    file.setReadable(true, false);
                    Intent intent = new Intent("android.intent.action.SEND");
//                    intent.setFlags(268435456);
                    intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                    intent.setType("image/png");
                    context.startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void dialogAddWhatAppSticker(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_add_to_whatapp);
        ((TextView) dialog.findViewById(R.id.tvMessage)).setText("A sticker pack that is already applied to WhatsApp cannot have less than 3 stickers. In order to remove additional stickers, please add more to the pack first or remove the pack from the WhatsApp app.");
        TextView textView = (TextView) dialog.findViewById(R.id.tvCancel);
        ((TextView) dialog.findViewById(R.id.tvOk)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
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
