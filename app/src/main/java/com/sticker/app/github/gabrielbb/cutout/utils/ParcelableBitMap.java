package com.sticker.app.github.gabrielbb.cutout.utils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableBitMap implements Parcelable {
    public static final Creator<ParcelableBitMap> CREATOR = new Creator<ParcelableBitMap>() {
        public ParcelableBitMap createFromParcel(Parcel parcel) {
            return new ParcelableBitMap(parcel);
        }

        public ParcelableBitMap[] newArray(int i) {
            return new ParcelableBitMap[i];
        }
    };
    private PojoBitMap pojoBitMap;

    public int describeContents() {
        return 0;
    }

    public PojoBitMap getLaptop() {
        return this.pojoBitMap;
    }

    public ParcelableBitMap(PojoBitMap pojoBitMap2) {
        this.pojoBitMap = pojoBitMap2;
    }

    protected ParcelableBitMap(Parcel parcel) {
        this.pojoBitMap = new PojoBitMap();
        this.pojoBitMap.setBitmap((Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader()));
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.pojoBitMap.getBitmap(), i);
    }
}
