package com.sticker.app.github.gabrielbb.cutout.test;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode.Sticker;
import com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode.StickerPack;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DataArchiver {
    private static int BUFFER = 8192;

    public static boolean writeStickerBookJSON(List<StickerPack> list, Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("StickerMaker", 0);
            String json = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializer()).create().toJson((Object) list, new TypeToken<ArrayList<StickerPack>>() {
            }.getType());
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("stickerbook", json);
            edit.apply();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<StickerPack> readStickerPackJSON(Context context) {
        return (ArrayList) new GsonBuilder().registerTypeAdapter(Uri.class, new UriDeserializer()).create().fromJson(context.getSharedPreferences("StickerMaker", 0).getString("stickerbook", ""), new TypeToken<ArrayList<StickerPack>>() {
        }.getType());
    }

    public static String createZipFileFromStickerPack(StickerPack stickerPack, Context context) {
        String str = context.getFilesDir() + "/" + stickerPack.getIdentifier();
        new createZip(context, stickerPack).execute(new Void[0]);
        return str + "/" + stickerPack.getIdentifier() + ".zip";
    }

    public static void importZipFileToStickerPack(Uri uri, Context context) {
        String str = "";
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            str = FilesUtils.inputStreamToSavedFile(openInputStream, context, UUID.randomUUID().toString() + ".zip");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new parseZip(context, str).execute(new Void[0]);
    }

    public static void zip(ArrayList<String> arrayList, String str) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(str)));
            byte[] bArr = new byte[BUFFER];
            for (int i = 0; i < arrayList.size(); i++) {
                Log.v("Compress", "Adding: " + arrayList.get(i));
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(arrayList.get(i)), BUFFER);
                zipOutputStream.putNextEntry(new ZipEntry(arrayList.get(i).substring(arrayList.get(i).lastIndexOf("/") + 1)));
                while (true) {
                    int read = bufferedInputStream.read(bArr, 0, BUFFER);
                    if (read == -1) {
                        break;
                    }
                    zipOutputStream.write(bArr, 0, read);
                }
                bufferedInputStream.close();
            }
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzip(String str, String str2) {
        dirChecker(str2);
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str));
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    Log.w("DECOMPRESSING FILE", nextEntry.getName());
                    if (nextEntry.isDirectory()) {
                        dirChecker(nextEntry.getName());
                    } else {
                        FileOutputStream fileOutputStream = new FileOutputStream(str2 + nextEntry.getName());
                        while (true) {
                            int read = zipInputStream.read();
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(read);
                        }
                        zipInputStream.closeEntry();
                        fileOutputStream.close();
                    }
                } else {
                    zipInputStream.close();
                    Log.w("ENDED DECOMPRESSING", "DONEEEEEE");
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void dirChecker(String str) {
        File file = new File(str);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

    /* access modifiers changed from: private */
    public static void stickerPackToJSONFile(StickerPack stickerPack, String str, Context context) {
        try {
            String json = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializer()).create().toJson((Object) stickerPack, (Type) StickerPack.class);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(str, stickerPack.getIdentifier() + ".json")));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /* access modifiers changed from: private */
    public static StickerPack JSONFileToStickerPack(String str, String str2, Context context) {
        String str3 = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str2, str + ".json"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            }
            fileInputStream.close();
            str3 = sb.toString();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e2) {
            Log.e("login activity", "Can not read file: " + e2.toString());
        }
        return (StickerPack) new GsonBuilder().registerTypeAdapter(Uri.class, new UriDeserializer()).create().fromJson(str3, StickerPack.class);
    }

    public static class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri uri, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(uri.toString());
        }
    }

    public static class UriDeserializer implements JsonDeserializer<Uri> {
        public Uri deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Uri.parse(jsonElement.toString().replace("\"", ""));
        }
    }

    private static class createZip extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        /* renamed from: sp */
        private StickerPack f81sp;

        public createZip(Context context, StickerPack stickerPack) {
            this.dialog = new ProgressDialog(context);
            this.f81sp = stickerPack;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.dialog.setMessage("Creating Sticker Pack Zip, Please wait...");
            this.dialog.show();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            String identifier = this.f81sp.getIdentifier();
           // this.dialog.getContext().getFilesDir() + "/" + identifier;
            ExternalPacksManager.sendStickerPackZipThroughWhatsApp(this.dialog.getContext(), identifier);
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            String identifier = this.f81sp.getIdentifier();
            ArrayList arrayList = new ArrayList();
            String str = this.dialog.getContext().getFilesDir() + "/" + identifier;
            DataArchiver.stickerPackToJSONFile(this.f81sp, str + "/", this.dialog.getContext());
            for (Sticker imageFileName : this.f81sp.getStickers()) {
                arrayList.add(str + "/" + this.f81sp.getIdentifier() + "-" + imageFileName.getImageFileName() + ".webp");
            }
            arrayList.add(str + "/" + this.f81sp.getIdentifier() + "-trayImage.webp");
            arrayList.add(str + "/" + this.f81sp.getIdentifier() + ".json");
            DataArchiver.zip(arrayList, str + "/" + this.f81sp.getIdentifier() + ".zip");
            return null;
        }
    }

    private static class parseZip extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private boolean isAlreadyLoaded = false;
        private boolean isFailed = false;
        private String zipPath;

        public parseZip(Context context, String str) {
            this.dialog = new ProgressDialog(context);
            this.dialog.setCancelable(false);
            this.zipPath = str;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.dialog.setMessage("Parsing Sticker Pack Zip, Please Wait...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (this.isAlreadyLoaded) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.dialog.getContext());
                builder.setMessage("Sticker Pack is already loaded.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
            if (this.isFailed) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this.dialog.getContext());
                builder2.setMessage("The zip file that was imported is not a proper sticker pack file.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder2.create().show();
            }
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            File file;
            String replace = this.zipPath.split("/")[this.zipPath.split("/").length - 1].replace(".zip", "");
            DataArchiver.unzip(this.zipPath, this.dialog.getContext().getFilesDir() + "/" + replace + "/");
            StringBuilder sb = new StringBuilder();
            sb.append(this.dialog.getContext().getFilesDir());
            sb.append("/");
            sb.append(replace);
            String actualIDOfPack = FilesUtils.getActualIDOfPack(sb.toString() + "/");
            File file2 = new File(this.dialog.getContext().getFilesDir(), replace);
            if (StickerBook.getStickerPackById(actualIDOfPack) == null) {
                try {
                    file = new File(this.dialog.getContext().getFilesDir(), actualIDOfPack);
                    try {
                        file2.renameTo(file);
                        StickerBook.addStickerPackExisting(DataArchiver.JSONFileToStickerPack(actualIDOfPack, file.getAbsolutePath(), this.dialog.getContext()));
                    } catch (Exception unused) {
                    }
                } catch (Exception unused2) {
                    file = null;
                    this.isFailed = true;
                    if (file != null) {
                        file.delete();
                    } else {
                        file2.delete();
                    }
                    return null;
                }
            } else {
                file2.delete();
                this.isAlreadyLoaded = true;
            }
            return null;
        }
    }
}
