package com.kalemao.library.imageview;

import static com.kalemao.library.imageview.glide.config.Contants.ANDROID_RESOURCE;
import static com.kalemao.library.imageview.glide.config.Contants.ASSERTS_PATH;
import static com.kalemao.library.imageview.glide.config.Contants.RAW;

import com.kalemao.library.base.RunTimeData;
import com.kalemao.library.imageview.glide.config.ScaleMode;
import com.kalemao.library.imageview.glide.loader.KLMImageLoader;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 圆圈的imageview Created by dim on 2017/5/16 15:40 邮箱：271756926@qq.com
 */
public class KLMCircleImageView extends KLMImageView {

    public KLMCircleImageView(Context context) {
        super(context);
    }

    public KLMCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KLMCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String url) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext()).url(url).animate(animationObject).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageUrl(Context context, String url) {
        KLMImageLoader.with(context).url(url).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageUrl(Context context, String url, int ScaleModeModel) {
        KLMImageLoader.with(context).url(url).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).asCircle().into(this);
    }

    public void setImageUrl(Context context, String url, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).url(url).animate(animationObject).override(imageWid, imageHei).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel)
                .asCircle().into(this);
    }

    public void setImageUrl(int res) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext()).res(res).animate(animationObject).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageUrl(Context context, int res) {
        KLMImageLoader.with(context).res(res).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageUrl(Context context, int res, int ScaleModeModel) {
        KLMImageLoader.with(context).res(res).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).asCircle().into(this);
    }

    public void setImageUrl(Context context, int res, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).res(res).animate(animationObject).override(imageWid, imageHei).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel)
                .asCircle().into(this);
    }

    public void setImageFilePath(String path) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext()).file("file://" + path).animate(animationObject).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageFilePath(Context context, String path) {
        KLMImageLoader.with(context).file("file://" + path).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP)
                .asCircle().into(this);
    }

    public void setImageFilePath(Context context, String path, int ScaleModeModel) {
        KLMImageLoader.with(context).file("file://" + path).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).asCircle()
                .into(this);
    }

    public void setImageFilePath(Context context, String path, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).file("file://" + path).animate(animationObject).override(imageWid, imageHei).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleModeModel).asCircle().into(this);
    }

    public void setImageAssertsPath(String imageName) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext()).asserts(ASSERTS_PATH + imageName).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageAssertsPath(Context context, String imageName) {
        KLMImageLoader.with(context).asserts(ASSERTS_PATH + imageName).animate(animationObject).placeHolder(mPlaceholderImageResouce)
                .scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageAssertsPath(Context context, String imageName, int ScaleModeModel) {
        KLMImageLoader.with(context).asserts(ASSERTS_PATH + imageName).animate(animationObject).placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel)
                .asCircle().into(this);
    }

    public void setImageAssertsPath(Context context, String imageName, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).asserts(ASSERTS_PATH + imageName).override(imageWid, imageHei).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).asCircle().into(this);
    }

    public void setImageRawPath(String imageName) {
        KLMImageLoader.with(RunTimeData.getInstance().getmContext())
                .raw(ANDROID_RESOURCE + RunTimeData.getInstance().getmContext().getPackageName() + RAW + imageName).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageRawPath(Context context, String imageName) {
        KLMImageLoader.with(context).raw(ANDROID_RESOURCE + context.getPackageName() + RAW + imageName).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleMode.CENTER_CROP).asCircle().into(this);
    }

    public void setImageRawPath(Context context, String imageName, int ScaleModeModel) {
        KLMImageLoader.with(context).raw(ANDROID_RESOURCE + context.getPackageName() + RAW + imageName).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).asCircle().into(this);
    }

    public void setImageRawPath(Context context, String imageName, int ScaleModeModel, int imageWid, int imageHei) {
        KLMImageLoader.with(context).raw(ANDROID_RESOURCE + context.getPackageName() + RAW + imageName).override(imageWid, imageHei).animate(animationObject)
                .placeHolder(mPlaceholderImageResouce).scale(ScaleModeModel).asCircle().into(this);
    }
}
