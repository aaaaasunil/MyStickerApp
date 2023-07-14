package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.sticker.app.R;
import java.io.FileNotFoundException;

public class StickerPackInfoActivity extends BaseActivity {
    private static final String TAG = "StickerPackInfoActivity";
    private ImageView ivBack;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sticker_pack_info);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        String stringExtra = getIntent().getStringExtra("sticker_pack_tray_icon");
        final String stringExtra2 = getIntent().getStringExtra("sticker_pack_website");
        final String stringExtra3 = getIntent().getStringExtra("sticker_pack_email");
        final String stringExtra4 = getIntent().getStringExtra("sticker_pack_privacy_policy");
        TextView textView = (TextView) findViewById(R.id.tray_icon);
        try {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getContentResolver().openInputStream(Uri.parse(stringExtra)));
            Drawable drawable = getResources().getDrawable(R.drawable.sticker_3rdparty_email);
            bitmapDrawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
            textView.setCompoundDrawables(bitmapDrawable, (Drawable) null, (Drawable) null, (Drawable) null);
        } catch (FileNotFoundException unused) {
            Log.e(TAG, "could not find the uri for the tray image:" + stringExtra);
        }
        TextView textView2 = (TextView) findViewById(R.id.view_webpage);
        if (TextUtils.isEmpty(stringExtra2)) {
            textView2.setVisibility(View.GONE);;
        } else {
            textView2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    StickerPackInfoActivity.this.launchWebpage(stringExtra2);
                }
            });
        }
        TextView textView3 = (TextView) findViewById(R.id.send_email);
        if (TextUtils.isEmpty(stringExtra3)) {
            textView3.setVisibility(View.GONE);;
        } else {
            textView3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    StickerPackInfoActivity.this.launchEmailClient(stringExtra3);
                }
            });
        }
        TextView textView4 = (TextView) findViewById(R.id.privacy_policy);
        if (TextUtils.isEmpty(stringExtra4)) {
            textView4.setVisibility(View.GONE);;
        } else {
            textView4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    StickerPackInfoActivity.this.launchWebpage(stringExtra4);
                }
            });
        }
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackInfoActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: private */
    public void launchEmailClient(String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("plain/text");
        intent.putExtra("android.intent.extra.EMAIL", new String[]{str});
        startActivity(Intent.createChooser(intent, "Send email with"));
    }

    /* access modifiers changed from: private */
    public void launchWebpage(String str) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }
}
