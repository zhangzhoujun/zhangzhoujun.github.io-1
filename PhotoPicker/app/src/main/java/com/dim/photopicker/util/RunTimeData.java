package com.dim.photopicker.util;

import android.graphics.Bitmap;

import java.util.ArrayList;

import com.dim.photopicker.R;
import com.dim.photopicker.model.ImageItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 运行时数据
 * 
 * @author zzj
 * 
 */
public class RunTimeData {
    private static RunTimeData   runTimeData; // 默认的搜索
    private ArrayList<ImageItem> choseImage;

    public static RunTimeData getInstance() {
        if (runTimeData == null) {
            runTimeData = new RunTimeData();
        }
        return runTimeData;
    }

    public ArrayList<ImageItem> getChoseImage() {
        return choseImage;
    }

    public void setChoseImage(ArrayList<ImageItem> choseImage) {
        this.choseImage = choseImage;
    }
    public final DisplayImageOptions picOptions           = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.klm_loading)
            .showImageOnFail(R.drawable.klm_loading).cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

}
