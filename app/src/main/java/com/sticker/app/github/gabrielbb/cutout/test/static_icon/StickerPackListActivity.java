package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sticker.app.R;
import com.sticker.app.github.gabrielbb.cutout.test.static_icon.StickerPackListAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;


public class StickerPackListActivity extends AddStickerPackActivity {
    public static final String EXTRA_STICKER_PACK_LIST_DATA = "sticker_pack_list";
    private static final int STICKER_PREVIEW_DISPLAY_LIMIT = 5;
    /* access modifiers changed from: private */
    public StickerPackListAdapter allStickerPacksListAdapter;
    String bbb;
    private ImageView ivAddNewPack;
    private ImageView ivBack;
    private final StickerPackListAdapter.OnAddButtonClickedListener onAddButtonClickedListener = new StickerPackListAdapter.OnAddButtonClickedListener() {
        public final void onAddButtonClicked(StickerPack stickerPack) {
            StickerPackListActivity.this.addStickerPackToWhatsApp(stickerPack.identifier, stickerPack.name);
        }
    };
    private LinearLayoutManager packLayoutManager;
    private RecyclerView packRecyclerView;
    private ArrayList<StickerPack> stickerPackList;
    private TextView tvCreateSticker;
    private WhiteListCheckAsyncTask whiteListCheckAsyncTask;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sticker_pack_list);
        initView();
    }

    private void initView() {
        this.packRecyclerView = (RecyclerView) findViewById(R.id.sticker_pack_list);
        this.ivAddNewPack = (ImageView) findViewById(R.id.ivAddNewPack);
        this.stickerPackList = getIntent().getParcelableArrayListExtra("sticker_pack_list");
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.ivBack.setVisibility(View.GONE);;
        this.tvCreateSticker = (TextView) findViewById(R.id.tvCreateSticker);
        this.ivAddNewPack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StickerPackListActivity stickerPackListActivity = StickerPackListActivity.this;
                stickerPackListActivity.startActivity(new Intent(stickerPackListActivity, com.sticker.app.github.gabrielbb.cutout.test.WhatsAppBasedCode.StickerPackListActivity.class));
            }
        });
        showStickerPackList(this.stickerPackList);
        new MaterialIntroView.Builder(this).enableIcon(true).
                setFocusGravity(FocusGravity.CENTER).setFocusType(Focus.MINIMUM).
                setDelayMillis(500).enableFadeAnimation(true).
                performClick(false).setInfoText("To create your own sticker packs, click here.").setShape(ShapeType.CIRCLE).setTarget(this.ivAddNewPack).setUsageId("intro_card").show();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.whiteListCheckAsyncTask = new WhiteListCheckAsyncTask(this);
        this.whiteListCheckAsyncTask.execute(this.stickerPackList.toArray(new StickerPack[0]));
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        WhiteListCheckAsyncTask whiteListCheckAsyncTask2 = this.whiteListCheckAsyncTask;
        if (whiteListCheckAsyncTask2 != null && !whiteListCheckAsyncTask2.isCancelled()) {
            this.whiteListCheckAsyncTask.cancel(true);
        }
    }

    private void showStickerPackList(List<StickerPack> list) {
        this.allStickerPacksListAdapter = new StickerPackListAdapter(list, this.onAddButtonClickedListener);
        this.packRecyclerView.setAdapter(this.allStickerPacksListAdapter);
        this.packLayoutManager = new LinearLayoutManager(this);
        this.packLayoutManager.setOrientation(RecyclerView.VERTICAL);
        this.packRecyclerView.setLayoutManager(this.packLayoutManager);
        this.packRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public final void onGlobalLayout() {
                StickerPackListActivity.this.recalculateColumnCount();
            }
        });
    }

    /* access modifiers changed from: private */
    public void recalculateColumnCount() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size);
        StickerPackListItemViewHolder stickerPackListItemViewHolder = (StickerPackListItemViewHolder) this.packRecyclerView.findViewHolderForAdapterPosition(this.packLayoutManager.findFirstVisibleItemPosition());
        if (stickerPackListItemViewHolder != null) {
            int measuredWidth = stickerPackListItemViewHolder.imageRowView.getMeasuredWidth();
            int min = Math.min(5, Math.max(measuredWidth / dimensionPixelSize, 1));
            int i = (measuredWidth - (dimensionPixelSize * min)) / (min - 1);
            this.allStickerPacksListAdapter.setImageRowSpec(min, 10);
        }
    }

    static class WhiteListCheckAsyncTask extends AsyncTask<StickerPack, Void, List<StickerPack>> {
        private final WeakReference<StickerPackListActivity> stickerPackListActivityWeakReference;

        WhiteListCheckAsyncTask(StickerPackListActivity stickerPackListActivity) {
            this.stickerPackListActivityWeakReference = new WeakReference<>(stickerPackListActivity);
        }

        /* access modifiers changed from: protected */
        public final List<StickerPack> doInBackground(StickerPack... stickerPackArr) {
            StickerPackListActivity stickerPackListActivity = (StickerPackListActivity) this.stickerPackListActivityWeakReference.get();
            if (stickerPackListActivity == null) {
                return Arrays.asList(stickerPackArr);
            }
            for (StickerPack stickerPack : stickerPackArr) {
                stickerPack.setIsWhitelisted(WhitelistCheck.isWhitelisted(stickerPackListActivity, stickerPack.identifier));
            }
            return Arrays.asList(stickerPackArr);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<StickerPack> list) {
            StickerPackListActivity stickerPackListActivity = (StickerPackListActivity) this.stickerPackListActivityWeakReference.get();
            if (stickerPackListActivity != null) {
                stickerPackListActivity.allStickerPacksListAdapter.setStickerPackList(list);
                stickerPackListActivity.allStickerPacksListAdapter.notifyDataSetChanged();
            }
        }
    }
}
