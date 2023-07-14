package com.sticker.app.github.gabrielbb.cutout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sticker.app.R;
import com.sticker.app.github.gabrielbb.cutout.utils.DynamicUnitUtils;
import com.sticker.app.github.gabrielbb.cutout.utils.NothingSelectedSpinnerAdapter;

import com.sticker.app.theartofdev.edmodo.cropper.CropImage;
import com.sticker.app.theartofdev.edmodo.cropper.CropImageView;
import com.sticker.app.yuku.ambilwarna.AmbilWarnaDialog;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import uk.co.senab.photoview.PhotoView;


public class StickerViewActivity extends AppCompatActivity {
    private static final short BORDER_SIZE = 45;
    private static final int CAMERA_REQUEST_CODE = 3;
    private static final int IMAGE_CHOOSER_REQUEST_CODE = 2;
    private static final int INTRO_REQUEST_CODE = 4;
    private static final String INTRO_SHOWN = "INTRO_SHOWN";
    public static final int PERM_RQST_CODE = 110;
    /* access modifiers changed from: private */
    public static final String TAG = "StickerViewActivity";
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private int RESULT_LOAD_IMAGE = 100;
    private ArrayList<Drawable> al_stickerImages = new ArrayList<>();
    /* access modifiers changed from: private */
    public EditText etText;
    private Uri imageUri;
    private ImageView ivAutoEraseBg;
    private ImageView ivBack;
    private ImageView ivDecoration;
    private ImageView ivEmojis;
    private ImageView ivEraseBg;
    private ImageView ivText;
    private LinearLayout llBottom;
    /* access modifiers changed from: private */
    public LinearLayout llLayout;
    private ProgressBar loadingView;
    private PhotoView pvPhotoView;
    private TextSticker sticker;
    /* access modifiers changed from: private */
    public StickerView stickerView;
    /* access modifiers changed from: private */
    public int textColor = -1;
    /* access modifiers changed from: private */
    public int textSize = 50;
    private TextView tvSaveSticker;
    private TextView tvText;
    /* access modifiers changed from: private */
    public Typeface typeface;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_sticker_view);
        initView();
    }

    private void initView() {
        this.llLayout = (LinearLayout) findViewById(R.id.llLayout);
        this.ivEraseBg = (ImageView) findViewById(R.id.ivEraseBg);
        this.ivAutoEraseBg = (ImageView) findViewById(R.id.ivAutoEraseBg);
        this.ivText = (ImageView) findViewById(R.id.ivText);
        this.stickerView = (StickerView) findViewById(R.id.sticker_view);
        this.pvPhotoView = (PhotoView) findViewById(R.id.pvPhotoView);
        this.tvSaveSticker = (TextView) findViewById(R.id.tvSaveSticker);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.tvText = (TextView) findViewById(R.id.tvText);
        this.ivEmojis = (ImageView) findViewById(R.id.ivEmojis);
        this.ivDecoration = (ImageView) findViewById(R.id.ivDecoration);
        this.loadingView = (ProgressBar) findViewById(R.id.loadingView);
        this.tvText.setText(Html.fromHtml("&#128104;&#8205;&#128105;&#8205;&#128103;&#8205;&#128102;"));
        this.llLayout.setDrawingCacheEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            start();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 110);
        }
        btnClick();
    }

    private void btnClick() {
        this.stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(StickerViewActivity.TAG, "onStickerAdded");
            }

            public void onStickerClicked(@NonNull Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    ((TextSticker) sticker).setTextColor(getResources().getColor(R.color.black));
                    StickerViewActivity.this.stickerView.replace(sticker);
                    StickerViewActivity.this.stickerView.invalidate();
                }
                Log.d(StickerViewActivity.TAG, "onStickerClicked");
            }

            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(StickerViewActivity.TAG, "onStickerDeleted");
            }

            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(StickerViewActivity.TAG, "onStickerDragFinished");
            }

            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d(StickerViewActivity.TAG, "onStickerTouchedDown");
            }

            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(StickerViewActivity.TAG, "onStickerZoomFinished");
            }

            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(StickerViewActivity.TAG, "onStickerFlipped");
            }

            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(StickerViewActivity.TAG, "onDoubleTapped: double tap will be with two click");
            }
        });
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity.this.finish();
            }
        });
        this.ivEraseBg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity.this.stickerView.setLocked(!StickerViewActivity.this.stickerView.isLocked());
                StickerViewActivity.this.llLayout.setDrawingCacheEnabled(true);
                Bitmap drawingCache = StickerViewActivity.this.llLayout.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap.createScaledBitmap(drawingCache, 500, 500, true).compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
                StickerViewActivity.this.llLayout.getDrawingCache(false);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                Intent intent = new Intent(StickerViewActivity.this, EraseOffsetActivity.class);
                intent.putExtra("image", byteArray);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StickerViewActivity.this.startActivity(intent);
                StickerViewActivity.this.finish();
            }
        });
        this.ivAutoEraseBg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity.this.stickerView.setLocked(!StickerViewActivity.this.stickerView.isLocked());
                StickerViewActivity.this.llLayout.setDrawingCacheEnabled(true);
                Bitmap drawingCache = StickerViewActivity.this.llLayout.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap.createScaledBitmap(drawingCache, 500, 500, true).compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
                StickerViewActivity.this.llLayout.getDrawingCache(false);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                StickerViewActivity.this.llLayout.setDrawingCacheEnabled(false);
                Intent intent = new Intent(StickerViewActivity.this, CutOutActivity.class);
                intent.putExtra("image", byteArray);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StickerViewActivity.this.startActivity(intent);
                StickerViewActivity.this.finish();
            }
        });
        this.ivText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity stickerViewActivity = StickerViewActivity.this;
                stickerViewActivity.dialogForText(stickerViewActivity);
            }
        });
        this.tvSaveSticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity.this.startSaveDrawingTask();
            }
        });
        this.ivDecoration.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity stickerViewActivity = StickerViewActivity.this;
                stickerViewActivity.dialogForDecoration(stickerViewActivity);
            }
        });
        this.ivEmojis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity stickerViewActivity = StickerViewActivity.this;
                try {
                    stickerViewActivity.dialogForStickerAdd(stickerViewActivity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (iArr.length <= 0 || iArr[0] != 0) {
            setResult(0);
            finish();
            return;
        }
        start();
    }

    public void testReplace(View view) {
        if (this.stickerView.replace(this.sticker)) {
            Toast.makeText(this, "Replace Sticker successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Replace Sticker failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void testLock(View view) {
        StickerView stickerView2 = this.stickerView;
        stickerView2.setLocked(!stickerView2.isLocked());
    }

    public void testRemove(View view) {
        if (this.stickerView.removeCurrentSticker()) {
            Toast.makeText(this, "Remove current Sticker successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Remove current Sticker failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void testRemoveAll(View view) {
        this.stickerView.removeAllStickers();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        try {
            if (SaveDrawingTask.SAVED_IMAGE_URi.equals("png")) {
                byte[] byteArrayExtra = getIntent().getByteArrayExtra("image");
                this.pvPhotoView.setImageBitmap(BitmapFactory.decodeByteArray(byteArrayExtra, 0, byteArrayExtra.length));
            } else {
                finish();
            }
        } catch (Exception unused) {
        }
        super.onResume();
    }

    public void setImage(Uri uri) {
        try {
            this.imageUri = uri;
            this.pvPhotoView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
        } catch (IOException e) {
            exitWithError(e);
        }
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_sticker_m), 0);
        bitmapStickerIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_sticker_m), 3);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon3 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_sticker_m), 1);
        bitmapStickerIcon3.setIconEvent(new FlipHorizontallyEvent());
        this.stickerView.setIcons(Arrays.asList(new BitmapStickerIcon[]{bitmapStickerIcon, bitmapStickerIcon2, bitmapStickerIcon3}));
        this.stickerView.setBackgroundColor(-1);
        this.stickerView.setLocked(false);
        this.stickerView.setConstrained(true);
        this.sticker = new TextSticker(this);
        this.sticker.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sticker_transparent_background));
        this.sticker.setText("Hello, world!");
        this.sticker.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        this.sticker.resizeText();
    }

    /* access modifiers changed from: private */
    public void startSaveDrawingTask() {
        StickerView stickerView2 = this.stickerView;
        stickerView2.setLocked(!stickerView2.isLocked());
        this.llLayout.setDrawingCacheEnabled(true);
        SaveDrawingTask saveDrawingTask = new SaveDrawingTask(this);
        int intExtra = getIntent().getIntExtra("CUTOUT_EXTRA_BORDER_COLOR", -1);
        if (intExtra != -1) {
            saveDrawingTask.execute(new Bitmap[]{BitmapUtility.getBorderedBitmap(this.llLayout.getDrawingCache(), intExtra, 45)});
            return;
        }
        this.loadingView.setVisibility(View.VISIBLE);
        this.llLayout.setDrawingCacheEnabled(true);
        this.llLayout.measure(View.MeasureSpec.makeMeasureSpec(DynamicUnitUtils.convertDpToPixels(512.0f), View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(DynamicUnitUtils.convertDpToPixels(512.0f), View.MeasureSpec.UNSPECIFIED));
        saveDrawingTask.execute(new Bitmap[]{this.llLayout.getDrawingCache()});
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 203) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
            if (i2 == -1) {
                setImage(activityResult.getUri());
            } else if (i2 == 204) {
                exitWithError(activityResult.getError());
            } else {
                setResult(0);
                finish();
            }
        } else if (i == this.RESULT_LOAD_IMAGE && i2 == -1 && intent != null) {
            Uri data = intent.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getContentResolver().query(data, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
            LinearLayout linearLayout = this.llLayout;
            linearLayout.setBackgroundDrawable(new BitmapDrawable(linearLayout.getResources(), bitmap));
        } else {
//            EasyImage.handleActivityResult(i, i2, intent, this, new DefaultCallback() {
//                @Override
//                public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
//                   // onPhotosReturned(imageFiles);
//                }
//
//                public void onImagePickerError(Exception exc, MediaSource imageSource, int i) {
//                    StickerViewActivity.this.exitWithError(exc);
//                }
//
//                public void onCanceled(@NonNull MediaSource source) {
//                    File lastlyTakenButCanceledPhoto;
////                    if (source == EasyImage. && (lastlyTakenButCanceledPhoto = EasyImage.lastlyTakenButCanceledPhoto(StickerViewActivity.this)) != null) {
////                        lastlyTakenButCanceledPhoto.delete();
////                    }
//                    StickerViewActivity.this.setResult(0);
//                    StickerViewActivity.this.finish();
//                }
//            });

        }
    }

    /* access modifiers changed from: package-private */
    public void exitWithError(Exception exc) {
        Intent intent = new Intent();
        intent.putExtra("CUTOUT_EXTRA_RESULT", exc);
        setResult(3680, intent);
        finish();
    }

    private void start() {
        CropImage.ActivityBuilder activityBuilder;
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            Uri extraSource = getExtraSource();
            if (getIntent().getBooleanExtra("CUTOUT_EXTRA_CROP", false)) {
                if (extraSource != null) {
                    activityBuilder = CropImage.activity(extraSource);
                } else if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0) {
                    activityBuilder = CropImage.activity();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 3);
                    return;
                }
                activityBuilder.setGuidelines(CropImageView.Guidelines.ON).start(this);
                return;
            }
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
    }

    private Uri getExtraSource() {
        if (getIntent().hasExtra("CUTOUT_EXTRA_SOURCE")) {
            return (Uri) getIntent().getParcelableExtra("CUTOUT_EXTRA_SOURCE");
        }
        return null;
    }

    public void dialogForDecoration(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_decoration);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rvView);
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(R.drawable.dec_one));
        arrayList.add(Integer.valueOf(R.drawable.dec_two));
        arrayList.add(Integer.valueOf(R.drawable.dec_three));
        arrayList.add(Integer.valueOf(R.drawable.dec_four));
        arrayList.add(Integer.valueOf(R.drawable.dec_five));
        arrayList.add(Integer.valueOf(R.drawable.dec_six));
        arrayList.add(Integer.valueOf(R.drawable.dec_seven));
        arrayList.add(Integer.valueOf(R.drawable.dec_eight));
        arrayList.add(Integer.valueOf(R.drawable.dec_nine));
        arrayList.add(Integer.valueOf(R.drawable.dec_ten));
        arrayList.add(Integer.valueOf(R.drawable.dec_eleven));
        arrayList.add(Integer.valueOf(R.drawable.dec_twelve));
        arrayList.add(Integer.valueOf(R.drawable.dec_thirteen));
        arrayList.add(Integer.valueOf(R.drawable.dec_fourteen));
        arrayList.add(Integer.valueOf(R.drawable.dec_fifteen));
        arrayList.add(Integer.valueOf(R.drawable.dec_sixteen));
        arrayList.add(Integer.valueOf(R.drawable.dec_seventeen));
        arrayList.add(Integer.valueOf(R.drawable.dec_eighteen));
        arrayList.add(Integer.valueOf(R.drawable.dec_nineteen));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_one));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_two));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_three));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_four));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_five));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_six));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_seven));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_eight));
        arrayList.add(Integer.valueOf(R.drawable.dec_twenty_nine));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        AdapterDecoration adapterDecoration = new AdapterDecoration(activity, arrayList, new AdapterDecoration.interfaceClick() {
            public void image(int i) {
                Drawable drawable = ContextCompat.getDrawable(activity, i);
                dialog.dismiss();
                StickerViewActivity.this.stickerView.addSticker(new DrawableSticker(drawable));
            }
        });
        recyclerView.setAdapter(adapterDecoration);
        adapterDecoration.notifyDataSetChanged();
        dialog.show();
    }

    public void dialogForStickerAdd(final Activity activity) throws IOException {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_decoration);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rvView);
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        InputStream openRawResource = getResources().openRawResource(R.raw.emoji_list);
        StringWriter stringWriter = new StringWriter();
        char[] cArr = new char[1024];
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openRawResource, "UTF-8"));
            while (true) {
                int read = bufferedReader.read(cArr);
                if (read != -1) {
                    stringWriter.write(cArr, 0, read);
                } else {
                    break;
                }
            }
            openRawResource.close();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            openRawResource.close();
        } catch (IOException e3) {
            e3.printStackTrace();
            openRawResource.close();
        } catch (Throwable th) {
            try {
                openRawResource.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
        String obj = stringWriter.toString();
        Log.e(">>>", String.valueOf(obj));
        try {
            JSONArray jSONArray = new JSONObject(obj).getJSONArray("Smileys & People");
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.getJSONObject(i).getString("emoji"));
            }
        } catch (JSONException e5) {
            e5.printStackTrace();
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        AdapertSticker adapertSticker = new AdapertSticker(activity, arrayList, new AdapertSticker.interfaceClick() {
            public void image(String str) {
                dialog.dismiss();
                TextSticker textSticker = new TextSticker(activity);
                textSticker.setText(str);
                StickerViewActivity.this.stickerView.setMinimumWidth(50);
                StickerViewActivity.this.stickerView.setMinimumHeight(50);
                textSticker.resizeText();
                StickerViewActivity.this.stickerView.addSticker(textSticker);
            }
        });
        recyclerView.setAdapter(adapertSticker);
        adapertSticker.notifyDataSetChanged();
        dialog.show();
    }

    public void onBackPressed() {
        super.onBackPressed();
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
        dialog.requestWindowFeature(1);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemen_2;
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
                    String str = (String) arrayList2.get(i);
                    Typeface unused = StickerViewActivity.this.typeface = Typeface.createFromAsset(StickerViewActivity.this.getAssets(), (String) arrayList.get(i));
                    StickerViewActivity.this.etText.setTypeface(StickerViewActivity.this.typeface);
                } catch (Exception unused2) {
                }
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.llColorChosser)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity.this.openColorDialog();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.ivTextSizeInc)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity stickerViewActivity = StickerViewActivity.this;
                int unused = stickerViewActivity.textSize = stickerViewActivity.textSize + 2;
                StickerViewActivity.this.etText.setTextSize((float) StickerViewActivity.this.textSize);
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.ivTextSizeDec)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerViewActivity stickerViewActivity = StickerViewActivity.this;
                int unused = stickerViewActivity.textSize = stickerViewActivity.textSize - 2;
                StickerViewActivity.this.etText.setTextSize((float) StickerViewActivity.this.textSize);
            }
        });
        ((TextView) dialog.findViewById(R.id.tvSaveSticker)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StickerViewActivity.this.etText.getText().toString().equals("")) {
                    Toast.makeText(activity, "Please enter text...", Toast.LENGTH_SHORT).show();
                    return;
                }
                final TextSticker textSticker = new TextSticker(activity);
                textSticker.setText(StickerViewActivity.this.etText.getText().toString());
                textSticker.setTextColor(StickerViewActivity.this.textColor);
                textSticker.setTypeface(StickerViewActivity.this.typeface);
                textSticker.setMaxTextSize((float) StickerViewActivity.this.textSize);
                textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                textSticker.resizeText();
                StickerViewActivity.this.stickerView.addSticker(textSticker);
                dialog.dismiss();
                StickerViewActivity.this.stickerView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (!textSticker.getText().equals("")) {
                            Toast.makeText(StickerViewActivity.this, textSticker.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void openColorDialog() {
        new AmbilWarnaDialog(this, this.textColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }

            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                int unused = StickerViewActivity.this.textColor = i;
                StickerViewActivity.this.etText.setTextColor(StickerViewActivity.this.textColor);
            }
        }).show();
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", (String) null));
    }
}
