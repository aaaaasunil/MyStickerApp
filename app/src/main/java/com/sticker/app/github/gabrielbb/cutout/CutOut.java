package com.sticker.app.github.gabrielbb.cutout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CutOut {
    public static final short CUTOUT_ACTIVITY_REQUEST_CODE = 368;
    public static final short CUTOUT_ACTIVITY_RESULT_ERROR_CODE = 3680;
    static final String CUTOUT_EXTRA_BORDER_COLOR = "CUTOUT_EXTRA_BORDER_COLOR";
    static final String CUTOUT_EXTRA_CROP = "CUTOUT_EXTRA_CROP";
    static final String CUTOUT_EXTRA_INTRO = "CUTOUT_EXTRA_INTRO";
    static final String CUTOUT_EXTRA_RESULT = "CUTOUT_EXTRA_RESULT";
    static final String CUTOUT_EXTRA_SOURCE = "CUTOUT_EXTRA_SOURCE";
    static Bitmap staticBitmap;

    public static ActivityBuilder activity() {
        return new ActivityBuilder();
    }

    public static final class ActivityBuilder {
        private int borderColor;
        private boolean bordered;
        private boolean crop;
        private boolean intro;
        @Nullable
        private Uri source;

        private ActivityBuilder() {
            this.crop = true;
            this.borderColor = -1;
        }

        private Intent getIntent(@NonNull Context context) {
            Intent intent = new Intent();
            intent.setClass(context, StickerViewActivity.class);
            Uri uri = this.source;
            if (uri != null) {
                intent.putExtra(CutOut.CUTOUT_EXTRA_SOURCE, uri);
            }
            if (this.bordered) {
                intent.putExtra(CutOut.CUTOUT_EXTRA_BORDER_COLOR, this.borderColor);
            }
            if (this.crop) {
                intent.putExtra(CutOut.CUTOUT_EXTRA_CROP, true);
            }
            if (this.intro) {
                intent.putExtra(CutOut.CUTOUT_EXTRA_INTRO, true);
            }
            return intent;
        }

        public ActivityBuilder src(Uri uri) {
            this.source = uri;
            return this;
        }

        public ActivityBuilder bordered() {
            this.bordered = true;
            return this;
        }

        public ActivityBuilder bordered(int i) {
            this.borderColor = i;
            return bordered();
        }

        public ActivityBuilder noCrop() {
            this.crop = false;
            return this;
        }

        public ActivityBuilder intro() {
            this.intro = true;
            return this;
        }

        public void start(@NonNull Activity activity) {
            activity.startActivityForResult(getIntent(activity), 368);
        }
    }

    public static Uri getUri(@Nullable Intent intent) {
        if (intent != null) {
            return (Uri) intent.getParcelableExtra(CUTOUT_EXTRA_RESULT);
        }
        return null;
    }

    public static Exception getError(@Nullable Intent intent) {
        if (intent != null) {
            return (Exception) intent.getSerializableExtra(CUTOUT_EXTRA_RESULT);
        }
        return null;
    }
}
