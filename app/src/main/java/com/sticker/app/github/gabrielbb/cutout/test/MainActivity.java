package com.sticker.app.github.gabrielbb.cutout.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sticker.app.R;
import com.sticker.app.github.gabrielbb.cutout.CutOut;
import com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode.StickerPackListActivity;

public class MainActivity extends AppCompatActivity {
    Context context;
    private ImageView imageView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_demo);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i != 368) {
            return;
        }
        if (i2 == -1) {
            CutOut.getUri(intent);
            startActivity(new Intent(this, StickerPackListActivity.class));
        } else if (i2 != 3680) {
            System.out.print("User cancelled the CutOut screen");
        } else {
            CutOut.getError(intent);
        }
    }

    public Uri getUriFromDrawable(int i) {
        return Uri.parse("android.resource://" + getPackageName() + "/drawable/" + getApplicationContext().getResources().getResourceEntryName(i));
    }
}
