package com.sticker.app.github.gabrielbb.cutout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alexvasilkov.gestures.views.interfaces.GestureView;
import com.google.android.gms.dynamite.descriptors.com.google.firebase.auth.ModuleDescriptor;
import com.sticker.app.R;
import com.sticker.app.github.gabrielbb.cutout.DrawView;
import com.sticker.app.github.gabrielbb.cutout.utils.NothingSelectedSpinnerAdapter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import yuku.ambilwarna.AmbilWarnaDialog;

public class CutOutActivity extends AppCompatActivity {
    private static final short BORDER_SIZE = 45;
    private static final short MAX_ERASER_SIZE = 150;
    private static final float MAX_ZOOM = 4.0f;
    /* access modifiers changed from: private */
    public int RESULT_LOAD_IMAGE = 100;
    ImageView autoClearButton;
    /* access modifiers changed from: private */
    public Bitmap bitmap;
    private Button btnDrawText;
    private Button btnText;
    /* access modifiers changed from: private */
    public DrawView drawView;
    /* access modifiers changed from: private */
    public EditText etText;
    private GestureView gestureView;

    /* renamed from: h */
    private int f64h;
    private ImageView ivAddSticker;
    ImageView ivBack;
    private ImageView ivMoveSticker;
    private RelativeLayout llBottom;
    public FrameLayout loadingModal;
    ImageView manualClearButton;
    private LinearLayout manualClearSettingsLayout;
    Uri selectedSticker;
    /* access modifiers changed from: private */
    public String strText = "";
    /* access modifiers changed from: private */
    public String strTextStyle = "AutourOne-Regular.otf";
    private SeekBar strokeBar;
    /* access modifiers changed from: private */
    public int textColor = -1;
    /* access modifiers changed from: private */
    public int textSize = 35;
    private RelativeLayout top_linear;
    private TextView tvSaveSticker;
    /* access modifiers changed from: private */
    public Typeface typeface;

