package com.kalemao.library.base

import android.Manifest
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.kalemao.library.R
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast

/**
 * Created by dim on 2017/6/6 11:28
 * 邮箱：271756926@qq.com
 */
abstract class BaseFragment : Fragment() {

    /**
     * 申请对应的权限
     */
    protected fun doesNeedCheckoutPermission(requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            // 申请WRITE_EXTERNAL_STORAGE权限
            if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 申请WRITE_EXTERNAL_STORAGE权限
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            } else if (requestCode == CAMERA_REQUEST_CODE && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 申请CAMERA权限
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            } else if (requestCode == CALL_PHONE_REQUEST_CODE && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 申请CAMERA权限
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), CALL_PHONE_REQUEST_CODE)
            } else if (requestCode == READ_PHONE_STATE && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // 申请READ_PHONE_STATE权限
                requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), READ_PHONE_STATE)
            } else if (requestCode == ACCESS_FINE_LOCATION && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请精准定位服务
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
            } else if (requestCode == ACCESS_COARSE_LOCATION && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请大致定位服务
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), ACCESS_COARSE_LOCATION)
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
     * 是否具有大致定位服务

     * @return
     */
    fun doesHaveCheckoutPermissionApproximateLocation(): Boolean {
        return doesNeedCheckoutPermission(ACCESS_COARSE_LOCATION)
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
     * 是否需要申请打电话权限

     * @return
     */
    protected fun doesNeedCheckoutPermissionCallPhone(): Boolean {
        return doesNeedCheckoutPermission(CALL_PHONE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        doRequestPermissionsNext(requestCode, grantResults)
    }

    /**
     * 是否需要打开精准定位服务

     * @return
     */
    fun doesNeedCheckoutPermissionPreciseLocation(): Boolean {
        return doesNeedCheckoutPermission(ACCESS_FINE_LOCATION)
    }

    /**
     * 是否需要打开大致定位服务

     * @return
     */
    fun doesNeedCheckoutPermissionApproximateLocationFragment(): Boolean {
        return doesNeedCheckoutPermission(ACCESS_COARSE_LOCATION)
    }

    /**
     * 处理申请权限返回判断
     */
    private fun doRequestPermissionsNext(requestCode: Int, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted 允许
            onRequestPermissionsGrantedFragment(requestCode)
        } else {
            // Permission Denied 拒绝
            onRequestPermissionsDeniedFragment(requestCode)
        }
    }

    /**
     * 申请权限同意返回的

     * @param requestCode
     * *            当前权限类型
     */
    protected fun onRequestPermissionsGrantedFragment(requestCode: Int) {
        toast(resources.getString(R.string.permission_ok))
    }

    /**
     * 申请权限拒绝了返回的

     * @param requestCode
     * *            当前权限类型
     */
    protected fun onRequestPermissionsDeniedFragment(requestCode: Int) {
        showDeniedPermissionsAlertFragment(requestCode)
    }

    /**
     * 申请权限初次取消再次弹窗提示然后弹窗消失的时候的回调

     * @param requestCode
     */
    protected fun onRequestPermissionsDialogDismissFragment(requestCode: Int, goToSet: Boolean) {}

    /**
     * 申请权限拒绝之后的提醒对话框

     * @param requestCode
     */
    private fun showDeniedPermissionsAlertFragment(requestCode: Int) {
        if (requestCode == 65543) {
            return
        }
        var toastMessageRequest: String
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
                onRequestPermissionsDialogDismissFragment(requestCode, true)
            }
            negativeButton(resources.getString(R.string.cancel)) {
                this@alert.dismiss()
                onRequestPermissionsDialogDismissFragment(requestCode, false)
            }
        }.show()
    }

    /**
     * 获取当前应用的名称
     */
    fun getApplicationName(): String {
        var packageManager: PackageManager? = null
        var applicationInfo: ApplicationInfo? = null
        try {
            packageManager = activity.applicationContext.packageManager
            applicationInfo = packageManager!!.getApplicationInfo(activity.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInfo = null
        }

        val applicationName = packageManager!!.getApplicationLabel(applicationInfo) as String
        return applicationName
    }

    /**
     * 打开权限设置界面
     */
    private fun gotoSetPermissions() {
        val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
        startActivity(intent)
    }

    // 检测SD卡权限
    val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1
    // 相机权限
    val CAMERA_REQUEST_CODE = 2
    // 电话权限
    val CALL_PHONE_REQUEST_CODE = 3
    // READ_PHONE_STATE
    val READ_PHONE_STATE = 4
    // 精准定位服务
    val ACCESS_FINE_LOCATION = 5
    // 大致定位服务
    val ACCESS_COARSE_LOCATION = 6
}