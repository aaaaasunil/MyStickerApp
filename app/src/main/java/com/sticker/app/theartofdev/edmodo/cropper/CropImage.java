package com.sticker.app.theartofdev.edmodo.cropper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.sticker.app.R;
import com.sticker.app.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CropImage {
    public static final int CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE = 2011;
    public static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203;
    public static final int CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE = 204;
    public static final String CROP_IMAGE_EXTRA_BUNDLE = "CROP_IMAGE_EXTRA_BUNDLE";
    public static final String CROP_IMAGE_EXTRA_OPTIONS = "CROP_IMAGE_EXTRA_OPTIONS";
    public static final String CROP_IMAGE_EXTRA_RESULT = "CROP_IMAGE_EXTRA_RESULT";
    public static final String CROP_IMAGE_EXTRA_SOURCE = "CROP_IMAGE_EXTRA_SOURCE";
    public static final int PICK_IMAGE_CHOOSER_REQUEST_CODE = 200;
    public static final int PICK_IMAGE_PERMISSIONS_REQUEST_CODE = 201;

    private CropImage() {
    }

    public static Bitmap toOvalBitmap(@NonNull Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawOval(new RectF(0.0f, 0.0f, (float) width, (float) height), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        bitmap.recycle();
        return createBitmap;
    }

    public static void startPickImageActivity(@NonNull Activity activity) {
        activity.startActivityForResult(getPickImageChooserIntent(activity), 200);
    }

    public static void startPickImageActivity(@NonNull Context context, @NonNull Fragment fragment) {
        fragment.startActivityForResult(getPickImageChooserIntent(context), 200);
    }

    public static Intent getPickImageChooserIntent(@NonNull Context context) {
        return getPickImageChooserIntent(context, context.getString(R.string.pick_image_intent_chooser_title), false, true);
    }

    public static Intent getPickImageChooserIntent(@NonNull Context context, CharSequence charSequence, boolean z, boolean z2) {
        Intent intent;
        ArrayList arrayList = new ArrayList();
        PackageManager packageManager = context.getPackageManager();
        if (!isExplicitCameraPermissionRequired(context) && z2) {
            arrayList.addAll(getCameraIntents(context, packageManager));
        }
        List<Intent> galleryIntents = getGalleryIntents(packageManager, "android.intent.action.GET_CONTENT", z);
        if (galleryIntents.size() == 0) {
            galleryIntents = getGalleryIntents(packageManager, "android.intent.action.PICK", z);
        }
        arrayList.addAll(galleryIntents);
        if (arrayList.isEmpty()) {
            intent = new Intent();
        } else {
            intent = (Intent) arrayList.get(arrayList.size() - 1);
            arrayList.remove(arrayList.size() - 1);
        }
        Intent createChooser = Intent.createChooser(intent, charSequence);
        createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList.toArray(new Parcelable[arrayList.size()]));
        return createChooser;
    }

    public static Intent getCameraIntent(@NonNull Context context, Uri uri) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (uri == null) {
            uri = getCaptureImageOutputUri(context);
        }
        intent.putExtra("output", uri);
        return intent;
    }

    public static List<Intent> getCameraIntents(@NonNull Context context, @NonNull PackageManager packageManager) {
        ArrayList arrayList = new ArrayList();
        Uri captureImageOutputUri = getCaptureImageOutputUri(context);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        for (ResolveInfo next : packageManager.queryIntentActivities(intent, 0)) {
            Intent intent2 = new Intent(intent);
            intent2.setComponent(new ComponentName(next.activityInfo.packageName, next.activityInfo.name));
            intent2.setPackage(next.activityInfo.packageName);
            if (captureImageOutputUri != null) {
                intent2.putExtra("output", captureImageOutputUri);
            }
            arrayList.add(intent2);
        }
        return arrayList;
    }

    public static List<Intent> getGalleryIntents(@NonNull PackageManager packageManager, String str, boolean z) {
        ArrayList arrayList = new ArrayList();
        Intent intent = str == "android.intent.action.GET_CONTENT" ? new Intent(str) : new Intent(str, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        for (ResolveInfo next : packageManager.queryIntentActivities(intent, 0)) {
            Intent intent2 = new Intent(intent);
            intent2.setComponent(new ComponentName(next.activityInfo.packageName, next.activityInfo.name));
            intent2.setPackage(next.activityInfo.packageName);
            arrayList.add(intent2);
        }
        if (!z) {
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Intent intent3 = (Intent) it.next();
                if (intent3.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                    arrayList.remove(intent3);
                    break;
                }
            }
        }
        return arrayList;
    }

    public static boolean isExplicitCameraPermissionRequired(@NonNull Context context) {
        return Build.VERSION.SDK_INT >= 23 && hasPermissionInManifest(context, "android.permission.CAMERA") && context.checkSelfPermission("android.permission.CAMERA") != 0;
    }

    public static boolean hasPermissionInManifest(@NonNull Context context, @NonNull String str) {
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr != null && strArr.length > 0) {
                for (String equalsIgnoreCase : strArr) {
                    if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return false;
    }

    public static Uri getCaptureImageOutputUri(@NonNull Context context) {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            return Uri.fromFile(new File(externalCacheDir.getPath(), "pickImageResult.jpeg"));
        }
        return null;
    }

    public static Uri getPickImageResultUri(@NonNull Context context, @Nullable Intent intent) {
        String action;
        boolean z = true;
        if (!(intent == null || intent.getData() == null || ((action = intent.getAction()) != null && action.equals("android.media.action.IMAGE_CAPTURE")))) {
            z = false;
        }
        return (z || intent.getData() == null) ? getCaptureImageOutputUri(context) : intent.getData();
    }

    public static boolean isReadExternalStoragePermissionsRequired(@NonNull Context context, @NonNull Uri uri) {
        return Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0 && isUriRequiresPermissions(context, uri);
    }

    public static boolean isUriRequiresPermissions(@NonNull Context context, @NonNull Uri uri) {
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            if (openInputStream == null) {
                return false;
            }
            openInputStream.close();
            return false;
        } catch (Exception unused) {
            return true;
        }
    }

    public static ActivityBuilder activity() {
        return new ActivityBuilder((Uri) null);
    }

    public static ActivityBuilder activity(@Nullable Uri uri) {
        return new ActivityBuilder(uri);
    }

    public static ActivityResult getActivityResult(@Nullable Intent intent) {
        if (intent != null) {
            return (ActivityResult) intent.getParcelableExtra(CROP_IMAGE_EXTRA_RESULT);
        }
        return null;
    }

    public static final class ActivityBuilder {
        private final CropImageOptions mOptions;
        @Nullable
        private final Uri mSource;

        private ActivityBuilder(@Nullable Uri uri) {
            this.mSource = uri;
            this.mOptions = new CropImageOptions();
        }

        public Intent getIntent(@NonNull Context context) {
            return getIntent(context, CropImageActivity.class);
        }

        public Intent getIntent(@NonNull Context context, @Nullable Class<?> cls) {
            this.mOptions.validate();
            Intent intent = new Intent();
            intent.setClass(context, cls);
            Bundle bundle = new Bundle();
            bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, this.mSource);
            bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, this.mOptions);
            intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundle);
            return intent;
        }

        public void start(@NonNull Activity activity) {
            this.mOptions.validate();
            activity.startActivityForResult(getIntent(activity), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        public void start(@NonNull Activity activity, @Nullable Class<?> cls) {
            this.mOptions.validate();
            activity.startActivityForResult(getIntent(activity, cls), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment) {
            fragment.startActivityForResult(getIntent(context), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        @RequiresApi(api = 11)
        public void start(@NonNull Context context, @NonNull android.app.Fragment fragment) {
            fragment.startActivityForResult(getIntent(context), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment, @Nullable Class<?> cls) {
            fragment.startActivityForResult(getIntent(context, cls), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        @RequiresApi(api = 11)
        public void start(@NonNull Context context, @NonNull android.app.Fragment fragment, @Nullable Class<?> cls) {
            fragment.startActivityForResult(getIntent(context, cls), CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        public ActivityBuilder setCropShape(@NonNull CropImageView.CropShape cropShape) {
            this.mOptions.cropShape = cropShape;
            return this;
        }

        public ActivityBuilder setSnapRadius(float f) {
            this.mOptions.snapRadius = f;
            return this;
        }

        public ActivityBuilder setTouchRadius(float f) {
            this.mOptions.touchRadius = f;
            return this;
        }

        public ActivityBuilder setGuidelines(@NonNull CropImageView.Guidelines guidelines) {
            this.mOptions.guidelines = guidelines;
            return this;
        }

        public ActivityBuilder setScaleType(@NonNull CropImageView.ScaleType scaleType) {
            this.mOptions.scaleType = scaleType;
            return this;
        }

        public ActivityBuilder setShowCropOverlay(boolean z) {
            this.mOptions.showCropOverlay = z;
            return this;
        }

        public ActivityBuilder setAutoZoomEnabled(boolean z) {
            this.mOptions.autoZoomEnabled = z;
            return this;
        }

        public ActivityBuilder setMultiTouchEnabled(boolean z) {
            this.mOptions.multiTouchEnabled = z;
            return this;
        }

        public ActivityBuilder setMaxZoom(int i) {
            this.mOptions.maxZoom = i;
            return this;
        }

        public ActivityBuilder setInitialCropWindowPaddingRatio(float f) {
            this.mOptions.initialCropWindowPaddingRatio = f;
            return this;
        }

        public ActivityBuilder setFixAspectRatio(boolean z) {
            this.mOptions.fixAspectRatio = z;
            return this;
        }

        public ActivityBuilder setAspectRatio(int i, int i2) {
            CropImageOptions cropImageOptions = this.mOptions;
            cropImageOptions.aspectRatioX = i;
            cropImageOptions.aspectRatioY = i2;
            cropImageOptions.fixAspectRatio = true;
            return this;
        }

        public ActivityBuilder setBorderLineThickness(float f) {
            this.mOptions.borderLineThickness = f;
            return this;
        }

        public ActivityBuilder setBorderLineColor(int i) {
            this.mOptions.borderLineColor = i;
            return this;
        }

        public ActivityBuilder setBorderCornerThickness(float f) {
            this.mOptions.borderCornerThickness = f;
            return this;
        }

        public ActivityBuilder setBorderCornerOffset(float f) {
            this.mOptions.borderCornerOffset = f;
            return this;
        }

        public ActivityBuilder setBorderCornerLength(float f) {
            this.mOptions.borderCornerLength = f;
            return this;
        }

        public ActivityBuilder setBorderCornerColor(int i) {
            this.mOptions.borderCornerColor = i;
            return this;
        }

        public ActivityBuilder setGuidelinesThickness(float f) {
            this.mOptions.guidelinesThickness = f;
            return this;
        }

        public ActivityBuilder setGuidelinesColor(int i) {
            this.mOptions.guidelinesColor = i;
            return this;
        }

        public ActivityBuilder setBackgroundColor(int i) {
            this.mOptions.backgroundColor = i;
            return this;
        }

        public ActivityBuilder setMinCropWindowSize(int i, int i2) {
            CropImageOptions cropImageOptions = this.mOptions;
            cropImageOptions.minCropWindowWidth = i;
            cropImageOptions.minCropWindowHeight = i2;
            return this;
        }

        public ActivityBuilder setMinCropResultSize(int i, int i2) {
            CropImageOptions cropImageOptions = this.mOptions;
            cropImageOptions.minCropResultWidth = i;
            cropImageOptions.minCropResultHeight = i2;
            return this;
        }

        public ActivityBuilder setMaxCropResultSize(int i, int i2) {
            CropImageOptions cropImageOptions = this.mOptions;
            cropImageOptions.maxCropResultWidth = i;
            cropImageOptions.maxCropResultHeight = i2;
            return this;
        }

        public ActivityBuilder setActivityTitle(CharSequence charSequence) {
            this.mOptions.activityTitle = charSequence;
            return this;
        }

        public ActivityBuilder setActivityMenuIconColor(int i) {
            this.mOptions.activityMenuIconColor = i;
            return this;
        }

        public ActivityBuilder setOutputUri(Uri uri) {
            this.mOptions.outputUri = uri;
            return this;
        }

        public ActivityBuilder setOutputCompressFormat(Bitmap.CompressFormat compressFormat) {
            this.mOptions.outputCompressFormat = compressFormat;
            return this;
        }

        public ActivityBuilder setOutputCompressQuality(int i) {
            this.mOptions.outputCompressQuality = i;
            return this;
        }

        public ActivityBuilder setRequestedSize(int i, int i2) {
            return setRequestedSize(i, i2, CropImageView.RequestSizeOptions.RESIZE_INSIDE);
        }

        public ActivityBuilder setRequestedSize(int i, int i2, CropImageView.RequestSizeOptions requestSizeOptions) {
            CropImageOptions cropImageOptions = this.mOptions;
            cropImageOptions.outputRequestWidth = i;
            cropImageOptions.outputRequestHeight = i2;
            cropImageOptions.outputRequestSizeOptions = requestSizeOptions;
            return this;
        }

        public ActivityBuilder setNoOutputImage(boolean z) {
            this.mOptions.noOutputImage = z;
            return this;
        }

        public ActivityBuilder setInitialCropWindowRectangle(Rect rect) {
            this.mOptions.initialCropWindowRectangle = rect;
            return this;
        }

        public ActivityBuilder setInitialRotation(int i) {
            this.mOptions.initialRotation = (i + 360) % 360;
            return this;
        }

        public ActivityBuilder setAllowRotation(boolean z) {
            this.mOptions.allowRotation = z;
            return this;
        }

        public ActivityBuilder setAllowFlipping(boolean z) {
            this.mOptions.allowFlipping = z;
            return this;
        }

        public ActivityBuilder setAllowCounterRotation(boolean z) {
            this.mOptions.allowCounterRotation = z;
            return this;
        }

        public ActivityBuilder setRotationDegrees(int i) {
            this.mOptions.rotationDegrees = (i + 360) % 360;
            return this;
        }

        public ActivityBuilder setFlipHorizontally(boolean z) {
            this.mOptions.flipHorizontally = z;
            return this;
        }

        public ActivityBuilder setFlipVertically(boolean z) {
            this.mOptions.flipVertically = z;
            return this;
        }

        public ActivityBuilder setCropMenuCropButtonTitle(CharSequence charSequence) {
            this.mOptions.cropMenuCropButtonTitle = charSequence;
            return this;
        }

        public ActivityBuilder setCropMenuCropButtonIcon(@DrawableRes int i) {
            this.mOptions.cropMenuCropButtonIcon = i;
            return this;
        }
    }

    public static final class ActivityResult extends CropImageView.CropResult implements Parcelable {
        public static final Creator<ActivityResult> CREATOR = new Creator<ActivityResult>() {
            public ActivityResult createFromParcel(Parcel parcel) {
                return new ActivityResult(parcel);
            }

            public ActivityResult[] newArray(int i) {
                return new ActivityResult[i];
            }
        };

        public int describeContents() {
            return 0;
        }

        public ActivityResult(Uri uri, Uri uri2, Exception exc, float[] fArr, Rect rect, int i, Rect rect2, int i2) {
            super((Bitmap) null, uri, (Bitmap) null, uri2, exc, fArr, rect, rect2, i, i2);
        }

        protected ActivityResult(Parcel parcel) {
            super((Bitmap) null, (Uri) parcel.readParcelable(Uri.class.getClassLoader()), (Bitmap) null, (Uri) parcel.readParcelable(Uri.class.getClassLoader()), (Exception) parcel.readSerializable(), parcel.createFloatArray(), (Rect) parcel.readParcelable(Rect.class.getClassLoader()), (Rect) parcel.readParcelable(Rect.class.getClassLoader()), parcel.readInt(), parcel.readInt());
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(getOriginalUri(), i);
            parcel.writeParcelable(getUri(), i);
            parcel.writeSerializable(getError());
            parcel.writeFloatArray(getCropPoints());
            parcel.writeParcelable(getCropRect(), i);
            parcel.writeParcelable(getWholeImageRect(), i);
            parcel.writeInt(getRotation());
            parcel.writeInt(getSampleSize());
        }
    }
}
