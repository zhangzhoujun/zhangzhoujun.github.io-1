package com.gos.nodetransfer.ui.other

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.bus.RxBus
import com.dim.library.utils.DLog
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.DownloadAppMessage
import com.qm.lib.entity.IBXAppMessage
import com.qm.lib.entity.ShowVideoEvent
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.JYUtils
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.utils.StringUtils
import com.touchxd.wreadsdk.WReadSDK
import com.touchxd.wreadsdk.model.ReadCode
import com.touchxd.wreadsdk.wmini.WMiniListener
import org.json.JSONObject


/**
 * @ClassName OthetAllActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/23 4:46 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Main.MAIN_THIRD)
class ThirdAllActivity : Activity() {

    @JvmField
    @Autowired(name = JYComConst.HOME_THIRD_TYPE)
    var third_type: String = ""

    @JvmField
    @Autowired(name = JYComConst.HOME_THIRD_KEY)
    var third_key: String = ""

    @JvmField
    @Autowired(name = "data")
    var data: String = ""

    @JvmField
    @Autowired(name = "third_dialog")
    var third_dialog: Boolean = true // 是否要显示加载框

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)

        if (StringUtils.instance.isEmpty(third_type)) {
            finish()
            return
        }
        DLog.d("ThirdAllActivity", "third_type = $third_type , third_key = $third_key")

        SLSLogUtils.instance.sendLogLoad(
            "ThirdAllActivity",
            third_type,
            "LOAD",
            -1,
            "third_key=$third_key"
        )

        when (third_type) {
//            "luckydraw" -> SceneUtil.luckyDraw(this) // 幸运转盘
//            "dati" -> SceneUtil.answerPage(this) // 答题
//            "ccy" -> SceneUtil.idiomPage(this) // 猜成语
//            "ggk" -> SceneUtil.scrapPage(this) // 刮刮卡
            "video" -> RxBus.getDefault().post(ShowVideoEvent(third_key, "1", third_dialog))// 激励视屏
            "IBXApp" -> RxBus.getDefault().post(IBXAppMessage())
            "read" -> openMiniProgram(third_key)

            "videoFN" -> RxBus.getDefault().post(ShowVideoEvent("", "2", third_dialog))// 激励视屏
            "downloadOrOpen" -> RxBus.getDefault().post(DownloadAppMessage(third_key))
            "videoAll" -> {
                // data={"CSJ_VIDEO_ID":"946148155","GDT_VIDEO_ID":"","KS_VIDEO_ID":""}
                DLog.d("ThirdAllActivity", "data = $data")
                val jsonObject = JSONObject(data)
                var CSJ_VIDEO_ID = ""
                var GDT_VIDEO_ID = ""
                var KS_VIDEO_ID = ""
                if (jsonObject.has("CSJ_VIDEO_ID")) {
                    DLog.d("ThirdAllActivity", "CSJ_VIDEO_ID =  ${jsonObject.get("CSJ_VIDEO_ID")}")
                    CSJ_VIDEO_ID = jsonObject.get("CSJ_VIDEO_ID").toString()
                }
                if (jsonObject.has("GDT_VIDEO_ID")) {
                    DLog.d("ThirdAllActivity", "GDT_VIDEO_ID =  ${jsonObject.get("GDT_VIDEO_ID")}")
                    GDT_VIDEO_ID = jsonObject.get("GDT_VIDEO_ID").toString()
                }
                if (jsonObject.has("KS_VIDEO_ID")) {
                    DLog.d("ThirdAllActivity", "KS_VIDEO_ID =  ${jsonObject.get("KS_VIDEO_ID")}")
                    KS_VIDEO_ID = jsonObject.get("KS_VIDEO_ID").toString()
                }
                RxBus.getDefault()
                    .post(ShowVideoEvent(CSJ_VIDEO_ID, GDT_VIDEO_ID, KS_VIDEO_ID, third_dialog))
            }
        }
        finish()
    }

    private fun openMiniProgram(readId: String) {
        var readCode = ReadCode.Builder()
            .setAppId(JYUtils.instance.getMetaDatByName(this, "WX_APP_ID"))
            .setReadId(readId)
            .setUserId(LocalUserManager.instance.getUserId())
            .setExtra("")
            .build()

        WReadSDK.openWxMini(this, readCode, object : WMiniListener {
            override fun onReward(rdata: Int) {

            }

            override fun onError(type: Int, code: Int, msg: String) {

            }
        })
    }

}