package com.sticker.app.github.gabrielbb.cutout.test;

import android.content.Context;
import android.util.Log;
import com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode.StickerPack;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class StickerBook {
    public static ArrayList<StickerPack> allStickerPacks = checkIfPacksAreNull();
    private static Context myContext;

    public static void init(Context context) {
        myContext = context;
        ArrayList<StickerPack> readStickerPackJSON = DataArchiver.readStickerPackJSON(context);
        if (readStickerPackJSON != null && readStickerPackJSON.size() != 0) {
            allStickerPacks = readStickerPackJSON;
        }
    }

    private static ArrayList<StickerPack> checkIfPacksAreNull() {
        if (allStickerPacks == null) {
            Log.w("IS PACKS NULL?", "YES");
            return new ArrayList<>();
        }
        Log.w("IS PACKS NULL?", "NO");
        return allStickerPacks;
    }

    public static void addStickerPackExisting(StickerPack stickerPack) {
        allStickerPacks.add(stickerPack);
    }

    public static ArrayList<StickerPack> getAllStickerPacks() {
        return allStickerPacks;
    }

    public static StickerPack getStickerPackByName(String str) {
        Iterator<StickerPack> it = allStickerPacks.iterator();
        while (it.hasNext()) {
            StickerPack next = it.next();
            if (next.getName().equals(str)) {
                return next;
            }
        }
        return null;
    }

    public static StickerPack getStickerPackById(String str) {
        if (allStickerPacks.isEmpty()) {
            init(myContext);
        }
        Log.w("THIS IS THE ALL STICKER", allStickerPacks.toString());
        Iterator<StickerPack> it = allStickerPacks.iterator();
        while (it.hasNext()) {
            StickerPack next = it.next();
            if (next.getIdentifier().equals(str)) {
                return next;
            }
        }
        return null;
    }

    public static StickerPack getStickerPackByIdWithContext(String str, Context context) {
        if (allStickerPacks.isEmpty()) {
            init(context);
        }
        Log.w("THIS IS THE ALL STICKER", allStickerPacks.toString());
        Iterator<StickerPack> it = allStickerPacks.iterator();
        while (it.hasNext()) {
            StickerPack next = it.next();
            if (next.getIdentifier().equals(str)) {
                return next;
            }
        }
        return null;
    }

    public static void deleteStickerPackById(String str) {
        StickerPack stickerPackById = getStickerPackById(str);
        new File(stickerPackById.getTrayImageUri().getPath()).getParentFile().delete();
        allStickerPacks.remove(stickerPackById);
    }

    public static StickerPack getStickerPackByIndex(int i) {
        return allStickerPacks.get(i);
    }
}
