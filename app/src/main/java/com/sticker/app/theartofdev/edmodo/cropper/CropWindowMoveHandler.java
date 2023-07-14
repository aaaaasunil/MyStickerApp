package com.sticker.app.theartofdev.edmodo.cropper;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;

final class CropWindowMoveHandler {
    private static final Matrix MATRIX = new Matrix();
    private final float mMaxCropHeight;
    private final float mMaxCropWidth;
    private final float mMinCropHeight;
    private final float mMinCropWidth;
    private final PointF mTouchOffset = new PointF();
    private final Type mType;

    public enum Type {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        CENTER
    }

    private static float calculateAspectRatio(float f, float f2, float f3, float f4) {
        return (f3 - f) / (f4 - f2);
    }

    public CropWindowMoveHandler(Type type, CropWindowHandler cropWindowHandler, float f, float f2) {
        this.mType = type;
        this.mMinCropWidth = cropWindowHandler.getMinCropWidth();
        this.mMinCropHeight = cropWindowHandler.getMinCropHeight();
        this.mMaxCropWidth = cropWindowHandler.getMaxCropWidth();
        this.mMaxCropHeight = cropWindowHandler.getMaxCropHeight();
        calculateTouchOffset(cropWindowHandler.getRect(), f, f2);
    }

    public void move(RectF rectF, float f, float f2, RectF rectF2, int i, int i2, float f3, boolean z, float f4) {
        float f5 = f + this.mTouchOffset.x;
        float f6 = f2 + this.mTouchOffset.y;
        if (this.mType == Type.CENTER) {
            moveCenter(rectF, f5, f6, rectF2, i, i2, f3);
        } else if (z) {
            moveSizeWithFixedAspectRatio(rectF, f5, f6, rectF2, i, i2, f3, f4);
        } else {
            moveSizeWithFreeAspectRatio(rectF, f5, f6, rectF2, i, i2, f3);
        }
    }

    private void calculateTouchOffset(RectF rectF, float f, float f2) {
        float f3;
        float f4 = 0.0f;
        switch (this.mType) {
            case TOP_LEFT:
                f4 = rectF.left - f;
                f3 = rectF.top - f2;
                break;
            case TOP_RIGHT:
                f4 = rectF.right - f;
                f3 = rectF.top - f2;
                break;
            case BOTTOM_LEFT:
                f4 = rectF.left - f;
                f3 = rectF.bottom - f2;
                break;
            case BOTTOM_RIGHT:
                f4 = rectF.right - f;
                f3 = rectF.bottom - f2;
                break;
            case LEFT:
                f4 = rectF.left - f;
                f3 = 0.0f;
                break;
            case TOP:
                f3 = rectF.top - f2;
                break;
            case RIGHT:
                f4 = rectF.right - f;
                f3 = 0.0f;
                break;
            case BOTTOM:
                f3 = rectF.bottom - f2;
                break;
            case CENTER:
                f4 = rectF.centerX() - f;
                f3 = rectF.centerY() - f2;
                break;
            default:
                f3 = 0.0f;
                break;
        }
        PointF pointF = this.mTouchOffset;
        pointF.x = f4;
        pointF.y = f3;
    }

    private void moveCenter(RectF rectF, float f, float f2, RectF rectF2, int i, int i2, float f3) {
        float centerX = f - rectF.centerX();
        float centerY = f2 - rectF.centerY();
        if (rectF.left + centerX < 0.0f || rectF.right + centerX > ((float) i) || rectF.left + centerX < rectF2.left || rectF.right + centerX > rectF2.right) {
            centerX /= 1.05f;
            this.mTouchOffset.x -= centerX / 2.0f;
        }
        if (rectF.top + centerY < 0.0f || rectF.bottom + centerY > ((float) i2) || rectF.top + centerY < rectF2.top || rectF.bottom + centerY > rectF2.bottom) {
            centerY /= 1.05f;
            this.mTouchOffset.y -= centerY / 2.0f;
        }
        rectF.offset(centerX, centerY);
        snapEdgesToBounds(rectF, rectF2, f3);
    }