    /* renamed from: w */
    private int f65w;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_edit);
        initView();
    }

    private void initView() {
        this.autoClearButton = (ImageView) findViewById(R.id.auto_clear_button);
        this.manualClearButton = (ImageView) findViewById(R.id.manual_clear_button);
        this.tvSaveSticker = (TextView) findViewById(R.id.tvSaveSticker);
        this.btnText = (Button) findViewById(R.id.btnText);
        this.btnDrawText = (Button) findViewById(R.id.btnDrawText);
        this.ivAddSticker = (ImageView) findViewById(R.id.ivAddSticker);
        this.ivMoveSticker = (ImageView) findViewById(R.id.ivMoveSticker);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.etText = (EditText) findViewById(R.id.etText);
        this.gestureView = (GestureView) findViewById(R.id.gestureView);
        this.drawView = (DrawView) findViewById(R.id.drawView);
        this.top_linear = (RelativeLayout) findViewById(R.id.top_linear);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.drawViewLayout);
        this.loadingModal = (FrameLayout) findViewById(R.id.loadingModal);
        this.strokeBar = (SeekBar) findViewById(R.id.strokeBar);
        int i = Build.VERSION.SDK_INT;
        this.strokeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                CutOutActivity.this.drawView.setStrokeWidth(seekBar.getProgress());
            }
        });
        this.strokeBar.setMax(ModuleDescriptor.MODULE_VERSION);
        this.strokeBar.setProgress(50);
        this.drawView.setDrawingCacheEnabled(true);
        this.drawView.setLayerType(View.LAYER_TYPE_HARDWARE, (Paint) null);
        this.drawView.setStrokeWidth(this.strokeBar.getProgress());
        this.loadingModal.setVisibility(View.INVISIBLE);
        this.drawView.setLoadingModal(this.loadingModal);
        this.manualClearSettingsLayout = (LinearLayout) findViewById(R.id.manual_clear_settings_layout);
        byte[] byteArrayExtra = getIntent().getByteArrayExtra("image");
        this.bitmap = BitmapFactory.decodeByteArray(byteArrayExtra, 0, byteArrayExtra.length);
        this.drawView.setBitmap(this.bitmap, "", false, this.strTextStyle, this.textColor, this.textSize, this.selectedSticker);
        setUndoRedo();
        initializeActionButtons();
        clicks();
    }

    private void clicks() {
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CutOutActivity.this.drawView.setDrawingCacheEnabled(true);
                Bitmap drawingCache = CutOutActivity.this.drawView.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap.createScaledBitmap(drawingCache, 512, 512, true).compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                CutOutActivity.this.drawView.setDrawingCacheEnabled(false);
                Intent intent = new Intent(CutOutActivity.this, StickerViewActivity.class);
                intent.putExtra("image", byteArray);
                CutOutActivity.this.startActivity(intent);
                CutOutActivity.this.finish();
            }
        });
        this.ivAddSticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                CutOutActivity cutOutActivity = CutOutActivity.this;
                cutOutActivity.startActivityForResult(intent, cutOutActivity.RESULT_LOAD_IMAGE);
            }
        });
        this.ivMoveSticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CutOutActivity.this.drawView.setAction(DrawView.DrawViewAction.ADD_STICKER);
            }
        });
        this.btnDrawText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CutOutActivity.this.drawView.setAction(DrawView.DrawViewAction.TEXT_DRAW);
            }
        });
        this.tvSaveSticker.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CutOutActivity.this.doneTask();
            }
        });
        this.btnText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CutOutActivity cutOutActivity = CutOutActivity.this;
                cutOutActivity.dialogForText(cutOutActivity);
            }
        });
    }

    /* access modifiers changed from: private */
    public void doneTask() {
        this.drawView.setDrawingCacheEnabled(true);
        Bitmap drawingCache = this.drawView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap.createScaledBitmap(drawingCache, 512, 512, true).compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.drawView.setDrawingCacheEnabled(false);
        Intent intent = new Intent(this, StickerViewActivity.class);
        intent.putExtra("image", byteArray);
        startActivity(intent);
        finish();
    }

    private void activateGestureView() {
        this.gestureView.getController().getSettings().setMaxZoom(MAX_ZOOM).setDoubleTapZoom(-1.0f).setPanEnabled(true).setZoomEnabled(true).setDoubleTapEnabled(true).setOverscrollDistance(0.0f, 0.0f).setOverzoomFactor(2.0f);
    }

    private void deactivateGestureView() {
        this.gestureView.getController().getSettings().setPanEnabled(false).setZoomEnabled(false).setDoubleTapEnabled(false);
    }

    private void initializeActionButtons() {
        Button button = (Button) findViewById(R.id.zoom_button);
        this.autoClearButton.setActivated(false);
        this.manualClearButton.setActivated(true);
        this.autoClearButton.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                CutOutActivity.lambda$initializeActionButtons$1(CutOutActivity.this, button, view);
            }
        });
        this.drawView.setAction(DrawView.DrawViewAction.MANUAL_CLEAR);
        this.manualClearButton.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                CutOutActivity.lambda$initializeActionButtons$2(CutOutActivity.this, button, view);
            }
        });
        button.setActivated(false);
        deactivateGestureView();
        button.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                CutOutActivity.lambda$initializeActionButtons$3(CutOutActivity.this, button, view);
            }
        });
    }

    public static /* synthetic */ void lambda$initializeActionButtons$1(CutOutActivity cutOutActivity, Button button, View view) {
        cutOutActivity.btnText.setActivated(false);
        cutOutActivity.drawView.setAction(DrawView.DrawViewAction.AUTO_CLEAR);
        cutOutActivity.manualClearSettingsLayout.setVisibility(View.INVISIBLE);
        cutOutActivity.autoClearButton.setActivated(true);
        button.setActivated(false);
        cutOutActivity.deactivateGestureView();
    }

    public static /* synthetic */ void lambda$initializeActionButtons$2(CutOutActivity cutOutActivity, Button button, View view) {
        try {
            cutOutActivity.drawView.setBitmap(cutOutActivity.bitmap, cutOutActivity.etText.getText().toString(), false, cutOutActivity.strTextStyle, cutOutActivity.textColor, cutOutActivity.textSize, cutOutActivity.selectedSticker);
        } catch (Exception unused) {
        }
        cutOutActivity.drawView.setAction(DrawView.DrawViewAction.MANUAL_CLEAR);
        cutOutActivity.manualClearSettingsLayout.setVisibility(View.VISIBLE);;
        cutOutActivity.autoClearButton.setActivated(false);
        button.setActivated(false);
        cutOutActivity.btnText.setActivated(false);
        cutOutActivity.deactivateGestureView();
    }

    public static /* synthetic */ void lambda$initializeActionButtons$3(CutOutActivity cutOutActivity, Button button, View view) {
        if (!button.isActivated()) {
            cutOutActivity.drawView.setAction(DrawView.DrawViewAction.ZOOM);
            cutOutActivity.manualClearSettingsLayout.setVisibility(View.INVISIBLE);
            button.setActivated(true);
            cutOutActivity.manualClearButton.setActivated(false);
            cutOutActivity.autoClearButton.setActivated(false);
            cutOutActivity.activateGestureView();
        }
    }

    private void setUndoRedo() {
        ImageView imageView = (ImageView) findViewById(R.id.undo);
        imageView.setEnabled(false);
        imageView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CutOutActivity.this.undo();
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.redo);
        imageView2.setEnabled(false);
        imageView2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CutOutActivity.this.redo();
            }
        });
        this.drawView.setButtons(imageView, imageView2, this.loadingModal);
    }

    private void setDrawViewBitmap(Uri uri) {
        try {
            Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            this.bitmap = bitmap2;
            this.drawView.setBitmap(bitmap2, "", false, this.strTextStyle, this.textColor, this.textSize, this.selectedSticker);
        } catch (IOException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void undo() {
        this.drawView.undo();
    }

    /* access modifiers changed from: private */
    public void redo() {
        this.drawView.redo();
    }

    /* access modifiers changed from: private */
    public void openColorDialog() {
        new AmbilWarnaDialog(this, this.textColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }

            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                int unused = CutOutActivity.this.textColor = i;
                CutOutActivity cutOutActivity = CutOutActivity.this;
                String unused2 = cutOutActivity.strText = cutOutActivity.etText.getText().toString();
                CutOutActivity.this.etText.setTextColor(CutOutActivity.this.textColor);
            }
        }).show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.drawView.setDrawingCacheEnabled(true);
        Bitmap drawingCache = this.drawView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap.createScaledBitmap(drawingCache, 512, 512, true).compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);

            }
        });
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.drawView.setDrawingCacheEnabled(false);
        Intent intent = new Intent(this, StickerViewActivity.class);
        intent.putExtra("image", byteArray);
        startActivity(intent);
        finish();
    }

    public void dialogForText(final Activity activity) {
        final ArrayList arrayList = new ArrayList();
        arrayList.add("Pacifico.ttf");
        arrayList.add("Pacifico.ttf");
        arrayList.add("Dreamwod.ttf");
        arrayList.add("LemonJelly.ttf");
        arrayList.add("QuiteMagical.ttf");
        arrayList.add("Raphtalia.ttf");
        arrayList.add("AutourOne-Regular.otf");
        arrayList.add("Roboto-Light.ttf");
        arrayList.add("Roboto-LightItalic.ttf");
        arrayList.add("Roboto-Medium.ttf");
        arrayList.add("Smilen.otf");
        arrayList.add("TheBrands.otf");
        arrayList.add("WhaleITried.ttf");
        arrayList.add("SourceSansPro-Bold.ttf");
        arrayList.add("SourceSansPro-Italic.ttf");
        arrayList.add("SourceSansPro-Light.ttf");
        arrayList.add("SourceSansPro-Regular.ttf");
        arrayList.add("SourceSansPro-Semibold.ttf");
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemen_2;
        dialog.requestWindowFeature(1);
        this.drawView.setAction(DrawView.DrawViewAction.MANUAL_CLEAR);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.edit_text);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spSelectTextFont);
        this.etText = (EditText) dialog.findViewById(R.id.etText);
        final ArrayList arrayList2 = new ArrayList();
        arrayList2.add("Pacifico");
        arrayList2.add("Pacifico");
        arrayList2.add("Dreamwod");
        arrayList2.add("LemonJelly");
        arrayList2.add("QuiteMagical");
        arrayList2.add("Raphtalia");
        arrayList2.add("AutourOne");
        arrayList2.add("Roboto");
        arrayList2.add("LightItalic");
        arrayList2.add("Medium");
        arrayList2.add("Smilen");
        arrayList2.add("TheBrand");
        arrayList2.add("WhalelTired");
        arrayList2.add("SourceBold");
        arrayList2.add("SourceItalic");
        arrayList2.add("SourceLight");
        arrayList2.add("SourceRegular");
        arrayList2.add("Semibold");
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), arrayList2);
        spinner.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(customAdapter, R.layout.spinnner_text, this));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                try {
                    String unused = CutOutActivity.this.strTextStyle = (String) arrayList.get(i);
                    Typeface unused2 = CutOutActivity.this.typeface = Typeface.createFromAsset(CutOutActivity.this.getAssets(), (String) arrayList.get(i));
                    CutOutActivity.this.etText.setTypeface(CutOutActivity.this.typeface);
                    CutOutActivity cutOutActivity = CutOutActivity.this;
                    Toast.makeText(cutOutActivity, StringUtils.SPACE + ((String) arrayList2.get(i)), Toast.LENGTH_SHORT).show();
                } catch (Exception unused3) {
                }
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.llColorChosser)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CutOutActivity.this.openColorDialog();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.ivTextSizeInc)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CutOutActivity cutOutActivity = CutOutActivity.this;
                int unused = cutOutActivity.textSize = cutOutActivity.textSize + 2;
                CutOutActivity.this.etText.setTextSize((float) CutOutActivity.this.textSize);
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.ivTextSizeDec)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CutOutActivity cutOutActivity = CutOutActivity.this;
                int unused = cutOutActivity.textSize = cutOutActivity.textSize - 2;
                CutOutActivity.this.etText.setTextSize((float) CutOutActivity.this.textSize);
            }
        });
        ((TextView) dialog.findViewById(R.id.tvSaveSticker)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CutOutActivity.this.etText.getText().toString().equals("")) {
                    Toast.makeText(activity, "Please enter text...", Toast.LENGTH_SHORT).show();
                    return;
                }
                CutOutActivity.this.drawView.setBitmap(CutOutActivity.this.bitmap, CutOutActivity.this.etText.getText().toString(), true, CutOutActivity.this.strTextStyle, CutOutActivity.this.textColor, CutOutActivity.this.textSize, CutOutActivity.this.selectedSticker);
                dialog.dismiss();
            }
        });
        this.etText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                CutOutActivity cutOutActivity = CutOutActivity.this;
                String unused = cutOutActivity.strText = cutOutActivity.etText.getText().toString();
                CutOutActivity.this.drawView.setBitmap(CutOutActivity.this.bitmap, CutOutActivity.this.etText.getText().toString(), true, CutOutActivity.this.strTextStyle, CutOutActivity.this.textColor, CutOutActivity.this.textSize, CutOutActivity.this.selectedSticker);
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                CutOutActivity cutOutActivity = CutOutActivity.this;
                String unused = cutOutActivity.strText = cutOutActivity.etText.getText().toString();
                CutOutActivity.this.drawView.setBitmap(CutOutActivity.this.bitmap, CutOutActivity.this.etText.getText().toString(), true, CutOutActivity.this.strTextStyle, CutOutActivity.this.textColor, CutOutActivity.this.textSize, CutOutActivity.this.selectedSticker);
            }
        });
        dialog.show();
    }

    public Uri getImageUri(Context context, Bitmap bitmap2) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap2.setHasAlpha(true);
        bitmap2.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap2, "Title", (String) null));
    }
}
