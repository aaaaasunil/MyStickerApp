package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

public class StickerApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

    }
}
