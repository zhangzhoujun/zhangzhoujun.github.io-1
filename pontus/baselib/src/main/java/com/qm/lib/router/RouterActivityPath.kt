package com.qm.lib.router

/**
 * @ClassName RouterActivityPath
 * @Description 用于组件开发中，ARouter单Activity跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 * @Author zhangzhoujun
 * @Date 2020/5/13 7:01 PM
 * @Version 1.0
 */
class RouterActivityPath {

    /**
     * 配置页
     */
    object JuggleAct {
        private const val JUGGLEACT = "/Juggle"
        const val JUGGLE_INDEX = "$JUGGLEACT/index"
        const val JUGGLE_DIALOG = "$JUGGLEACT/dialog"
        const val JUGGLE_DIALOG_SHOW = "$JUGGLEACT/dialogShow"
        const val JUGGLE_QR = "$JUGGLEACT/qr"
        const val JUGGLE_PLAYER = "$JUGGLEACT/player"
        const val JUGGLE_BIGPIC = "$JUGGLEACT/bigpic"
    }

    /**
     * 主页
     */
    object Main {
        private const val MAIN = "/main"
        const val MAIN_INDEX = "$MAIN/index"
        const val MAIN_GUIDE = "$MAIN/guide"
        const val MAIN_THIRD = "$MAIN/third"
        const val MAIN_USAGE = "$MAIN/usage"
    }

    /**
     * 登录注册
     */
    object Login {
        private const val LOGIN = "/login"
        const val LOGIN_LOGIN = "$LOGIN/login"
    }


    /**
     * 商品　
     */
    object Goods {
        private const val GOODS = "/goods"
        const val GOODS_MAIN = "$GOODS/index"
    }

    /**
     * 我的
     */
    object Mine {
        private const val MINE_ACT = "/mineAct"
        const val MINE_SET = "$MINE_ACT/set"
        const val INFO = "$MINE_ACT/info"
    }

    /**
     * UMShare
     */
    object UMShare {
        private const val UM_SHARE = "/umshare"
        const val SHARE_GUIDE = "$UM_SHARE/guide"
        const val SHARE_MAIN = "$UM_SHARE/main"
        const val SHARE_ONE_DIALOG = "$UM_SHARE/oneDialog"
    }

    /**
     * PAY
     */
    object Pay {
        private const val PAY = "/pay"
        const val PAY_MAIN = "$PAY/main"
    }

    /**
     * COMMON
     */
    object Common {
        private const val COMMON = "/common"
        const val COMMON_RESULT = "$COMMON/result"
        const val COMMON_FEEDBACK = "$COMMON/feedback"
    }

    /**
     * Version update
     */
    object Version {
        private const val VERSION = "/version"
        const val VERSION_UPDATE = "$VERSION/update"
        const val VERSION_DOWNLOAD = "$VERSION/download"
    }

    /**
     * Webview
     */
    object Webview {
        private const val WEBVIEW = "/webact"
        const val WEBVIEW_MAIN = "$WEBVIEW/index"
    }

    object Watch {
        private const val WATCH = "/watch"
        const val WATCH_MAIN = "$WATCH/main"
        const val WATCH_WALK_MAIN = "$WATCH/walkmain"
        const val WATCH_XINLV_MAIN = "$WATCH/xinlvmain"
        const val WATCH_XUEYANG_MAIN = "$WATCH/mainxueyang"
        const val WATCH_TIWEN_MAIN = "$WATCH/maintiwen"
        const val WATCH_XUEYA_MAIN = "$WATCH/mainxueya"
        const val WATCH_SHUIMIAN_MAIN = "$WATCH/mainshuimian"
    }


    /**
     * Step
     */
    object Step {
        private const val STEP = "/step"
        const val STEP_INDEX = "$STEP/index"
        const val STEP_RECORD = "$STEP/record"
    }
}