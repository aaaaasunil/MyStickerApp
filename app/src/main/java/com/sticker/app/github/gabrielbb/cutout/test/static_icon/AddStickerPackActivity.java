package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sticker.app.R;
import com.sticker.app.github.gabrielbb.cutout.test.BuildConfig;
import com.sticker.app.github.gabrielbb.cutout.test.static_icon.AddStickerPackActivity;

public abstract class AddStickerPackActivity extends BaseActivity {
    private static final int ADD_PACK = 200;
    private static final String TAG = "AddStickerPackActivity";

    /* access modifiers changed from: protected */
    public void addStickerPackToWhatsApp(String str, String str2) {
        try {
            if (WhitelistCheck.isWhatsAppConsumerAppInstalled(getPackageManager()) || WhitelistCheck.isWhatsAppSmbAppInstalled(getPackageManager())) {
                boolean isStickerPackWhitelistedInWhatsAppConsumer = WhitelistCheck.isStickerPackWhitelistedInWhatsAppConsumer(this, str);
                boolean isStickerPackWhitelistedInWhatsAppSmb = WhitelistCheck.isStickerPackWhitelistedInWhatsAppSmb(this, str);
                if (!isStickerPackWhitelistedInWhatsAppConsumer && !isStickerPackWhitelistedInWhatsAppSmb) {
                    launchIntentToAddPackToChooser(str, str2);
                } else if (!isStickerPackWhitelistedInWhatsAppConsumer) {
                    launchIntentToAddPackToSpecificPackage(str, str2, "com.whatsapp");
                } else if (!isStickerPackWhitelistedInWhatsAppSmb) {
                    launchIntentToAddPackToSpecificPackage(str, str2, "com.whatsapp.w4b");
                } else {
                    Toast.makeText(this, "add_pack fail prompt update whatsapp", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Add pack fail prompt update whatsapp", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "error adding sticker pack to WhatsApp", e);
            Toast.makeText(this, "add pack fail prompt update whatsapp", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchIntentToAddPackToSpecificPackage(String str, String str2, String str3) {
        Intent createIntentToAddStickerPack = createIntentToAddStickerPack(str, str2);
        createIntentToAddStickerPack.setPackage(str3);
        try {
            startActivityForResult(createIntentToAddStickerPack, 200);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, "add_pack_fail_prompt_update_whatsapp", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchIntentToAddPackToChooser(String str, String str2) {
        try {
            startActivityForResult(Intent.createChooser(createIntentToAddStickerPack(str, str2), getString(R.string.add_to_whatsapp)), 200);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, "add_pack_fail_prompt_update_whatsapp", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private Intent createIntentToAddStickerPack(String str, String str2) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra("sticker_pack_id", str);
        intent.putExtra("sticker_pack_authority", BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra("sticker_pack_name", str2);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 200 || i2 != 0) {
            return;
        }
        if (intent != null) {
            String stringExtra = intent.getStringExtra("validation_error");
            if (stringExtra != null) {
                Log.e(TAG, "Validation failed:" + stringExtra);
                return;
            }
            return;
        }
        new StickerPackNotAddedMessageFragment().show(getSupportFragmentManager(), "sticker_pack_not_added");
    }

    public static final class StickerPackNotAddedMessageFragment extends DialogFragment {
        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getActivity()).setMessage((CharSequence) "add_pack_fail_prompt_update_whatsapp").setCancelable(true).setPositiveButton("OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    StickerPackNotAddedMessageFragment.this.dismiss();
                }
            }).setNeutralButton((CharSequence) "add_pack_fail_prompt_update_play_link", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    StickerPackNotAddedMessageFragment.this.launchWhatsAppPlayStorePage();
                }
            }).create();
        }

        /* access modifiers changed from: private */
        public void launchWhatsAppPlayStorePage() {
            if (getActivity() != null) {
                PackageManager packageManager = getActivity().getPackageManager();
                boolean isPackageInstalled = WhitelistCheck.isPackageInstalled("com.whatsapp", packageManager);
                boolean isPackageInstalled2 = WhitelistCheck.isPackageInstalled("com.whatsapp.w4b", packageManager);
                if (isPackageInstalled && isPackageInstalled2) {
                    launchPlayStoreWithUri("https://play.google.com/store/apps/developer?id=WhatsApp+Inc.");
                } else if (isPackageInstalled) {
                    launchPlayStoreWithUri("http://play.google.com/store/apps/details?id=com.whatsapp");
                } else if (isPackageInstalled2) {
                    launchPlayStoreWithUri("http://play.google.com/store/apps/details?id=com.whatsapp.w4b");
                }
            }
        }

        private void launchPlayStoreWithUri(String str) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setPackage("com.android.vending");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(getActivity(), "cannot_find_play_store", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
