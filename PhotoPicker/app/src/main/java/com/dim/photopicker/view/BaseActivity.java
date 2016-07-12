package com.dim.photopicker.view;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dim.photopicker.R;

/**
 * Created by zhangzhoujun on 16/7/12 14:43 邮箱：271756926@qq.com
 */
public class BaseActivity extends Activity {
    private static String   APP_NAME                                       = "你妹";
    // 检测SD卡权限
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE            = 1;
    private final String    WRITE_EXTERNAL_STORAGE_REQUEST_CODE_MESSAGE_MM = "请在在设置-应用-" + APP_NAME + "-权限中开启存储空间权限，以正常使用" + APP_NAME + "功能";
    // 相机权限
    public static final int CAMERA_REQUEST_CODE                            = 2;
    private final String    CAMERA_REQUEST_CODE_MESSAGE_MM                 = "请在在设置-应用-" + APP_NAME + "-权限中开启相机权限，以正常使用" + APP_NAME + "功能";
    // 电话权限
    public static final int CALL_PHONE_REQUEST_CODE                        = 3;
    private final String    CALL_PHONE_REQUEST_CODE_MESSAGE_MM             = "请在在设置-应用-" + APP_NAME + "-权限中开启电话权限，以正常使用" + APP_NAME + "功能";
    // READ_PHONE_STATE
    public static final int READ_PHONE_STATE                               = 4;
    private final String    READ_PHONE_STATE_MESSAGE_MM                    = "请在在设置-应用-" + APP_NAME + "-权限中开启电话权限，以正常使用" + APP_NAME + "功能";

    // 精准定位服务
    public static final int ACCESS_FINE_LOCATION                           = 5;
    private final String    ACCESS_FINE_LOCATION_MESSAGE_MM                = "请在在设置-应用-" + APP_NAME + "-权限中开启定位服务，以正常使用" + APP_NAME + "功能";

    // 大致定位服务
    public static final int ACCESS_COARSE_LOCATION                         = 6;
    private final String    ACCESS_COARSE_LOCATION_MESSAGE_MM              = "请在在设置-应用-" + APP_NAME + "-权限中开启定位服务，以正常使用" + APP_NAME + "功能";

