package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.sticker.app.github.gabrielbb.cutout.test.ImageManipulation;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StickerPack implements Parcelable {
    public static final Creator<StickerPack> CREATOR = new Creator<StickerPack>() {
        public StickerPack createFromParcel(Parcel parcel) {
            return new StickerPack(parcel);
        }

        public StickerPack[] newArray(int i) {
            return new StickerPack[i];
        }
    };
    String androidPlayStoreLink;
    String identifier;
    String iosAppStoreLink;
    private boolean isWhitelisted;
    final String licenseAgreementWebsite;
    String name;
    final String privacyPolicyWebsite;
    String publisher;
    final String publisherEmail;
    final String publisherWebsite;
    private List<Sticker> stickers;
    private int stickersAddedIndex = 0;
    private long totalSize;
    String trayImageFile;
    Uri trayImageUri;

    public int describeContents() {
        return 0;
    }

    public StickerPack(String str, String str2, String str3, Uri uri, String str4, String str5, String str6, String str7, Context context) {
        this.identifier = str;
        this.name = str2;
        this.publisher = str3;
        this.trayImageFile = "trayimage";
        this.trayImageUri = ImageManipulation.convertIconTrayToWebP(uri, this.identifier, "trayImage", context);
        this.publisherEmail = str4;
        this.publisherWebsite = str5;
        this.privacyPolicyWebsite = str6;
        this.licenseAgreementWebsite = str7;
        this.stickers = new ArrayList();
    }

    /* access modifiers changed from: package-private */
    public void setIsWhitelisted(boolean z) {
        this.isWhitelisted = z;
    }

    /* access modifiers changed from: package-private */
    public boolean getIsWhitelisted() {
        return this.isWhitelisted;
    }

    protected StickerPack(Parcel parcel) {
        boolean z = false;
        this.identifier = parcel.readString();
        this.name = parcel.readString();
        this.publisher = parcel.readString();
        this.trayImageFile = parcel.readString();
        this.publisherEmail = parcel.readString();
        this.publisherWebsite = parcel.readString();
        this.privacyPolicyWebsite = parcel.readString();
        this.licenseAgreementWebsite = parcel.readString();
        this.iosAppStoreLink = parcel.readString();
        this.stickers = parcel.createTypedArrayList(Sticker.CREATOR);
        this.totalSize = parcel.readLong();
        this.androidPlayStoreLink = parcel.readString();
        this.isWhitelisted = parcel.readByte() != 0 ? true : z;
    }

    public void addSticker(Uri uri, Context context) {
        String valueOf = String.valueOf(this.stickersAddedIndex);
        this.stickers.add(new Sticker(valueOf, ImageManipulation.convertImageToWebP(uri, this.identifier, valueOf, context), new ArrayList()));
        this.stickersAddedIndex++;
    }

    public void deleteSticker(Sticker sticker) {
        new File(sticker.getUri().getPath()).delete();
        this.stickers.remove(sticker);
    }

    public Sticker getSticker(int i) {
        return this.stickers.get(i);
    }

    public Sticker getStickerById(int i) {
        for (Sticker next : this.stickers) {
            if (next.getImageFileName().equals(String.valueOf(i))) {
                return next;
            }
        }
        return null;
    }

    public void setAndroidPlayStoreLink(String str) {
        this.androidPlayStoreLink = str;
    }

    public void setIosAppStoreLink(String str) {
        this.iosAppStoreLink = str;
    }

    public List<Sticker> getStickers() {
        return this.stickers;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.identifier);
        parcel.writeString(this.name);
        parcel.writeString(this.publisher);
        parcel.writeString(this.trayImageFile);
        parcel.writeString(this.publisherEmail);
        parcel.writeString(this.publisherWebsite);
        parcel.writeString(this.privacyPolicyWebsite);
        parcel.writeString(this.licenseAgreementWebsite);
        parcel.writeString(this.iosAppStoreLink);
        parcel.writeTypedList(this.stickers);
        parcel.writeLong(this.totalSize);
        parcel.writeString(this.androidPlayStoreLink);
        parcel.writeByte(this.isWhitelisted ? (byte) 1 : 0);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public Uri getTrayImageUri() {
        return this.trayImageUri;
    }
}
