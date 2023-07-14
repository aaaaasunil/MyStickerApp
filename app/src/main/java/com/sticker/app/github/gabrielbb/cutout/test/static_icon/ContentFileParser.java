package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.text.TextUtils;
import android.util.JsonReader;
import androidx.annotation.NonNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class ContentFileParser {
    private static final int LIMIT_EMOJI_COUNT = 3;

    ContentFileParser() {
    }

    @NonNull
    static List<StickerPack> parseStickerPacks(@NonNull InputStream inputStream) throws Throwable {
        Throwable th = null;
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        try {
            jsonReader.setLenient(true);
            List<StickerPack> readStickerPacks = readStickerPacks(jsonReader);
            jsonReader.close();
            return readStickerPacks;
        } catch (Throwable unused) {
        }
        throw th;
    }

    @NonNull
    private static List<StickerPack> readStickerPacks(@NonNull JsonReader jsonReader) throws IOException, IllegalStateException {
        ArrayList<StickerPack> arrayList = new ArrayList<>();
        jsonReader.beginObject();
        String str = null;
        String str2 = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if ("android_play_store_link".equals(nextName)) {
                str = jsonReader.nextString();
            } else if ("ios_app_store_link".equals(nextName)) {
                str2 = jsonReader.nextString();
            } else if ("sticker_packs".equals(nextName)) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    arrayList.add(readStickerPack(jsonReader));
                }
                jsonReader.endArray();
            } else {
                throw new IllegalStateException("unknown field in json: " + nextName);
            }
        }
        jsonReader.endObject();
        if (arrayList.size() != 0) {
            for (StickerPack stickerPack : arrayList) {
                stickerPack.setAndroidPlayStoreLink(str);
                stickerPack.setIosAppStoreLink(str2);
            }
            return arrayList;
        }
        throw new IllegalStateException("sticker pack list cannot be empty");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    @NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static StickerPack readStickerPack(@NonNull JsonReader r15) throws IOException, IllegalStateException {
        /*
            r15.beginObject()
            java.lang.String r0 = ""
            r1 = 0
            r2 = 0
            r12 = r0
            r4 = r2
            r5 = r4
            r6 = r5
            r7 = r6
            r8 = r7
            r9 = r8
            r10 = r9
            r11 = r10
            r13 = 0
        L_0x0011:
            boolean r0 = r15.hasNext()
            if (r0 == 0) goto L_0x00ed
            java.lang.String r0 = r15.nextName()
            r3 = -1
            int r14 = r0.hashCode()
            switch(r14) {
                case -1618432855: goto L_0x008d;
                case -692149211: goto L_0x0083;
                case -5607704: goto L_0x0079;
                case 3373707: goto L_0x006f;
                case 335244632: goto L_0x0065;
                case 597456295: goto L_0x005a;
                case 967150217: goto L_0x0050;
                case 993370168: goto L_0x0045;
                case 1414128537: goto L_0x003b;
                case 1447404028: goto L_0x0031;
                case 1531715286: goto L_0x0025;
                default: goto L_0x0023;
            }
        L_0x0023:
            goto L_0x0097
        L_0x0025:
            java.lang.String r14 = "stickers"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 8
            goto L_0x0098
        L_0x0031:
            java.lang.String r14 = "publisher"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 2
            goto L_0x0098
        L_0x003b:
            java.lang.String r14 = "publisher_email"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 4
            goto L_0x0098
        L_0x0045:
            java.lang.String r14 = "avoid_cache"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 10
            goto L_0x0098
        L_0x0050:
            java.lang.String r14 = "tray_image_file"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 3
            goto L_0x0098
        L_0x005a:
            java.lang.String r14 = "image_data_version"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 9
            goto L_0x0098
        L_0x0065:
            java.lang.String r14 = "publisher_website"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 5
            goto L_0x0098
        L_0x006f:
            java.lang.String r14 = "name"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 1
            goto L_0x0098
        L_0x0079:
            java.lang.String r14 = "license_agreement_website"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 7
            goto L_0x0098
        L_0x0083:
            java.lang.String r14 = "privacy_policy_website"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 6
            goto L_0x0098
        L_0x008d:
            java.lang.String r14 = "identifier"
            boolean r0 = r0.equals(r14)
            if (r0 == 0) goto L_0x0097
            r0 = 0
            goto L_0x0098
        L_0x0097:
            r0 = -1
        L_0x0098:
            switch(r0) {
                case 0: goto L_0x00e6;
                case 1: goto L_0x00df;
                case 2: goto L_0x00d8;
                case 3: goto L_0x00d1;
                case 4: goto L_0x00ca;
                case 5: goto L_0x00c3;
                case 6: goto L_0x00bc;
                case 7: goto L_0x00b5;
                case 8: goto L_0x00ae;
                case 9: goto L_0x00a7;
                case 10: goto L_0x00a0;
                default: goto L_0x009b;
            }
        L_0x009b:
            r15.skipValue()
            goto L_0x0011
        L_0x00a0:
            boolean r0 = r15.nextBoolean()
            r13 = r0
            goto L_0x0011
        L_0x00a7:
            java.lang.String r0 = r15.nextString()
            r12 = r0
            goto L_0x0011
        L_0x00ae:
            java.util.List r0 = readStickers(r15)
            r2 = r0
            goto L_0x0011
        L_0x00b5:
            java.lang.String r0 = r15.nextString()
            r11 = r0
            goto L_0x0011
        L_0x00bc:
            java.lang.String r0 = r15.nextString()
            r10 = r0
            goto L_0x0011
        L_0x00c3:
            java.lang.String r0 = r15.nextString()
            r9 = r0
            goto L_0x0011
        L_0x00ca:
            java.lang.String r0 = r15.nextString()
            r8 = r0
            goto L_0x0011
        L_0x00d1:
            java.lang.String r0 = r15.nextString()
            r7 = r0
            goto L_0x0011
        L_0x00d8:
            java.lang.String r0 = r15.nextString()
            r6 = r0
            goto L_0x0011
        L_0x00df:
            java.lang.String r0 = r15.nextString()
            r5 = r0
            goto L_0x0011
        L_0x00e6:
            java.lang.String r0 = r15.nextString()
            r4 = r0
            goto L_0x0011
        L_0x00ed:
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 != 0) goto L_0x0160
            boolean r0 = android.text.TextUtils.isEmpty(r5)
            if (r0 != 0) goto L_0x0158
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 != 0) goto L_0x0150
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            if (r0 != 0) goto L_0x0148
            if (r2 == 0) goto L_0x0140
            int r0 = r2.size()
            if (r0 == 0) goto L_0x0140
            java.lang.String r0 = ".."
            boolean r0 = r4.contains(r0)
            if (r0 != 0) goto L_0x0138
            java.lang.String r0 = "/"
            boolean r0 = r4.contains(r0)
            if (r0 != 0) goto L_0x0138
            boolean r0 = android.text.TextUtils.isEmpty(r12)
            if (r0 != 0) goto L_0x0130
            r15.endObject()
            com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerPack r15 = new com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerPack
            r3 = r15
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
            r15.setStickers(r2)
            return r15
        L_0x0130:
            java.lang.IllegalStateException r15 = new java.lang.IllegalStateException
            java.lang.String r0 = "image_data_version should not be empty"
            r15.<init>(r0)
            throw r15
        L_0x0138:
            java.lang.IllegalStateException r15 = new java.lang.IllegalStateException
            java.lang.String r0 = "identifier should not contain .. or / to prevent directory traversal"
            r15.<init>(r0)
            throw r15
        L_0x0140:
            java.lang.IllegalStateException r15 = new java.lang.IllegalStateException
            java.lang.String r0 = "sticker list is empty"
            r15.<init>(r0)
            throw r15
        L_0x0148:
            java.lang.IllegalStateException r15 = new java.lang.IllegalStateException
            java.lang.String r0 = "tray_image_file cannot be empty"
            r15.<init>(r0)
            throw r15
        L_0x0150:
            java.lang.IllegalStateException r15 = new java.lang.IllegalStateException
            java.lang.String r0 = "publisher cannot be empty"
            r15.<init>(r0)
            throw r15
        L_0x0158:
            java.lang.IllegalStateException r15 = new java.lang.IllegalStateException
            java.lang.String r0 = "name cannot be empty"
            r15.<init>(r0)
            throw r15
        L_0x0160:
            java.lang.IllegalStateException r15 = new java.lang.IllegalStateException
            java.lang.String r0 = "identifier cannot be empty"
            r15.<init>(r0)
            throw r15
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.test.static_icon.ContentFileParser.readStickerPack(android.util.JsonReader):com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerPack");
    }

    @NonNull
    private static List<Sticker> readStickers(@NonNull JsonReader jsonReader) throws IOException, IllegalStateException {
        jsonReader.beginArray();
        ArrayList arrayList = new ArrayList();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            String str = null;
            ArrayList arrayList2 = new ArrayList(3);
            while (jsonReader.hasNext()) {
                String nextName = jsonReader.nextName();
                if ("image_file".equals(nextName)) {
                    str = jsonReader.nextString();
                } else if ("emojis".equals(nextName)) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        arrayList2.add(jsonReader.nextString());
                    }
                    jsonReader.endArray();
                } else {
                    throw new IllegalStateException("unknown field in json: " + nextName);
                }
            }
            jsonReader.endObject();
            if (TextUtils.isEmpty(str)) {
                throw new IllegalStateException("sticker image_file cannot be empty");
            } else if (!str.endsWith(".webp")) {
                throw new IllegalStateException("image file for stickers should be webp files, image file is: " + str);
            } else if (str.contains("..") || str.contains("/")) {
                throw new IllegalStateException("the file name should not contain .. or / to prevent directory traversal, image file is:" + str);
            } else {
                arrayList.add(new Sticker(str, (List<String>) arrayList2));
            }
        }
        jsonReader.endArray();
        return arrayList;
    }
}
