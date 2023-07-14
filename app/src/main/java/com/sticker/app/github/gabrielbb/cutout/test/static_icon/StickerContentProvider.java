package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.sticker.app.github.gabrielbb.cutout.test.BuildConfig;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StickerContentProvider extends ContentProvider {
    public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
  //  public static final Uri AUTHORITY_URI = new Uri.Builder().scheme("content").authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath("metadata").build();

    public static final String AUTHORITY = "com.sticker.app.stickercontentprovider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY + "/metadata");
    public static final String AVOID_CACHE = "whatsapp_will_not_cache_stickers";
    private static final String CONTENT_FILE_NAME = "contents.json";
    public static final String IMAGE_DATA_VERSION = "image_data_version";
    public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
    public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String METADATA = "metadata";
    private static final int METADATA_CODE = 1;
    private static final int METADATA_CODE_FOR_SINGLE_PACK = 2;
    public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
    public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
    public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
    static final String STICKERS = "stickers";
    static final String STICKERS_ASSET = "stickers_asset";
    private static final int STICKERS_ASSET_CODE = 4;
    private static final int STICKERS_CODE = 3;
    public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji";
    public static final String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name";
    public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
    public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
    public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
    private static final int STICKER_PACK_TRAY_ICON_CODE = 5;
    private List<StickerPack> stickerPackList=new ArrayList<>();

    public boolean onCreate() {
        Context context = getContext();
        context.getClass();
        if (BuildConfig.CONTENT_PROVIDER_AUTHORITY.startsWith(context.getPackageName())) {
            MATCHER.addURI(AUTHORITY, "metadata", 1);
            MATCHER.addURI(AUTHORITY, "metadata/*", 2);
            MATCHER.addURI(AUTHORITY, "stickers/*", 3);
            for (StickerPack next : getStickerPackList()) {
                UriMatcher uriMatcher = MATCHER;
                uriMatcher.addURI(AUTHORITY, "stickers_asset/" + next.identifier + "/" + next.trayImageFile, 5);
                for (Sticker sticker : next.getStickers()) {
                    UriMatcher uriMatcher2 = MATCHER;
                    uriMatcher2.addURI(BuildConfig.CONTENT_PROVIDER_AUTHORITY, "stickers_asset/" + next.identifier + "/" + sticker.imageFileName, 4);
                }
            }
            return true;
        }
        throw new IllegalStateException("your authority (com.sticker.app.stickercontentprovider) for the content provider should start with your package name: " + getContext().getPackageName());
    }

    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, String str, String[] strArr2, String str2) {
        int match = MATCHER.match(uri);
        if (match == 1) {
            return getPackForAllStickerPacks(uri);
        }
        if (match == 2) {
            return getCursorForSingleStickerPack(uri);
        }
        if (match == 3) {
            return getStickersForAStickerPack(uri);
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    @Nullable
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String str) {
        int match = MATCHER.match(uri);
        if (match == 4 || match == 5) {
            return getImageAsset(uri);
        }
        return null;
    }

    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case 1:
                return "vnd.android.cursor.dir/vnd.com.sticker.app.stickercontentprovider.metadata";
            case 2:
                return "vnd.android.cursor.item/vnd.com.sticker.app.stickercontentprovider.metadata";
            case 3:
                return "vnd.android.cursor.dir/vnd.com.sticker.app.stickercontentprovider.stickers";
            case 4:
                return "image/webp";
            case 5:
                return "image/png";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private synchronized void readContentFile(@NonNull Context context) {
        InputStream open;
        try {
            open = context.getAssets().open(CONTENT_FILE_NAME);
            this.stickerPackList = ContentFileParser.parseStickerPacks(open);
            if (open != null) {
                open.close();
            }
        } catch (IOException | IllegalStateException e) {
            throw new RuntimeException("contents.json file has some issues: " + e.getMessage(), e);
        } catch (Throwable unused) {
        }
        return;
        //throw th;
    }

    private List<StickerPack> getStickerPackList() {
        if (this.stickerPackList == null) {
            Context context = getContext();
            context.getClass();
            readContentFile(context);
        }
        return this.stickerPackList;
    }

    private Cursor getPackForAllStickerPacks(@NonNull Uri uri) {
        return getStickerPackInfo(uri, getStickerPackList());
    }

    private Cursor getCursorForSingleStickerPack(@NonNull Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        for (StickerPack next : getStickerPackList()) {
            if (lastPathSegment.equals(next.identifier)) {
                return getStickerPackInfo(uri, Collections.singletonList(next));
            }
        }
        return getStickerPackInfo(uri, new ArrayList());
    }

    @NonNull
    private Cursor getStickerPackInfo(@NonNull Uri uri, @NonNull List<StickerPack> list) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"sticker_pack_identifier", "sticker_pack_name", "sticker_pack_publisher", "sticker_pack_icon", "android_play_store_link", "ios_app_download_link", "sticker_pack_publisher_email", "sticker_pack_publisher_website", "sticker_pack_privacy_policy_website", "sticker_pack_license_agreement_website", IMAGE_DATA_VERSION, AVOID_CACHE});
        for (StickerPack next : list) {
            MatrixCursor.RowBuilder newRow = matrixCursor.newRow();
            newRow.add(next.identifier);
            newRow.add(next.name);
            newRow.add(next.publisher);
            newRow.add(next.trayImageFile);
            newRow.add(next.androidPlayStoreLink);
            newRow.add(next.iosAppStoreLink);
            newRow.add(next.publisherEmail);
            newRow.add(next.publisherWebsite);
            newRow.add(next.privacyPolicyWebsite);
            newRow.add(next.licenseAgreementWebsite);
            newRow.add(next.imageDataVersion);
            newRow.add(Integer.valueOf(next.avoidCache ? 1 : 0));
        }
        Context context = getContext();
        context.getClass();
        matrixCursor.setNotificationUri(context.getContentResolver(), uri);
        return matrixCursor;
    }

    @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"sticker_file_name", "sticker_emoji"});
        for (StickerPack next : getStickerPackList()) {
            if (lastPathSegment.equals(next.identifier)) {
                for (Sticker next2 : next.getStickers()) {
                    matrixCursor.addRow(new Object[]{next2.imageFileName, TextUtils.join(",", next2.emojis)});
                }
            }
        }
        Context context = getContext();
        context.getClass();
        matrixCursor.setNotificationUri(context.getContentResolver(), uri);
        return matrixCursor;
    }

    private AssetFileDescriptor getImageAsset(Uri uri) throws IllegalArgumentException {
        Context context = getContext();
        context.getClass();
        AssetManager assets = context.getAssets();
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 3) {
            String str = pathSegments.get(pathSegments.size() - 1);
            String str2 = pathSegments.get(pathSegments.size() - 2);
            if (TextUtils.isEmpty(str2)) {
                throw new IllegalArgumentException("identifier is empty, uri: " + uri);
            } else if (!TextUtils.isEmpty(str)) {
                for (StickerPack next : getStickerPackList()) {
                    if (str2.equals(next.identifier)) {
                        if (str.equals(next.trayImageFile)) {
                            return fetchFile(uri, assets, str, str2);
                        }
                        for (Sticker sticker : next.getStickers()) {
                            if (str.equals(sticker.imageFileName)) {
                                return fetchFile(uri, assets, str, str2);
                            }
                        }
                        continue;
                    }
                }
                return null;
            } else {
                throw new IllegalArgumentException("file name is empty, uri: " + uri);
            }
        } else {
            throw new IllegalArgumentException("path segments should be morning, uri is: " + uri);
        }
    }

    private AssetFileDescriptor fetchFile(@NonNull Uri uri, @NonNull AssetManager assetManager, @NonNull String str, @NonNull String str2) {
        try {
            return assetManager.openFd(str2 + "/" + str);
        } catch (IOException e) {
            Context context = getContext();
            context.getClass();
            String packageName = context.getPackageName();
            Log.e(packageName, "IOException when getting asset file, uri:" + uri, e);
            return null;
        }
    }

    public int delete(@NonNull Uri uri, @Nullable String str, String[] strArr) {
        throw new UnsupportedOperationException("Not supported");
    }

    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("Not supported");
    }

    public int update(@NonNull Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("Not supported");
    }
}
