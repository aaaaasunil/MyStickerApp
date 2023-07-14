package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public SpacesItemDecoration(int i) {
        this.mSpace = i;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int i = this.mSpace;
        rect.left = i;
        rect.right = i;
        rect.bottom = i;
        if (recyclerView.getChildAdapterPosition(view) == 0) {
            rect.top = this.mSpace;
        }
    }
}
