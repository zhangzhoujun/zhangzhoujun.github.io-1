package com.kalemao.library.imageview;

import static com.kalemao.library.imageview.glide.config.Contants.ANDROID_RESOURCE;
import static com.kalemao.library.imageview.glide.config.Contants.ASSERTS_PATH;
import static com.kalemao.library.imageview.glide.config.Contants.RAW;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.kalemao.library.R;
import com.kalemao.library.base.RunTimeData;
import com.kalemao.library.imageview.glide.config.ScaleMode;
import com.kalemao.library.imageview.glide.loader.KLMImageLoader;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dim on 2017/5/16 16:40 邮箱：271756926@qq.com
 */

public class KLMImageView extends AppCompatImageView {

    protected int                            mPlaceholderImageResouce; // 占位图,不设置占位图，默认显示菊花
    protected int                            mFailureImageResouce;     // 失败的图片
    protected ViewPropertyAnimation.Animator animationObject;

    protected void initPlaceholderImage() {
        if (mPlaceholderImageResouce == 0) {
            mPlaceholderImageResouce = R.drawable.klm_default_iamge;
        }
    }

    protected void initFailureImage() {
        if (mFailureImageResouce == 0) {
            mFailureImageResouce = R.drawable.klm_default_iamge;
        }
    }

    protected void initKLMImageViewDefault() {
        initPlaceholderImage();
        initFailureImage();
        animationObject = new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);

                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(500);
                fadeAnim.start();
            }
        };
    }

    public KLMImageView(Context context) {
        super(context);
        initKLMImageViewDefault();
    }

    public KLMImageView(Context context, int placeholderImageResouce, int failureImageResouce) {
        super(context);
        this.mFailureImageResouce = failureImageResouce;
        this.mPlaceholderImageResouce = placeholderImageResouce;
    }

    public KLMImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initKLMImageViewDefault();
    }

    public KLMImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initKLMImageViewDefault();
    }

    public void setImageUrl(String url) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext()).url(url).animate(animationObject).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageUrl(Context context, String url) {
        KLMImageLoader.with(context).url(url).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageUrl(Context context, String url, int ScaleModeModel) {
        KLMImageLoader.with(context).url(url).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).into(this);
    }

    public void setImageUrl(Context context, String url, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).url(url).animate(animationObject).override(imageWid, imageHei).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel)
                .into(this);
    }

    public void setImageUrl(int res) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext()).res(res).animate(animationObject).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageUrl(Context context, int res) {
        KLMImageLoader.with(context).res(res).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageUrl(Context context, int res, int ScaleModeModel) {
        KLMImageLoader.with(context).res(res).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).into(this);
    }

    public void setImageUrl(Context context, int res, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).res(res).animate(animationObject).override(imageWid, imageHei).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel)
                .into(this);
    }

    public void setImageFilePath(String path) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext()).file("file://" + path).animate(animationObject).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageFilePath(Context context, String path) {
        KLMImageLoader.with(context).file("file://" + path).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP)
                .into(this);
    }

    public void setImageFilePath(Context context, String path, int ScaleModeModel) {
        KLMImageLoader.with(context).file("file://" + path).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).into(this);
    }

    public void setImageFilePath(Context context, String path, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).file("file://" + path).animate(animationObject).override(imageWid, imageHei).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleModeModel).into(this);
    }

    public void setImageFilePathNoAnimal(Context context, String path, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).file("file://" + path).override(imageWid, imageHei).placeHolder(mPlaceholderImageResouce)
                .diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).scale(ScaleModeModel).into(this);
    }

    public void setImageFilePathNoPlaceholder(Context context, String path, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).file("file://" + path).animate(animationObject).override(imageWid, imageHei).scale(ScaleModeModel).into(this);
    }

    public void setImageFilePathWithPlaceholder(Context context, String path, int ScaleModeModel, int imageWid, int imageHei, int pPlaceholderResouce) {
        KLMImageLoader.with(context).file("file://" + path).animate(animationObject).override(imageWid, imageHei).placeHolder(pPlaceholderResouce)
                .scale(ScaleModeModel).into(this);
    }

    public void setImageAssertsPath(String imageName) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext()).asserts(ASSERTS_PATH + imageName).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageAssertsPath(Context context, String imageName) {
        KLMImageLoader.with(context).asserts(ASSERTS_PATH + imageName).animate(animationObject).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageAssertsPath(Context context, String imageName, int ScaleModeModel) {
        KLMImageLoader.with(context).asserts(ASSERTS_PATH + imageName).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel)
                .into(this);
    }

    public void setImageAssertsPath(Context context, String imageName, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).asserts(ASSERTS_PATH + imageName).override(imageWid, imageHei).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).into(this);
    }

    public void setImageRawPath(String imageName) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext())
                .raw(ANDROID_RESOURCE + RunTimeData.getInstance().getmContext().getPackageName() + RAW + imageName).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageRawPath(Context context, String imageName) {
        KLMImageLoader.with(context).raw(ANDROID_RESOURCE + context.getPackageName() + RAW + imageName).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).into(this);
    }

    public void setImageRawPath(Context context, String imageName, int ScaleModeModel) {
        KLMImageLoader.with(context).raw(ANDROID_RESOURCE + context.getPackageName() + RAW + imageName).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).into(this);
    }

    public void setImageRawPath(Context context, String imageName, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).raw(ANDROID_RESOURCE + context.getPackageName() + RAW + imageName).override(imageWid, imageHei).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).into(this);
    }

    public void setPlaceholderImageResouce(int resource) {
        this.mPlaceholderImageResouce = resource;
    }

    public void setFailureImage(int resource) {
        this.mFailureImageResouce = resource;
    }

    public void setImageUrlAsGift(Context context, String url) {
        Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(this);
    }

    public void setImageByteWithPlaceholder(Context context, byte[] bytes, int ScaleModeModel, int imageWid, int imageHei) {
        Glide.with(context).load(bytes).override(imageWid, imageHei).diskCacheStrategy(DiskCacheStrategy.ALL).into(this);
    }

    public void setImageByte(Context context, byte[] bytes, KLMImageView imageView) {
        Glide.with(context).load(bytes).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
