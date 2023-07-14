package com.sticker.app.github.gabrielbb.cutout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Pair;
import java.io.File;
import java.lang.ref.WeakReference;

public class SaveDrawingTask extends AsyncTask<Bitmap, Void, Pair<File, Exception>> {
    private static final String SAVED_IMAGE_NAME = "cutout_tmp";
    public static String SAVED_IMAGE_URi = "png";
    private WeakReference<StickerViewActivity> activityWeakReference;
    Context context;
    String text = this.text;

    SaveDrawingTask(StickerViewActivity stickerViewActivity) {
        this.activityWeakReference = new WeakReference<>(stickerViewActivity);
        this.context = stickerViewActivity;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0095, code lost:
        r8 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0096, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x009a, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x009b, code lost:
        r6 = r1;
        r1 = r8;
        r8 = r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Pair<File, Exception> doInBackground(Bitmap... r8) {
        /*
            r7 = this;
            r0 = 0
            java.lang.String r1 = r7.text     // Catch:{ IOException -> 0x00a8 }
            java.io.File r1 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ IOException -> 0x00a8 }
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x00a8 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a8 }
            r3.<init>()     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r1 = r1.getAbsolutePath()     // Catch:{ IOException -> 0x00a8 }
            r3.append(r1)     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r1 = "/EraseBG"
            r3.append(r1)     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r1 = r3.toString()     // Catch:{ IOException -> 0x00a8 }
            r2.<init>(r1)     // Catch:{ IOException -> 0x00a8 }
            boolean r1 = r2.exists()     // Catch:{ IOException -> 0x00a8 }
            if (r1 != 0) goto L_0x002a
            r2.mkdirs()     // Catch:{ IOException -> 0x00a8 }
        L_0x002a:
            java.io.File r1 = new java.io.File     // Catch:{ IOException -> 0x00a8 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a8 }
            r3.<init>()     // Catch:{ IOException -> 0x00a8 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00a8 }
            r3.append(r4)     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r4 = ".png"
            r3.append(r4)     // Catch:{ IOException -> 0x00a8 }
            java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x00a8 }
            r1.<init>(r2, r3)     // Catch:{ IOException -> 0x00a8 }
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00a8 }
            r2.<init>(r1)     // Catch:{ IOException -> 0x00a8 }
            r3 = 0
            r8 = r8[r3]     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            java.lang.ref.WeakReference<com.sticker.app.github.gabrielbb.cutout.StickerViewActivity> r4 = r7.activityWeakReference     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            java.lang.Object r4 = r4.get()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            com.sticker.app.github.gabrielbb.cutout.StickerViewActivity r4 = (com.sticker.app.github.gabrielbb.cutout.StickerViewActivity) r4     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            android.content.Context r4 = r4.getApplicationContext()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            android.content.res.Resources r4 = r4.getResources()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            android.util.DisplayMetrics r4 = r4.getDisplayMetrics()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            float r4 = r4.density     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r5 = 70
            r8.compress(r4, r5, r2)     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r2.flush()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r2.close()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            java.lang.ref.WeakReference<com.sticker.app.github.gabrielbb.cutout.StickerViewActivity> r8 = r7.activityWeakReference     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            java.lang.Object r8 = r8.get()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            com.sticker.app.github.gabrielbb.cutout.StickerViewActivity r8 = (com.sticker.app.github.gabrielbb.cutout.StickerViewActivity) r8     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            android.content.Context r8 = r8.getApplicationContext()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            java.lang.String r5 = r1.toString()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r4[r3] = r5     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            com.sticker.app.github.gabrielbb.cutout.SaveDrawingTask$1 r3 = new com.sticker.app.github.gabrielbb.cutout.SaveDrawingTask$1     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r3.<init>()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            android.media.MediaScannerConnection.scanFile(r8, r4, r0, r3)     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            android.util.Pair r8 = new android.util.Pair     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r8.<init>(r1, r0)     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r2.close()     // Catch:{ IOException -> 0x00a8 }
            return r8
        L_0x0095:
            r8 = move-exception
            r1 = r0
            goto L_0x009e
        L_0x0098:
            r8 = move-exception
            throw r8     // Catch:{ all -> 0x009a }
        L_0x009a:
            r1 = move-exception
            r6 = r1
            r1 = r8
            r8 = r6
        L_0x009e:
            if (r1 == 0) goto L_0x00a4
            r2.close()     // Catch:{ Throwable -> 0x00a7 }
            goto L_0x00a7
        L_0x00a4:
            r2.close()     // Catch:{ IOException -> 0x00a8 }
        L_0x00a7:
            throw r8     // Catch:{ IOException -> 0x00a8 }
        L_0x00a8:
            r8 = move-exception
            android.util.Pair r1 = new android.util.Pair
            r1.<init>(r0, r8)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sticker.app.github.gabrielbb.cutout.SaveDrawingTask.doInBackground(android.graphics.Bitmap[]):android.util.Pair");
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Pair<File, Exception> pair) {
        super.onPostExecute(pair);
        Intent intent = new Intent();
        if (pair.first != null) {
            Uri fromFile = Uri.fromFile((File) pair.first);
            SAVED_IMAGE_URi = String.valueOf(fromFile);
            intent.putExtra("CUTOUT_EXTRA_RESULT", fromFile);
            ((StickerViewActivity) this.activityWeakReference.get()).setResult(-1, intent);
            ((StickerViewActivity) this.activityWeakReference.get()).finish();
            return;
        }
        ((StickerViewActivity) this.activityWeakReference.get()).exitWithError((Exception) pair.second);
    }
}
