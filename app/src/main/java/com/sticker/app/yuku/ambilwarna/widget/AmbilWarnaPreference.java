package com.sticker.app.yuku.ambilwarna.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.R;

public class AmbilWarnaPreference extends Preference {
    private final boolean supportsAlpha;
    int value;

    public AmbilWarnaPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.supportsAlpha = context.obtainStyledAttributes(attributeSet, R.styleable.AmbilWarnaPreference).getBoolean(R.styleable.AmbilWarnaPreference_supportsAlpha, false);
        setWidgetLayoutResource(R.layout.ambilwarna_pref_widget);
    }

    /* access modifiers changed from: protected */
    public void onBindView(View view) {
        super.onBindView(view);
        View findViewById = view.findViewById(R.id.ambilwarna_pref_widget_box);
        if (findViewById != null) {
            findViewById.setBackgroundColor(this.value);
        }
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        new AmbilWarnaDialog(getContext(), this.value, this.supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }

            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
                if (AmbilWarnaPreference.this.callChangeListener(Integer.valueOf(i))) {
                    AmbilWarnaPreference ambilWarnaPreference = AmbilWarnaPreference.this;
                    ambilWarnaPreference.value = i;
                    boolean unused = ambilWarnaPreference.persistInt(ambilWarnaPreference.value);
                    AmbilWarnaPreference.this.notifyChanged();
                }
            }
        }).show();
    }

    public void forceSetValue(int i) {
        this.value = i;
        persistInt(i);
        notifyChanged();
    }

    /* access modifiers changed from: protected */
    public Object onGetDefaultValue(TypedArray typedArray, int i) {
        return Integer.valueOf(typedArray.getInteger(i, 0));
    }

    /* access modifiers changed from: protected */
    public void onSetInitialValue(boolean z, Object obj) {
        if (z) {
            this.value = getPersistedInt(this.value);
            return;
        }
        int intValue = ((Integer) obj).intValue();
        this.value = intValue;
        persistInt(intValue);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (isPersistent()) {
            return onSaveInstanceState;
        }
        SavedState savedState = new SavedState(onSaveInstanceState);
        savedState.value = this.value;
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.value = savedState.value;
        notifyChanged();
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int value;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.value = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.value);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }
}