    private void moveSizeWithFreeAspectRatio(RectF rectF, float f, float f2, RectF rectF2, int i, int i2, float f3) {
        switch (this.mType) {
            case TOP_LEFT:
                RectF rectF3 = rectF;
                RectF rectF4 = rectF2;
                float f4 = f3;
                adjustTop(rectF3, f2, rectF4, f4, 0.0f, false, false);
                adjustLeft(rectF3, f, rectF4, f4, 0.0f, false, false);
                return;
            case TOP_RIGHT:
                RectF rectF5 = rectF;
                RectF rectF6 = rectF2;
                adjustTop(rectF5, f2, rectF6, f3, 0.0f, false, false);
                adjustRight(rectF5, f, rectF6, i, f3, 0.0f, false, false);
                return;
            case BOTTOM_LEFT:
                RectF rectF7 = rectF;
                RectF rectF8 = rectF2;
                adjustBottom(rectF7, f2, rectF8, i2, f3, 0.0f, false, false);
                adjustLeft(rectF7, f, rectF8, f3, 0.0f, false, false);
                return;
            case BOTTOM_RIGHT:
                RectF rectF9 = rectF;
                RectF rectF10 = rectF2;
                float f5 = f3;
                adjustBottom(rectF9, f2, rectF10, i2, f5, 0.0f, false, false);
                adjustRight(rectF9, f, rectF10, i, f5, 0.0f, false, false);
                return;
            case LEFT:
                adjustLeft(rectF, f, rectF2, f3, 0.0f, false, false);
                return;
            case TOP:
                adjustTop(rectF, f2, rectF2, f3, 0.0f, false, false);
                return;
            case RIGHT:
                adjustRight(rectF, f, rectF2, i, f3, 0.0f, false, false);
                return;
            case BOTTOM:
                adjustBottom(rectF, f2, rectF2, i2, f3, 0.0f, false, false);
                return;
            default:
                return;
        }
    }

    private void moveSizeWithFixedAspectRatio(RectF rectF, float f, float f2, RectF rectF2, int i, int i2, float f3, float f4) {
        RectF rectF3 = rectF;
        float f5 = f;
        float f6 = f2;
        RectF rectF4 = rectF2;
        float f7 = f4;
        switch (this.mType) {
            case TOP_LEFT:
                if (calculateAspectRatio(f, f6, rectF3.right, rectF3.bottom) < f7) {
                    adjustTop(rectF, f2, rectF2, f3, f4, true, false);
                    adjustLeftByAspectRatio(rectF, f7);
                    return;
                }
                adjustLeft(rectF, f, rectF2, f3, f4, true, false);
                adjustTopByAspectRatio(rectF, f7);
                return;
            case TOP_RIGHT:
                if (calculateAspectRatio(rectF3.left, f6, f, rectF3.bottom) < f7) {
                    adjustTop(rectF, f2, rectF2, f3, f4, false, true);
                    adjustRightByAspectRatio(rectF, f7);
                    return;
                }
                adjustRight(rectF, f, rectF2, i, f3, f4, true, false);
                adjustTopByAspectRatio(rectF, f7);
                return;
            case BOTTOM_LEFT:
                if (calculateAspectRatio(f, rectF3.top, rectF3.right, f6) < f7) {
                    adjustBottom(rectF, f2, rectF2, i2, f3, f4, true, false);
                    adjustLeftByAspectRatio(rectF, f7);
                    return;
                }
                adjustLeft(rectF, f, rectF2, f3, f4, false, true);
                adjustBottomByAspectRatio(rectF, f7);
                return;
            case BOTTOM_RIGHT:
                if (calculateAspectRatio(rectF3.left, rectF3.top, f, f6) < f7) {
                    adjustBottom(rectF, f2, rectF2, i2, f3, f4, false, true);
                    adjustRightByAspectRatio(rectF, f7);
                    return;
                }
                adjustRight(rectF, f, rectF2, i, f3, f4, false, true);
                adjustBottomByAspectRatio(rectF, f7);
                return;
            case LEFT:
                adjustLeft(rectF, f, rectF2, f3, f4, true, true);
                adjustTopBottomByAspectRatio(rectF, rectF4, f7);
                return;
            case TOP:
                adjustTop(rectF, f2, rectF2, f3, f4, true, true);
                adjustLeftRightByAspectRatio(rectF, rectF4, f7);
                return;
            case RIGHT:
                adjustRight(rectF, f, rectF2, i, f3, f4, true, true);
                adjustTopBottomByAspectRatio(rectF, rectF4, f7);
                return;
            case BOTTOM:
                adjustBottom(rectF, f2, rectF2, i2, f3, f4, true, true);
                adjustLeftRightByAspectRatio(rectF, rectF4, f7);
                return;
            default:
                return;
        }
    }

    private void snapEdgesToBounds(RectF rectF, RectF rectF2, float f) {
        if (rectF.left < rectF2.left + f) {
            rectF.offset(rectF2.left - rectF.left, 0.0f);
        }
        if (rectF.top < rectF2.top + f) {
            rectF.offset(0.0f, rectF2.top - rectF.top);
        }
        if (rectF.right > rectF2.right - f) {
            rectF.offset(rectF2.right - rectF.right, 0.0f);
        }
        if (rectF.bottom > rectF2.bottom - f) {
            rectF.offset(0.0f, rectF2.bottom - rectF.bottom);
        }
    }

