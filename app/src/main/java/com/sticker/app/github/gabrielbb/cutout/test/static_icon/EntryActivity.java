package com.sticker.app.github.gabrielbb.cutout.test.static_icon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sticker.app.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class EntryActivity extends BaseActivity {
    String ddfgf;
    private LoadListAsyncTask loadListAsyncTask;
    private View progressBar;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_entry);
        overridePendingTransition(0, 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        this.progressBar = findViewById(R.id.entry_activity_progress);
        this.loadListAsyncTask = new LoadListAsyncTask(this);
        this.loadListAsyncTask.execute();
    }

    /* access modifiers changed from: private */
    public void showStickerPack(ArrayList<StickerPack> arrayList) {
        this.progressBar.setVisibility(View.GONE);
        if (arrayList.size() > 1) {
            Intent intent = new Intent(this, StickerPackListActivity.class);
            intent.putParcelableArrayListExtra("sticker_pack_list", arrayList);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
            return;
        }
        Intent intent2 = new Intent(this, StickerPackDetailsActivity.class);
        intent2.putExtra("show_up_button", false);
        intent2.putExtra("sticker_pack", arrayList.get(0).name);
        startActivity(intent2);
        finish();
        overridePendingTransition(0, 0);
    }

    /* access modifiers changed from: private */
    public void showErrorMessage(String str) {
        this.progressBar.setVisibility(View.GONE);
        Log.e("EntryActivity", "error fetching sticker packs, " + str);
        ((TextView) findViewById(R.id.error_message)).setText(getString(R.string.error_message, new Object[]{str}));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        LoadListAsyncTask loadListAsyncTask2 = this.loadListAsyncTask;
        if (loadListAsyncTask2 != null && !loadListAsyncTask2.isCancelled()) {
            this.loadListAsyncTask.cancel(true);
        }
    }

    static class LoadListAsyncTask extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {
        private final WeakReference<EntryActivity> contextWeakReference;

        LoadListAsyncTask(EntryActivity entryActivity) {
            this.contextWeakReference = new WeakReference<>(entryActivity);
        }

        /* access modifiers changed from: protected */
        public Pair<String, ArrayList<StickerPack>> doInBackground(Void... voidArr) {
            try {
                Context context = (Context) this.contextWeakReference.get();
                if (context == null) {
                    return new Pair<String, ArrayList<StickerPack>>("could not fetch sticker packs", (ArrayList<StickerPack>) null);
                }
                ArrayList<StickerPack> fetchStickerPacks = StickerPackLoader.fetchStickerPacks(context);
                if (fetchStickerPacks.size() == 0) {
                    return new Pair<String, ArrayList<StickerPack>>("could not find any packs", (ArrayList<StickerPack>) null);
                }
                Iterator<StickerPack> it = fetchStickerPacks.iterator();
                while (it.hasNext()) {
                    StickerPack next = it.next();
                }
                return new Pair<String, ArrayList<StickerPack>>((String) null, fetchStickerPacks);
            } catch (Exception e) {
                Log.e("EntryActivity", "error fetching sticker packs", e);
                return new Pair<String, ArrayList<StickerPack>>(e.getMessage(), (StickerPack) null);
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Pair<String, ArrayList<StickerPack>> pair) {
            EntryActivity entryActivity = (EntryActivity) this.contextWeakReference.get();
            if (entryActivity == null) {
                return;
            }
            if (pair.first != null) {
                entryActivity.showErrorMessage((String) pair.first);
            } else {
                entryActivity.showStickerPack((ArrayList) pair.second);
            }
        }
    }
}
