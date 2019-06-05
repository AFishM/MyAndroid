package com.xuzixu.myandroid.img;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.xuzixu.myandroid.R;

import java.io.IOException;


public class ImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        IntensifyImageView intensifyImageView=(IntensifyImageView)findViewById(R.id.intensify_image);
        try {
            intensifyImageView.setImage(getAssets().open("xingren.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().maxMemory();
        long i=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
    }

    /**
     * 加载一张图片的指定区域
     */
    private void decodeRegion() throws IOException {
        BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance("", false);
        decoder.getHeight();
        Rect rect = new Rect(10, 10, 80, 80);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bitmap = decoder.decodeRegion(rect, options);
        decoder.recycle();
    }

    /**
     * 一张宽度2400px，高度为3200px的jpg格式的图片，假设它现在的大小为3mb，
     * 但是如果我们直接在android中解码，BitmapFactory.decodeFile("bigImage.jpg");
     * Android在解码这种图片的时候会申请29MB左右的内存来进行解码
     * 因为图片的大小 = 图片总像素 * 图片单个像素的大小
     * 在Android中展示颜色，一般情况下使用的是ARGB_8888，每个像素的大小为4byte，详见Bitmap.Config
     */
    private void loadLargeBitmap(ImageView imageView, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background, options);
        int width = options.outWidth;
        int height = options.outHeight;
        String mimeType = options.outMimeType;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            while (halfWidth / inSampleSize >= reqWidth && halfHeight / inSampleSize >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background, options);
        imageView.setImageBitmap(bitmap);
    }
}
