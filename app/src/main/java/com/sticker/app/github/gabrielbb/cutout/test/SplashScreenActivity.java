package com.sticker.app.github.gabrielbb.cutout.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.sticker.app.R;
import com.sticker.app.github.gabrielbb.cutout.test.static_icon.EntryActivity;

public class SplashScreenActivity extends Activity {

    /* renamed from: bb */
    String f93bb;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);
        ((TextView) findViewById(R.id.tvText)).setTypeface(Typeface.createFromAsset(getAssets(), "Pacifico.ttf"));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, EntryActivity.class));
                SplashScreenActivity.this.finish();
            }
        }, 2000);
    }
}
