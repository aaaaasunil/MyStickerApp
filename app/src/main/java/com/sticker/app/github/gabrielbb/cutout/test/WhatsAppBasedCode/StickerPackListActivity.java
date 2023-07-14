package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sticker.app.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.sticker.app.github.gabrielbb.cutout.test.BuildConfig;
import com.sticker.app.github.gabrielbb.cutout.test.DataArchiver;
import com.sticker.app.github.gabrielbb.cutout.test.StickerBook;
import com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode.StickerPackListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;


public class StickerPackListActivity extends BaseActivity {
    public static final String EXTRA_STICKER_PACK_LIST_DATA = "sticker_pack_list";
    private static final int STICKER_PREVIEW_DISPLAY_LIMIT = 5;
    private static final String TAG = "StickerPackList";
    /* access modifiers changed from: private */
    public static StickerPackListAdapter allStickerPacksListAdapter;
    public static Context context;
    public static String newCreator;
    public static String newName;
    private static RecyclerView packRecyclerView;
    AdRequest adRequest;
    private AdView adView;
    private ImageView ivAddNewPack;
    private ImageView ivBack;
    private ImageView ivCreatePack;
    private ImageView ivInfo;
    private StickerPackListAdapter.OnAddButtonClickedListener onAddButtonClickedListener = new StickerPackListAdapter.OnAddButtonClickedListener() {
        public void onAddButtonClicked(StickerPack stickerPack) {
            if (stickerPack.getStickers().size() >= 3) {
                Intent intent = new Intent();
                intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
                intent.putExtra("sticker_pack_id", stickerPack.identifier);
                intent.putExtra("sticker_pack_authority", BuildConfig.CONTENT_PROVIDER_AUTHORITY2);
                intent.putExtra("sticker_pack_name", stickerPack.name);
                try {
                    StickerPackListActivity.this.startActivityForResult(intent, 200);
                } catch (ActivityNotFoundException unused) {
                    Toast.makeText(StickerPackListActivity.this, R.string.error_adding_sticker_pack, Toast.LENGTH_SHORT).show();
                }
            } else {
                AlertDialog create = new AlertDialog.Builder(StickerPackListActivity.this).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
                create.setTitle("Invalid Action");
                create.setMessage("In order to be applied to WhatsApp, the sticker pack must have at least morning stickers. Please add more stickers first.");
                create.show();
            }
        }
    };
    private LinearLayoutManager packLayoutManager;
    ArrayList<StickerPack> stickerPackList;
    private TextView tvCreateSticker;
    private TextView tvDataNotFound;
    WhiteListCheckAsyncTask whiteListCheckAsyncTask;

