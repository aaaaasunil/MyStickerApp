package com.sticker.app.github.gabrielbb.cutout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.internal.view.SupportMenu;

public class BrushImageView extends AppCompatImageView {
    int alpga = 200;
    public float centerx;
    public float centery;
    int density = ((int) this.metrics.density);
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    public float offset;
    public float smallRadious;
    public float width;

    public BrushImageView(Context context) {
        super(context);
        int i = this.density;
        this.offset = (float) (i * 100);
        this.centerx = (float) (i * 166);
        this.centery = (float) (i * 200);
        this.width = (float) (i * 33);
        this.smallRadious = (float) (i * 15);
    }

    public BrushImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int i = this.density;
        this.offset = (float) (i * 100);
        this.centerx = (float) (i * 166);
        this.centery = (float) (i * 200);
        this.width = (float) (i * 33);
        this.smallRadious = (float) (i * 8);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("SAVE_COUNT", "" + canvas.getSaveCount());
        if (canvas.getSaveCount() > 1) {
            canvas.restore();
        }
        canvas.save();
        if (this.offset > 0.0f) {
            Paint paint = new Paint();
            paint.setColor(SupportMenu.CATEGORY_MASK);
            paint.setAntiAlias(true);
            canvas.drawCircle(this.centerx, this.centery, this.smallRadious, paint);
        }
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#ffbd0a"));
        paint2.setAntiAlias(true);
        canvas.drawCircle(this.centerx, this.centery - this.offset, this.width, paint2);
    }
}
