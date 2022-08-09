package com.qm.module_juggle.qr

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zbar.ZBarView
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.DLog
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.router.RouterManager
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeActQrBinding
import com.qm.module_juggle.utils.JugglePathUtils


/**
 * @ClassName QRActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/30 3:47 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.JuggleAct.JUGGLE_QR)
class QRActivity : JYActivity<HomeActQrBinding, QRViewModel>() {

    private lateinit var qrCodeView: ZBarView

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.home_act_qr
    }

    override fun initData() {
        super.initData()

        initView()
    }

    private fun getBind(): HomeActQrBinding {
        return binding as HomeActQrBinding
    }

    private fun initView() {
        qrCodeView = getBind().zbarview

        qrCodeView.setDelegate(object : QRCodeView.Delegate {
            override fun onScanQRCodeSuccess(result: String) {
                //扫描成功后处理事件
                Toast.makeText(this@QRActivity, result, Toast.LENGTH_SHORT).show()
                qrCodeView.startSpot() //继续扫描
                val txtText = findViewById<View>(R.id.txtText) as TextView
                txtText.text = result

                DLog.d("result -> $result")
                if(result.startsWith("arouter")){
                    RouterManager.instance.gotoNativeApp(result)
                }
                qrCodeView.stopCamera()

            }

            override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
                // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
                var tipText: String = qrCodeView.scanBoxView.tipText
                val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
                if (isDark) {
                    if (!tipText.contains(ambientBrightnessTip)) {
                        qrCodeView.scanBoxView.tipText = tipText + ambientBrightnessTip
                    }
                } else {
                    if (tipText.contains(ambientBrightnessTip)) {
                        tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                        qrCodeView.scanBoxView.tipText = tipText
                    }
                }
            }

            override fun onScanQRCodeOpenCameraError() {
                Toast.makeText(this@QRActivity, "错误", Toast.LENGTH_SHORT).show()
            }
        })
        qrCodeView.startCamera()

        getBind().startSpot.setOnClickListener {
            onStart()
            Toast.makeText(this@QRActivity, "开始扫码", Toast.LENGTH_SHORT).show()
        }

        getBind().stopSpot.setOnClickListener {
            onStop()
            Toast.makeText(this@QRActivity, "停止扫码", Toast.LENGTH_SHORT).show()
        }

        getBind().openFlashlight.setOnClickListener {
            qrCodeView.openFlashlight()
            Toast.makeText(this@QRActivity, "打开闪光灯", Toast.LENGTH_SHORT).show()
        }

        getBind().closeFlashlight.setOnClickListener {
            qrCodeView.closeFlashlight()
            Toast.makeText(this@QRActivity, "关闭闪光灯", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStart() {
        super.onStart()
        qrCodeView.startCamera()
        qrCodeView.startSpotAndShowRect()
    }

    override fun onStop() {
        qrCodeView.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        qrCodeView.onDestroy()
        super.onDestroy()
    }

}