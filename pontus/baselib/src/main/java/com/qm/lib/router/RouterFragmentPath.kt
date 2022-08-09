package com.qm.lib.router

/**
 * @ClassName RouterFragmentPath
 * @Description 用于组件开发中，ARouter多Fragment跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 * @Author zhangzhoujun
 * @Date 2020/5/13 7:03 PM
 * @Version 1.0
 */
class RouterFragmentPath {

    /**
     * 配置页
     */
    object JuggleFra {
        private const val JUGGLEFRA = "/JuggleFra"
        const val JUGGLE_FRA_INDEX = "$JUGGLEFRA/index"
        const val JUGGLE_FRA_DIY = "$JUGGLEFRA/diy"
    }

    /**
     * 主APP
     */
    object Main {
        private const val MAIN = "/ValueChain"
        const val MIAN_KS = "$MAIN/ks"
        const val MIAN_TASK = "$MAIN/task"
        const val MIAN_KS_NEW = "$MAIN/ks_new"
    }

    /**
     * 服务站
     */
    object Station {
        private const val STATION = "/Station"
        const val STATION_INDEX = "$STATION/index"
    }

    /**
     * 个人中心
     */
    object Mine {
        private const val MINE = "/mine"
        const val MINE_INDEX = "$MINE/index"
    }

    /**
     * 商学院
     */
    object School {
        private const val SCHOOL = "/school"
        const val SCHOOL_INDEX = "$SCHOOL/index"
    }

    /**
     * Webview
     */
    object Webview {
        private const val WEB = "/webFragment"
        const val WEB_INDEX = "$WEB/index"
    }

    /**
     * watch
     */
    object Watch {
        private const val WATCH = "/watchFragment"
        const val WATCH_INDEX = "$WATCH/index"
        const val WATCH_SPORT = "$WATCH/sport"
        const val WATCH_DEVICE = "$WATCH/device"
    }
}