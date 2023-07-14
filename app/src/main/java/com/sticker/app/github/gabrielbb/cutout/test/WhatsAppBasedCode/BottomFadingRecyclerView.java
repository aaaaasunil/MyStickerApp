package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class BottomFadingRecyclerView extends RecyclerView {
    /* access modifiers changed from: protected */
    public float getTopFadingEdgeStrength() {
        return 0.0f;
    }

    public BottomFadingRecyclerView(Context context) {
        super(context);
    }

    public BottomFadingRecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BottomFadingRecyclerView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
