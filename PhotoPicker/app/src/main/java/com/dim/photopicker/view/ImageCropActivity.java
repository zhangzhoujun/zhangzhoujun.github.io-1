package com.dim.photopicker.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.dim.photopicker.R;
import com.dim.photopicker.model.ImageItem;
import com.dim.photopicker.util.CropImageView;
import com.dim.photopicker.util.RunTimeData;

/**
 * Created by zhangzhoujun on 16/7/13 15:36 邮箱：271756926@qq.com
 */
public class ImageCropActivity extends BaseActivity {
    private CropImageView        mView;
    private ArrayList<ImageItem> selectedImgs = new ArrayList<ImageItem>();
    private int                  currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aact_cropimage);

        selectedImgs = RunTimeData.getInstance().getChoseImage();
        RunTimeData.getInstance().setChoseImage(null);

        mView = (CropImageView) findViewById(R.id.cropimage);
        // 调用该方法得到剪裁好的图片
        // Bitmap mBitmap= mView.getCropImage();
        showPic();
    }

    private void showPic() {
        if (selectedImgs != null && currentIndex < selectedImgs.size()) {
            try {
                mView.setDrawable(getImageDrawable(selectedImgs.get(currentIndex).sourcePath), 300, 300);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BitmapDrawable getImageDrawable(String path) throws IOException {
        // 打开文件
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bt = new byte[1024];
        // 得到文件的输入流
        InputStream in = new FileInputStream(file);
        // 将文件读出到输出流中
        int readLength = in.read(bt);
        while (readLength != -1) {
            outStream.write(bt, 0, readLength);
            readLength = in.read(bt);
        }
        // 转换成byte 后 再格式化成位图
        byte[] data = outStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
        BitmapDrawable bd = new BitmapDrawable(bitmap);

        return bd;
    }
}
