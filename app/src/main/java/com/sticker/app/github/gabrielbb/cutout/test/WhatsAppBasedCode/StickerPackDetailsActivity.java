package com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.sticker.app.R;
import com.sticker.app.github.gabrielbb.cutout.CutOut;
import com.sticker.app.github.gabrielbb.cutout.SaveDrawingTask;
import com.sticker.app.github.gabrielbb.cutout.test.BuildConfig;
import com.sticker.app.github.gabrielbb.cutout.test.DataArchiver;
import com.sticker.app.github.gabrielbb.cutout.test.StickerBook;
import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class StickerPackDetailsActivity extends BaseActivity {
    public static final int ADD_PACK = 200;
    public static final short CUTOUT_ACTIVITY_REQUEST_CODE = 368;
    public static final String EXTRA_SHOW_UP_BUTTON = "show_up_button";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_DATA = "sticker_pack";
    public static final String EXTRA_STICKER_PACK_EMAIL = "sticker_pack_email";
    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    public static final String EXTRA_STICKER_PACK_PRIVACY_POLICY = "sticker_pack_privacy_policy";
    public static final String EXTRA_STICKER_PACK_TRAY_ICON = "sticker_pack_tray_icon";
    public static final String EXTRA_STICKER_PACK_WEBSITE = "sticker_pack_website";
    private static final String TAG = "StickerPackDetails";
    private View addButton;
    private ImageView deleteButton;
    /* access modifiers changed from: private */
    public View divider;
    private final RecyclerView.OnScrollListener dividerScrollListener = new RecyclerView.OnScrollListener() {
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            updateDivider(recyclerView);
        }

        public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            updateDivider(recyclerView);
        }

        private void updateDivider(RecyclerView recyclerView) {
            int i = 0;
            boolean z = recyclerView.computeVerticalScrollOffset() > 0;
            if (StickerPackDetailsActivity.this.divider != null) {
                View access$800 = StickerPackDetailsActivity.this.divider;
                if (!z) {
                    i = 4;
                }
                access$800.setVisibility(View.VISIBLE);
            }
        }
    };
    /* access modifiers changed from: private */
    public ImageView ivAddClose;
    private ImageView ivBack;
    /* access modifiers changed from: private */
    public ImageView ivBg;
    private ImageView ivInfo;
    private CircleImageView ivTrayImage;
    private RelativeLayout ivfloatingBtn;
    private GridLayoutManager layoutManager;
    private int numColumns;
    private final ViewTreeObserver.OnGlobalLayoutListener pageLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
            stickerPackDetailsActivity.setNumColumns(stickerPackDetailsActivity.recyclerView.getWidth() / StickerPackDetailsActivity.this.recyclerView.getContext().getResources().getDimensionPixelSize(R.dimen.sticker_pack_details_image_size));
        }
    };
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    private RelativeLayout rlSharePack;
    private RelativeLayout rlbackground;
    /* access modifiers changed from: private */
    public StickerPack stickerPack;
    /* access modifiers changed from: private */
    public StickerPreviewAdapter stickerPreviewAdapter;
    /* access modifiers changed from: private */
    public TextView tvAddSticker;
    /* access modifiers changed from: private */
    public TextView tvAddWhatsApp;
    private View tvAlreadyAdded;
    /* access modifiers changed from: private */
    public TextView tvDeletePack;
    /* access modifiers changed from: private */

    /* renamed from: up */
    public boolean f94up;
    private WhiteListCheckAsyncTask whiteListCheckAsyncTask;

    private void floatingAction() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.sticker_pack_details);
        initView();
    }

    private void initView() {
        getIntent().getBooleanExtra("show_up_button", false);
        TextView textView = (TextView) findViewById(R.id.tvPackName);
        TextView textView2 = (TextView) findViewById(R.id.tvAuthor);
        this.ivTrayImage = (CircleImageView) findViewById(R.id.ivTrayImage);
        this.stickerPack = StickerBook.getStickerPackById(getIntent().getStringExtra("sticker_pack"));
        this.rlbackground = (RelativeLayout) findViewById(R.id.rlbackground);
        this.tvAddWhatsApp = (TextView) findViewById(R.id.tvAddWhatsApp);
        this.ivAddClose = (ImageView) findViewById(R.id.ivAddClose);
        this.tvAddSticker = (TextView) findViewById(R.id.tvAddSticker);
        this.tvDeletePack = (TextView) findViewById(R.id.tvDeletePack);
        this.ivInfo = (ImageView) findViewById(R.id.ivInfo);
        this.ivBg = (ImageView) findViewById(R.id.ivBg);
        this.ivfloatingBtn = (RelativeLayout) findViewById(R.id.ivfloatingBtn);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.rlSharePack = (RelativeLayout) findViewById(R.id.rlSharePack);
        this.tvAlreadyAdded = findViewById(R.id.tvAlreadyAdded);
        this.layoutManager = new GridLayoutManager(this, 1);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this.pageLayoutListener);
        this.recyclerView.addOnScrollListener(this.dividerScrollListener);
        this.divider = findViewById(R.id.divider);
        if (this.stickerPreviewAdapter == null) {
            this.stickerPreviewAdapter = new StickerPreviewAdapter(getLayoutInflater(), R.drawable.sticker_error, getResources().getDimensionPixelSize(R.dimen.sticker_pack_details_image_size), getResources().getDimensionPixelSize(R.dimen.sticker_pack_details_image_size), this.stickerPack);
            this.recyclerView.setAdapter(this.stickerPreviewAdapter);
        }
        textView.setText(this.stickerPack.name);
        textView2.setText(this.stickerPack.publisher);
        this.ivTrayImage.setImageURI(this.stickerPack.getTrayImageUri());
        clicks();
    }

    private void clicks() {
        this.ivTrayImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
                stickerPackDetailsActivity.dialogForTaryIcon(stickerPackDetailsActivity, stickerPackDetailsActivity.stickerPack);
            }
        });
        findViewById(R.id.llAddToWhatsApp).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CutOut.activity().start(StickerPackDetailsActivity.this);
            }
        });
        this.ivInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StickerPackDetailsActivity.this.stickerPack != null) {
                    StickerPackDetailsActivity.this.launchInfoActivity(StickerPackDetailsActivity.this.stickerPack.publisherWebsite, StickerPackDetailsActivity.this.stickerPack.publisherEmail, StickerPackDetailsActivity.this.stickerPack.privacyPolicyWebsite, StickerPackDetailsActivity.this.stickerPack.getTrayImageUri().toString());
                }
            }
        });
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackDetailsActivity.this.finish();
            }
        });
        this.rlSharePack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DataArchiver.createZipFileFromStickerPack(StickerPackDetailsActivity.this.stickerPack, StickerPackDetailsActivity.this);
            }
        });
        this.tvAddWhatsApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StickerPackDetailsActivity.this.stickerPack.getStickers().size() >= 3) {
                    StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity.addStickerPackToWhatsApp(stickerPackDetailsActivity.stickerPack);
                } else {
                    StickerPackDetailsActivity stickerPackDetailsActivity2 = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity2.dialogAddWhatAppSticker(stickerPackDetailsActivity2);
                }
                if (StickerPackDetailsActivity.this.f94up) {
                    StickerPackDetailsActivity stickerPackDetailsActivity3 = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity3.slideDown(stickerPackDetailsActivity3.ivBg);
                } else {
                    StickerPackDetailsActivity stickerPackDetailsActivity4 = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity4.slideUp(stickerPackDetailsActivity4.ivBg);
                    StickerPackDetailsActivity.this.ivAddClose.setImageResource(R.drawable.ic_close);
                }
                StickerPackDetailsActivity stickerPackDetailsActivity5 = StickerPackDetailsActivity.this;
                boolean unused = stickerPackDetailsActivity5.f94up = !stickerPackDetailsActivity5.f94up;
            }
        });
        this.tvDeletePack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StickerPackDetailsActivity.this.f94up) {
                    StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity.slideDown(stickerPackDetailsActivity.ivBg);
                } else {
                    StickerPackDetailsActivity stickerPackDetailsActivity2 = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity2.slideUp(stickerPackDetailsActivity2.ivBg);
                    StickerPackDetailsActivity.this.ivAddClose.setImageResource(R.drawable.ic_close);
                }
                StickerPackDetailsActivity stickerPackDetailsActivity3 = StickerPackDetailsActivity.this;
                boolean unused = stickerPackDetailsActivity3.f94up = !stickerPackDetailsActivity3.f94up;
                StickerPackDetailsActivity stickerPackDetailsActivity4 = StickerPackDetailsActivity.this;
                stickerPackDetailsActivity4.dialogPack(stickerPackDetailsActivity4);
            }
        });
        this.tvAddSticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StickerPackDetailsActivity.this.f94up) {
                    StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity.slideDown(stickerPackDetailsActivity.ivBg);
                } else {
                    StickerPackDetailsActivity stickerPackDetailsActivity2 = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity2.slideUp(stickerPackDetailsActivity2.ivBg);
                    StickerPackDetailsActivity.this.ivAddClose.setImageResource(R.drawable.ic_close);
                }
                StickerPackDetailsActivity stickerPackDetailsActivity3 = StickerPackDetailsActivity.this;
                boolean unused = stickerPackDetailsActivity3.f94up = !stickerPackDetailsActivity3.f94up;
                CutOut.activity().start(StickerPackDetailsActivity.this);
            }
        });
        customeFloatingButton();
    }

    /* access modifiers changed from: private */
    public void launchInfoActivity(String str, String str2, String str3, String str4) {
        Intent intent = new Intent(this, StickerPackInfoActivity.class);
        intent.putExtra("sticker_pack_id", this.stickerPack.identifier);
        intent.putExtra("sticker_pack_website", str);
        intent.putExtra("sticker_pack_email", str2);
        intent.putExtra("sticker_pack_privacy_policy", str3);
        intent.putExtra("sticker_pack_tray_icon", this.stickerPack.getTrayImageUri().toString());
        startActivity(intent);
    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PathInterpolatorCompat.MAX_NUM_POINTS);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        StickerPack stickerPack2;
        if (menuItem.getItemId() != R.id.action_info || (stickerPack2 = this.stickerPack) == null) {
            return super.onOptionsItemSelected(menuItem);
        }
        launchInfoActivity(stickerPack2.publisherWebsite, this.stickerPack.publisherEmail, this.stickerPack.privacyPolicyWebsite, this.stickerPack.getTrayImageUri().toString());
        return true;
    }

    /* access modifiers changed from: private */
    public void addStickerPackToWhatsApp(StickerPack stickerPack2) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        Log.w("IS IT A NEW IDENTIFIER?", stickerPack2.getIdentifier());
        intent.putExtra("sticker_pack_id", stickerPack2.getIdentifier());
        intent.putExtra("sticker_pack_authority", BuildConfig.CONTENT_PROVIDER_AUTHORITY2);
        intent.putExtra("sticker_pack_name", stickerPack2.getName());
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, R.string.error_adding_sticker_pack, Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        String stringExtra;
        super.onActivityResult(i, i2, intent);
        if (i == 200) {
            if (i2 == 0 && intent != null && (stringExtra = intent.getStringExtra("validation_error")) != null) {
                Log.e(TAG, "Validation Failed:" + stringExtra);
            }
        } else if (i == 368) {
            if (intent == null && !SaveDrawingTask.SAVED_IMAGE_URi.equals("png")) {
                Uri parse = Uri.parse(SaveDrawingTask.SAVED_IMAGE_URi);
                SaveDrawingTask.SAVED_IMAGE_URi = "png";
                this.stickerPack.addSticker(parse, this);
            }
            if (intent != null) {
                if (intent.getClipData() != null) {
                    ClipData clipData = intent.getClipData();
                    for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                        clipData.getItemAt(i3);
                    }
                } else {
                    Uri uri = CutOut.getUri(intent);
                    SaveDrawingTask.SAVED_IMAGE_URi = "png";
                    this.stickerPack.addSticker(uri, this);
                }
                finish();
                startActivity(getIntent());
            }
        }
    }

    /* access modifiers changed from: private */
    public void setNumColumns(int i) {
        if (this.numColumns != i) {
            this.layoutManager.setSpanCount(i);
            this.numColumns = i;
            StickerPreviewAdapter stickerPreviewAdapter2 = this.stickerPreviewAdapter;
            if (stickerPreviewAdapter2 != null) {
                stickerPreviewAdapter2.notifyDataSetChanged();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!SaveDrawingTask.SAVED_IMAGE_URi.equals("png")) {
            Uri parse = Uri.parse(SaveDrawingTask.SAVED_IMAGE_URi);
            SaveDrawingTask.SAVED_IMAGE_URi = "png";
            this.stickerPack.addSticker(parse, this);
        }
        this.whiteListCheckAsyncTask = new WhiteListCheckAsyncTask(this);
        this.whiteListCheckAsyncTask.execute(new StickerPack[]{this.stickerPack});
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        WhiteListCheckAsyncTask whiteListCheckAsyncTask2 = this.whiteListCheckAsyncTask;
        if (whiteListCheckAsyncTask2 != null && !whiteListCheckAsyncTask2.isCancelled()) {
            this.whiteListCheckAsyncTask.cancel(true);
        }
    }

    /* access modifiers changed from: private */
    public void updateAddUI(Boolean bool) {
        if (bool.booleanValue()) {
            this.tvAlreadyAdded.setVisibility(View.VISIBLE);;
        } else {
            this.tvAlreadyAdded.setVisibility(View.GONE);;
        }
    }

    class WhiteListCheckAsyncTask extends AsyncTask<StickerPack, Void, Boolean> {
        private final WeakReference<StickerPackDetailsActivity> stickerPackDetailsActivityWeakReference;

        WhiteListCheckAsyncTask(StickerPackDetailsActivity stickerPackDetailsActivity) {
            this.stickerPackDetailsActivityWeakReference = new WeakReference<>(stickerPackDetailsActivity);
        }

        /* access modifiers changed from: protected */
        public final Boolean doInBackground(StickerPack... stickerPackArr) {
            StickerPack stickerPack = stickerPackArr[0];
            StickerPackDetailsActivity stickerPackDetailsActivity = (StickerPackDetailsActivity) this.stickerPackDetailsActivityWeakReference.get();
            if (stickerPackDetailsActivity == null) {
                return false;
            }
            return Boolean.valueOf(WhitelistCheck.isWhitelisted(stickerPackDetailsActivity, stickerPack.identifier));
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            StickerPackDetailsActivity stickerPackDetailsActivity = (StickerPackDetailsActivity) this.stickerPackDetailsActivityWeakReference.get();
            if (stickerPackDetailsActivity != null) {
                stickerPackDetailsActivity.updateAddUI(bool);
                StickerPackDetailsActivity stickerPackDetailsActivity2 = StickerPackDetailsActivity.this;
                StickerPack unused = stickerPackDetailsActivity2.stickerPack = StickerBook.getStickerPackById(stickerPackDetailsActivity2.getIntent().getStringExtra("sticker_pack"));
                if (StickerPackDetailsActivity.this.stickerPreviewAdapter != null) {
                    StickerPackDetailsActivity.this.stickerPreviewAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void dialogPack(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemen_2;
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_delete_pack);
        ((TextView) dialog.findViewById(R.id.tvOk)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerBook.deleteStickerPackById(StickerPackDetailsActivity.this.stickerPack.getIdentifier());
                StickerPackDetailsActivity.this.finish();
                Toast.makeText(StickerPackDetailsActivity.this, "Sticker Pack deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.tvCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void dialogAddWhatAppSticker(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_add_to_whatapp);
        TextView textView = (TextView) dialog.findViewById(R.id.tvCancel);
        ((TextView) dialog.findViewById(R.id.tvOk)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void customeFloatingButton() {
        this.ivfloatingBtn.setVisibility(View.VISIBLE);;
        this.rlbackground.setVisibility(View.VISIBLE);;
        this.ivBg.setVisibility(View.INVISIBLE);
        this.tvAddSticker.setVisibility(View.INVISIBLE);
        this.tvDeletePack.setVisibility(View.INVISIBLE);
        this.tvAddWhatsApp.setVisibility(View.INVISIBLE);
        this.ivfloatingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StickerPackDetailsActivity.this.f94up) {
                    StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity.slideDown(stickerPackDetailsActivity.ivBg);
                } else {
                    StickerPackDetailsActivity stickerPackDetailsActivity2 = StickerPackDetailsActivity.this;
                    stickerPackDetailsActivity2.slideUp(stickerPackDetailsActivity2.ivBg);
                    StickerPackDetailsActivity.this.ivAddClose.setImageResource(R.drawable.ic_close);
                }
                StickerPackDetailsActivity stickerPackDetailsActivity3 = StickerPackDetailsActivity.this;
                boolean unused = stickerPackDetailsActivity3.f94up = !stickerPackDetailsActivity3.f94up;
            }
        });
    }

    public void slideDown(View view) {
        this.ivAddClose.setImageResource(R.drawable.ic_add);
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) view.getHeight());
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        this.tvDeletePack.startAnimation(loadAnimation);
        this.tvAddSticker.startAnimation(loadAnimation);
        this.tvAddWhatsApp.startAnimation(loadAnimation);
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);;
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) view.getHeight(), 0.0f);
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Animation loadAnimation = AnimationUtils.loadAnimation(StickerPackDetailsActivity.this.getApplicationContext(), R.anim.fadein);
                StickerPackDetailsActivity.this.tvDeletePack.startAnimation(loadAnimation);
                StickerPackDetailsActivity.this.tvAddSticker.startAnimation(loadAnimation);
                StickerPackDetailsActivity.this.tvAddWhatsApp.startAnimation(loadAnimation);
            }
        }, 100);
    }

    public void dialogForTaryIcon(Context context, StickerPack stickerPack2) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemen_3;
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_delete_sticker);
        TextView textView = (TextView) dialog.findViewById(R.id.tvCancel);
        ((ImageView) dialog.findViewById(R.id.imageView)).setImageURI(stickerPack2.getTrayImageUri());
        dialog.dismiss();
        ((TextView) dialog.findViewById(R.id.tvOk)).setVisibility(View.GONE);;
        textView.setText("OK");
        ((TextView) dialog.findViewById(R.id.tvText)).setVisibility(View.GONE);;
        ((TextView) dialog.findViewById(R.id.tvTitle)).setText("Tray Icon");
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
