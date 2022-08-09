package com.qm.module_juggle.player

import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.ToastUtils
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYComConst
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeActPlayerBinding
import com.video.player.lib.manager.VideoPlayerManager
import com.video.player.lib.manager.VideoWindowManager


/**
 * @ClassName PlayerActivity
 * @Description 视频播放
 * https://github.com/Yuye584312311/iMusic
 * @Author zhangzhoujun
 * @Date 2020/11/15 9:33 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.JuggleAct.JUGGLE_PLAYER)
class PlayerActivity : JYActivity<HomeActPlayerBinding, PlayerViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.VIDEO_PLAYER_URL)
    var video_url: String = ""

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.home_act_player
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)

        if (TextUtils.isEmpty(video_url)) {
            ToastUtils.showShort("数据错误，请重试")
            finish()
            return
        }

        getBind().videoTrack.startPlayVideo(video_url, "")
    }

    private fun getBind(): HomeActPlayerBinding {
        return binding as HomeActPlayerBinding
    }

    override fun onResume() {
        super.onResume()
        VideoPlayerManager.getInstance().onResume()
    }

    override fun onPause() {
        super.onPause()
        VideoPlayerManager.getInstance().onPause()
    }

    override fun onBackPressed() {
        //尝试弹射返回
        if (VideoPlayerManager.getInstance().isBackPressed) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        VideoPlayerManager.getInstance().onDestroy()
        //如果你的Activity是MainActivity并且你开启过悬浮窗口播放器，则还需要对其释放
        VideoWindowManager.getInstance().onDestroy()
    }
}