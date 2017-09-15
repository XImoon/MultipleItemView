package cn.ximoon.fillandimagetext.htmlgap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Administrator on 2015/8/17.
 */
/*
public class URLImageGetter implements Html.ImageGetter{
    private String shopDeString;
    private TextView textView;
    Context context;
    private DisplayImageOptions options;
    public URLImageGetter(String shopDeString,Context context,TextView textView,DisplayImageOptions options) {
        this.shopDeString = shopDeString;
        this.context = context;
        this.textView = textView;
        this.options = options;
    }
    @Override
    public Drawable getDrawable(String source) {

        final URLDrawable urlDrawable = new URLDrawable();
        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        final ImageSize imageSize = new ImageSize(ScreenUtils.getScreenWidth(context),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        NonViewAware nonViewAware = new NonViewAware(imageSize, ViewScaleType.fromImageView(img));
        ImageLoader.getInstance().displayImage(source,nonViewAware,options,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                urlDrawable.bitmap = loadedImage;
                int w =  loadedImage.getWidth();
                int h = loadedImage.getHeight();
                urlDrawable.setBounds(0, 0, loadedImage.getWidth(),loadedImage.getHeight()-150);
                */
/*LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                layoutparams.topMargin = - 50;
                textView.setLayoutParams(layoutparams);*//*

                textView.invalidate();
                textView.setText(textView.getText()); // 解决图文重叠
            }
        });
        */
/*ImageLoader.getInstance().loadImage(source,options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }
        });*//*

        return urlDrawable;
    }
}
*/
public class URLImageGetter implements Html.ImageGetter {
    private String shopDeString;
    private TextView textView;
    Context context;
    private DisplayImageOptions options;

    public URLImageGetter(String shopDeString, Context context, TextView textView, DisplayImageOptions options) {
        this.shopDeString = shopDeString;
        this.context = context;
        this.textView = textView;
        this.options = options;
    }

    @Override
    public Drawable getDrawable(final String source) {
        final URLDrawable urlDrawable = new URLDrawable();
//        ImageLoader.getInstance().loadImage(source,
//                new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        // 取得想要缩放的matrix參數
//                        int width = loadedImage.getWidth();
//                        int height = loadedImage.getHeight();
//                        float f = 0;
//                        if (height < 54)
//                            f = (float) (54.0 / height);
//                        else
//                            f = 1.0f;
//                        Matrix matrix = new Matrix();
//                        matrix.postScale(f, f);
//                        // 得到新的圖片
//                        Bitmap newbm = Bitmap.createBitmap(loadedImage, 0, 0, width, height, matrix, true);
//                        urlDrawable.bitmap = newbm;
//                        urlDrawable.setBounds(0, 0, newbm.getWidth(), newbm.getHeight());
//                        textView.invalidate();
//                        textView.setText(textView.getText());
//
//                    }
//                });

        //解决微信分享 图片过大，不显示图片的问题
        Glide.with(context).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap loadedImage, GlideAnimation<? super Bitmap> glideAnimation) {
                // 取得想要缩放的matrix參數
                        int width = loadedImage.getWidth();
                        int height = loadedImage.getHeight();
                        float f = 0;
                        if (height < 54)
                            f = (float) (54.0 / height);
                        else
                            f = 1.0f;
                        Matrix matrix = new Matrix();
                        matrix.postScale(f, f);
                        // 得到新的圖片
                        Bitmap newbm = Bitmap.createBitmap(loadedImage, 0, 0, width, height, matrix, true);
                        urlDrawable.bitmap = newbm;
                        urlDrawable.setBounds(0, 0, newbm.getWidth(), newbm.getHeight());
                        textView.invalidate();
                        textView.setText(textView.getText());
            }
        });
        return urlDrawable;
    }

    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }
}

