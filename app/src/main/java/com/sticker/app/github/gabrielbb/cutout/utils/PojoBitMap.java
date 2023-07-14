package com.sticker.app.github.gabrielbb.cutout.utils;

import android.graphics.Bitmap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoBitMap {
    @SerializedName("bitmap")
    @Expose
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }
}
