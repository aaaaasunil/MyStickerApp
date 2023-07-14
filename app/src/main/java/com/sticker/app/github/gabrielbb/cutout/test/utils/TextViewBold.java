package com.sticker.app.github.gabrielbb.cutout.test.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewBold extends TextView {
    public TextViewBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewBold(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyCustomFont(context);
    }

    public TextViewBold(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        setTypeface(FontCache.getTypeface("SourceSansPro-Bold.ttf", context));
    }
}
