package com.sticker.app.github.gabrielbb.cutout.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.sticker.app.R;

import java.net.URL;

public class EditTextActivity extends AppCompatActivity {
    ImageView ivImage;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_demo);
        this.ivImage = (ImageView) findViewById(R.id.ivImage);
        new DownloadImageTask(this.ivImage).execute(new String[]{"https://github.com/amanjeetsingh150/UberCarAnimation"});
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView imageView) {
            this.bmImage = imageView;
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(String... strArr) {
            try {
                return BitmapFactory.decodeStream(new URL(strArr[0]).openStream());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            this.bmImage.setImageBitmap(bitmap);
        }
    }
}