    private void adjustLeft(RectF rectF, float f, RectF rectF2, float f2, float f3, boolean z, boolean z2) {
        if (f < 0.0f) {
            f /= 1.05f;
            this.mTouchOffset.x -= f / 1.1f;
        }
        if (f < rectF2.left) {
            this.mTouchOffset.x -= (f - rectF2.left) / 2.0f;
        }
        if (f - rectF2.left < f2) {
            f = rectF2.left;
        }
        if (rectF.right - f < this.mMinCropWidth) {
            f = rectF.right - this.mMinCropWidth;
        }
        if (rectF.right - f > this.mMaxCropWidth) {
            f = rectF.right - this.mMaxCropWidth;
        }
        if (f - rectF2.left < f2) {
            f = rectF2.left;
        }
        if (f3 > 0.0f) {
            float f4 = (rectF.right - f) / f3;
            if (f4 < this.mMinCropHeight) {
                f = Math.max(rectF2.left, rectF.right - (this.mMinCropHeight * f3));
                f4 = (rectF.right - f) / f3;
            }
            if (f4 > this.mMaxCropHeight) {
                f = Math.max(rectF2.left, rectF.right - (this.mMaxCropHeight * f3));
                f4 = (rectF.right - f) / f3;
            }
            if (!z || !z2) {
                if (z && rectF.bottom - f4 < rectF2.top) {
                    f = Math.max(rectF2.left, rectF.right - ((rectF.bottom - rectF2.top) * f3));
                    f4 = (rectF.right - f) / f3;
                }
                if (z2 && rectF.top + f4 > rectF2.bottom) {
                    f = Math.max(f, Math.max(rectF2.left, rectF.right - ((rectF2.bottom - rectF.top) * f3)));
                }
            } else {
                f = Math.max(f, Math.max(rectF2.left, rectF.right - (rectF2.height() * f3)));
            }
        }
        rectF.left = f;
    }

    private void adjustRight(RectF rectF, float f, RectF rectF2, int i, float f2, float f3, boolean z, boolean z2) {
        float f4 = (float) i;
        if (f > f4) {
            f = ((f - f4) / 1.05f) + f4;
            this.mTouchOffset.x -= (f - f4) / 1.1f;
        }
        if (f > rectF2.right) {
            this.mTouchOffset.x -= (f - rectF2.right) / 2.0f;
        }
        if (rectF2.right - f < f2) {
            f = rectF2.right;
        }
        if (f - rectF.left < this.mMinCropWidth) {
            f = rectF.left + this.mMinCropWidth;
        }
        if (f - rectF.left > this.mMaxCropWidth) {
            f = rectF.left + this.mMaxCropWidth;
        }
        if (rectF2.right - f < f2) {
            f = rectF2.right;
        }
        if (f3 > 0.0f) {
            float f5 = (f - rectF.left) / f3;
            if (f5 < this.mMinCropHeight) {
                f = Math.min(rectF2.right, rectF.left + (this.mMinCropHeight * f3));
                f5 = (f - rectF.left) / f3;
            }
            if (f5 > this.mMaxCropHeight) {
                f = Math.min(rectF2.right, rectF.left + (this.mMaxCropHeight * f3));
                f5 = (f - rectF.left) / f3;
            }
            if (!z || !z2) {
                if (z && rectF.bottom - f5 < rectF2.top) {
                    f = Math.min(rectF2.right, rectF.left + ((rectF.bottom - rectF2.top) * f3));
                    f5 = (f - rectF.left) / f3;
                }
                if (z2 && rectF.top + f5 > rectF2.bottom) {
                    f = Math.min(f, Math.min(rectF2.right, rectF.left + ((rectF2.bottom - rectF.top) * f3)));
                }
            } else {
                f = Math.min(f, Math.min(rectF2.right, rectF.left + (rectF2.height() * f3)));
            }
        }
        rectF.right = f;
    }

