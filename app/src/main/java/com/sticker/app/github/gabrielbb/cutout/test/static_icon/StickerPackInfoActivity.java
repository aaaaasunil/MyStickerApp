package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.core.view.ViewCompat;
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
        String stringExtra2 = getIntent().getStringExtra("sticker_pack_website");
        String stringExtra3 = getIntent().getStringExtra("sticker_pack_email");
        String stringExtra4 = getIntent().getStringExtra("sticker_pack_privacy_policy");
        String stringExtra5 = getIntent().getStringExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_LICENSE_AGREEMENT);
        TextView textView = (TextView) findViewById(R.id.tray_icon);
        try {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), getContentResolver().openInputStream(Uri.parse(stringExtra)));
            Drawable drawableForAllAPIs = getDrawableForAllAPIs(R.drawable.sticker_3rdparty_email);
            bitmapDrawable.setBounds(new Rect(0, 0, drawableForAllAPIs.getIntrinsicWidth(), drawableForAllAPIs.getIntrinsicHeight()));
            if (Build.VERSION.SDK_INT > 17) {
                textView.setCompoundDrawablesRelative(bitmapDrawable, (Drawable) null, (Drawable) null, (Drawable) null);
            } else if (ViewCompat.getLayoutDirection(textView) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                textView.setCompoundDrawables((Drawable) null, (Drawable) null, bitmapDrawable, (Drawable) null);
            } else {
                textView.setCompoundDrawables(bitmapDrawable, (Drawable) null, (Drawable) null, (Drawable) null);
            }
        } catch (FileNotFoundException unused) {
            Log.e(TAG, "could not find the uri for the tray image:" + stringExtra);
        }
        setupTextView(stringExtra2, R.id.view_webpage);
        TextView textView2 = (TextView) findViewById(R.id.send_email);
        if (TextUtils.isEmpty(stringExtra3)) {
            textView2.setVisibility(View.GONE);
        } else {
            textView2.setOnClickListener(new View.OnClickListener() {
//                private final /* synthetic */ String f$1;
//
//                {
//                    this.f$1 = r2;
//                }

                public final void onClick(View view) {
                    StickerPackInfoActivity.this.launchEmailClient(stringExtra3);
                }
            });
        }
        setupTextView(stringExtra4, R.id.privacy_policy);
        setupTextView(stringExtra5, R.id.license_agreement);
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackInfoActivity.this.finish();
            }
        });
    }

    private void setupTextView(String str, @IdRes int i) {
        TextView textView = (TextView) findViewById(i);
        if (TextUtils.isEmpty(str)) {
            textView.setVisibility(View.GONE);;
        } else {
            textView.setOnClickListener(new View.OnClickListener() {


                public final void onClick(View view) {
                    StickerPackInfoActivity.this.launchWebpage(str);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void launchEmailClient(String str) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", str, (String) null));
        intent.putExtra("android.intent.extra.EMAIL", new String[]{str});
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.info_send_email_to_prompt)));
    }

    /* access modifiers changed from: private */
    public void launchWebpage(String str) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }

    private Drawable getDrawableForAllAPIs(@DrawableRes int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getDrawable(i);
        }
        return getResources().getDrawable(i);
    }
}
