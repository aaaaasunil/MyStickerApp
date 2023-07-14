package com.sticker.app.github.gabrielbb.cutout.utils;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

public class NothingSelectedSpinnerAdapter implements SpinnerAdapter, ListAdapter {
    protected static final int EXTRA = 1;
    protected SpinnerAdapter adapter;
    protected Context context;
    protected LayoutInflater layoutInflater;
    protected int nothingSelectedDropdownLayout;
    protected int nothingSelectedLayout;

    public boolean areAllItemsEnabled() {
        return false;
    }

    public int getItemViewType(int i) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEnabled(int i) {
        return i != 0;
    }

    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter, int i, Context context2) {
        this(spinnerAdapter, i, -1, context2);
    }

    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter, int i, int i2, Context context2) {
        this.adapter = spinnerAdapter;
        this.context = context2;
        this.nothingSelectedLayout = i;
        this.nothingSelectedDropdownLayout = i2;
        this.layoutInflater = LayoutInflater.from(context2);
    }

    public final View getView(int i, View view, ViewGroup viewGroup) {
        if (i == 0) {
            return getNothingSelectedView(viewGroup);
        }
        return this.adapter.getView(i - 1, (View) null, viewGroup);
    }

    /* access modifiers changed from: protected */
    public View getNothingSelectedView(ViewGroup viewGroup) {
        return this.layoutInflater.inflate(this.nothingSelectedLayout, viewGroup, false);
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (i != 0) {
            return this.adapter.getDropDownView(i - 1, (View) null, viewGroup);
        }
        if (this.nothingSelectedDropdownLayout == -1) {
            return new View(this.context);
        }
        return getNothingSelectedDropdownView(viewGroup);
    }

    /* access modifiers changed from: protected */
    public View getNothingSelectedDropdownView(ViewGroup viewGroup) {
        return this.layoutInflater.inflate(this.nothingSelectedDropdownLayout, viewGroup, false);
    }

    public int getCount() {
        int count = this.adapter.getCount();
        if (count == 0) {
            return 0;
        }
        return count + 1;
    }

    public Object getItem(int i) {
        if (i == 0) {
            return null;
        }
        return this.adapter.getItem(i - 1);
    }

    public long getItemId(int i) {
        return i >= 1 ? this.adapter.getItemId(i - 1) : (long) (i - 1);
    }

    public boolean hasStableIds() {
        return this.adapter.hasStableIds();
    }

    public boolean isEmpty() {
        return this.adapter.isEmpty();
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.adapter.registerDataSetObserver(dataSetObserver);
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.adapter.unregisterDataSetObserver(dataSetObserver);
    }
}
