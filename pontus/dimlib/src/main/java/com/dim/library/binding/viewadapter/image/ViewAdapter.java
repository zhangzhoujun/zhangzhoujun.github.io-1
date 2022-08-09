package com.dim.library.binding.viewadapter.image;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dim.library.utils.DLog;

/**
 * Created by  on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, int url, int placeholderRes) {
        if (url != 0) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"base64Pat"})
    public static void setImageBase64Path(ImageView imageView, String base64Pat) {
        DLog.d("加载base图片 -->  " + base64Pat);
        if (!TextUtils.isEmpty(base64Pat)) {
            byte[] decode = null;
            if (isBase64Img(base64Pat)) {
                base64Pat = base64Pat.split(",")[1];
            }
            decode = Base64.decode(base64Pat, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            imageView.setImageBitmap(decodedByte);
        }
    }

    private static boolean isBase64Img(String imgurl) {
        if (!TextUtils.isEmpty(imgurl) && (imgurl.startsWith("data:image/png;base64,") || imgurl.startsWith("data:image/jpeg;base64,")
                || imgurl.startsWith("data:image/*;base64,") || imgurl.startsWith("data:image/jpg;base64,")
        )) {
            return true;
        }
        return false;
    }
}

