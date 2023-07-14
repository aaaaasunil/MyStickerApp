package com.sticker.app.github.gabrielbb.cutout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

class BitmapUtility {
    BitmapUtility() {
    }

    static Bitmap getResizedBitmap(Bitmap bitmap, int i, int i2) {
        if (i2 <= 0 || i <= 0) {
            return bitmap;
        }
        float width = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
        float f = (float) i;
        float f2 = (float) i2;
        if (f / f2 > width) {
            i = (int) (f2 * width);
        } else {
            i2 = (int) (f / width);
        }
        return Bitmap.createScaledBitmap(bitmap, i, i2, true);
    }

    static Bitmap getBorderedBitmap(Bitmap bitmap, int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.drawBitmap(createScaledBitmap, ((float) (bitmap.getWidth() - createScaledBitmap.getWidth())) * 0.5f, ((float) (bitmap.getHeight() - createScaledBitmap.getHeight())) * 0.5f, (Paint) null);
        return createBitmap;
    }
}
