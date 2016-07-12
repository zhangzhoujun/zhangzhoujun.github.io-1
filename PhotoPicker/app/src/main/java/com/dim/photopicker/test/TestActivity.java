package com.dim.photopicker.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.dim.photopicker.R;
import com.dim.photopicker.model.ImageItem;
import com.dim.photopicker.util.CustomConstants;
import com.dim.photopicker.util.IntentConstants;
import com.dim.photopicker.util.RunTimeData;
import com.dim.photopicker.view.BaseActivity;
import com.dim.photopicker.view.ImageBucketChooseActivity;

/**
 * Created by zhangzhoujun on 16/7/12 14:39 邮箱：271756926@qq.com
 */
public class TestActivity extends BaseActivity {
    private static final int      TAKE_PICTURE = 0x000000;
    private String                path         = "";
    public static List<ImageItem> mDataList    = new ArrayList<ImageItem>();

    private Button                button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });
    }

    public void showAlert() {
        String[] items = { "拍照", "图库" };
        new AlertDialog.Builder(TestActivity.this) // buildAlertDialog
                .setItems(items, new DialogInterface.OnClickListener() { // content
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 1) {
                                    if (!doesNeedCheckoutPermissionWriteExternalStorage()) {
                                        gotoPackage();
                                    }
                                } else if (which == 0) {
                                    if (!doesNeedCheckoutPermissionCamera()) {
                                        takePhoto();
                                    }
                                }
                            }
                        }).show();

    }

    /**
     * 去选照片
     */
    private void gotoPackage() {
        int count = getAvailableSize();
        if (count == 0) {
            return;
        }
        Intent intent = new Intent(TestActivity.this, ImageBucketChooseActivity.class);
        intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, count);
        startActivityForResult(intent, CustomConstants.RESULT_PICTURE_BACK);
    }

    private int getAvailableSize() {
        int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }

    public void takePhoto() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File vFile = new File(Environment.getExternalStorageDirectory() + "/myimage/", String.valueOf(System.currentTimeMillis()) + ".jpg");
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case CustomConstants.RESULT_PICTURE_BACK:
            if (RunTimeData.getInstance() == null || RunTimeData.getInstance().getChoseImage() == null) {
                return;
            }
            List<ImageItem> incomingDataList = RunTimeData.getInstance().getChoseImage();
            RunTimeData.getInstance().setChoseImage(null);
            // 选取相册返回的数据的处理
            break;
        case TAKE_PICTURE:
            if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE && resultCode == -1 && !TextUtils.isEmpty(path)) {
                ImageItem item = new ImageItem();
                item.sourcePath = path;
                // 拍照的处理
            }
            break;
        }
    }
}
