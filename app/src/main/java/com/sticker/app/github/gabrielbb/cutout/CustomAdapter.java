package com.sticker.app.github.gabrielbb.cutout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sticker.app.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    List<String> arrayList;
    Context context;
    LayoutInflater inflter;

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public CustomAdapter(Context context2, List<String> list) {
        this.context = context2;
        this.arrayList = list;
        this.inflter = LayoutInflater.from(context2);
    }

    public int getCount() {
        return this.arrayList.size();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = this.inflter.inflate(R.layout.spinner, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tvTextSpinner);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setText(this.arrayList.get(i));
        return inflate;
    }
}
