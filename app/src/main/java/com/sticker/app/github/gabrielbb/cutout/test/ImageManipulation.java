package com.sticker.app.github.gabrielbb.cutout.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageManipulation {
    public static Uri convertImageToWebP(Uri uri, String str, String str2, Context context) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            dirChecker(context.getFilesDir() + "/" + str);
            String str3 = context.getFilesDir() + "/" + str + "/" + str + "-" + str2 + ".webp";
            Log.w("Conversion Data: ", "path: " + str3);
            makeSmallestBitmapCompatible(str3, bitmap);
            return Uri.fromFile(new File(str3));
        } catch (Exception e) {
            e.printStackTrace();
            return uri;
        }
    }

    public static Uri convertIconTrayToWebP(Uri uri, String str, String str2, Context context) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            dirChecker(context.getFilesDir() + "/" + str);
            String str3 = context.getFilesDir() + "/" + str + "/" + str + "-" + str2 + ".webp";
            Log.w("Conversion Data: ", "path: " + str3);
            FileOutputStream fileOutputStream = new FileOutputStream(str3);
            Bitmap.createScaledBitmap(bitmap, 256, 256, true).compress(Bitmap.CompressFormat.WEBP, 60, fileOutputStream);
            fileOutputStream.close();
            return Uri.fromFile(new File(str3));
        } catch (Exception e) {
            e.printStackTrace();
            return uri;
        }
    }

    private static void dirChecker(String str) {
        File file = new File(str);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

    private static byte[] getByteArray(Bitmap bitmap, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, i, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static void makeSmallestBitmapCompatible(String str, Bitmap bitmap) {
        FileOutputStream fileOutputStream;
        int i;
        bitmap.getWidth();
        bitmap.getHeight();
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fileOutputStream = null;
        }
        float width = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
        int i2 = 512;
        if (1.0f > width) {
            i = (int) (width * 512.0f);
        } else {
            i2 = (int) (512.0f / width);
            i = 512;
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i2, true);
        int i3 = 100000;
        int i4 = 100;
        while (i3 / 1000 >= 100) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            createScaledBitmap.compress(Bitmap.CompressFormat.WEBP, i4, byteArrayOutputStream);
            i3 = byteArrayOutputStream.toByteArray().length;
            i4 -= 10;
            Log.w("IMAGE SIZE IS NOW", i3 + "");
        }
        try {
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
