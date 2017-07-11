package com.kalemao.library.base

import android.Manifest
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.kalemao.library.R
import com.kalemao.library.utils.ActivityManager
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast


/**
 * Created by dim on 2017/5/27 10:07
 * 邮箱：271756926@qq.com
 */
abstract class BaseActivity : AppCompatActivity() {
    // 检测SD卡权限
    val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1
    // 相机权限
    val CAMERA_REQUEST_CODE = 2
    // 电话权限
    val CALL_PHONE_REQUEST_CODE = 3
    //  READ_PHONE_STATE
    val READ_PHONE_STATE = 4
    // 精准定位服务
    val ACCESS_FINE_LOCATION = 5
    // 大致定位服务
    val ACCESS_COARSE_LOCATION = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.getInstance().addActivity(this)
        beforeInit()
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID())
        }
        //绑定activity
        ButterKnife.bind( this ) ;

        initView(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.getInstance().popOneActivity(this)
    }

    /**
     * 界面初始化前期准备
     */
    protected fun beforeInit() {}

    /**
     * 获取布局ID
     * @return 布局id
     */
    abstract protected fun getContentViewLayoutID(): Int

    /**
     * 初始化布局以及View控件
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 申请对应的权限
     */
    protected fun doesNeedCheckoutPermission(requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            // 申请WRITE_EXTERNAL_STORAGE权限
            if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            } else if (requestCode == CAMERA_REQUEST_CODE && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 申请CAMERA权限
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            } else if (requestCode == CALL_PHONE_REQUEST_CODE && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 申请电话权限
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), CALL_PHONE_REQUEST_CODE)
            } else if (requestCode == READ_PHONE_STATE && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // 申请READ_PHONE_STATE权限
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), READ_PHONE_STATE)
            } else if (requestCode == ACCESS_FINE_LOCATION && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请精准定位服务
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
            } else if (requestCode == ACCESS_COARSE_LOCATION && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请大致定位服务
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), ACCESS_COARSE_LOCATION)
            } else {
                return false
            }
            return true
        } else {
            // Pre-Marshmallow
            return false
        }
    }

    /**
     * 是否具有某种权限
     * @param requestCode
     * *
     * @return
     */
    protected fun doesHaveCheckoutPermission(requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            } else if (requestCode == CAMERA_REQUEST_CODE && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            } else if (requestCode == CALL_PHONE_REQUEST_CODE && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            } else if (requestCode == READ_PHONE_STATE && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            } else if (requestCode == ACCESS_FINE_LOCATION && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            } else if (requestCode == ACCESS_COARSE_LOCATION && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            } else {
                return false
            }
            return true
        } else {
            // Pre-Marshmallow
            return false
        }
    }

    /**
     * 是否需要申请SD卡权限

     * @return
     */
    protected fun doesNeedCheckoutPermissionWriteExternalStorage(): Boolean {
        return doesNeedCheckoutPermission(WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
    }

    /**
     * 是否需要申请相机权限

     * @return
     */
    protected fun doesNeedCheckoutPermissionCamera(): Boolean {
        return doesNeedCheckoutPermission(CAMERA_REQUEST_CODE)
    }

    /**
     * 是否需要申请READ_PHONE_STATE权限

     * @return
     */
    protected fun doesNeedCheckoutPermissionPhotoState(): Boolean {
        return doesNeedCheckoutPermission(READ_PHONE_STATE)
    }

    /**
     * 是否需要申请打电话权限

     * @return
     */
    protected fun doesNeedCheckoutPermissionCallPhone(): Boolean {
        return doesNeedCheckoutPermission(CALL_PHONE_REQUEST_CODE)
    }

    /**
     * 是否需要打开精准定位服务
     * @return
     */
    protected fun doesNeedCheckoutPermissionPreciseLocation(): Boolean {
        return doesNeedCheckoutPermission(ACCESS_FINE_LOCATION)
    }

    /**
     * 是否需要打开大致定位服务
     * @return
     */
    protected fun doesNeedCheckoutPermissionApproximateLocation(): Boolean {
        return doesNeedCheckoutPermission(ACCESS_COARSE_LOCATION)
    }

    /**
     * 是否具有大致定位服务
     * @return
     */
    protected fun doesHaveCheckoutPermissionApproximateLocation(): Boolean {
        return doesHaveCheckoutPermission(ACCESS_COARSE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        doRequestPermissionsNext(requestCode, grantResults)
    }

    /*
     * 处理申请权限返回判断
     */
    protected fun doRequestPermissionsNext(requestCode: Int, grantResults: IntArray) {
        if (grantResults.size != 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 允许
                onRequestPermissionsGranted(requestCode)
            } else {
                // Permission Denied 拒绝
                onRequestPermissionsDenied(requestCode)
            }
        }
    }

    /**
     * 申请权限同意返回的

     * @param requestCode
     * *            当前权限类型
     */
    protected fun onRequestPermissionsGranted(requestCode: Int) {
        toast(resources.getString(R.string.permission_ok))
    }

    /**
     * 申请权限拒绝了返回的

     * @param requestCode
     * *            当前权限类型
     */
    protected fun onRequestPermissionsDenied(requestCode: Int) {
        showDeniedPermissionsAlert(requestCode)
    }

    /**
     * 申请权限初次取消再次弹窗提示然后弹窗消失的时候的回调

     * @param requestCode
     */
    protected fun onRequestPermissionsDialogDismiss(requestCode: Int, goToSet: Boolean) {}

    /**
     * 申请权限拒绝之后的提醒对话框
     * @param requestCode
     */
    protected fun showDeniedPermissionsAlert(requestCode: Int) {
        if (requestCode == 65543) {
            return
        }
        var toastMessageRequest = ""
        var toastMessage: String

        // 申请WRITE_EXTERNAL_STORAGE权限
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            toastMessageRequest = resources.getString(R.string.request_write_external_storage)
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            toastMessageRequest = resources.getString(R.string.request_camera)
        } else if (requestCode == CALL_PHONE_REQUEST_CODE) {
            toastMessageRequest = resources.getString(R.string.request_call_phone)
        } else if (requestCode == ACCESS_FINE_LOCATION) {
            toastMessageRequest = resources.getString(R.string.request_access_fine_location)
        } else if (requestCode == ACCESS_COARSE_LOCATION) {
            toastMessageRequest = resources.getString(R.string.request_access_fine_location)
        } else if (requestCode == READ_PHONE_STATE) {
            toastMessageRequest = resources.getString(R.string.request_read_phone)
        } else {
            return
        }

        toastMessage = String.format("%s%s%s%s%s%s", resources.getString(R.string.request_access_left), getApplicationName(), toastMessageRequest, resources.getString(R.string.request_access_right_1),
                getApplicationName(), resources.getString(R.string.request_access_right_2))

        alert(toastMessage, resources.getString(R.string.permission_need)) {
            positiveButton(resources.getString(R.string.go_set)) {
                gotoSetPermissions()
                this@alert.dismiss()
                onRequestPermissionsDialogDismiss(requestCode, true)
            }
            negativeButton(resources.getString(R.string.cancel)) {
                this@alert.dismiss()
                onRequestPermissionsDialogDismiss(requestCode, false)
                doesNeedFinish()
            }
        }.show()
    }

    protected fun doesNeedFinish(){

    }

    /**
     * 获取当前应用的名称
     */
    protected fun getApplicationName(): String {
        var packageManager: PackageManager? = null
        var applicationInfo: ApplicationInfo? = null
        try {
            packageManager = applicationContext.packageManager
            applicationInfo = packageManager!!.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInfo = null
        }

        val applicationName = packageManager!!.getApplicationLabel(applicationInfo) as String
        return applicationName
    }

    /**
     * 打开权限设置界面
     */
    protected  fun gotoSetPermissions() {
        val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this@BaseActivity.finish()
    }
}