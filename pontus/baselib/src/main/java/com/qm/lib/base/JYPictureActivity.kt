package com.qm.lib.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.qm.lib.utils.JYComConst

/**
 * @ClassName JYActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/14 8:33 PM
 * @Version 1.0
 */
abstract class JYPictureActivity<V : ViewDataBinding?, VM : PictureViewModel?> :
    JYActivity<V, VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getPictureVM().initPictureViewModel(this@JYPictureActivity)
    }

    private fun getPictureVM(): PictureViewModel {
        return viewModel as PictureViewModel
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        var result: Uri? = null
        if (requestCode == JYComConst.FILE_CAMERA_RESULT_CODE) {
            if (null != data && null != data.data) {
                result = data.data
            }
            getPictureVM().onGetPicBack(requestCode, result)

        } else if (requestCode == JYComConst.FILE_CHOOSER_RESULT_CODE) {
            if (data != null) {
                result = data.data
            }
            getPictureVM().onGetPicBack(requestCode, result)
        } else if(requestCode == JYComConst.FILE_REQUEST_CROP){
            if (null != data && null != data.data) {
                result = data.data
            }
            getPictureVM().onGetPicBack(requestCode, result)
        }
    }
}