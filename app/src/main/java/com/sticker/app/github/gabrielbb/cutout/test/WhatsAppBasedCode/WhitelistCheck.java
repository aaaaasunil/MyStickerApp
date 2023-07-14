package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;

public class WhitelistCheck {
    private static final String AUTHORITY_QUERY_PARAM = "authority";
    private static String CONSUMER_WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static String CONTENT_PROVIDER = ".provider.sticker_whitelist_check";
    private static final String IDENTIFIER_QUERY_PARAM = "identifier";
    private static String QUERY_PATH = "is_whitelisted";
    private static String QUERY_RESULT_COLUMN_NAME = "result";
    private static String SMB_WHATSAPP_PACKAGE_NAME = "com.whatsapp.w4b";
    private static String STICKER_APP_AUTHORITY = "com.sticker.app.WhatsAppBasedCode.StickerPack";

    static boolean isWhitelisted(@NonNull Context context, @NonNull String str) {
        try {
            boolean isWhitelistedFromProvider = isWhitelistedFromProvider(context, str, CONSUMER_WHATSAPP_PACKAGE_NAME);
            boolean isWhitelistedFromProvider2 = isWhitelistedFromProvider(context, str, SMB_WHATSAPP_PACKAGE_NAME);
            if (!isWhitelistedFromProvider || !isWhitelistedFromProvider2) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0075, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0079, code lost:
        if (r9 != null) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x007b, code lost:
        if (r10 != null) goto L_0x007d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0081, code lost:
        r9.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isWhitelistedFromProvider(@NonNull Context r9, @NonNull String r10, String r11) {
        /*
            android.content.pm.PackageManager r0 = r9.getPackageManager()
            boolean r1 = isPackageInstalled(r11, r0)
            r2 = 1
            if (r1 == 0) goto L_0x008b
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r11)
            java.lang.String r11 = CONTENT_PROVIDER
            r1.append(r11)
            java.lang.String r11 = r1.toString()
            r1 = 128(0x80, float:1.794E-43)
            android.content.pm.ProviderInfo r0 = r0.resolveContentProvider(r11, r1)
            r1 = 0
            if (r0 != 0) goto L_0x0026
            return r1
        L_0x0026:
            android.net.Uri$Builder r0 = new android.net.Uri$Builder
            r0.<init>()
            java.lang.String r3 = "content"
            android.net.Uri$Builder r0 = r0.scheme(r3)
            android.net.Uri$Builder r11 = r0.authority(r11)
            java.lang.String r0 = QUERY_PATH
            android.net.Uri$Builder r11 = r11.appendPath(r0)
            java.lang.String r0 = "authority"
            java.lang.String r3 = STICKER_APP_AUTHORITY
            android.net.Uri$Builder r11 = r11.appendQueryParameter(r0, r3)
            java.lang.String r0 = "identifier"
            android.net.Uri$Builder r10 = r11.appendQueryParameter(r0, r10)
            android.net.Uri r4 = r10.build()
            android.content.ContentResolver r3 = r9.getContentResolver()
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r9 = r3.query(r4, r5, r6, r7, r8)
            r10 = 0
            if (r9 == 0) goto L_0x0085
            boolean r11 = r9.moveToFirst()     // Catch:{ Throwable -> 0x0077 }
            if (r11 == 0) goto L_0x0085
            java.lang.String r11 = QUERY_RESULT_COLUMN_NAME     // Catch:{ Throwable -> 0x0077 }
            int r11 = r9.getColumnIndexOrThrow(r11)     // Catch:{ Throwable -> 0x0077 }
            int r10 = r9.getInt(r11)     // Catch:{ Throwable -> 0x0077 }
            if (r10 != r2) goto L_0x006f
            r1 = 1
        L_0x006f:
            if (r9 == 0) goto L_0x0074
            r9.close()
        L_0x0074:
            return r1
        L_0x0075:
            r11 = move-exception
            goto L_0x0079
        L_0x0077:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0075 }
        L_0x0079:
            if (r9 == 0) goto L_0x0084
            if (r10 == 0) goto L_0x0081
            r9.close()     // Catch:{ Throwable -> 0x0084 }
            goto L_0x0084
        L_0x0081:
            r9.close()
        L_0x0084:
            throw r11
        L_0x0085:
            if (r9 == 0) goto L_0x008a
            r9.close()
        L_0x008a:
            return r1
        L_0x008b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode.WhitelistCheck.isWhitelistedFromProvider(android.content.Context, java.lang.String, java.lang.String):boolean");
    }

    private static boolean isPackageInstalled(String str, PackageManager packageManager) {
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
            if (applicationInfo != null) {
                return applicationInfo.enabled;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
