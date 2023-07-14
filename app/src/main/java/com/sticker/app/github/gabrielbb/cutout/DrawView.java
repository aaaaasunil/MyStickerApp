package com.sticker.app.github.gabrielbb.cutout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.dynamite.descriptors.com.google.firebase.auth.ModuleDescriptor;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import org.apache.commons.lang3.StringUtils;

class DrawView extends View {
    private static final float COLOR_TOLERANCE = 50.0f;
    private static final float TOUCH_TOLERANCE = 4.0f;
    /* access modifiers changed from: private */
    public static Bitmap imageBitmap;

    /* renamed from: b */
    private Bitmap f66b;

    /* renamed from: c */
    private Canvas f67c;
    private Canvas canvas;
    private int color = -1;
    Context context;
    private DrawViewAction currentAction;
    /* access modifiers changed from: private */
    public final Stack<Pair<Pair<Path, Paint>, Bitmap>> cuts = new Stack<>();
    private WeakReference<DrawView> drawViewWeakReference;
    private View etEditText;
    private FrameLayout frameLayout;
    private boolean isStickerAdded;
    private boolean isTextDrag;
    private Path livePath;
    /* access modifiers changed from: private */
    public View loadingModal;
    private Random mRandom = new Random();
    private Paint pBg;
    private Paint pLine;
    private Paint pText;
    private Paint pathPaint;
    private float pathX;
    private float pathY;
    private ImageView redoButton;
    private Uri selectImage;
    private PointF start = new PointF();
    int stickerX;
    int stickerY;
    private String text;
    private int textSize = 35;
    String textStyle;
    int textX;
    int textY;
    private Path touchPath;
    /* access modifiers changed from: private */
    public ImageView undoButton;
    private final Stack<Pair<Pair<Path, Paint>, Bitmap>> undoneCuts = new Stack<>();

    /* renamed from: x */
    private int f68x;

    /* renamed from: y */
    private int f69y;

    public enum DrawViewAction {
        AUTO_CLEAR,
        MANUAL_CLEAR,
        ZOOM,
        TEXT_DRAW,
        ADD_STICKER
    }

