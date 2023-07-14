package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.facebook.animated.webp.WebPImage;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.sticker.app.github.gabrielbb.cutout.test.BuildConfig;
import com.sticker.app.github.gabrielbb.cutout.utils.ExifInterface;

import java.io.IOException;
import java.util.List;

public class StickerPackValidator {
    private static final int CHAR_COUNT_MAX = 128;
    private static final int EMOJI_LIMIT = 3;
    private static final int IMAGE_HEIGHT = 512;
    private static final int IMAGE_WIDTH = 512;
    private static final long ONE_KIBIBYTE = 8192;
    private static final int STICKER_FILE_SIZE_LIMIT_KB = 100;
    private static final int STICKER_SIZE_MAX = 30;
    private static final int STICKER_SIZE_MIN = 3;
    private static final int TRAY_IMAGE_DIMENSION_MAX = 512;
    private static final int TRAY_IMAGE_DIMENSION_MIN = 24;
    private static final int TRAY_IMAGE_FILE_SIZE_MAX_KB = 50;

    public static void verifyStickerPackValidity(@NonNull Context context, @NonNull StickerPack stickerPack) throws IllegalStateException {
        if (TextUtils.isEmpty(stickerPack.identifier)) {
            throw new IllegalStateException("sticker pack identifier is empty");
        } else if (stickerPack.identifier.length() <= 128) {
            checkStringValidity(stickerPack.identifier);
            if (TextUtils.isEmpty(stickerPack.publisher)) {
                throw new IllegalStateException("sticker pack publisher is empty, sticker pack identifier:" + stickerPack.identifier);
            } else if (stickerPack.publisher.length() > 128) {
                throw new IllegalStateException("sticker pack publisher cannot exceed 128 characters, sticker pack identifier:" + stickerPack.identifier);
            } else if (TextUtils.isEmpty(stickerPack.name)) {
                throw new IllegalStateException("sticker pack name is empty, sticker pack identifier:" + stickerPack.identifier);
            } else if (stickerPack.name.length() > 128) {
                throw new IllegalStateException("sticker pack name cannot exceed 128 characters, sticker pack identifier:" + stickerPack.identifier);
            } else if (!TextUtils.isEmpty(stickerPack.trayImageFile)) {
                try {
                    byte[] fetchStickerAsset = fetchStickerAsset(stickerPack.identifier, stickerPack.trayImageFile, context.getContentResolver());
                    if (((long) fetchStickerAsset.length) <= 409600) {
                        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(fetchStickerAsset, 0, fetchStickerAsset.length);
                        if (decodeByteArray.getHeight() > 512 || decodeByteArray.getHeight() < 24) {
                            throw new IllegalStateException("tray image height should between 24 and 512 pixels, current tray image height is " + decodeByteArray.getHeight() + ", tray image file: " + stickerPack.trayImageFile);
                        } else if (decodeByteArray.getWidth() > 512 || decodeByteArray.getWidth() < 24) {
                            throw new IllegalStateException("tray image width should be between 24 and 512 pixels, current tray image width is " + decodeByteArray.getWidth() + ", tray image file: " + stickerPack.trayImageFile);
                        } else {
                            List<Sticker> stickers = stickerPack.getStickers();
                            if (stickers.size() < 3 || stickers.size() > 30) {
                                throw new IllegalStateException("sticker pack sticker count should be between morning to 30 inclusive, it currently has " + stickers.size() + ", sticker pack identifier:" + stickerPack.identifier);
                            }
                            for (Sticker validateSticker : stickers) {
                                validateSticker(context, stickerPack.identifier, validateSticker);
                            }
                        }
                    } else {
                        throw new IllegalStateException("tray image should be less than 50 KB, tray image file: " + stickerPack.trayImageFile);
                    }
                } catch (IOException e) {
                    throw new IllegalStateException("Cannot open tray image, " + stickerPack.trayImageFile, e);
                }
            } else {
                throw new IllegalStateException("sticker pack tray id is empty, sticker pack identifier:" + stickerPack.identifier);
            }
        } else {
            throw new IllegalStateException("sticker pack identifier cannot exceed 128 characters");
        }
    }

    private static void validateSticker(@NonNull Context context, @NonNull String str, @NonNull Sticker sticker) throws IllegalStateException {
        if (sticker.emojis.size() > 3) {
            throw new IllegalStateException("emoji count exceed limit, sticker pack identifier:" + str + ", filename:" + sticker.imageFileName);
        } else if (!TextUtils.isEmpty(sticker.imageFileName)) {
            validateStickerFile(context, str, sticker.imageFileName);
        } else {
            throw new IllegalStateException("no file path for sticker, sticker pack identifier:" + str);
        }
    }

