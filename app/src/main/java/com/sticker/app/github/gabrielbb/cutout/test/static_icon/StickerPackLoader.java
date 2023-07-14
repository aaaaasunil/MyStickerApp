package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.ANDROID_APP_DOWNLOAD_LINK_IN_QUERY;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.AVOID_CACHE;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.IMAGE_DATA_VERSION;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.IOS_APP_DOWNLOAD_LINK_IN_QUERY;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.LICENSE_AGREENMENT_WEBSITE;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.PRIVACY_POLICY_WEBSITE;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.PUBLISHER_EMAIL;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.PUBLISHER_WEBSITE;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.STICKER_FILE_EMOJI_IN_QUERY;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.STICKER_FILE_NAME_IN_QUERY;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.STICKER_PACK_ICON_IN_QUERY;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.STICKER_PACK_IDENTIFIER_IN_QUERY;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.STICKER_PACK_NAME_IN_QUERY;
import static com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerContentProvider.STICKER_PACK_PUBLISHER_IN_QUERY;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import com.sticker.app.github.gabrielbb.cutout.test.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
class StickerPackLoader {

    /**
     * Get the list of sticker packs for the sticker content provider
     */
    @NonNull
    static ArrayList<StickerPack> fetchStickerPacks(Context context) throws IllegalStateException {
        final Cursor cursor = context.getContentResolver().query(StickerContentProvider.AUTHORITY_URI, null, null, null, null);
        if (cursor == null) {
            throw new IllegalStateException("could not fetch from content provider, " + BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        }
        HashSet<String> identifierSet = new HashSet<>();
        final ArrayList<StickerPack> stickerPackList = fetchFromContentProvider(cursor);
        for (StickerPack stickerPack : stickerPackList) {
            if (identifierSet.contains(stickerPack.identifier)) {
                throw new IllegalStateException("sticker pack identifiers should be unique, there are more than one pack with identifier:" + stickerPack.identifier);
            } else {
                identifierSet.add(stickerPack.identifier);
            }
        }
        if (stickerPackList.isEmpty()) {
            throw new IllegalStateException("There should be at least one sticker pack in the app");
        }
        for (StickerPack stickerPack : stickerPackList) {
            final List<Sticker> stickers = getStickersForPack(context, stickerPack);
            stickerPack.setStickers(stickers);
            StickerPackValidator.verifyStickerPackValidity(context, stickerPack);
        }
        return stickerPackList;
    }

    @NonNull
    private static List<Sticker> getStickersForPack(Context context, StickerPack stickerPack) {
        final List<Sticker> stickers = fetchFromContentProviderForStickers(stickerPack.identifier, context.getContentResolver());
        for (Sticker sticker : stickers) {
            final byte[] bytes;
            try {
                bytes = fetchStickerAsset(stickerPack.identifier, sticker.imageFileName, context.getContentResolver());
                if (bytes.length <= 0) {
                    throw new IllegalStateException("Asset file is empty, pack: " + stickerPack.name + ", sticker: " + sticker.imageFileName);
                }
                sticker.setSize(bytes.length);
            } catch (IOException | IllegalArgumentException e) {
                throw new IllegalStateException("Asset file doesn't exist. pack: " + stickerPack.name + ", sticker: " + sticker.imageFileName, e);
            }
        }
        return stickers;
    }


    @NonNull
    private static ArrayList<StickerPack> fetchFromContentProvider(Cursor cursor) {
        ArrayList<StickerPack> stickerPackList = new ArrayList<>();
        cursor.moveToFirst();
        do {
            final String identifier = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_PACK_IDENTIFIER_IN_QUERY));
            final String name = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_PACK_NAME_IN_QUERY));
            final String publisher = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_PACK_PUBLISHER_IN_QUERY));
            final String trayImage = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_PACK_ICON_IN_QUERY));
            final String androidPlayStoreLink = cursor.getString(cursor.getColumnIndexOrThrow(ANDROID_APP_DOWNLOAD_LINK_IN_QUERY));
            final String iosAppLink = cursor.getString(cursor.getColumnIndexOrThrow(IOS_APP_DOWNLOAD_LINK_IN_QUERY));
            final String publisherEmail = cursor.getString(cursor.getColumnIndexOrThrow(PUBLISHER_EMAIL));
            final String publisherWebsite = cursor.getString(cursor.getColumnIndexOrThrow(PUBLISHER_WEBSITE));
            final String privacyPolicyWebsite = cursor.getString(cursor.getColumnIndexOrThrow(PRIVACY_POLICY_WEBSITE));
            final String licenseAgreementWebsite = cursor.getString(cursor.getColumnIndexOrThrow(LICENSE_AGREENMENT_WEBSITE));
            final String imageDataVersion = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_DATA_VERSION));
            final boolean avoidCache = cursor.getShort(cursor.getColumnIndexOrThrow(AVOID_CACHE)) > 0;
            final StickerPack stickerPack = new StickerPack(identifier, name, publisher, trayImage, publisherEmail, publisherWebsite, privacyPolicyWebsite, licenseAgreementWebsite, imageDataVersion, avoidCache);
            stickerPack.setAndroidPlayStoreLink(androidPlayStoreLink);
            stickerPack.setIosAppStoreLink(iosAppLink);
            stickerPackList.add(stickerPack);
        } while (cursor.moveToNext());
        return stickerPackList;
    }

    @NonNull
    private static List<Sticker> fetchFromContentProviderForStickers(String identifier, ContentResolver contentResolver) {
        Uri uri = getStickerListUri(identifier);

        final String[] projection = {STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY};
        final Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        List<Sticker> stickers = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                final String name = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_FILE_NAME_IN_QUERY));
                final String emojisConcatenated = cursor.getString(cursor.getColumnIndexOrThrow(STICKER_FILE_EMOJI_IN_QUERY));
                List<String> emojis = new ArrayList<>(StickerPackValidator.EMOJI_MAX_LIMIT);
                if (!TextUtils.isEmpty(emojisConcatenated)) {
                    emojis = Arrays.asList(emojisConcatenated.split(","));
                }
                stickers.add(new Sticker(name, emojis));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return stickers;
    }

    static byte[] fetchStickerAsset(@NonNull final String identifier, @NonNull final String name, ContentResolver contentResolver) throws IOException {
        try (final InputStream inputStream = contentResolver.openInputStream(getStickerAssetUri(identifier, name));
             final ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            if (inputStream == null) {
                throw new IOException("cannot read sticker asset:" + identifier + "/" + name);
            }
            int read;
            byte[] data = new byte[16384];

            while ((read = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
            return buffer.toByteArray();
        }
    }

    private static Uri getStickerListUri(String identifier) {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider.STICKERS).appendPath(identifier).build();
    }

    static Uri getStickerAssetUri(String identifier, String stickerName) {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider.STICKERS_ASSET).appendPath(identifier).appendPath(stickerName).build();
    }
}

