package com.sticker.app.yuku.ambilwarna;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.core.view.ViewCompat;

import com.sticker.app.R;

public class AmbilWarnaDialog {
    int alpha;
    final float[] currentColorHsv;
    final AlertDialog dialog;
    final OnAmbilWarnaListener listener;
    /* access modifiers changed from: private */
    public final boolean supportsAlpha;
    final ImageView viewAlphaCheckered;
    final ImageView viewAlphaCursor;
    final View viewAlphaOverlay;
    final ViewGroup viewContainer;
    final ImageView viewCursor;
    final View viewHue;

    final View viewSatVal;
    final View viewNewColor;
    final View viewOldColor;
    final ImageView viewTarget;

    public interface OnAmbilWarnaListener {
        void onCancel(AmbilWarnaDialog ambilWarnaDialog);

        void onOk(AmbilWarnaDialog ambilWarnaDialog, int i);
    }

    public AmbilWarnaDialog(Context context, int i, OnAmbilWarnaListener onAmbilWarnaListener) {
        this(context, i, false, onAmbilWarnaListener);
    }

    public AmbilWarnaDialog(Context context, int i, boolean z, OnAmbilWarnaListener onAmbilWarnaListener) {
        this.currentColorHsv = new float[3];
        this.supportsAlpha = z;
        this.listener = onAmbilWarnaListener;
        i = !z ? i | ViewCompat.MEASURED_STATE_MASK : i;
        Color.colorToHSV(i, this.currentColorHsv);
        this.alpha = Color.alpha(i);
        final View inflate = LayoutInflater.from(context).inflate(R.layout.ambilwarna_dialog, (ViewGroup) null);
        this.viewHue = inflate.findViewById(R.id.ambilwarna_viewHue);
        this.viewSatVal = (AmbilWarnaSquare) inflate.findViewById(R.id.ambilwarna_viewSatBri);
        this.viewCursor = (ImageView) inflate.findViewById(R.id.ambilwarna_cursor);
        this.viewOldColor = inflate.findViewById(R.id.ambilwarna_oldColor);
        this.viewNewColor = inflate.findViewById(R.id.ambilwarna_newColor);
        this.viewTarget = (ImageView) inflate.findViewById(R.id.ambilwarna_target);
        this.viewContainer = (ViewGroup) inflate.findViewById(R.id.ambilwarna_viewContainer);
        this.viewAlphaOverlay = inflate.findViewById(R.id.ambilwarna_overlay);
        this.viewAlphaCursor = (ImageView) inflate.findViewById(R.id.ambilwarna_alphaCursor);
        this.viewAlphaCheckered = (ImageView) inflate.findViewById(R.id.ambilwarna_alphaCheckered);
        int i2 = 0;
        this.viewAlphaOverlay.setVisibility(z ? 0 : 8);
        this.viewAlphaCursor.setVisibility(z ? 0 : 8);
        this.viewAlphaCheckered.setVisibility(!z ? 8 : i2);
//        this.viewSatVal.setHue(9f);
        this.viewOldColor.setBackgroundColor(i);
        this.viewNewColor.setBackgroundColor(i);
        this.viewHue.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                    return false;
                }
                float y = motionEvent.getY();
                if (y < 0.0f) {
                    y = 0.0f;
                }
                if (y > ((float) AmbilWarnaDialog.this.viewHue.getMeasuredHeight())) {
                    y = ((float) AmbilWarnaDialog.this.viewHue.getMeasuredHeight()) - 0.001f;
                }
                float measuredHeight = 360.0f - ((360.0f / ((float) AmbilWarnaDialog.this.viewHue.getMeasuredHeight())) * y);
                if (measuredHeight == 360.0f) {
                    measuredHeight = 0.0f;
                }
                AmbilWarnaDialog.this.setHue(measuredHeight);
//                AmbilWarnaDialog.this.viewSatVal.setHue(AmbilWarnaDialog.this.getHue());
                AmbilWarnaDialog.this.moveCursor();
                AmbilWarnaDialog.this.viewNewColor.setBackgroundColor(AmbilWarnaDialog.this.getColor());
                AmbilWarnaDialog.this.updateAlphaView();
                return true;
            }
        });
        if (z) {
            this.viewAlphaCheckered.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                        return false;
                    }
                    float y = motionEvent.getY();
                    if (y < 0.0f) {
                        y = 0.0f;
                    }
                    if (y > ((float) AmbilWarnaDialog.this.viewAlphaCheckered.getMeasuredHeight())) {
                        y = ((float) AmbilWarnaDialog.this.viewAlphaCheckered.getMeasuredHeight()) - 0.001f;
                    }
                    int round = Math.round(255.0f - ((255.0f / ((float) AmbilWarnaDialog.this.viewAlphaCheckered.getMeasuredHeight())) * y));
                    AmbilWarnaDialog.this.setAlpha(round);
                    AmbilWarnaDialog.this.moveAlphaCursor();
                    AmbilWarnaDialog.this.viewNewColor.setBackgroundColor((round << 24) | (AmbilWarnaDialog.this.getColor() & ViewCompat.MEASURED_SIZE_MASK));
                    return true;
                }
            });
        }
        this.viewSatVal.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                    return false;
                }
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (x < 0.0f) {
                    x = 0.0f;
                }
                if (x > ((float) AmbilWarnaDialog.this.viewSatVal.getMeasuredWidth())) {
                    x = (float) AmbilWarnaDialog.this.viewSatVal.getMeasuredWidth();
                }
                if (y < 0.0f) {
                    y = 0.0f;
                }
                if (y > ((float) AmbilWarnaDialog.this.viewSatVal.getMeasuredHeight())) {
                    y = (float) AmbilWarnaDialog.this.viewSatVal.getMeasuredHeight();
                }
                AmbilWarnaDialog ambilWarnaDialog = AmbilWarnaDialog.this;
                ambilWarnaDialog.setSat((1.0f / ((float) ambilWarnaDialog.viewSatVal.getMeasuredWidth())) * x);
                AmbilWarnaDialog ambilWarnaDialog2 = AmbilWarnaDialog.this;
                ambilWarnaDialog2.setVal(1.0f - ((1.0f / ((float) ambilWarnaDialog2.viewSatVal.getMeasuredHeight())) * y));
                AmbilWarnaDialog.this.moveTarget();
                AmbilWarnaDialog.this.viewNewColor.setBackgroundColor(AmbilWarnaDialog.this.getColor());
                return true;
            }
        });
        this.dialog = new AlertDialog.Builder(context).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (AmbilWarnaDialog.this.listener != null) {
                    OnAmbilWarnaListener onAmbilWarnaListener = AmbilWarnaDialog.this.listener;
                    AmbilWarnaDialog ambilWarnaDialog = AmbilWarnaDialog.this;
                    onAmbilWarnaListener.onOk(ambilWarnaDialog, ambilWarnaDialog.getColor());
                }
            }
        }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (AmbilWarnaDialog.this.listener != null) {
                    AmbilWarnaDialog.this.listener.onCancel(AmbilWarnaDialog.this);
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                if (AmbilWarnaDialog.this.listener != null) {
                    AmbilWarnaDialog.this.listener.onCancel(AmbilWarnaDialog.this);
                }
            }
        }).create();
        this.dialog.setView(inflate, 0, 0, 0, 0);
        inflate.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                AmbilWarnaDialog.this.moveCursor();
                if (AmbilWarnaDialog.this.supportsAlpha) {
                    AmbilWarnaDialog.this.moveAlphaCursor();
                }
                AmbilWarnaDialog.this.moveTarget();
                if (AmbilWarnaDialog.this.supportsAlpha) {
                    AmbilWarnaDialog.this.updateAlphaView();
                }
                inflate.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void moveCursor() {
        float measuredHeight = ((float) this.viewHue.getMeasuredHeight()) - ((getHue() * ((float) this.viewHue.getMeasuredHeight())) / 360.0f);
        if (measuredHeight == ((float) this.viewHue.getMeasuredHeight())) {
            measuredHeight = 0.0f;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewCursor.getLayoutParams();
        double left = (double) this.viewHue.getLeft();
        double floor = Math.floor((double) (this.viewCursor.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) this.viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) this.viewHue.getTop()) + measuredHeight);
        double floor2 = Math.floor((double) (this.viewCursor.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) this.viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        this.viewCursor.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public void moveTarget() {
        float sat = getSat() * ((float) this.viewSatVal.getMeasuredWidth());
        float val = (1.0f - getVal()) * ((float) this.viewSatVal.getMeasuredHeight());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewTarget.getLayoutParams();
        double left = (double) (((float) this.viewSatVal.getLeft()) + sat);
        double floor = Math.floor((double) (this.viewTarget.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) this.viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) this.viewSatVal.getTop()) + val);
        double floor2 = Math.floor((double) (this.viewTarget.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) this.viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        this.viewTarget.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public void moveAlphaCursor() {
        float measuredHeight = (float) this.viewAlphaCheckered.getMeasuredHeight();
        float alpha2 = measuredHeight - ((getAlpha() * measuredHeight) / 255.0f);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewAlphaCursor.getLayoutParams();
        double left = (double) this.viewAlphaCheckered.getLeft();
        double floor = Math.floor((double) (this.viewAlphaCursor.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) this.viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) this.viewAlphaCheckered.getTop()) + alpha2);
        double floor2 = Math.floor((double) (this.viewAlphaCursor.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) this.viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        this.viewAlphaCursor.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: private */
    public int getColor() {
        return (Color.HSVToColor(this.currentColorHsv) & ViewCompat.MEASURED_SIZE_MASK) | (this.alpha << 24);
    }

    /* access modifiers changed from: private */
    public float getHue() {
        return this.currentColorHsv[0];
    }

    private float getAlpha() {
        return (float) this.alpha;
    }

    private float getSat() {
        return this.currentColorHsv[1];
    }

    private float getVal() {
        return this.currentColorHsv[2];
    }

    /* access modifiers changed from: private */
    public void setHue(float f) {
        this.currentColorHsv[0] = f;
    }

    /* access modifiers changed from: private */
    public void setSat(float f) {
        this.currentColorHsv[1] = f;
    }

    /* access modifiers changed from: private */
    public void setAlpha(int i) {
        this.alpha = i;
    }

    /* access modifiers changed from: private */
    public void setVal(float f) {
        this.currentColorHsv[2] = f;
    }

    public void show() {
        this.dialog.show();
    }

    public AlertDialog getDialog() {
        return this.dialog;
    }

    /* access modifiers changed from: private */
    public void updateAlphaView() {
        this.viewAlphaOverlay.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.HSVToColor(this.currentColorHsv), 0}));
    }
}
