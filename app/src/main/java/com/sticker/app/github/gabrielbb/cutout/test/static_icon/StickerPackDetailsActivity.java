package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.sticker.app.R;
import com.sticker.app.github.gabrielbb.cutout.test.utils.Constants;

import java.lang.ref.WeakReference;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import de.hdodenhof.circleimageview.CircleImageView;

public class StickerPackDetailsActivity extends AddStickerPackActivity {
    public static final String EXTRA_SHOW_UP_BUTTON = "show_up_button";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_DATA = "sticker_pack";
    public static final String EXTRA_STICKER_PACK_EMAIL = "sticker_pack_email";
    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_LICENSE_AGREEMENT = "sticker_pack_license_agreement";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    public static final String EXTRA_STICKER_PACK_PRIVACY_POLICY = "sticker_pack_privacy_policy";
    public static final String EXTRA_STICKER_PACK_TRAY_ICON = "sticker_pack_tray_icon";
    public static final String EXTRA_STICKER_PACK_WEBSITE = "sticker_pack_website";
    private View addButton;
    private View alreadyAddedText;
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
                View access$500 = StickerPackDetailsActivity.this.divider;
                if (!z) {
                    i = 4;
                }
                access$500.setVisibility(View.VISIBLE);
            }
        }
    };
    private ImageView ivBack;
    private ImageView ivInfo;
    private GridLayoutManager layoutManager;
    private LinearLayout llAlreadyAdded;
    /* access modifiers changed from: private */
    public InterstitialAd mInterstitialAd;
    private int numColumns;
    private final ViewTreeObserver.OnGlobalLayoutListener pageLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
            stickerPackDetailsActivity.setNumColumns(stickerPackDetailsActivity.recyclerView.getWidth() / StickerPackDetailsActivity.this.recyclerView.getContext().getResources().getDimensionPixelSize(R.dimen.sticker_pack_details_image_size));
        }
    };
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public StickerPack stickerPack;
    private StickerPreviewAdapter stickerPreviewAdapter;
    private TextView tvWhattAppAdd;
    private WhiteListCheckAsyncTask whiteListCheckAsyncTask;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sticker_pack_details);
        MobileAds.initialize(this, "ca-app-pub-2044501287137385~8630720099");
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                StickerPackDetailsActivity.this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        if (Constants.isAddOpen.equals("isAddShow")) {
            this.mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    if (StickerPackDetailsActivity.this.mInterstitialAd.isLoaded()) {
                        StickerPackDetailsActivity.this.mInterstitialAd.show();
                    }
                }
            });
            Constants.isAddOpen = "isAddShowed";
        }
        boolean booleanExtra = getIntent().getBooleanExtra("show_up_button", false);
        this.stickerPack = (StickerPack) getIntent().getParcelableExtra("sticker_pack");
        TextView textView = (TextView) findViewById(R.id.pack_name);
        TextView textView2 = (TextView) findViewById(R.id.author);
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.tray_image);
        TextView textView3 = (TextView) findViewById(R.id.pack_size);
        this.ivInfo = (ImageView) findViewById(R.id.ivInfo);
        this.llAlreadyAdded = (LinearLayout) findViewById(R.id.llAlreadyAdded);
        this.addButton = findViewById(R.id.add_to_whatsapp_button);
        new MaterialIntroView.Builder(this).enableIcon(false).setFocusGravity(FocusGravity.CENTER).setFocusType(Focus.MINIMUM).setDelayMillis(500).enableFadeAnimation(true).performClick(false).setInfoText("ADD TO WHATSAPP..").setShape(ShapeType.RECTANGLE).setTarget(this.addButton).setUsageId("intro_card2").show();
        this.alreadyAddedText = findViewById(R.id.already_added_text);
        this.layoutManager = new GridLayoutManager(this, 1);
        this.recyclerView = (RecyclerView) findViewById(R.id.sticker_list);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.ivInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Uri stickerAssetUri = StickerPackLoader.getStickerAssetUri(StickerPackDetailsActivity.this.stickerPack.identifier, StickerPackDetailsActivity.this.stickerPack.trayImageFile);
                StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
                stickerPackDetailsActivity.launchInfoActivity(stickerPackDetailsActivity.stickerPack.publisherWebsite, StickerPackDetailsActivity.this.stickerPack.publisherEmail, StickerPackDetailsActivity.this.stickerPack.privacyPolicyWebsite, StickerPackDetailsActivity.this.stickerPack.licenseAgreementWebsite, stickerAssetUri.toString());
            }
        });
        this.ivInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Uri stickerAssetUri = StickerPackLoader.getStickerAssetUri(StickerPackDetailsActivity.this.stickerPack.identifier, StickerPackDetailsActivity.this.stickerPack.trayImageFile);
                StickerPackDetailsActivity stickerPackDetailsActivity = StickerPackDetailsActivity.this;
                stickerPackDetailsActivity.launchInfoActivity(stickerPackDetailsActivity.stickerPack.publisherWebsite, StickerPackDetailsActivity.this.stickerPack.publisherEmail, StickerPackDetailsActivity.this.stickerPack.privacyPolicyWebsite, StickerPackDetailsActivity.this.stickerPack.licenseAgreementWebsite, stickerAssetUri.toString());
            }
        });
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackDetailsActivity.this.finish();
            }
        });
        this.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this.pageLayoutListener);
        this.recyclerView.addOnScrollListener(this.dividerScrollListener);
        this.divider = findViewById(R.id.divider);
        if (this.stickerPreviewAdapter == null) {
            this.stickerPreviewAdapter = new StickerPreviewAdapter(getLayoutInflater(), R.drawable.sticker_error, getResources().getDimensionPixelSize(R.dimen.sticker_pack_details_image_size), getResources().getDimensionPixelSize(R.dimen.sticker_pack_details_image_size), this.stickerPack);
            this.recyclerView.setAdapter(this.stickerPreviewAdapter);
        }
        textView.setText(this.stickerPack.name);
        textView2.setText(this.stickerPack.publisher);
        circleImageView.setImageURI(StickerPackLoader.getStickerAssetUri(this.stickerPack.identifier, this.stickerPack.trayImageFile));
        textView3.setText(Formatter.formatShortFileSize(this, this.stickerPack.getTotalSize()));
        this.addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackDetailsActivity.this.addStickerPackToWhatsApp(StickerPackDetailsActivity.this.stickerPack.identifier, StickerPackDetailsActivity.this.stickerPack.name);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(booleanExtra);
          //  getSupportActionBar().setTitle((CharSequence) booleanExtra ? getResources().getString(R.string.title_activity_sticker_pack_details_multiple_pack) : getResources().getQuantityString(R.plurals.title_activity_sticker_packs_list, 1));
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_sticker_pack_details_multiple_pack));
        }
    }

    /* access modifiers changed from: private */
    public void launchInfoActivity(String str, String str2, String str3, String str4, String str5) {
        Intent intent = new Intent(this, StickerPackInfoActivity.class);
        intent.putExtra("sticker_pack_id", this.stickerPack.identifier);
        intent.putExtra("sticker_pack_website", str);
        intent.putExtra("sticker_pack_email", str2);
        intent.putExtra("sticker_pack_privacy_policy", str3);
        intent.putExtra(EXTRA_STICKER_PACK_LICENSE_AGREEMENT, str4);
        intent.putExtra("sticker_pack_tray_icon", str5);
        startActivity(intent);
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
        launchInfoActivity(this.stickerPack.publisherWebsite, this.stickerPack.publisherEmail, this.stickerPack.privacyPolicyWebsite, this.stickerPack.licenseAgreementWebsite, StickerPackLoader.getStickerAssetUri(stickerPack2.identifier, this.stickerPack.trayImageFile).toString());
        return true;
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
        this.whiteListCheckAsyncTask = new WhiteListCheckAsyncTask(this);
        this.whiteListCheckAsyncTask.execute(this.stickerPack);
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
            this.llAlreadyAdded.setVisibility(View.VISIBLE);
            this.addButton.setVisibility(View.GONE);
            this.alreadyAddedText.setVisibility(View.VISIBLE);
            return;
        }
        this.addButton.setVisibility(View.VISIBLE);
        this.alreadyAddedText.setVisibility(View.GONE);
    }

    static class WhiteListCheckAsyncTask extends AsyncTask<StickerPack, Void, Boolean> {
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
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }
}
