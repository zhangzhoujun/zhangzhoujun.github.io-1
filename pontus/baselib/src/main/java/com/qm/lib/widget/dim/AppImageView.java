package com.qm.lib.widget.dim;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.qm.lib.R;
import com.qm.lib.utils.ScreenUtils;

/**
 * @author dim
 * @create at 2019/3/21 15:19
 * @description:
 */
public class AppImageView extends androidx.appcompat.widget.AppCompatImageView {

    public AppImageView(Context context) {
        super(context);
    }

    public AppImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(int res) {
        if (res == 0) {
            return;
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.lib_base_icon_empty);
        options.error(R.drawable.lib_base_icon_empty);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.skipMemoryCache(false);
        options.dontAnimate();
        options.format(DecodeFormat.PREFER_ARGB_8888);
        Glide.with(this)
                .load(res)
                .apply(options)
                .into(this);
    }

    public void setImageUrl(Drawable res) {
        if (res == null) {
            return;
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.lib_base_icon_empty);
        options.error(R.drawable.lib_base_icon_empty);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.skipMemoryCache(false);
        options.dontAnimate();
        options.format(DecodeFormat.PREFER_ARGB_8888);
        Glide.with(this)
                .load(res)
                .apply(options)
                .into(this);
    }

    public void setImageUrl(String url, int wid, int hei) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.lib_base_icon_empty);
        options.error(R.drawable.lib_base_icon_empty);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.skipMemoryCache(false);
        options.dontAnimate();
        options.override(wid, hei);
        options.format(DecodeFormat.PREFER_ARGB_8888);
        Glide.with(this)
                .load(url)
                .apply(options)
                .into(this);
        // loadRoundImg(this, url, R.drawable.lib_base_icon_empty, R.drawable.lib_base_icon_empty,wid ,hei);
    }

    public void loadRoundImg(ImageView imageView, String url, @DrawableRes int placeholderId,
                             @DrawableRes int errorId, int wid, int hei) {

        Glide.with(imageView.getContext()).load(url)
                .apply(new RequestOptions().placeholder(placeholderId).error(errorId).centerCrop().override(wid, hei).diskCacheStrategy(DiskCacheStrategy.ALL))
                .thumbnail(loadTransform(imageView.getContext(), placeholderId))
                .thumbnail(loadTransform(imageView.getContext(), errorId))
                .into(imageView);
    }

    private RequestBuilder loadTransform(Context context, @DrawableRes int placeholderId) {

        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop());

    }

    public void setImageUrl(String url, String widHeight, int count) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (widHeight == null || TextUtils.isEmpty(widHeight)) {
            setImageUrl(url);
        } else {
            String[] iconData = widHeight.split("\\*");
            if (iconData.length != 2) {
                setImageUrl(url);
            } else {
                int wid = ScreenUtils.getScreenWidth(this.getContext()) / count;
                int hei = wid * Integer.parseInt(iconData[1]) / Integer.parseInt(iconData[0]);
                setImageUrl(url, wid, hei);
            }
        }
    }

    public void setImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.lib_base_icon_empty);
        options.error(R.drawable.lib_base_icon_empty);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.skipMemoryCache(false);
        options.dontAnimate();

        Glide.with(this)
                .load(url)
                .apply(options)
                .into(this);
    }

    public void setImageDiyUrl(String url, int wid, int hei, float tl, float tr, float br, float bl) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.lib_base_icon_empty);
        options.error(R.drawable.lib_base_icon_empty);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.skipMemoryCache(false);
        options.dontAnimate();
        options.override(wid, hei);
        options.format(DecodeFormat.PREFER_ARGB_8888);
//        options.centerCrop();
//        options.transform(new RoundedCornersTransform(this.getContext(), radius.get(0), radius.get(1), radius.get(2), radius.get(3)));
//        Glide.with(this)
//                .load(url)
//                .apply(options)
////                .transform(new CornerTransform(this.getContext(),200, radius.get(0), radius.get(1), radius.get(2), radius.get(3)))
//                .into(this);

        Glide.with(this)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new MultiTransformation(
                        new CenterCrop(),
                        new RoundedCornersTransform(this.getContext(), tl, tr, br, bl))))
                .into(this);
    }


    public void setImageUrl(String url, Drawable resId, boolean round) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        RequestOptions options = new RequestOptions();
        if (round) {
            options.circleCrop();
        }
        options.placeholder(resId);
        options.error(resId);
        Glide.with(this)
                .load(url)
                .apply(options)
                .into(this);
    }

    public void setImageUrlCorners(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(10);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(this)
                .load(url)
                .apply(options)
                .into(this);
    }

    public void setImageUrlCorners(String url, int corner) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(corner);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);

        Glide.with(this)
                .load(url)
                .apply(options)
                .into(this);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setGoodsImage(String url, int wid, int hei) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        loadRoundImg(this, url, R.drawable.lib_base_icon_empty, R.drawable.lib_base_icon_empty, 10, wid, hei);
    }

    public void loadRoundImg(ImageView imageView, String url, @DrawableRes int placeholderId,
                             @DrawableRes int errorId, float radius, int wid, int hei) {

        Glide.with(imageView.getContext()).load(url)
                .apply(new RequestOptions().placeholder(placeholderId).error(errorId).centerCrop().override(wid, hei)
                        .transform(new GlideRoundTransform(imageView.getContext(), radius)).diskCacheStrategy(DiskCacheStrategy.ALL))
                .thumbnail(loadTransform(imageView.getContext(), placeholderId, radius))
                .thumbnail(loadTransform(imageView.getContext(), errorId, radius))
                .into(imageView);
    }

    private RequestBuilder loadTransform(Context context, @DrawableRes int placeholderId, float radius) {

        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(new GlideRoundTransform(context, radius)));

    }

    public void setImageUrlRound(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(this).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(this);
    }

    public void setImageUrlRound(int res) {
        if (res == 0) {
            return;
        }
        Glide.with(this).load(res).apply(RequestOptions.bitmapTransform(new CircleCrop()).fitCenter()).into(this);
    }

    public void setImageUrlGif(int res) {
        if (res == 0) {
            return;
        }

        Glide.with(this).load(res).listener(new RequestListener<Drawable>() {

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model,
                                           com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource,
                                           boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    //加载一次
                    ((GifDrawable) resource).setLoopCount(1);
                }
                return false;
            }
        }).into(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //高度根据使得图片的宽度充满屏幕计算而得
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
