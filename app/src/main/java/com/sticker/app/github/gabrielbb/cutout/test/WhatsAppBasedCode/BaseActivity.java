package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public abstract class BaseActivity extends AppCompatActivity {
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static final class MessageDialogFragment extends DialogFragment {
        private static final String ARG_MESSAGE = "message";
        private static final String ARG_TITLE_ID = "title_id";

        public static DialogFragment newInstance(@StringRes int i, String str) {
            MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_TITLE_ID, i);
            bundle.putString("message", str);
            messageDialogFragment.setArguments(bundle);
            return messageDialogFragment;
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            int i = getArguments().getInt(ARG_TITLE_ID);
            AlertDialog.Builder positiveButton = new AlertDialog.Builder(getActivity()).setMessage((CharSequence) getArguments().getString("message")).setCancelable(true).setPositiveButton(17039370, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MessageDialogFragment.this.dismiss();
                }
            });
            if (i != 0) {
                positiveButton.setTitle(i);
            }
            return positiveButton.create();
        }
    }
}
