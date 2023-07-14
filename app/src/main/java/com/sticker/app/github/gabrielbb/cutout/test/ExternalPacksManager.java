package com.sticker.app.github.gabrielbb.cutout.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import com.sticker.app.R;
import java.io.File;
import java.util.ArrayList;

public class ExternalPacksManager {
    public static void sendStickerPackZipThroughWhatsApp(Context context, String str) {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        FilesUtils.handleCopyFilesToSdCard(context, str);
        Uri fromFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getString(R.string.app_name) + "/" + str + ".zip"));
        Uri parse = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_sticker), (String) null, (String) null));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("*/*");
        intent.addFlags(1);
        ArrayList arrayList = new ArrayList();
        arrayList.add(parse);
        arrayList.add(fromFile);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.setPackage("com.whatsapp");
        context.startActivity(intent);
    }
}