//class StickerPackLoader {
//    StickerPackLoader() {
//    }
//
//    @NonNull
//    static ArrayList<StickerPack> fetchStickerPacks(Context context) throws IllegalStateException {
////        Cursor query = context.getContentResolver().query(StickerContentProvider.AUTHORITY_URI, (String[]) null, (String) null, (String[]) null, (String) null);
//        final Cursor query = context.getContentResolver().query(StickerContentProvider.AUTHORITY_URI, null, null, null, null);
//
//        if (query != null) {
//            HashSet hashSet = new HashSet();
//            ArrayList<StickerPack> fetchFromContentProvider = fetchFromContentProvider(query);
//            Iterator<StickerPack> it = fetchFromContentProvider.iterator();
//            while (it.hasNext()) {
//                StickerPack next = it.next();
//                if (!hashSet.contains(next.identifier)) {
//                    hashSet.add(next.identifier);
//                } else {
//                    throw new IllegalStateException("sticker pack identifiers should be unique, there are more than one pack with identifier:" + next.identifier);
//                }
//            }
//            if (!fetchFromContentProvider.isEmpty()) {
//                Iterator<StickerPack> it2 = fetchFromContentProvider.iterator();
//                while (it2.hasNext()) {
//                    StickerPack next2 = it2.next();
//                    next2.setStickers(getStickersForPack(context, next2));
//                }
//                return fetchFromContentProvider;
//            }
//            throw new IllegalStateException("There should be at least one sticker pack in the app");
//        }
//        throw new IllegalStateException("could not fetch from content provider, com.sticker.app.stickercontentprovider");
//    }
//
//    @NonNull
//    private static List<Sticker> getStickersForPack(Context context, StickerPack stickerPack) {
//        List<Sticker> fetchFromContentProviderForStickers = fetchFromContentProviderForStickers(stickerPack.identifier, context.getContentResolver());
//        for (Sticker next : fetchFromContentProviderForStickers) {
//            byte[] bArr = new byte[0];
//            try {
//                byte[] fetchStickerAsset = fetchStickerAsset(stickerPack.identifier, next.imageFileName, context.getContentResolver());
//                if (fetchStickerAsset.length > 0) {
//                    next.setSize((long) fetchStickerAsset.length);
//                } else {
//                    throw new IllegalStateException("Asset file is empty, pack: " + stickerPack.name + ", sticker: " + next.imageFileName);
//                }
//            } catch (IOException | IllegalArgumentException e) {
//                throw new IllegalStateException("Asset file doesn't exist. pack: " + stickerPack.name + ", sticker: " + next.imageFileName + " stickers.size()" + bArr.length, e);
//            }
//        }
//        return fetchFromContentProviderForStickers;
//    }
//
//    @NonNull
//    private static ArrayList<StickerPack> fetchFromContentProvider(Cursor cursor) {
//        ArrayList<StickerPack> arrayList = new ArrayList<>();
//        Log.d("StickerPacKLoader", "fetchFromContentProvider: " + cursor.getCount());
//        cursor.moveToFirst();
//        do {
//            String string = cursor.getString(cursor.getColumnIndexOrThrow("sticker_pack_identifier"));
//            String string2 = cursor.getString(cursor.getColumnIndexOrThrow("sticker_pack_name"));
//            String string3 = cursor.getString(cursor.getColumnIndexOrThrow("sticker_pack_publisher"));
//            String string4 = cursor.getString(cursor.getColumnIndexOrThrow("sticker_pack_icon"));
//            String string5 = cursor.getString(cursor.getColumnIndexOrThrow("android_play_store_link"));
//            String string6 = cursor.getString(cursor.getColumnIndexOrThrow("ios_app_download_link"));
//            StickerPack stickerPack = new StickerPack(string, string2, string3, string4, cursor.getString(cursor.getColumnIndexOrThrow("sticker_pack_publisher_email")), cursor.getString(cursor.getColumnIndexOrThrow("sticker_pack_publisher_website")), cursor.getString(cursor.getColumnIndexOrThrow("sticker_pack_privacy_policy_website")), cursor.getString(cursor.getColumnIndexOrThrow("sticker_pack_license_agreement_website")), cursor.getString(cursor.getColumnIndexOrThrow(StickerContentProvider.IMAGE_DATA_VERSION)), cursor.getShort(cursor.getColumnIndexOrThrow(StickerContentProvider.AVOID_CACHE)) > 0);
//            stickerPack.setAndroidPlayStoreLink(string5);
//            stickerPack.setIosAppStoreLink(string6);
//            arrayList.add(stickerPack);
//        } while (cursor.moveToNext());
//        return arrayList;
//    }
//
//    /* JADX WARNING: Removed duplicated region for block: B:8:0x0057  */
//    @NonNull
//    /* Code decompiled incorrectly, please refer to instructions dump. */
//    private static List<Sticker> fetchFromContentProviderForStickers(String r6, android.content.ContentResolver r7) {
//        /*
//            android.net.Uri r1 = getStickerListUri(r6)
//            r6 = 2
//            java.lang.String[] r2 = new java.lang.String[r6]
//            java.lang.String r6 = "sticker_file_name"
//            r0 = 0
//            r2[r0] = r6
//            java.lang.String r6 = "sticker_emoji"
//            r0 = 1
//            r2[r0] = r6
//            r3 = 0
//            r4 = 0
//            r5 = 0
//            r0 = r7
//            android.database.Cursor r6 = r0.query(r1, r2, r3, r4, r5)
//            java.util.ArrayList r7 = new java.util.ArrayList
//            r7.<init>()
//            if (r6 == 0) goto L_0x0055
//            int r0 = r6.getCount()
//            if (r0 <= 0) goto L_0x0055
//            r6.moveToFirst()
//        L_0x0029:
//            java.lang.String r0 = "sticker_file_name"
//            int r0 = r6.getColumnIndexOrThrow(r0)
//            java.lang.String r0 = r6.getString(r0)
//            java.lang.String r1 = "sticker_emoji"
//            int r1 = r6.getColumnIndexOrThrow(r1)
//            java.lang.String r1 = r6.getString(r1)
//            com.sticker.app.github.gabrielbb.cutout.test.static_icon.Sticker r2 = new com.sticker.app.github.gabrielbb.cutout.test.static_icon.Sticker
//            java.lang.String r3 = ","
//            java.lang.String[] r1 = r1.split(r3)
//            java.util.List r1 = java.util.Arrays.asList(r1)
//            r2.<init>((java.lang.String) r0, (java.util.List<java.lang.String>) r1)
//            r7.add(r2)
//            boolean r0 = r6.moveToNext()
//            if (r0 != 0) goto L_0x0029
//        L_0x0055:
//            if (r6 == 0) goto L_0x005a
//            r6.close()
//        L_0x005a:
//            return r7
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerPackLoader.fetchFromContentProviderForStickers(java.lang.String, android.content.ContentResolver):java.util.List");
//    }
//
//    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002e, code lost:
//        r6 = th;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
//        r7 = null;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0053, code lost:
//        r7 = move-exception;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0054, code lost:
//        r5 = r7;
//        r7 = r6;
//        r6 = r5;
//     */
//    /* Code decompiled incorrectly, please refer to instructions dump. */
//    static byte[] fetchStickerAsset(@NonNull String r6, @NonNull String r7, android.content.ContentResolver r8) throws IOException {
//        /*
//            android.net.Uri r0 = getStickerAssetUri(r6, r7)
//            java.io.InputStream r8 = r8.openInputStream(r0)
//            r0 = 0
//            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0063 }
//            r1.<init>()     // Catch:{ Throwable -> 0x0063 }
//            if (r8 == 0) goto L_0x0033
//            r6 = 16384(0x4000, float:2.2959E-41)
//            byte[] r6 = new byte[r6]     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//        L_0x0014:
//            int r7 = r6.length     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            r2 = 0
//            int r7 = r8.read(r6, r2, r7)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            r3 = -1
//            if (r7 == r3) goto L_0x0021
//            r1.write(r6, r2, r7)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            goto L_0x0014
//        L_0x0021:
//            byte[] r6 = r1.toByteArray()     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            r1.close()     // Catch:{ Throwable -> 0x0063 }
//            if (r8 == 0) goto L_0x002d
//            r8.close()
//        L_0x002d:
//            return r6
//        L_0x002e:
//            r6 = move-exception
//            r7 = r0
//            goto L_0x0057
//        L_0x0031:
//            r6 = move-exception
//            goto L_0x0052
//        L_0x0033:
//            java.io.IOException r2 = new java.io.IOException     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            r3.<init>()     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            java.lang.String r4 = "cannot read sticker asset:"
//            r3.append(r4)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            r3.append(r6)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            java.lang.String r6 = "/"
//            r3.append(r6)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            r3.append(r7)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            java.lang.String r6 = r3.toString()     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            r2.<init>(r6)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//            throw r2     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
//        L_0x0052:
//            throw r6     // Catch:{ all -> 0x0053 }
//        L_0x0053:
//            r7 = move-exception
//            r5 = r7
//            r7 = r6
//            r6 = r5
//        L_0x0057:
//            if (r7 == 0) goto L_0x005d
//            r1.close()     // Catch:{ Throwable -> 0x0060 }
//            goto L_0x0060
//        L_0x005d:
//            r1.close()     // Catch:{ Throwable -> 0x0063 }
//        L_0x0060:
//            throw r6     // Catch:{ Throwable -> 0x0063 }
//        L_0x0061:
//            r6 = move-exception
//            goto L_0x0066
//        L_0x0063:
//            r6 = move-exception
//            r0 = r6
//            throw r0     // Catch:{ all -> 0x0061 }
//        L_0x0066:
//            if (r8 == 0) goto L_0x0071
//            if (r0 == 0) goto L_0x006e
//            r8.close()     // Catch:{ Throwable -> 0x0071 }
//            goto L_0x0071
//        L_0x006e:
//            r8.close()
//        L_0x0071:
//            throw r6
//            return
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerPackLoader.fetchStickerAsset(java.lang.String, java.lang.String, android.content.ContentResolver):byte[]");
//    }
//
//    private static Uri getStickerListUri(String str) {
//        return new Uri.Builder().scheme("content").authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath("stickers").appendPath(str).build();
//    }
//
//    static Uri getStickerAssetUri(String str, String str2) {
//        return new Uri.Builder().scheme("content").authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath("stickers_asset").appendPath(str).appendPath(str2).build();
//    }
//}