    private void startInAppPurchase(String str) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sticker_pack_list);
        initView();
    }

    private void initView() {
        Uri uri;
        this.tvDataNotFound = (TextView) findViewById(R.id.tvDataNotFound);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.ivInfo = (ImageView) findViewById(R.id.ivInfo);
        this.ivCreatePack = (ImageView) findViewById(R.id.ivCreatePack);
        this.ivCreatePack.setVisibility(View.VISIBLE);;
        this.tvCreateSticker = (TextView) findViewById(R.id.tvCreateSticker);
        this.tvCreateSticker.setVisibility(View.GONE);;

        this.ivAddNewPack = (ImageView) findViewById(R.id.ivAddNewPack);
        this.ivAddNewPack.setVisibility(View.GONE);;
        context = getApplicationContext();
        packRecyclerView = (RecyclerView) findViewById(R.id.sticker_pack_list);
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackListActivity.this.finish();
            }
        });
        this.ivInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackListActivity.this.finish();
            }
        });
        this.ivCreatePack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackListActivity stickerPackListActivity = StickerPackListActivity.this;
                stickerPackListActivity.showDialog(stickerPackListActivity);
            }
        });
        this.stickerPackList = StickerBook.getAllStickerPacks();
        if (StickerBook.getAllStickerPacks().isEmpty()) {
            StickerBook.init(this);
        }
        showStickerPackList(this.stickerPackList);
        if ("android.intent.action.SEND".equals(getIntent().getAction())) {
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("android.intent.extra.STREAM") && (uri = (Uri) extras.getParcelable("android.intent.extra.STREAM")) != null) {
                DataArchiver.importZipFileToStickerPack(uri, this);
            }
        }
        new MaterialIntroView.Builder(this).enableIcon(false).setFocusGravity(FocusGravity.CENTER).setFocusType(Focus.MINIMUM).setDelayMillis(500).enableFadeAnimation(true).performClick(false).setInfoText("ADD TO WHATSAPP..").setShape(ShapeType.RECTANGLE).setTarget(this.ivCreatePack).setUsageId("intro_card2").show();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (getIntent().getAction() == null) {
            Log.v("Example", "Force restart");
            Intent intent = new Intent(this, StickerPackListActivity.class);
            intent.setAction("Already created");
            startActivity(intent);
            finish();
        }
        this.whiteListCheckAsyncTask = new WhiteListCheckAsyncTask(this);
        this.whiteListCheckAsyncTask.execute(new List[]{this.stickerPackList});
        if (this.stickerPackList.size() == 0) {
            this.tvDataNotFound.setVisibility(View.VISIBLE);;
        } else {
            this.tvDataNotFound.setVisibility(View.GONE);;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        DataArchiver.writeStickerBookJSON(StickerBook.getAllStickerPacks(), this);
        WhiteListCheckAsyncTask whiteListCheckAsyncTask2 = this.whiteListCheckAsyncTask;
        if (whiteListCheckAsyncTask2 != null && !whiteListCheckAsyncTask2.isCancelled()) {
            this.whiteListCheckAsyncTask.cancel(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        DataArchiver.writeStickerBookJSON(StickerBook.getAllStickerPacks(), this);
        super.onDestroy();
    }

    public void showStickerPackList(List<StickerPack> list) {
        allStickerPacksListAdapter = new StickerPackListAdapter(list, this.onAddButtonClickedListener);
        packRecyclerView.setAdapter(allStickerPacksListAdapter);
        this.packLayoutManager = new LinearLayoutManager(this);
        this.packLayoutManager.setOrientation(1);
        packRecyclerView.setLayoutManager(this.packLayoutManager);
        packRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public final void onGlobalLayout() {
                StickerPackListActivity.this.recalculateColumnCount();
            }
        });
    }

    /* access modifiers changed from: private */
    public void recalculateColumnCount() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size);
        StickerPackListItemViewHolder stickerPackListItemViewHolder = (StickerPackListItemViewHolder) packRecyclerView.findViewHolderForAdapterPosition(this.packLayoutManager.findFirstVisibleItemPosition());
        if (stickerPackListItemViewHolder != null) {
            allStickerPacksListAdapter.setMaxNumberOfStickersInARow(Math.min(5, Math.max(stickerPackListItemViewHolder.imageRowView.getMeasuredWidth() / dimensionPixelSize, 1)));
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        String stringExtra;
        super.onActivityResult(i, i2, intent);
        if (i == 200) {
            if (i2 == 0 && intent != null && (stringExtra = intent.getStringExtra("validation_error")) != null) {
                Log.e(TAG, "Validation failed:" + stringExtra);
            }
        } else if (intent != null && i == 2319) {
            createNewStickerPackAndOpenIt(newName, newCreator, intent.getData());
        } else if (i == 1114) {
            makeIntroNotRunAgain();
            new MaterialIntroView.Builder(this).enableIcon(false).setFocusGravity(FocusGravity.CENTER).setFocusType(Focus.MINIMUM).setDelayMillis(500).enableFadeAnimation(true).performClick(true).setInfoText("Create a sticker packs.").setShape(ShapeType.CIRCLE).setTarget(findViewById(R.id.action_add)).setUsageId("intro_card").show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    static class WhiteListCheckAsyncTask extends AsyncTask<List<StickerPack>, Void, List<StickerPack>> {
        private final WeakReference<StickerPackListActivity> stickerPackListActivityWeakReference;

        WhiteListCheckAsyncTask(StickerPackListActivity stickerPackListActivity) {
            this.stickerPackListActivityWeakReference = new WeakReference<>(stickerPackListActivity);
        }

        /* access modifiers changed from: protected */
        @SafeVarargs
        public final List<StickerPack> doInBackground(List<StickerPack>... listArr) {
            List<StickerPack> list = listArr[0];
            StickerPackListActivity stickerPackListActivity = (StickerPackListActivity) this.stickerPackListActivityWeakReference.get();
            if (stickerPackListActivity == null) {
                return list;
            }
            for (StickerPack next : list) {
                next.setIsWhitelisted(WhitelistCheck.isWhitelisted(stickerPackListActivity, next.identifier));
            }
            return list;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<StickerPack> list) {
            if (((StickerPackListActivity) this.stickerPackListActivityWeakReference.get()) != null) {
                StickerPackListActivity.allStickerPacksListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1 && iArr[0] != 0) {
            AlertDialog create = new AlertDialog.Builder(this).setPositiveButton("Let's Go", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    StickerPackListActivity.verifyStoragePermissions(StickerPackListActivity.this);
                }
            }).create();
            create.setTitle("Notice!");
            create.setMessage("We've recognized you denied the storage access permission for this app.\n\nIn order for this app to work, storage access is required.");
            create.show();
        }
    }

    private void addNewStickerPackInInterface() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Sticker Pack");
        builder.setMessage("Please specify title and creator for the pack.");
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        final EditText editText = new EditText(this);
        editText.setLines(1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(50, 0, 50, 10);
        editText.setLayoutParams(layoutParams);
        editText.setHint("Pack Name");
        editText.setInputType(65536);
        linearLayout.addView(editText);
        final EditText editText2 = new EditText(this);
        if (Build.VERSION.SDK_INT >= 26) {
            editText2.setAutofillHints(new String[]{"name"});
        }
        editText2.setLines(1);
        editText2.setLayoutParams(layoutParams);
        editText2.setInputType(65536);
        editText2.setHint("Creator");
        linearLayout.addView(editText2);
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", (DialogInterface.OnClickListener) null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog create = builder.create();
        create.show();
        final Button button = create.getButton(-1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText())) {
                    editText.setError("Package name is required!");
                }
                if (TextUtils.isEmpty(editText2.getText())) {
                    editText2.setError("Creator is required!");
                }
                if (!TextUtils.isEmpty(editText.getText()) && !TextUtils.isEmpty(editText2.getText())) {
                    create.dismiss();
                    StickerPackListActivity.this.createDialogForPickingIconImage(editText, editText2);
                }
            }
        });
        editText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent == null || keyEvent.getKeyCode() != 66) && i != 6) {
                    return false;
                }
                button.performClick();
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void createDialogForPickingIconImage(final EditText editText, final EditText editText2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick your pack's icon image");
        builder.setMessage("Now you will pick the new sticker pack's icon image.").setCancelable(false).setPositiveButton("Let's go", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                StickerPackListActivity.this.openFileTray(editText.getText().toString(), editText2.getText().toString());
            }
        });
        builder.create().show();
    }

    private void createNewStickerPackAndOpenIt(String str, String str2, Uri uri) {
        String uuid = UUID.randomUUID().toString();
        StickerBook.addStickerPackExisting(new StickerPack(uuid, str, str2, uri, "", "", "", "", this));
        Intent intent = new Intent(this, StickerPackDetailsActivity.class);
        intent.putExtra("show_up_button", true);
        intent.putExtra("sticker_pack", uuid);
        intent.putExtra("isNewlyCreated", true);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void openFileTray(String str, String str2) {
        Intent intent = new Intent();
        intent.setType("image/*");
        newName = str;
        newCreator = str2;
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, ""), 2319);
    }

    private void makeIntroNotRunAgain() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (!defaultSharedPreferences.getBoolean("isAlreadyShown", false)) {
            SharedPreferences.Editor edit = defaultSharedPreferences.edit();
            edit.putBoolean("isAlreadyShown", false);
            edit.commit();
        }
    }

    private boolean toShowIntro() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("isAlreadyShown", true);
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_create_package);
        final EditText editText = (EditText) dialog.findViewById(R.id.etPackageName);
        final EditText editText2 = (EditText) dialog.findViewById(R.id.etCreatorName);
        ((TextView) dialog.findViewById(R.id.tvCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.tvCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                editText.setError((CharSequence) null);
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                editText2.setError((CharSequence) null);
            }
        });
        ((TextView) dialog.findViewById(R.id.tvOk)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText())) {
                    editText.setError("Package name is required!");
                }
                if (TextUtils.isEmpty(editText2.getText())) {
                    editText2.setError("Creator is required!");
                }
                if (!TextUtils.isEmpty(editText.getText()) && !TextUtils.isEmpty(editText2.getText())) {
                    dialog.dismiss();
                    StickerPackListActivity stickerPackListActivity = StickerPackListActivity.this;
                    stickerPackListActivity.dialogChossePackIcon(stickerPackListActivity, editText, editText2);
                }
            }
        });
        dialog.show();
    }

    public void dialogChossePackIcon(Activity activity, final EditText editText, final EditText editText2) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemen_2;
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_chosse_trayicon);
        ((TextView) dialog.findViewById(R.id.tvOk)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackListActivity.this.openFileTray(editText.getText().toString(), editText2.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }
}