    private static void validateStickerFile(@NonNull Context context, @NonNull String str, @NonNull String str2) throws IllegalStateException {
        try {
            byte[] fetchStickerAsset = fetchStickerAsset(str, str2, context.getContentResolver());
            if (((long) fetchStickerAsset.length) <= 819200) {
                WebPImage create = WebPImage.createFromByteArray(fetchStickerAsset, ImageDecodeOptions.defaults());
                if (create.getHeight() != 512) {
                    throw new IllegalStateException("sticker height should be 512, sticker pack identifier:" + str + ", filename:" + str2);
                } else if (create.getWidth() != 512) {
                    throw new IllegalStateException("sticker width should be 512, sticker pack identifier:" + str + ", filename:" + str2);
                } else if (create.getFrameCount() > 1) {
                    throw new IllegalStateException("sticker shoud be a static image, no animated sticker support at the moment, sticker pack identifier:" + str + ", filename:" + str2);
                }
            } else {
                throw new IllegalStateException("sticker should be less than 100KB, sticker pack identifier:" + str + ", filename:" + str2);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Error parsing webp image, sticker pack identifier:" + str + ", filename:" + str2, e);
        } catch (IOException e2) {
            throw new IllegalStateException("cannot open sticker file: sticker pack identifier:" + str + ", filename:" + str2, e2);
        }
    }

    private static void checkStringValidity(@NonNull String str) {
        if (!str.matches("[\\w-.,'\\s]+")) {
            throw new IllegalStateException(str + " contains invalid characters, allowed characters are a to z, A to Z, _ , ' - . and space character");
        } else if (str.contains("..")) {
            throw new IllegalStateException(str + " cannot contain ..");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002e, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0053, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0054, code lost:
        r5 = r7;
        r7 = r6;
        r6 = r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] fetchStickerAsset(@NonNull String r6, @NonNull String r7, android.content.ContentResolver r8) throws IOException {
        /*
            android.net.Uri r0 = getStickerAssetUri(r6, r7)
            java.io.InputStream r8 = r8.openInputStream(r0)
            r0 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0063 }
            r1.<init>()     // Catch:{ Throwable -> 0x0063 }
            if (r8 == 0) goto L_0x0033
            r6 = 16384(0x4000, float:2.2959E-41)
            byte[] r6 = new byte[r6]     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
        L_0x0014:
            int r7 = r6.length     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            r2 = 0
            int r7 = r8.read(r6, r2, r7)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            r3 = -1
            if (r7 == r3) goto L_0x0021
            r1.write(r6, r2, r7)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            goto L_0x0014
        L_0x0021:
            byte[] r6 = r1.toByteArray()     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            r1.close()     // Catch:{ Throwable -> 0x0063 }
            if (r8 == 0) goto L_0x002d
            r8.close()
        L_0x002d:
            return r6
        L_0x002e:
            r6 = move-exception
            r7 = r0
            goto L_0x0057
        L_0x0031:
            r6 = move-exception
            goto L_0x0052
        L_0x0033:
            java.io.IOException r2 = new java.io.IOException     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            r3.<init>()     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            java.lang.String r4 = "cannot read sticker asset:"
            r3.append(r4)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            r3.append(r6)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            java.lang.String r6 = "/"
            r3.append(r6)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            r3.append(r7)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            java.lang.String r6 = r3.toString()     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            r2.<init>(r6)     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
            throw r2     // Catch:{ Throwable -> 0x0031, all -> 0x002e }
        L_0x0052:
            throw r6     // Catch:{ all -> 0x0053 }
        L_0x0053:
            r7 = move-exception
            r5 = r7
            r7 = r6
            r6 = r5
        L_0x0057:
            if (r7 == 0) goto L_0x005d
            r1.close()     // Catch:{ Throwable -> 0x0060 }
            goto L_0x0060
        L_0x005d:
            r1.close()     // Catch:{ Throwable -> 0x0063 }
        L_0x0060:
            throw r6     // Catch:{ Throwable -> 0x0063 }
        L_0x0061:
            r6 = move-exception
            goto L_0x0066
        L_0x0063:
            r6 = move-exception
            r0 = r6
            throw r0     // Catch:{ all -> 0x0061 }
        L_0x0066:
            if (r8 == 0) goto L_0x0071
            if (r0 == 0) goto L_0x006e
            r8.close()     // Catch:{ Throwable -> 0x0071 }
            goto L_0x0071
        L_0x006e:
            r8.close()
        L_0x0071:
            throw r6
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode.StickerPackValidator.fetchStickerAsset(java.lang.String, java.lang.String, android.content.ContentResolver):byte[]");
    }

    private static Uri getStickerAssetUri(String str, String str2) {
        return new Uri.Builder().scheme("content").authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY2).appendPath("stickers_asset").appendPath(ExifInterface.GPS_MEASUREMENT_2D).appendPath(str2).build();
    }
}
