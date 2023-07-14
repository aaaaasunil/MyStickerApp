package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Sticker implements Parcelable {
    public static final Creator<Sticker> CREATOR = new Creator<Sticker>() {
        public Sticker createFromParcel(Parcel parcel) {
            return new Sticker(parcel);
        }

        public Sticker[] newArray(int i) {
            return new Sticker[i];
        }
    };
    List<String> emojis;
    String imageFileName;
    long size;
    Uri uri;

    public int describeContents() {
        return 0;
    }

    public Sticker(String str, List<String> list) {
        this.imageFileName = str;
        this.emojis = list;
    }

    public Sticker(String str, Uri uri2, List<String> list) {
        this.imageFileName = str;
        this.emojis = list;
        this.uri = uri2;
    }

    public Sticker(Parcel parcel) {
        this.imageFileName = parcel.readString();
        this.emojis = parcel.createStringArrayList();
        this.size = parcel.readLong();
    }

    public void setSize(long j) {
        this.size = j;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.imageFileName);
        parcel.writeStringList(this.emojis);
        parcel.writeLong(this.size);
    }

    public Uri getUri() {
        return this.uri;
    }

    public String getImageFileName() {
        return this.imageFileName;
    }
}