    /**
     * 创建函数
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 申请对应的权限
     */
    protected boolean doesNeedCheckoutPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            // 申请WRITE_EXTERNAL_STORAGE权限
            if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else if (requestCode == CAMERA_REQUEST_CODE
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 申请CAMERA权限
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, CAMERA_REQUEST_CODE);
            } else if (requestCode == CALL_PHONE_REQUEST_CODE
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 申请电话权限
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CALL_PHONE }, CALL_PHONE_REQUEST_CODE);
            } else if (requestCode == READ_PHONE_STATE
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // 申请READ_PHONE_STATE权限
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_PHONE_STATE }, READ_PHONE_STATE);
            } else if (requestCode == ACCESS_FINE_LOCATION
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请精准定位服务
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, ACCESS_FINE_LOCATION);
            } else if (requestCode == ACCESS_COARSE_LOCATION
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请大致定位服务
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, ACCESS_FINE_LOCATION);
            } else {
                return false;
            }
            return true;
        } else {
            // Pre-Marshmallow
            return false;
        }
    }

    /**
     * 是否需要申请SD卡权限
     *
     * @return
     */
    public boolean doesNeedCheckoutPermissionWriteExternalStorage() {
        return doesNeedCheckoutPermission(WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    /**
     * 是否需要申请相机权限
     *
     * @return
     */
    public boolean doesNeedCheckoutPermissionCamera() {
        return doesNeedCheckoutPermission(CAMERA_REQUEST_CODE);
    }

    /**
     * 是否需要申请READ_PHONE_STATE权限
     *
     * @return
     */
    public boolean doesNeedCheckoutPermissionPhotoState() {
        return doesNeedCheckoutPermission(READ_PHONE_STATE);
    }

    /**
     * 是否需要申请打电话权限
     *
     * @return
     */
    public boolean doesNeedCheckoutPermissionCallPhone() {
        return doesNeedCheckoutPermission(CALL_PHONE_REQUEST_CODE);
    }

    /**
     * 是否需要打开精准定位服务
     * 
     * @return
     */
    public boolean doesNeedCheckoutPermissionPreciseLocation() {
        return doesNeedCheckoutPermission(ACCESS_FINE_LOCATION);
    }

    /**
     * 是否需要打开大致定位服务
     * 
     * @return
     */
    public boolean doesNeedCheckoutPermissionApproximateLocation() {
        return doesNeedCheckoutPermission(ACCESS_COARSE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doRequestPermissionsNext(requestCode, grantResults);
    }

    /*
     * 处理申请权限返回判断
     */

    protected void doRequestPermissionsNext(int requestCode, int[] grantResults) {
        if (grantResults.length != 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 允许
                onRequestPermissionsGranted(requestCode);
            } else {
                // Permission Denied 拒绝
                onRequestPermissionsDenied(requestCode);
            }
        }
    }

    /**
     * 申请权限同意返回的
     *
     * @param requestCode
     *            当前权限类型
     */
    protected void onRequestPermissionsGranted(int requestCode) {
        /* T.showShort(this, "授权权限成功"); */
        Toast.makeText(this, "授权权限成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 申请权限拒绝了返回的
     *
     * @param requestCode
     *            当前权限类型
     */
    protected void onRequestPermissionsDenied(int requestCode) {
        showDeniedPermissionsAlert(requestCode);
    }

    /**
     * 申请权限初次取消再次弹窗提示然后弹窗消失的时候的回调
     *
     * @param requestCode
     */
    protected void onRequestPermissionsDialogDismiss(int requestCode) {
    }

    /**
     * 申请权限拒绝之后的提醒对话框
     *
     * @param requestCode
     */
    private void showDeniedPermissionsAlert(final int requestCode) {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.common_view_permissions_show);
        TextView tvContent = (TextView) window.findViewById(R.id.v_p_content);
        // 申请WRITE_EXTERNAL_STORAGE权限
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            tvContent.setText(WRITE_EXTERNAL_STORAGE_REQUEST_CODE_MESSAGE_MM);
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            tvContent.setText(CAMERA_REQUEST_CODE_MESSAGE_MM);
        } else if (requestCode == CALL_PHONE_REQUEST_CODE) {
            tvContent.setText(CALL_PHONE_REQUEST_CODE_MESSAGE_MM);
        } else if (requestCode == READ_PHONE_STATE) {
            tvContent.setText(READ_PHONE_STATE_MESSAGE_MM);
        } else if (requestCode == ACCESS_FINE_LOCATION) {
            tvContent.setText(ACCESS_FINE_LOCATION_MESSAGE_MM);
        } else if (requestCode == ACCESS_COARSE_LOCATION) {
            tvContent.setText(ACCESS_COARSE_LOCATION_MESSAGE_MM);
        }
        Button btnCancel = (Button) window.findViewById(R.id.v_p_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                onRequestPermissionsDialogDismiss(requestCode);
                doesNeedFinish();
            }
        });
        Button btnSet = (Button) window.findViewById(R.id.v_p_set);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSetPermissions();
                alertDialog.dismiss();
                onRequestPermissionsDialogDismiss(requestCode);
                doesNeedFinish();
            }
        });
    }

    /**
     * 打开权限设置界面
     */
    private void gotoSetPermissions() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        startActivity(intent);
    }

    /**
     * 获取当前activity的名字，如果是Loading且获取SD卡权限的，不给就关掉应用
     *
     * @return
     */
    protected void doesNeedFinish() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        // if (name.equals("com.kalemao.thalassa.ui.LoadingActivity")) {
        // /*
        // * exit();
        // * android.os.Process.killProcess(android.os.Process.myPid()); //
        // * 获取PID MobclickAgent.onKillProcess(this); System.exit(0);
        // */
        // }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
