package com.sticker.app.github.gabrielbb.cutout.test.utils;

import android.content.Context;
import android.graphics.Typeface;
import java.util.Hashtable;

public class FontCache {
    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public static Typeface getTypeface(String str, Context context) {
        Typeface typeface = fontCache.get(str);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), str);
                fontCache.put(str, typeface);
            } catch (Exception unused) {
                return null;
            }
        }
        return typeface;
    }
}