    public DrawView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        this.context = context2;
        this.livePath = new Path();
        this.pathPaint = new Paint(1);
        this.pathPaint.setDither(true);
        this.pathPaint.setColor(-16776961);
        this.pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.pathPaint.setStyle(Paint.Style.STROKE);
        this.pathPaint.setStrokeJoin(Paint.Join.ROUND);
        this.pathPaint.setStrokeCap(Paint.Cap.ROUND);
        this.pBg = new Paint();
        this.pBg.setColor(-1);
        this.pLine = new Paint();
        this.pLine.setColor(-16711936);
        this.pLine.setAntiAlias(true);
        this.pLine.setStyle(Paint.Style.STROKE);
        this.pLine.setStrokeWidth(12.0f);
        this.touchPath = new Path();
    }

    public void setButtons(ImageView imageView, ImageView imageView2, FrameLayout frameLayout2) {
        this.undoButton = imageView;
        this.redoButton = imageView2;
        this.frameLayout = frameLayout2;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        //super.onSizeChanged(SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT, 512, i3, i4);
        resizeBitmap(getWidth(), getHeight());
        this.f66b = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        this.f67c = new Canvas(this.f66b);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        Log.e("widthMeasureSpec", MeasureSpec.toString(i) + StringUtils.SPACE + MeasureSpec.toString(i2));
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = MeasureSpec.getSize(i2);
        if (mode != 1073741824) {
            if (mode == Integer.MIN_VALUE) {
                size = Math.min(512, size);
            } else {
                size = 512;
            }
        }
        if (mode2 != 1073741824) {
            size2 = mode2 == Integer.MIN_VALUE ? Math.min(512, size2) : 512;
        }
        setMeasuredDimension(size, size);
       // Log.e(SettingsJsonConstants.ICON_WIDTH_KEY, size + "" + size2);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas2) {
        super.onDraw(canvas2);
        canvas2.save();
        this.canvas = canvas2;
        int width = (canvas2.getWidth() - imageBitmap.getWidth()) / 2;
        int height = (canvas2.getHeight() - imageBitmap.getHeight()) / 2;
        Bitmap bitmap = imageBitmap;
        if (bitmap != null) {
            canvas2.drawBitmap(bitmap, (float) width, (float) height, (Paint) null);
            Iterator it = this.cuts.iterator();
            while (it.hasNext()) {
                Pair pair = (Pair) it.next();
                if (pair.first != null) {
                    canvas2.drawPath((Path) ((Pair) pair.first).first, (Paint) ((Pair) pair.first).second);
                }
            }
            if (this.currentAction == DrawViewAction.MANUAL_CLEAR) {
                canvas2.drawPath(this.livePath, this.pathPaint);
            } else {
                DrawViewAction drawViewAction = this.currentAction;
                DrawViewAction drawViewAction2 = DrawViewAction.TEXT_DRAW;
            }
        }
        canvas2.drawBitmap(this.f66b, 0.0f, 0.0f, this.pBg);
        canvas2.drawPath(this.touchPath, this.pLine);
        redrawImage(this.text, this.color, this.textSize);
        canvas2.restore();
    }

    private void touchStart(float f, float f2) {
        this.pathX = f;
        this.pathY = f2;
        this.undoneCuts.clear();
        this.redoButton.setEnabled(false);
        if (this.currentAction == DrawViewAction.AUTO_CLEAR) {
            new AutomaticPixelClearingTask(this).execute(new Integer[]{Integer.valueOf((int) f), Integer.valueOf((int) f2)});
        } else {
            this.livePath.moveTo(f, f2);
        }
        invalidate();
    }

    private void touchMove(float f, float f2) {
        if (this.currentAction == DrawViewAction.MANUAL_CLEAR) {
            float abs = Math.abs(f - this.pathX);
            float abs2 = Math.abs(f2 - this.pathY);
            if (abs >= TOUCH_TOLERANCE || abs2 >= TOUCH_TOLERANCE) {
                Path path = this.livePath;
                float f3 = this.pathX;
                float f4 = this.pathY;
                path.quadTo(f3, f4, (f + f3) / 2.0f, (f2 + f4) / 2.0f);
                this.pathX = f;
                this.pathY = f2;
            }
        }
    }

    private void touchUp() {
        if (this.currentAction == DrawViewAction.MANUAL_CLEAR) {
            this.livePath.lineTo(this.pathX, this.pathY);
            this.cuts.push(new Pair(new Pair(this.livePath, this.pathPaint), (Object) null));
            this.livePath = new Path();
            this.undoButton.setEnabled(true);
        }
    }

    public void undo() {
        if (this.cuts.size() > 0) {
            Pair pop = this.cuts.pop();
            if (pop.second != null) {
                this.undoneCuts.push(new Pair((Object) null, imageBitmap));
                imageBitmap = (Bitmap) pop.second;
            } else {
                this.undoneCuts.push(pop);
            }
            if (this.cuts.isEmpty()) {
                this.undoButton.setEnabled(false);
            }
            this.redoButton.setEnabled(true);
            invalidate();
        }
    }

    public void redo() {
        if (this.undoneCuts.size() > 0) {
            Pair pop = this.undoneCuts.pop();
            if (pop.second != null) {
                this.cuts.push(new Pair((Object) null, imageBitmap));
                imageBitmap = (Bitmap) pop.second;
            } else {
                this.cuts.push(pop);
            }
            if (this.undoneCuts.isEmpty()) {
                this.redoButton.setEnabled(false);
            }
            this.undoButton.setEnabled(true);
            invalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (imageBitmap != null) {
            if (this.currentAction == DrawViewAction.MANUAL_CLEAR) {
                switch (motionEvent.getAction()) {
                    case 0:
                        if (this.isTextDrag) {
                            this.f68x = (int) motionEvent.getX();
                            this.f69y = (int) motionEvent.getY();
                        } else {
                            touchStart(motionEvent.getX(), motionEvent.getY());
                        }
                        return true;
                    case 1:
                        if (!this.isTextDrag) {
                            touchUp();
                        }
                        invalidate();
                        return true;
                    case 2:
                        if (this.isTextDrag) {
                            this.f68x = (int) motionEvent.getX();
                            this.f69y = (int) motionEvent.getY();
                        } else {
                            touchMove(motionEvent.getX(), motionEvent.getY());
                        }
                        invalidate();
                        return true;
                }
            } else if (this.currentAction == DrawViewAction.AUTO_CLEAR) {
                if (motionEvent.getAction() == 0) {
                    touchStart(motionEvent.getX(), motionEvent.getY());
                    invalidate();
                    return true;
                }
            } else if (this.currentAction == DrawViewAction.TEXT_DRAW) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                switch (motionEvent.getAction()) {
                    case 0:
                        this.touchPath.moveTo(x, y);
                        break;
                    case 1:
                        this.touchPath.lineTo(x, y);
                        this.f67c.drawPath(this.touchPath, this.pLine);
                        this.touchPath.reset();
                        break;
                    case 2:
                        this.touchPath.lineTo(x, y);
                        break;
                    default:
                        return false;
                }
                invalidate();
                return true;
            } else if (this.currentAction == DrawViewAction.ADD_STICKER) {
                switch (motionEvent.getAction()) {
                    case 0:
                        this.f68x = (int) motionEvent.getX();
                        this.f69y = (int) motionEvent.getY();
                        invalidate();
                        return true;
                    case 1:
                        return true;
                    case 2:
                        this.f68x = (int) motionEvent.getX();
                        this.f69y = (int) motionEvent.getY();
                        invalidate();
                        return true;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    private void resizeBitmap(int i, int i2) {
        Bitmap bitmap;
        if (i > 0 && i2 > 0 && (bitmap = imageBitmap) != null) {
            imageBitmap = BitmapUtility.getResizedBitmap(bitmap, i, i2);
            imageBitmap.setHasAlpha(true);
            invalidate();
        }
    }

    public void setBitmap(Bitmap bitmap, String str, boolean z, String str2, int i, int i2, Uri uri) {
        imageBitmap = bitmap;
        this.text = str;
        this.isTextDrag = z;
        this.textStyle = str2;
        this.color = i;
        this.textSize = i2;
        this.selectImage = uri;
        resizeBitmap(getWidth(), getHeight());
    }

    public Bitmap getCurrentBitmap() {
        return imageBitmap;
    }

    public void setAction(DrawViewAction drawViewAction) {
        this.currentAction = drawViewAction;
    }

    public void setStrokeWidth(int i) {
        this.pathPaint = new Paint(this.pathPaint);
        this.pathPaint.setStrokeWidth((float) i);
    }

    public void setLoadingModal(View view) {
        this.loadingModal = view;
    }

    private static class AutomaticPixelClearingTask extends AsyncTask<Integer, Void, Bitmap> {
        private WeakReference<DrawView> drawViewWeakReference;

        AutomaticPixelClearingTask(DrawView drawView) {
            this.drawViewWeakReference = new WeakReference<>(drawView);
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            ((DrawView) this.drawViewWeakReference.get()).loadingModal.setVisibility(View.VISIBLE);;
            Stack access$200 = ((DrawView) this.drawViewWeakReference.get()).cuts;
            DrawView drawView = (DrawView) this.drawViewWeakReference.get();
            access$200.push(new Pair((Object) null, DrawView.imageBitmap));
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(Integer... numArr) {
            Bitmap bitmap;
            try {
                DrawView drawView = (DrawView) this.drawViewWeakReference.get();
                Bitmap access$100 = DrawView.imageBitmap;
                int pixel = access$100.getPixel(numArr[0].intValue(), numArr[1].intValue());
                int width = access$100.getWidth();
                int height = access$100.getHeight();
                int[] iArr = new int[(width * height)];
                access$100.getPixels(iArr, 0, width, 0, 0, width, height);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                for (int i = 0; i < height; i++) {
                    for (int i2 = 0; i2 < width; i2++) {
                        int i3 = (i * width) + i2;
                        int i4 = iArr[i3];
                        int alpha2 = Color.alpha(i4);
                        int red2 = Color.red(i4);
                        int green2 = Color.green(i4);
                        int blue2 = Color.blue(i4);
                        float f = (float) alpha;
                        float f2 = (float) alpha2;
                        if (f - DrawView.COLOR_TOLERANCE < f2 && f2 < f + DrawView.COLOR_TOLERANCE) {
                            float f3 = (float) red;
                            float f4 = (float) red2;
                            if (f3 - DrawView.COLOR_TOLERANCE < f4 && f4 < f3 + DrawView.COLOR_TOLERANCE) {
                                float f5 = (float) green;
                                float f6 = (float) green2;
                                if (f5 - DrawView.COLOR_TOLERANCE < f6 && f6 < f5 + DrawView.COLOR_TOLERANCE) {
                                    float f7 = (float) blue;
                                    float f8 = (float) blue2;
                                    if (f7 - DrawView.COLOR_TOLERANCE < f8 && f8 < f7 + DrawView.COLOR_TOLERANCE) {
                                        iArr[i3] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                try {
                    bitmap.setPixels(iArr, 0, width, 0, 0, width, height);
                } catch (Exception unused) {
                }
            } catch (Exception unused2) {
                bitmap = null;
            }
            return bitmap == null ? DrawView.imageBitmap : bitmap;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            DrawView drawView = (DrawView) this.drawViewWeakReference.get();
            Bitmap unused = DrawView.imageBitmap = bitmap;
            ((DrawView) this.drawViewWeakReference.get()).undoButton.setEnabled(true);
            ((DrawView) this.drawViewWeakReference.get()).loadingModal.setVisibility(View.INVISIBLE);
            ((DrawView) this.drawViewWeakReference.get()).invalidate();
        }
    }

    public void redrawImage(String str, int i, int i2) {
        if (this.f68x == 0) {
            this.f68x = imageBitmap.getWidth() / 2;
            this.f69y = (imageBitmap.getHeight() / 2) + ModuleDescriptor.MODULE_VERSION;
        }
        Paint paint = new Paint(1);
        paint.setColor(i);
        paint.setTextSize((float) i2);
        paint.setTypeface(Typeface.createFromAsset(this.context.getAssets(), this.textStyle));
        paint.setStyle(Paint.Style.FILL);
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        this.textX = this.f68x - (rect.width() / 2);
        this.textY = this.f69y - (str.length() / 2);
        this.canvas.drawText(str, (float) this.textX, (float) this.textY, paint);
    }

    public void addStickers() {
        if (this.f68x == 0) {
            this.f68x = imageBitmap.getWidth() / 2;
            this.f69y = (imageBitmap.getHeight() / 2) + ModuleDescriptor.MODULE_VERSION;
        }
        if (this.currentAction == DrawViewAction.ADD_STICKER) {
            this.isStickerAdded = true;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), this.selectImage);
                this.stickerX = this.f68x - (bitmap.getWidth() / 2);
                this.stickerY = this.f69y - (bitmap.getHeight() / 2);
                this.canvas.drawBitmap(bitmap, (float) this.stickerX, (float) this.stickerY, (Paint) null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (this.isStickerAdded) {
            try {
                this.canvas.drawBitmap(MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), this.selectImage), (float) this.stickerX, (float) this.stickerY, (Paint) null);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
