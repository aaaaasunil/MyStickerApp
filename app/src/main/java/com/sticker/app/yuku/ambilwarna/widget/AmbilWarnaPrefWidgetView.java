package com.sticker.app.yuku.ambilwarna.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AmbilWarnaPrefWidgetView extends View {
    Paint paint = new Paint();
    float rectSize;
    float strokeWidth;

    public AmbilWarnaPrefWidgetView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        float f = context.getResources().getDisplayMetrics().density;
        this.rectSize = (float) Math.floor((double) ((24.0f * f) + 0.5f));
        this.strokeWidth = (float) Math.floor((double) ((f * 1.0f) + 0.5f));
        this.paint.setColor(-1);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.strokeWidth);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = this.strokeWidth;
        float f2 = this.rectSize;
        canvas.drawRect(f, f, f2 - f, f2 - f, this.paint);
    }
}
