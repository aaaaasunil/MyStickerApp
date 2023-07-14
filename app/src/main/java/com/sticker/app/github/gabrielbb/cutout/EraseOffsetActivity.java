package com.sticker.app.github.gabrielbb.cutout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.gms.dynamite.descriptors.com.google.firebase.auth.ModuleDescriptor;
import com.sticker.app.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class EraseOffsetActivity extends Activity {
    /* access modifiers changed from: private */
    public int MODE = 0;
    /* access modifiers changed from: private */
    public Bitmap bitmapMaster;
    private BrushImageView brushImageView;
    /* access modifiers changed from: private */
    public float brushSize = 70.0f;
    private Vector<Integer> brushSizes = new Vector<>();
    private Canvas canvasMaster;
    /* access modifiers changed from: private */
    public float currentx;
    /* access modifiers changed from: private */
    public float currenty;
    /* access modifiers changed from: private */
    public Path drawingPath;
    /* access modifiers changed from: private */
    public Bitmap highResolutionOutput;
    private int imageViewHeight;
    private int imageViewWidth;
    /* access modifiers changed from: private */
    public int initialDrawingCount;
    private int initialDrawingCountLimit = 20;
    private boolean isImageResized;
    /* access modifiers changed from: private */
    public boolean isMultipleTouchErasing;
    /* access modifiers changed from: private */
    public boolean isTouchOnBitmap;
    private ImageView ivBack;
    private ImageView ivDone;
    private ImageView ivRedo;
    private ImageView ivUndo;
    private Bitmap lastEditedBitmap;
    private LinearLayout llTopBar;
    private Point mainViewSize;
    /* access modifiers changed from: private */
    public MediaScannerConnection msConn;
    /* access modifiers changed from: private */
    public int offset = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
    /* access modifiers changed from: private */
    public Bitmap originalBitmap;
    private ArrayList<Path> paths = new ArrayList<>();
    private Vector<Integer> redoBrushSizes = new Vector<>();
    private ArrayList<Path> redoPaths = new ArrayList<>();
    private Bitmap resizedBitmap;
    private RelativeLayout rlImageViewContainer;
    private SeekBar sbOffset;
    private SeekBar sbWidth;
    /* access modifiers changed from: private */
    public TouchImageView touchImageView;
    private TextView tvDone;
    private int undoLimit = 10;
    private int updatedBrushSize;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_erase_offset);
        setRequestedOrientation(1);
        this.drawingPath = new Path();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        this.mainViewSize = new Point();
        defaultDisplay.getSize(this.mainViewSize);
        initViews();
        getIntent();
        byte[] byteArrayExtra = getIntent().getByteArrayExtra("image");
        this.originalBitmap = BitmapFactory.decodeByteArray(byteArrayExtra, 0, byteArrayExtra.length);
        setBitMap();
        updateBrush((float) (this.mainViewSize.x / 2), (float) (this.mainViewSize.y / 2));
    }

    public void initViews() {
        this.tvDone = (TextView) findViewById(R.id.tvDone);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.touchImageView = (TouchImageView) findViewById(R.id.drawingImageView);
        this.brushImageView = (BrushImageView) findViewById(R.id.brushContainingView);
        this.llTopBar = (LinearLayout) findViewById(R.id.ll_top_bar);
        this.rlImageViewContainer = (RelativeLayout) findViewById(R.id.rl_image_view_container);
        this.ivUndo = (ImageView) findViewById(R.id.iv_undo);
        this.ivRedo = (ImageView) findViewById(R.id.iv_redo);
        this.ivDone = (ImageView) findViewById(R.id.iv_done);
        this.sbOffset = (SeekBar) findViewById(R.id.sb_offset);
        this.sbWidth = (SeekBar) findViewById(R.id.sb_width);
        this.rlImageViewContainer.getLayoutParams().height = this.mainViewSize.y - this.llTopBar.getLayoutParams().height;
        this.imageViewWidth = this.mainViewSize.x;
        this.imageViewHeight = this.rlImageViewContainer.getLayoutParams().height;
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (EraseOffsetActivity.this.bitmapMaster == null) {
                    EraseOffsetActivity eraseOffsetActivity = EraseOffsetActivity.this;
                    Bitmap unused = eraseOffsetActivity.bitmapMaster = eraseOffsetActivity.originalBitmap;
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                EraseOffsetActivity eraseOffsetActivity2 = EraseOffsetActivity.this;
                Bitmap unused2 = eraseOffsetActivity2.bitmapMaster = Bitmap.createScaledBitmap(eraseOffsetActivity2.bitmapMaster, 512, 512, true);
                EraseOffsetActivity.this.bitmapMaster.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                Intent intent = new Intent(EraseOffsetActivity.this, StickerViewActivity.class);
                intent.putExtra("image", byteArray);
                EraseOffsetActivity.this.startActivity(intent);
                EraseOffsetActivity.this.finish();
            }
        });
        this.tvDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (EraseOffsetActivity.this.bitmapMaster == null) {
                    EraseOffsetActivity eraseOffsetActivity = EraseOffsetActivity.this;
                    Bitmap unused = eraseOffsetActivity.bitmapMaster = eraseOffsetActivity.originalBitmap;
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                EraseOffsetActivity eraseOffsetActivity2 = EraseOffsetActivity.this;
                Bitmap unused2 = eraseOffsetActivity2.bitmapMaster = Bitmap.createScaledBitmap(eraseOffsetActivity2.bitmapMaster, 512, 512, true);
                EraseOffsetActivity.this.bitmapMaster.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                Intent intent = new Intent(EraseOffsetActivity.this, StickerViewActivity.class);
                intent.putExtra("image", byteArray);
                EraseOffsetActivity.this.startActivity(intent);
                EraseOffsetActivity.this.finish();
            }
        });
        this.ivUndo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EraseOffsetActivity.this.undo();
            }
        });
        this.ivRedo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EraseOffsetActivity.this.redo();
            }
        });
        this.ivDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EraseOffsetActivity.this.saveImage();
            }
        });
        this.touchImageView.setOnTouchListener(new OnTouchListner());
        this.sbWidth.setMax(ModuleDescriptor.MODULE_VERSION);
        this.sbWidth.setProgress((int) (this.brushSize - 20.0f));
        this.sbWidth.setOnSeekBarChangeListener(new OnWidthSeekbarChangeListner());
        this.sbOffset.setMax(350);
        this.sbOffset.setProgress(this.offset);
        this.sbOffset.setOnSeekBarChangeListener(new OnOffsetSeekbarChangeListner());
    }

    public void resetPathArrays() {
        this.ivUndo.setEnabled(false);
        this.ivRedo.setEnabled(false);
        this.paths.clear();
        this.brushSizes.clear();
        this.redoPaths.clear();
        this.redoBrushSizes.clear();
    }

    public void resetRedoPathArrays() {
        this.ivRedo.setEnabled(false);
        this.redoPaths.clear();
        this.redoBrushSizes.clear();
    }

    public void undo() {
        int size = this.paths.size();
        if (size != 0) {
            if (size == 1) {
                this.ivUndo.setEnabled(false);
            }
            int i = size - 1;
            this.redoPaths.add(this.paths.remove(i));
            this.redoBrushSizes.add(this.brushSizes.remove(i));
            if (!this.ivRedo.isEnabled()) {
                this.ivRedo.setEnabled(true);
            }
            UpdateCanvas();
        }
    }

    public void redo() {
        int size = this.redoPaths.size();
        if (size != 0) {
            if (size == 1) {
                this.ivRedo.setEnabled(false);
            }
            int i = size - 1;
            this.paths.add(this.redoPaths.remove(i));
            this.brushSizes.add(this.redoBrushSizes.remove(i));
            if (!this.ivUndo.isEnabled()) {
                this.ivUndo.setEnabled(true);
            }
            UpdateCanvas();
        }
    }

    public void setBitMap() {
        this.isImageResized = false;
        Bitmap bitmap = this.resizedBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.resizedBitmap = null;
        }
        Bitmap bitmap2 = this.bitmapMaster;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.bitmapMaster = null;
        }
        this.canvasMaster = null;
        this.resizedBitmap = resizeBitmapByCanvas();
        this.lastEditedBitmap = this.resizedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        this.bitmapMaster = Bitmap.createBitmap(this.lastEditedBitmap.getWidth(), this.lastEditedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        this.canvasMaster = new Canvas(this.bitmapMaster);
        this.canvasMaster.drawBitmap(this.lastEditedBitmap, 0.0f, 0.0f, (Paint) null);
        this.touchImageView.setImageBitmap(this.bitmapMaster);
        resetPathArrays();
        this.touchImageView.setPan(false);
        this.brushImageView.invalidate();
    }

    public Bitmap resizeBitmapByCanvas() {
        float f;
        float f2;
        float width = (float) this.originalBitmap.getWidth();
        float height = (float) this.originalBitmap.getHeight();
        if (width > height) {
            int i = this.imageViewWidth;
            float f3 = (float) i;
            f = (((float) i) * height) / width;
            f2 = f3;
        } else {
            int i2 = this.imageViewHeight;
            f = (float) i2;
            f2 = (((float) i2) * width) / height;
        }
        if (f2 > width || f > height) {
            return this.originalBitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap((int) f2, (int) f, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float f4 = f2 / width;
        Matrix matrix = new Matrix();
        matrix.postTranslate(0.0f, (f - (height * f4)) / 2.0f);
        matrix.preScale(f4, f4);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(this.originalBitmap, matrix, paint);
        this.isImageResized = true;
        return createBitmap;
    }

    /* access modifiers changed from: private */
    public void moveTopoint(float f, float f2) {
        float imageViewZoom = getImageViewZoom();
        float f3 = f2 - ((float) this.offset);
        if (this.redoPaths.size() > 0) {
            resetRedoPathArrays();
        }
        PointF imageViewTranslation = getImageViewTranslation();
        double d = (double) (f - imageViewTranslation.x);
        double d2 = (double) imageViewZoom;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = (double) (f3 - imageViewTranslation.y);
        Double.isNaN(d3);
        Double.isNaN(d2);
        this.drawingPath.moveTo((float) ((int) ((float) (d / d2))), (float) ((int) ((float) (d3 / d2))));
        this.updatedBrushSize = (int) (this.brushSize / imageViewZoom);
    }

    /* access modifiers changed from: private */
    public void lineTopoint(Bitmap bitmap, float f, float f2) {
        int i = this.initialDrawingCount;
        int i2 = this.initialDrawingCountLimit;
        if (i < i2) {
            this.initialDrawingCount = i + 1;
            if (this.initialDrawingCount == i2) {
                this.isMultipleTouchErasing = true;
            }
        }
        float imageViewZoom = getImageViewZoom();
        float f3 = f2 - ((float) this.offset);
        PointF imageViewTranslation = getImageViewTranslation();
        double d = (double) (f - imageViewTranslation.x);
        double d2 = (double) imageViewZoom;
        Double.isNaN(d);
        Double.isNaN(d2);
        int i3 = (int) ((float) (d / d2));
        double d3 = (double) (f3 - imageViewTranslation.y);
        Double.isNaN(d3);
        Double.isNaN(d2);
        int i4 = (int) ((float) (d3 / d2));
        if (!this.isTouchOnBitmap && i3 > 0 && i3 < bitmap.getWidth() && i4 > 0 && i4 < bitmap.getHeight()) {
            this.isTouchOnBitmap = true;
        }
        this.drawingPath.lineTo((float) i3, (float) i4);
    }

    /* access modifiers changed from: private */
    public void addDrawingPathToArrayList() {
        if (this.paths.size() >= this.undoLimit) {
            UpdateLastEiditedBitmapForUndoLimit();
            this.paths.remove(0);
            this.brushSizes.remove(0);
        }
        if (this.paths.size() == 0) {
            this.ivUndo.setEnabled(true);
            this.ivRedo.setEnabled(false);
        }
        this.brushSizes.add(Integer.valueOf(this.updatedBrushSize));
        this.paths.add(this.drawingPath);
        this.drawingPath = new Path();
    }

    /* access modifiers changed from: private */
    public void drawOnTouchMove() {
        Paint paint = new Paint();
        paint.setStrokeWidth((float) this.updatedBrushSize);
        paint.setColor(0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.canvasMaster.drawPath(this.drawingPath, paint);
        this.touchImageView.invalidate();
    }

    public void UpdateLastEiditedBitmapForUndoLimit() {
        Canvas canvas = new Canvas(this.lastEditedBitmap);
        for (int i = 0; i < 1; i++) {
            int intValue = this.brushSizes.get(i).intValue();
            Paint paint = new Paint();
            paint.setColor(0);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            paint.setStrokeWidth((float) intValue);
            canvas.drawPath(this.paths.get(i), paint);
        }
    }

    public void UpdateCanvas() {
        this.canvasMaster.drawColor(0, PorterDuff.Mode.CLEAR);
        this.canvasMaster.drawBitmap(this.lastEditedBitmap, 0.0f, 0.0f, (Paint) null);
        for (int i = 0; i < this.paths.size(); i++) {
            int intValue = this.brushSizes.get(i).intValue();
            Paint paint = new Paint();
            paint.setColor(0);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            paint.setStrokeWidth((float) intValue);
            this.canvasMaster.drawPath(this.paths.get(i), paint);
        }
        this.touchImageView.invalidate();
    }

    public void updateBrushWidth() {
        BrushImageView brushImageView2 = this.brushImageView;
        brushImageView2.width = this.brushSize / 2.0f;
        brushImageView2.invalidate();
    }

    public void updateBrushOffset() {
        this.brushImageView.centery += ((float) this.offset) - this.brushImageView.offset;
        BrushImageView brushImageView2 = this.brushImageView;
        brushImageView2.offset = (float) this.offset;
        brushImageView2.invalidate();
    }

    public void updateBrush(float f, float f2) {
        BrushImageView brushImageView2 = this.brushImageView;
        brushImageView2.offset = (float) this.offset;
        brushImageView2.centerx = f;
        brushImageView2.centery = f2;
        brushImageView2.width = this.brushSize / 2.0f;
        brushImageView2.invalidate();
    }

    public float getImageViewZoom() {
        return this.touchImageView.getCurrentZoom();
    }

    public PointF getImageViewTranslation() {
        return this.touchImageView.getTransForm();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        UpdateCanvas();
        Bitmap bitmap = this.lastEditedBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.lastEditedBitmap = null;
        }
        Bitmap bitmap2 = this.originalBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.originalBitmap = null;
        }
        Bitmap bitmap3 = this.resizedBitmap;
        if (bitmap3 != null) {
            bitmap3.recycle();
            this.resizedBitmap = null;
        }
        Bitmap bitmap4 = this.bitmapMaster;
        if (bitmap4 != null) {
            bitmap4.recycle();
            this.bitmapMaster = null;
        }
        Bitmap bitmap5 = this.highResolutionOutput;
        if (bitmap5 != null) {
            bitmap5.recycle();
            this.highResolutionOutput = null;
        }
    }

    private class OnTouchListner implements View.OnTouchListener {
        OnTouchListner() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (motionEvent.getPointerCount() != 1 && !EraseOffsetActivity.this.isMultipleTouchErasing) {
                if (EraseOffsetActivity.this.initialDrawingCount > 0) {
                    EraseOffsetActivity.this.UpdateCanvas();
                    EraseOffsetActivity.this.drawingPath.reset();
                    int unused = EraseOffsetActivity.this.initialDrawingCount = 0;
                }
                EraseOffsetActivity.this.touchImageView.onTouchEvent(motionEvent);
                int unused2 = EraseOffsetActivity.this.MODE = 2;
            } else if (action == 0) {
                boolean unused3 = EraseOffsetActivity.this.isTouchOnBitmap = false;
                EraseOffsetActivity.this.touchImageView.onTouchEvent(motionEvent);
                int unused4 = EraseOffsetActivity.this.MODE = 1;
                int unused5 = EraseOffsetActivity.this.initialDrawingCount = 0;
                boolean unused6 = EraseOffsetActivity.this.isMultipleTouchErasing = false;
                EraseOffsetActivity.this.moveTopoint(motionEvent.getX(), motionEvent.getY());
                EraseOffsetActivity.this.updateBrush(motionEvent.getX(), motionEvent.getY());
            } else if (action == 2) {
                if (EraseOffsetActivity.this.MODE == 1) {
                    float unused7 = EraseOffsetActivity.this.currentx = motionEvent.getX();
                    float unused8 = EraseOffsetActivity.this.currenty = motionEvent.getY();
                    EraseOffsetActivity eraseOffsetActivity = EraseOffsetActivity.this;
                    eraseOffsetActivity.updateBrush(eraseOffsetActivity.currentx, EraseOffsetActivity.this.currenty);
                    EraseOffsetActivity eraseOffsetActivity2 = EraseOffsetActivity.this;
                    eraseOffsetActivity2.lineTopoint(eraseOffsetActivity2.bitmapMaster, EraseOffsetActivity.this.currentx, EraseOffsetActivity.this.currenty);
                    EraseOffsetActivity.this.drawOnTouchMove();
                }
            } else if (action == 1 || action == 6) {
                if (EraseOffsetActivity.this.MODE == 1 && EraseOffsetActivity.this.isTouchOnBitmap) {
                    EraseOffsetActivity.this.addDrawingPathToArrayList();
                }
                boolean unused9 = EraseOffsetActivity.this.isMultipleTouchErasing = false;
                int unused10 = EraseOffsetActivity.this.initialDrawingCount = 0;
                int unused11 = EraseOffsetActivity.this.MODE = 0;
            }
            if (action == 1 || action == 6) {
                int unused12 = EraseOffsetActivity.this.MODE = 0;
            }
            return true;
        }
    }

    private class OnWidthSeekbarChangeListner implements SeekBar.OnSeekBarChangeListener {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        OnWidthSeekbarChangeListner() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            float unused = EraseOffsetActivity.this.brushSize = ((float) i) + 20.0f;
            EraseOffsetActivity.this.updateBrushWidth();
        }
    }

    private class OnOffsetSeekbarChangeListner implements SeekBar.OnSeekBarChangeListener {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        OnOffsetSeekbarChangeListner() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int unused = EraseOffsetActivity.this.offset = i;
            EraseOffsetActivity.this.updateBrushOffset();
        }
    }

    /* access modifiers changed from: private */
    public void saveImage() {
        makeHighResolutionOutput();
        new imageSaveByAsync().execute(new String[0]);
    }

    private void makeHighResolutionOutput() {
        if (this.isImageResized) {
            Bitmap createBitmap = Bitmap.createBitmap(this.originalBitmap.getWidth(), this.originalBitmap.getHeight(), this.originalBitmap.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.argb(255, 255, 255, 255));
            Rect rect = new Rect(0, 0, this.bitmapMaster.getWidth(), this.bitmapMaster.getHeight());
            Rect rect2 = new Rect(0, 0, this.originalBitmap.getWidth(), this.originalBitmap.getHeight());
            canvas.drawRect(rect2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawBitmap(this.bitmapMaster, rect, rect2, paint);
            this.highResolutionOutput = null;
            this.highResolutionOutput = Bitmap.createBitmap(this.originalBitmap.getWidth(), this.originalBitmap.getHeight(), this.originalBitmap.getConfig());
            Canvas canvas2 = new Canvas(this.highResolutionOutput);
            canvas2.drawBitmap(this.originalBitmap, 0.0f, 0.0f, (Paint) null);
            Paint paint2 = new Paint();
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
            if (createBitmap != null && !createBitmap.isRecycled()) {
                createBitmap.recycle();
                return;
            }
            return;
        }
        this.highResolutionOutput = null;
        Bitmap bitmap = this.bitmapMaster;
        this.highResolutionOutput = bitmap.copy(bitmap.getConfig(), true);
    }

    private class imageSaveByAsync extends AsyncTask<String, Void, Boolean> {
        private imageSaveByAsync() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            EraseOffsetActivity.this.getWindow().setFlags(16, 16);
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(String... strArr) {
            try {
                EraseOffsetActivity.this.savePhoto(EraseOffsetActivity.this.highResolutionOutput);
                return true;
            } catch (Exception unused) {
                return false;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            Toast makeText = Toast.makeText(EraseOffsetActivity.this.getBaseContext(), "PNG Saved", 1);
            makeText.setGravity(17, 0, 0);
            makeText.show();
            EraseOffsetActivity.this.getWindow().clearFlags(16);
        }
    }

    public void savePhoto(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), "ImageEraser");
        file.mkdir();
        Calendar instance = Calendar.getInstance();
        String str = String.valueOf(instance.get(2)) + String.valueOf(instance.get(5)) + String.valueOf(instance.get(1)) + String.valueOf(instance.get(11)) + String.valueOf(instance.get(12)) + String.valueOf(instance.get(13));
        File file2 = new File(file, str.toString() + ".png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        scanPhoto(file2.toString());
    }

    public void scanPhoto(String str) {
        this.msConn = new MediaScannerConnection(this, new ScanPhotoConnection(str));
        this.msConn.connect();
    }

    class ScanPhotoConnection implements MediaScannerConnection.MediaScannerConnectionClient {
        final String val$imageFileName;

        ScanPhotoConnection(String str) {
            this.val$imageFileName = str;
        }

        public void onMediaScannerConnected() {
            EraseOffsetActivity.this.msConn.scanFile(this.val$imageFileName, (String) null);
        }

        public void onScanCompleted(String str, Uri uri) {
            EraseOffsetActivity.this.msConn.disconnect();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.bitmapMaster == null) {
            this.bitmapMaster = this.originalBitmap;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.bitmapMaster = Bitmap.createScaledBitmap(this.bitmapMaster, 512, 512, true);
        this.bitmapMaster.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Intent intent = new Intent(this, StickerViewActivity.class);
        intent.putExtra("image", byteArray);
        startActivity(intent);
        finish();
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", (String) null));
    }
}