    private void adjustTop(RectF rectF, float f, RectF rectF2, float f2, float f3, boolean z, boolean z2) {
        if (f < 0.0f) {
            f /= 1.05f;
            this.mTouchOffset.y -= f / 1.1f;
        }
        if (f < rectF2.top) {
            this.mTouchOffset.y -= (f - rectF2.top) / 2.0f;
        }
        if (f - rectF2.top < f2) {
            f = rectF2.top;
        }
        if (rectF.bottom - f < this.mMinCropHeight) {
            f = rectF.bottom - this.mMinCropHeight;
        }
        if (rectF.bottom - f > this.mMaxCropHeight) {
            f = rectF.bottom - this.mMaxCropHeight;
        }
        if (f - rectF2.top < f2) {
            f = rectF2.top;
        }
        if (f3 > 0.0f) {
            float f4 = (rectF.bottom - f) * f3;
            if (f4 < this.mMinCropWidth) {
                f = Math.max(rectF2.top, rectF.bottom - (this.mMinCropWidth / f3));
                f4 = (rectF.bottom - f) * f3;
            }
            if (f4 > this.mMaxCropWidth) {
                f = Math.max(rectF2.top, rectF.bottom - (this.mMaxCropWidth / f3));
                f4 = (rectF.bottom - f) * f3;
            }
            if (!z || !z2) {
                if (z && rectF.right - f4 < rectF2.left) {
                    f = Math.max(rectF2.top, rectF.bottom - ((rectF.right - rectF2.left) / f3));
                    f4 = (rectF.bottom - f) * f3;
                }
                if (z2 && rectF.left + f4 > rectF2.right) {
                    f = Math.max(f, Math.max(rectF2.top, rectF.bottom - ((rectF2.right - rectF.left) / f3)));
                }
            } else {
                f = Math.max(f, Math.max(rectF2.top, rectF.bottom - (rectF2.width() / f3)));
            }
        }
        rectF.top = f;
    }

    private void adjustBottom(RectF rectF, float f, RectF rectF2, int i, float f2, float f3, boolean z, boolean z2) {
        float f4 = (float) i;
        if (f > f4) {
            f = ((f - f4) / 1.05f) + f4;
            this.mTouchOffset.y -= (f - f4) / 1.1f;
        }
        if (f > rectF2.bottom) {
            this.mTouchOffset.y -= (f - rectF2.bottom) / 2.0f;
        }
        if (rectF2.bottom - f < f2) {
            f = rectF2.bottom;
        }
        if (f - rectF.top < this.mMinCropHeight) {
            f = rectF.top + this.mMinCropHeight;
        }
        if (f - rectF.top > this.mMaxCropHeight) {
            f = rectF.top + this.mMaxCropHeight;
        }
        if (rectF2.bottom - f < f2) {
            f = rectF2.bottom;
        }
        if (f3 > 0.0f) {
            float f5 = (f - rectF.top) * f3;
            if (f5 < this.mMinCropWidth) {
                f = Math.min(rectF2.bottom, rectF.top + (this.mMinCropWidth / f3));
                f5 = (f - rectF.top) * f3;
            }
            if (f5 > this.mMaxCropWidth) {
                f = Math.min(rectF2.bottom, rectF.top + (this.mMaxCropWidth / f3));
                f5 = (f - rectF.top) * f3;
            }
            if (!z || !z2) {
                if (z && rectF.right - f5 < rectF2.left) {
                    f = Math.min(rectF2.bottom, rectF.top + ((rectF.right - rectF2.left) / f3));
                    f5 = (f - rectF.top) * f3;
                }
                if (z2 && rectF.left + f5 > rectF2.right) {
                    f = Math.min(f, Math.min(rectF2.bottom, rectF.top + ((rectF2.right - rectF.left) / f3)));
                }
            } else {
                f = Math.min(f, Math.min(rectF2.bottom, rectF.top + (rectF2.width() / f3)));
            }
        }
        rectF.bottom = f;
    }

    private void adjustLeftByAspectRatio(RectF rectF, float f) {
        rectF.left = rectF.right - (rectF.height() * f);
    }

    private void adjustTopByAspectRatio(RectF rectF, float f) {
        rectF.top = rectF.bottom - (rectF.width() / f);
    }

    private void adjustRightByAspectRatio(RectF rectF, float f) {
        rectF.right = rectF.left + (rectF.height() * f);
    }

    private void adjustBottomByAspectRatio(RectF rectF, float f) {
        rectF.bottom = rectF.top + (rectF.width() / f);
    }

    private void adjustLeftRightByAspectRatio(RectF rectF, RectF rectF2, float f) {
        rectF.inset((rectF.width() - (rectF.height() * f)) / 2.0f, 0.0f);
        if (rectF.left < rectF2.left) {
            rectF.offset(rectF2.left - rectF.left, 0.0f);
        }
        if (rectF.right > rectF2.right) {
            rectF.offset(rectF2.right - rectF.right, 0.0f);
        }
    }

    private void adjustTopBottomByAspectRatio(RectF rectF, RectF rectF2, float f) {
        rectF.inset(0.0f, (rectF.height() - (rectF.width() / f)) / 2.0f);
        if (rectF.top < rectF2.top) {
            rectF.offset(0.0f, rectF2.top - rectF.top);
        }
        if (rectF.bottom > rectF2.bottom) {
            rectF.offset(0.0f, rectF2.bottom - rectF.bottom);
        }
    }
}
