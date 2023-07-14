package com.sticker.app.github.gabrielbb.cutout;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class TextDrawable extends Drawable implements TextWatcher {
    private boolean mBindToViewPaint;
    private boolean mFitTextEnabled;
    private Rect mHeightBounds;
    private boolean mInitFitText;
    private Paint mPaint;
    private float mPrevTextSize;
    private String mText;
    private WeakReference<TextView> ref;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public TextDrawable(Paint paint, String str) {
        this.mBindToViewPaint = false;
        this.mPrevTextSize = 0.0f;
        this.mInitFitText = false;
        this.mFitTextEnabled = false;
        this.mText = str;
        this.mPaint = new Paint(paint);
        this.mHeightBounds = new Rect();
        init();
    }

    public TextDrawable(TextView textView, String str, boolean z, boolean z2) {
        this((Paint) textView.getPaint(), str);
        this.ref = new WeakReference<>(textView);
        if (z || z2) {
            if (z) {
                textView.addTextChangedListener(this);
            }
            this.mBindToViewPaint = z2;
        }
    }

    public TextDrawable(TextView textView, boolean z, boolean z2) {
        this(textView, textView.getText().toString(), false, false);
    }

    public TextDrawable(TextView textView) {
        this(textView, false, false);
    }

    public TextDrawable(TextView textView, String str) {
        this(textView, str, false, false);
    }

    public void draw(Canvas canvas) {
        if (!this.mBindToViewPaint || this.ref.get() == null) {
            if (this.mInitFitText) {
                fitTextAndInit();
            }
            canvas.drawText(this.mText, 0.0f, (float) getBounds().height(), this.mPaint);
            return;
        }
        canvas.drawText(this.mText, 0.0f, (float) getBounds().height(), ((TextView) this.ref.get()).getPaint());
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public int getOpacity() {
        int alpha = this.mPaint.getAlpha();
        if (alpha == 0) {
            return -2;
        }
        return alpha == 255 ? -1 : -3;
    }

    private void init() {
        Rect bounds = getBounds();
        this.mPaint.getTextBounds("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", 0, 1, this.mHeightBounds);
        float measureText = this.mPaint.measureText(this.mText);
        bounds.top = this.mHeightBounds.top;
        bounds.bottom = this.mHeightBounds.bottom;
        bounds.right = (int) measureText;
        bounds.left = 0;
        setBounds(bounds);
    }

    public void setPaint(Paint paint) {
        this.mPaint = new Paint(paint);
        if (!this.mFitTextEnabled || this.mInitFitText) {
            init();
        } else {
            fitTextAndInit();
        }
        invalidateSelf();
    }

    public Paint getPaint() {
        return this.mPaint;
    }

    public void setText(String str) {
        this.mText = str;
        if (!this.mFitTextEnabled || this.mInitFitText) {
            init();
        } else {
            fitTextAndInit();
        }
        invalidateSelf();
    }

    public String getText() {
        return this.mText;
    }

    public void afterTextChanged(Editable editable) {
        setText(editable.toString());
    }

    public void setFillText(boolean z) {
        this.mFitTextEnabled = z;
        if (z) {
            this.mPrevTextSize = this.mPaint.getTextSize();
            if (this.ref.get() == null) {
                return;
            }
            if (((TextView) this.ref.get()).getWidth() > 0) {
                fitTextAndInit();
            } else {
                this.mInitFitText = true;
            }
        } else {
            float f = this.mPrevTextSize;
            if (f > 0.0f) {
                this.mPaint.setTextSize(f);
            }
            init();
        }
    }

    private void fitTextAndInit() {
        float width = ((float) ((TextView) this.ref.get()).getWidth()) / this.mPaint.measureText(this.mText);
        Paint paint = this.mPaint;
        paint.setTextSize(paint.getTextSize() * width);
        this.mInitFitText = false;
        init();
    }
}
