package com.qm.lib.base

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.dim.library.utils.DLog
import com.dim.library.utils.RxUtils
import com.dim.library.utils.ToastUtils
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.entity.COssConfig
import com.qm.lib.entity.MOssConfig
import com.qm.lib.http.BaseService
import com.qm.lib.http.RetrofitClient
import com.qm.lib.utils.*
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @ClassName PictureViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/6/25 9:05 PM
 * @Version 1.0
 */
abstract class PictureViewModel(application: Application) : ToolbarViewModel(application) {

    private lateinit var ossConfig: MOssConfig
    private lateinit var mActivity: FragmentActivity

    var mNeedCrop = false

    fun initPictureViewModel(activity: FragmentActivity) {
        mActivity = activity
    }

    protected fun onPictureClick(needCrop: Boolean = false) {
        mNeedCrop = needCrop
        if (lacksPermissions(getApplication(), permissions)) {
            initPermissions()
        } else {
            openImageChooser()
        }
    }

    //// premission

    /**
     * 读写权限  自己可以添加需要判断的权限
     */
    private var permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    private fun lacksPermissions(
        mContexts: Context,
        permissions: Array<String>
    ): Boolean {
        for (permission in permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否缺少权限
     */
    private fun lacksPermission(
        mContexts: Context,
        permission: String
    ): Boolean {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED
    }

    private fun initPermissions() {
        // RxPermissions 0.10.2 有个fragment的问题 会导致crash，先 延迟 + 捕获处理下看看情况
        try {
            val rxPermissions =
                RxPermissions(mActivity)
            Observable.just("")
                .delay(200, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(lifecycleProvider))
                .compose(RxUtils.schedulersTransformer())
                .subscribe {
                    rxPermissions.request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                        .debounce(1, TimeUnit.SECONDS)
                        .subscribe { aBoolean: Boolean? ->
                            if (!aBoolean!!) {
                                ToastUtils.showShort("请同意权限")
                            } else {
                                openImageChooser()
                            }
                        }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //// pic


    //拍照图片路径
    private var cameraFielPath: String? = null

    private fun openImageChooser() {
        val items = arrayOf<CharSequence>("拍照", "相册")
        MaterialDialog.Builder(mActivity)
            .items(*items)
            .positiveText("取消")
            .onPositive { dialog, _ ->
                dialog.dismiss()
            }
            .cancelable(false)
            .canceledOnTouchOutside(false)
            .itemsCallback { _, _, position, _ ->
                if (position == 0) {
                    SLSLogUtils.instance.sendLogClick("Picture", "", "CAMERO")
                    takeCamera()
                } else if (position == 1) {
                    SLSLogUtils.instance.sendLogClick("Picture", "", "ALBUM")
                    takePhoto()
                }
            }.show()
    }

    //选择图片
    private fun takePhoto() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        mActivity.startActivityForResult(
            Intent.createChooser(i, "Image Chooser"), JYComConst.FILE_CHOOSER_RESULT_CODE
        )
    }

    //拍照
    fun takeCamera() {
        val saveFile = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )
        cameraFielPath = saveFile.path
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(mActivity, saveFile))
            mActivity.startActivityForResult(
                intent, JYComConst.FILE_CAMERA_RESULT_CODE
            )
        } else {
            val intentFromCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intentFromCapture.putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(saveFile)
            )
            mActivity.startActivityForResult(
                intentFromCapture, JYComConst.FILE_CAMERA_RESULT_CODE
            )
        }
    }

    fun onGetPicBack(type: Int, uri: Uri?) {
        var result: Uri? = null
        // 拍照
        if (type == JYComConst.FILE_CAMERA_RESULT_CODE) {
            if (mNeedCrop) {
                cropPhoto(cameraFielPath!!)
                return
            }
            if (uri == null && hasFile(cameraFielPath)) {
                result = Uri.fromFile(File(cameraFielPath))
            } else {
                result = uri
            }
        }
        // 相机
        else if (type == JYComConst.FILE_CHOOSER_RESULT_CODE) {
            result = uri

            if (result == null || mNeedCrop) {
                cropPhoto(FileUtil.getFilePathByUri(mActivity, result))
                return
            }
        } else {
            if (uri == null && hasFile(cropUrl)) {
                result = Uri.fromFile(File(cropUrl))
            } else {
                result = uri
            }
        }
        getOssConfig(FileUtil.getFilePathByUri(mActivity, result))
    }

    fun uploadImg(path: String) {
        val file = File(path)
        val fileName = file.name
        if (ossConfig == null) {
            ToastUtils.showShort("上传失败")
            return
        }
        val config = COssConfig(
            ossConfig.AccessKeyId, ossConfig.Policy,
            ossConfig.Signature, "201",
            ossConfig.Directory.toString() + fileName
        )
        SystemUtil.fileUpload(
            ossConfig.Host, file, config
        ) { response ->
            if (response.isSuccess) {
                val location: String = response.location
                onPicUploadSuccess(response.key, location)
            } else {
                ToastUtils.showShort("上传失败...")
            }
        }
    }

    /**
     * 判断文件是否存在
     */
    fun hasFile(path: String?): Boolean {
        try {
            val f = File(path)
            if (!f.exists()) {
                return false
            }
        } catch (e: java.lang.Exception) {
            // TODO: handle exception
            return false
        }
        return true
    }

    private fun getOssConfig(path: String) {
        val api: BaseService = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance().execute(
            api.ossConfig,
            object : AppObserver<BaseResultBean<MOssConfig>>() {
                override fun onSuccess(o: BaseResultBean<MOssConfig>) {
                    super.onSuccess(o)
                    if (o.doesSuccess()) {
                        ossConfig = o.data
                        uploadImg(path)
                    }
                }
            })
    }

    protected abstract fun onPicUploadSuccess(key: String, path: String)

    var cropUrl: String? = ""

    // 图片裁剪
    private fun cropPhoto(path: String) {

        val uri = Uri.fromFile(File(path))

        cropUrl =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "crop" + path.substring(
                path.lastIndexOf("/") + 1
            )

        val cropUri = Uri.fromFile(File(cropUrl))
        DLog.d(" 保存裁剪图片路径：$cropUrl")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(getUriForFile(mActivity, File(path)), "image/*")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri)
            intent.putExtra("crop", "true")
            intent.putExtra("aspectX", 1) // 裁剪框比例

            intent.putExtra("aspectY", 1)
            intent.putExtra("outputX", 400) // 输出图片大小

            intent.putExtra("outputY", 400)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            mActivity.startActivityForResult(intent, JYComConst.FILE_REQUEST_CROP)
        } else {
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(uri, "image/*")
            intent.putExtra("crop", "true")

            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)

            intent.putExtra("outputX", 320)
            intent.putExtra("outputY", 320)
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri)
            mActivity.startActivityForResult(intent, JYComConst.FILE_REQUEST_CROP)
        }
    }

    private fun getUriForFile(context: Context?, file: File?): Uri? {
        if (context == null || file == null) {
            throw java.lang.NullPointerException()
        }
        val uri: Uri
        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //            uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", file);
            FileProvider.getUriForFile(
                context.applicationContext,
                RuntimeData.getInstance().applicationId.toString() + ".fileProvider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
        DLog.d("uri : " + uri.path.toString())
        return uri
    }
}