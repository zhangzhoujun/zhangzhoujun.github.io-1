package com.qm.lib.widget.dim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.qm.lib.R;
import com.qm.lib.utils.StringUtils;

import org.json.JSONObject;

/**
 * @author dim
 * @create at 2019/3/22 09:50
 * @description:
 */
public final class ViewAdapter {

    @BindingAdapter(value = {"resId", "localId", "base64Pat", "url", "round", "cornerUrl"}, requireAll = false)
    public static void setAppImage(AppImageView imageView, Drawable resId, int localId, String base64Pat, String url, boolean round, String cornerUrl) {
        if (!TextUtils.isEmpty(cornerUrl)) {
            imageView.setImageUrlCorners(cornerUrl, (int) imageView.getContext().getResources().getDimension(R.dimen.dp_15));
        } else if (TextUtils.isEmpty(base64Pat)) {
            if (TextUtils.isEmpty(url)) {
                if (resId == null) {
                    Glide.with(imageView.getContext())
                            .load(localId).into(imageView);
                } else {
                    Glide.with(imageView.getContext())
                            .load(resId)
                            .into(imageView);
                }
            } else {
                if (round) {
                    imageView.setImageUrlRound(url);
                } else {
                    imageView.setImageUrl(url);
                }
            }
        } else {
            byte[] decode = null;
            if (isBase64Img(base64Pat)) {
                base64Pat = base64Pat.split(",")[1];
            }
            decode = Base64.decode(base64Pat, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            imageView.setImageBitmap(decodedByte);
        }
    }

    @BindingAdapter(value = {"placeId", "webUrl", "iconRound"}, requireAll = false)
    public static void setAppImage(AppImageView imageView, Drawable placeId, String webUrl, boolean round) {
        if (TextUtils.isEmpty(webUrl) || null == placeId) {
            return;
        }
        imageView.setImageUrl(webUrl, placeId, round);
    }

    private static boolean isBase64Img(String imgurl) {
        if (!TextUtils.isEmpty(imgurl) && (imgurl.startsWith("data:image/png;base64,") || imgurl.startsWith("data:image/jpeg;base64,")
                || imgurl.startsWith("data:image/*;base64,") || imgurl.startsWith("data:image/jpg;base64,")
        )) {
            return true;
        }
        return false;
    }

    @BindingAdapter(value = {"canClick"}, requireAll = false)
    public static void setNodeButton(DimButton dimButton, boolean canClick) {
        dimButton.setCanClick(canClick);
    }

    @BindingAdapter(value = {"strike_thru"}, requireAll = false)
    public static void setAppTextStrike(AppTextView textview, boolean strike_thru) {
        if (strike_thru) {
            textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        }
    }

    @BindingAdapter(value = {"assertShow"}, requireAll = false)
    public static void setAppTextAsserts(AppTextView textview, String dataString, String key) {
        if (StringUtils.Companion.getInstance().isEmpty(dataString) || StringUtils.Companion.getInstance().isEmpty(key)) {
            return;
        }
        try {
            JSONObject mapJSON = new JSONObject(dataString);
            if (mapJSON.has(key)) {
                textview.setText(mapJSON.get(key).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
