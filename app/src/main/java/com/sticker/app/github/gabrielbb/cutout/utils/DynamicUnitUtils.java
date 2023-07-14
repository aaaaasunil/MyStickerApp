package com.sticker.app.github.gabrielbb.cutout.utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class DynamicUnitUtils {
    public static int convertDpToPixels(float f) {
        return Math.round(TypedValue.applyDimension(1, f, Resources.getSystem().getDisplayMetrics()));
    }

    public static int convertPixelsToDp(int i) {
        return Math.round(((float) i) / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int convertSpToPixels(float f) {
        return Math.round(TypedValue.applyDimension(2, f, Resources.getSystem().getDisplayMetrics()));
    }

    public static int convertPixelsToSp(int i) {
        return Math.round(((float) i) / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int convertDpToSp(float f) {
        return Math.round(((float) convertDpToPixels(f)) / ((float) convertSpToPixels(f)));
    }
}
