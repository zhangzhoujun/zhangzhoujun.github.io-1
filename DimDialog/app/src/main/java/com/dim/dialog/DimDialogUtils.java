package com.dim.dialog;

import android.content.Context;

/**
 * Created by dim on 2017/1/16 11:02 邮箱：271756926@qq.com
 */

public class DimDialogUtils {

    // dip转像素
    public static int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);

        return valuePixels;

    }
}
