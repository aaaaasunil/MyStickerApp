package com.sticker.app.github.gabrielbb.cutout.test;

import android.content.Context;
import android.os.Environment;
import com.sticker.app.R;
import java.io.File;
import java.io.IOException;

public class FilesUtils {
    public static void handleCopyFilesToSdCard(Context context, String str) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getString(R.string.app_name));
        deleteAllFiles(file);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            exportFile(new File(context.getFilesDir() + "/" + str + "/" + str + ".zip"), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteAllFiles(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    deleteAllFiles(file2);
                    file2.delete();
                } else {
                    file2.delete();
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.nio.channels.FileChannel} */
    /* JADX WARNING: type inference failed for: r1v0, types: [java.io.File] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x006b A[DONT_GENERATE] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0070 A[DONT_GENERATE] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static File exportFile(File r8, File r9) throws IOException {
        /*
            boolean r0 = r9.exists()
            r1 = 0
            if (r0 != 0) goto L_0x000e
            boolean r0 = r9.mkdir()
            if (r0 != 0) goto L_0x000e
            return r1
        L_0x000e:
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r9 = r9.getPath()
            r2.append(r9)
            java.lang.String r9 = java.io.File.separator
            r2.append(r9)
            java.lang.String r9 = r8.getPath()
            java.lang.String r3 = "/"
            java.lang.String[] r9 = r9.split(r3)
            java.lang.String r3 = r8.getPath()
            java.lang.String r4 = "/"
            java.lang.String[] r3 = r3.split(r4)
            int r3 = r3.length
            int r3 = r3 + -1
            r9 = r9[r3]
            r2.append(r9)
            java.lang.String r9 = r2.toString()
            r0.<init>(r9)
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0059 }
            r9.<init>(r8)     // Catch:{ FileNotFoundException -> 0x0059 }
            java.nio.channels.FileChannel r8 = r9.getChannel()     // Catch:{ FileNotFoundException -> 0x0059 }
            java.io.FileOutputStream r9 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0057 }
            r9.<init>(r0)     // Catch:{ FileNotFoundException -> 0x0057 }
            java.nio.channels.FileChannel r1 = r9.getChannel()     // Catch:{ FileNotFoundException -> 0x0057 }
            goto L_0x005e
        L_0x0057:
            r9 = move-exception
            goto L_0x005b
        L_0x0059:
            r9 = move-exception
            r8 = r1
        L_0x005b:
            r9.printStackTrace()
        L_0x005e:
            r3 = 0
            long r5 = r8.size()     // Catch:{ all -> 0x0074 }
            r2 = r8
            r7 = r1
            r2.transferTo(r3, r5, r7)     // Catch:{ all -> 0x0074 }
            if (r8 == 0) goto L_0x006e
            r8.close()
        L_0x006e:
            if (r1 == 0) goto L_0x0073
            r1.close()
        L_0x0073:
            return r0
        L_0x0074:
            r9 = move-exception
            if (r8 == 0) goto L_0x007a
            r8.close()
        L_0x007a:
            if (r1 == 0) goto L_0x007f
            r1.close()
        L_0x007f:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.test.FilesUtils.exportFile(java.io.File, java.io.File):java.io.File");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0098, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0099, code lost:
        r4.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a0, code lost:
        return r0.getAbsolutePath();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0094 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String inputStreamToSavedFile(java.io.InputStream r4, Context r5, String r6) {
        /*
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.io.File r2 = android.os.Environment.getExternalStorageDirectory()
            java.lang.String r2 = r2.getAbsolutePath()
            r1.append(r2)
            java.lang.String r2 = "/"
            r1.append(r2)
            r2 = 2131755050(0x7f10002a, float:1.9140968E38)
            java.lang.String r3 = r5.getString(r2)
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            deleteAllFiles(r0)
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.io.File r3 = android.os.Environment.getExternalStorageDirectory()
            java.lang.String r3 = r3.getAbsolutePath()
            r1.append(r3)
            java.lang.String r3 = "/"
            r1.append(r3)
            java.lang.String r5 = r5.getString(r2)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r0.<init>(r5, r6)
            r0.createNewFile()     // Catch:{ IOException -> 0x0083 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0083 }
            r5.<init>(r0)     // Catch:{ IOException -> 0x0083 }
            r6 = 4096(0x1000, float:5.74E-42)
            byte[] r6 = new byte[r6]     // Catch:{ all -> 0x007e }
        L_0x005c:
            int r1 = r4.read(r6)     // Catch:{ all -> 0x007e }
            r2 = -1
            if (r1 == r2) goto L_0x0068
            r2 = 0
            r5.write(r6, r2, r1)     // Catch:{ all -> 0x007e }
            goto L_0x005c
        L_0x0068:
            r5.flush()     // Catch:{ all -> 0x007e }
            r0.getAbsolutePath()     // Catch:{ all -> 0x007e }
            r5.close()     // Catch:{ IOException -> 0x0083 }
            r4.close()     // Catch:{ IOException -> 0x0075 }
            goto L_0x0079
        L_0x0075:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0079:
            java.lang.String r4 = r0.getAbsolutePath()
            return r4
        L_0x007e:
            r6 = move-exception
            r5.close()     // Catch:{ IOException -> 0x0083 }
            throw r6     // Catch:{ IOException -> 0x0083 }
        L_0x0083:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ all -> 0x0094 }
            r4.close()     // Catch:{ IOException -> 0x008b }
            goto L_0x008f
        L_0x008b:
            r4 = move-exception
            r4.printStackTrace()
        L_0x008f:
            java.lang.String r4 = r0.getAbsolutePath()
            return r4
        L_0x0094:
            r4.close()     // Catch:{ IOException -> 0x0098 }
            goto L_0x009c
        L_0x0098:
            r4 = move-exception
            r4.printStackTrace()
        L_0x009c:
            java.lang.String r4 = r0.getAbsolutePath()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.test.FilesUtils.inputStreamToSavedFile(java.io.InputStream, android.content.Context, java.lang.String):java.lang.String");
    }

    public static String getActualIDOfPack(String str) {
        File[] listFiles = new File(str).listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].getName().contains(".json")) {
                return listFiles[i].getName().replace(".json", "");
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0030, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x003e, code lost:
        r5 = r2;
        r2 = r1;
        r1 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x004b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x004f, code lost:
        if (r6 != null) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0051, code lost:
        if (r7 != null) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0057, code lost:
        r6.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getUriSize(android.net.Uri r6, Context r7) throws IOException {
        /*
            android.content.ContentResolver r7 = r7.getContentResolver()
            java.io.InputStream r6 = r7.openInputStream(r6)
            r7 = 0
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x004d }
            r0.<init>()     // Catch:{ Throwable -> 0x004d }
            if (r6 == 0) goto L_0x0034
            r1 = 16384(0x4000, float:2.2959E-41)
            byte[] r1 = new byte[r1]     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
        L_0x0014:
            int r2 = r1.length     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            r3 = 0
            int r2 = r6.read(r1, r3, r2)     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            r4 = -1
            if (r2 == r4) goto L_0x0021
            r0.write(r1, r3, r2)     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            goto L_0x0014
        L_0x0021:
            byte[] r1 = r0.toByteArray()     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            int r1 = r1.length     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            r0.close()     // Catch:{ Throwable -> 0x004d }
            if (r6 == 0) goto L_0x002e
            r6.close()
        L_0x002e:
            return r1
        L_0x002f:
            r1 = move-exception
            r2 = r7
            goto L_0x0041
        L_0x0032:
            r1 = move-exception
            goto L_0x003c
        L_0x0034:
            java.io.IOException r1 = new java.io.IOException     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            java.lang.String r2 = "cannot get URI SIZE"
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
            throw r1     // Catch:{ Throwable -> 0x0032, all -> 0x002f }
        L_0x003c:
            throw r1     // Catch:{ all -> 0x003d }
        L_0x003d:
            r2 = move-exception
            r5 = r2
            r2 = r1
            r1 = r5
        L_0x0041:
            if (r2 == 0) goto L_0x0047
            r0.close()     // Catch:{ Throwable -> 0x004a }
            goto L_0x004a
        L_0x0047:
            r0.close()     // Catch:{ Throwable -> 0x004d }
        L_0x004a:
            throw r1     // Catch:{ Throwable -> 0x004d }
        L_0x004b:
            r0 = move-exception
            goto L_0x004f
        L_0x004d:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x004b }
        L_0x004f:
            if (r6 == 0) goto L_0x005a
            if (r7 == 0) goto L_0x0057
            r6.close()     // Catch:{ Throwable -> 0x005a }
            goto L_0x005a
        L_0x0057:
            r6.close()
        L_0x005a:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.test.FilesUtils.getUriSize(android.net.Uri, android.content.Context):int");
    }
}
