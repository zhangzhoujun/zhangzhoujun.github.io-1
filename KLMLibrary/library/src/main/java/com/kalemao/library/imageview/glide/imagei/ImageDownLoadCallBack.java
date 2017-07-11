package com.kalemao.library.imageview.glide.imagei;

import android.graphics.Bitmap;

/**
 * Created by doudou on 2017/5/2.
 */

public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
