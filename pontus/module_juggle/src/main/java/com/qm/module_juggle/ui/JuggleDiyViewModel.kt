package com.qm.module_juggle.ui

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.widget.RelativeLayout
import com.dim.library.utils.DLog
import com.google.gson.JsonObject
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.BaseBean
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.http.RetrofitClient
import com.qm.lib.utils.ColorUtils
import com.qm.lib.utils.JYUtils
import com.qm.lib.utils.RuntimeData
import com.qm.lib.utils.ScreenUtils
import com.qm.lib.utils.StringUtils
import com.qm.lib.widget.dim.AppImageView
import com.qm.lib.widget.dim.AppTextView
import com.qm.module_juggle.entity.MHomeDiyBean
import com.qm.module_juggle.http.HomeService
import com.qm.module_juggle.utils.JugglePathUtils
import org.json.JSONException
import org.json.JSONObject

/**
 * @ClassName JuggleDiyViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 3/3/21 11:37 AM
 * @Version 1.0
 */
class JuggleDiyViewModel(application: Application) : BaseAppViewModel(application) {

    lateinit var mRoot: RelativeLayout
    var pageKey: String = ""
    var pageTag: String = ""
    var pageTagLast: String = ""
    var pageServerData: String = ""
    var pageCount: Int = 1
    lateinit var mContext: Context

    fun initData(
        pageKey: String,
        root: RelativeLayout,
        context: Context,
        pageTag: String,
        pageCount: Int,
        pageTagLast: String,
        pageServerData: String
    ) {
        this.mRoot = root
        this.mContext = context
        this.pageTag = pageTag
        this.pageTagLast = pageTagLast
        this.pageCount = pageCount
        this.pageServerData = pageServerData

        if (pageKey.contains("=")) {
            var keyList = pageKey.split("=")
            this.pageKey = keyList[keyList.size - 1]
        } else {
            this.pageKey = pageKey
        }

        getDiyData()
    }

    private fun getDiyData() {
        val api: HomeService = RetrofitClient.getInstance().create(HomeService::class.java)

        RetrofitClient.getInstance()
            .execute(api.getPageDiyConfig(pageKey),
                object : AppObserver<BaseResultBean<MHomeDiyBean>>() {
                    override fun onSuccess(bean: BaseResultBean<MHomeDiyBean>) {
                        super.onSuccess(bean)
                        if (bean.doesSuccess()) {
                            if (pageLinkerServerMap.isEmpty() && bean.data.page_base.server_links != null && bean.data.page_base.server_links.isNotEmpty()) {
                                getPagelinkServer(bean.data)
                            } else {
                                initView(bean.data)
                            }
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                    }
                })
    }

    private var pageLinkerServerMap = HashMap<String, String>()

    private fun getPagelinkServer(bean: MHomeDiyBean) {
        pageLinkerServerMap.clear()
        val api: HomeService = RetrofitClient.getInstance().create(HomeService::class.java)

        for (index in bean.page_base.server_links) {
            RetrofitClient.getInstance().execute(
                api.getIndoAssets(
                    RuntimeData.getInstance().httpUrl + "/api" + index
                ),
                object : AppObserver<BaseResultBean<JsonObject>>() {
                    override fun onSuccess(o: BaseResultBean<JsonObject>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            pageLinkerServerMap[index] = o.data.toString()
                        }
                        onGetPagelinkServerBack(bean)
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        onGetPagelinkServerBack(bean)
                    }
                })
        }
    }

    private fun onGetPagelinkServerBack(bean: MHomeDiyBean) {
        if (pageLinkerServerMap != null && pageLinkerServerMap.size == bean.page_base.server_links.size) {
            initView(bean)
        }
    }

    private fun initView(data: MHomeDiyBean) {
        try {
            filterData(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var widHeight = data.page_base.width_height
        var rootWid = 0
        var rootHei = 0
        if (widHeight != null && !TextUtils.isEmpty(widHeight)) {
            val iconData: Array<String> =
                widHeight.split("\\*".toRegex()).toTypedArray()
            if (iconData.size == 2) {
                val wid: Int = ScreenUtils.getScreenWidth(mContext) / pageCount
                DLog.d("FUCKFUCK","wid = ===== -> $wid")
                val hei = wid * iconData[1].toInt() / iconData[0].toInt()
                var lp = mRoot.layoutParams
                lp.height = hei

                rootWid = wid
                rootHei = hei

                mRoot.layoutParams = lp

                if (data.page_base.bg_url.isNotEmpty()) {
                    var bgIcon = AppImageView(mContext)
                    bgIcon.setImageUrl(data.page_base.bg_url, wid, hei)
                    mRoot.addView(bgIcon)
                }
            }
        }
        for (index in 0 until data.rows.size) {
            var itemData = data.rows[index]
            itemData.pageKey = pageKey
            itemData.pageTag = pageTag
            itemData.lastPageTag = pageTagLast
            if (itemData.data_type == "text") {
                var textView = createTextView(itemData, rootWid, rootHei)
                mRoot.addView(textView)
            } else if (itemData.data_type == "button") {
                mRoot.addView(createButton(itemData, rootWid, rootHei))
            } else if (itemData.data_type == "data") {
                var textView = createTextViewData(itemData, rootWid, rootHei)

                mRoot.addView(textView)
            }
        }
    }

    private fun px2dip(pxValue: Float): Float {
//        val scale = mContext.resources.displayMetrics.density;
//        return pxValue / scale + 0.5f
        return pxValue / 2
    }

    private fun createTextView(
        itemData: MHomeDiyBean.MHomeDataItem,
        rootWid: Int,
        rootHei: Int
    ): AppTextView {
        var textView = AppTextView(mContext)

        var lp = RelativeLayout.LayoutParams(
            (rootWid * itemData.relatively.width / 100).toInt(),
            (rootHei * itemData.relatively.height / 100).toInt()
        )
        lp.leftMargin = (rootWid * itemData.relatively.left / 100).toInt()
        lp.topMargin = (rootHei * itemData.relatively.top / 100).toInt()

        textView.text = itemData.text

        if (itemData.text_align == "left") {
            textView.gravity = Gravity.LEFT
        } else if (itemData.text_align == "center") {
            textView.gravity = Gravity.CENTER
        } else if (itemData.text_align == "right") {
            textView.gravity = Gravity.RIGHT
        }
        textView.setTextColor(ColorUtils.instance.getColorForString(itemData.font_color))
        if (itemData.font_weight) {
            textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD);
        }

        textView.textSize = px2dip(itemData.font_size.toFloat())

        textView.layoutParams = lp

        textView.setOnClickListener {
            JugglePathUtils.instance.onJuggleItemClick(itemData, "DIY", -1)
        }

        return textView
    }

    private fun createTextViewData(
        itemData: MHomeDiyBean.MHomeDataItem,
        rootWid: Int,
        rootHei: Int
    ): AppTextView {
        var textView = AppTextView(mContext)

        var lp = RelativeLayout.LayoutParams(
            (rootWid * itemData.relatively.width / 100).toInt(),
            (rootHei * itemData.relatively.height / 100).toInt()
        )
        lp.leftMargin = (rootWid * itemData.relatively.left / 100).toInt()
        lp.topMargin = (rootHei * itemData.relatively.top / 100).toInt()

        if (pageServerData != null && !pageServerData.isEmpty()) {
            var dataString = pageServerData
            if (dataString.isNullOrEmpty()) {
                textView.text = itemData.text
            } else {
                var mapJSON = JSONObject(dataString)
                var textString = itemData.text

                val iter: Iterator<String> = mapJSON.keys()
                while (iter.hasNext()) {
                    val key = iter.next()
                    try {
                        val value = mapJSON.get(key).toString()
                        textString = textString.replace("{$key}", value)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                textView.text = textString

            }
        } else if (!itemData.data_server_link.isNullOrEmpty() && pageLinkerServerMap.containsKey(
                itemData.data_server_link
            )
        ) {
            var dataString = pageLinkerServerMap[itemData.data_server_link]
            if (dataString.isNullOrEmpty()) {
                textView.text = itemData.text
            } else {
                var mapJSON = JSONObject(dataString)
                var textString = itemData.text

                val iter: Iterator<String> = mapJSON.keys()
                while (iter.hasNext()) {
                    val key = iter.next()
                    try {
                        val value = mapJSON.get(key).toString()
                        textString = textString.replace("{$key}", value)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                textView.text = textString

            }
        } else {
            textView.text = itemData.text
        }

        if (itemData.text_align == "left") {
            textView.gravity = Gravity.LEFT
        } else if (itemData.text_align == "center") {
            textView.gravity = Gravity.CENTER
        } else if (itemData.text_align == "right") {
            textView.gravity = Gravity.RIGHT
        }
        textView.setTextColor(ColorUtils.instance.getColorForString(itemData.font_color))
        if (itemData.font_weight) {
            textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD);
        }

        textView.textSize = px2dip(itemData.font_size.toFloat())

        textView.layoutParams = lp

        textView.setOnClickListener {
            JugglePathUtils.instance.onJuggleItemClick(itemData, "DIY", -1)
        }

        return textView
    }

    private fun createButton(
        itemData: MHomeDiyBean.MHomeDataItem,
        rootWid: Int,
        rootHei: Int
    ): AppImageView {
        var iconWid = (rootWid * itemData.relatively.width / 100).toInt()
        var iconHeight = (rootHei * itemData.relatively.height / 100).toInt()

        var imageView = AppImageView(mContext)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        var lp = RelativeLayout.LayoutParams(iconWid, iconHeight)
        lp.leftMargin = (rootWid * itemData.relatively.left / 100).toInt()
        lp.topMargin = (rootHei * itemData.relatively.top / 100).toInt()

        imageView.layoutParams = lp

        val tl: Float = itemData.radius[0]
        val tr: Float = itemData.radius[1]
        val br: Float = itemData.radius[2]
        val bl: Float = itemData.radius[3]
        if (pageServerData != null && !pageServerData.isEmpty()) {
            var dataString = pageServerData
            if (dataString.isNullOrEmpty()) {
                imageView.setImageDiyUrl(
                    itemData.trigger_img,
                    iconWid,
                    iconHeight, tl, tr, br, bl
                )
            } else {
                var mapJSON = JSONObject(dataString)
                if (mapJSON.has(itemData.data_key)) {
                    imageView.setImageDiyUrl(
                        mapJSON.get(itemData.data_key).toString(),
                        iconWid,
                        iconHeight, tl, tr, br, bl
                    )
                }
            }
        } else if (!itemData.data_server_link.isNullOrEmpty() && pageLinkerServerMap.containsKey(
                itemData.data_server_link
            ) && !itemData.data_key.isNullOrEmpty()
        ) {
            var dataString = pageLinkerServerMap[itemData.data_server_link]
            if (dataString.isNullOrEmpty()) {
                imageView.setImageDiyUrl(
                    itemData.trigger_img,
                    iconWid,
                    iconHeight, tl, tr, br, bl
                )
            } else {
                var mapJSON = JSONObject(dataString)
                if (mapJSON.has(itemData.data_key)) {
                    imageView.setImageDiyUrl(
                        mapJSON.get(itemData.data_key).toString(),
                        iconWid,
                        iconHeight, tl, tr, br, bl
                    )
                }
            }
        } else {
            imageView.setImageDiyUrl(
                itemData.trigger_img,
                iconWid,
                iconHeight, tl, tr, br, bl
            )
        }

        imageView.setOnClickListener {
            JugglePathUtils.instance.onJuggleItemClick(itemData, "DIY", -1)
        }

        return imageView
    }

    private fun filterData(bean: MHomeDiyBean) {
        // 黑白名单 用户身份级别
        var mAppType = JYUtils.instance.getMetaDatByName(mContext, "QY_APP_NAME")
        var mUserType = LocalUserManager.instance.getUserIdentity()
        for (i in bean.rows.size - 1 downTo 0) {
            var item = bean.rows[i]
            if (isNeedDeleteUsers(
                    mAppType,
                    mUserType, item
                )
            ) {
                bean.rows.removeAt(i)
            }
        }

        // 黑白名单 用户级别
        var mUserMobile = LocalUserManager.instance.getUserMobile()
        for (i in bean.rows.size - 1 downTo 0) {
            var item = bean.rows[i]
            if (isNeedDeleteLevel(
                    mUserMobile,
                    item
                )
            ) {
                bean.rows.removeAt(i)
            }

        }

    }

    private fun isNeedDeleteUsers(
        mAppType: String,
        mUserType: String,
        bean: MHomeDiyBean.MHomeDataItem
    ): Boolean {
        // 先处理时间
        if (bean.limit_time != null
            && bean.limit_time.size == 2
            && bean.limit_time[0] != null
            && bean.limit_time[1] != null
            && bean.limit_time[0].isNotEmpty()
            && bean.limit_time[1].isNotEmpty()
            && bean.limit_time[0].length == 13
            && bean.limit_time[1].length == 13
        ) {
            val currentTime = System.currentTimeMillis()
            val startTime = bean.limit_time[0].toLong()
            val endTime = bean.limit_time[1].toLong()
            if (currentTime in startTime..endTime) {

            } else {
                return true
            }
        }

        var inWhiteLevel = false
        var hasWhiteLevel = false
        var inBlackLevel = false

        var whiteLevel = bean.white_level
        if (whiteLevel != null && whiteLevel.isNotEmpty()) {
            hasWhiteLevel = true
            for (index in whiteLevel.indices) {
                if (whiteLevel[index][0] == mAppType && whiteLevel[index][1] == mUserType) {
                    inWhiteLevel = true
                    break
                }
            }
        }

        // 黑名单
        var blackLevel = bean.black_level
        if (blackLevel != null && blackLevel.isNotEmpty()) {
            for (index in blackLevel.indices) {
                if (blackLevel[index][0] == mAppType && blackLevel[index][1] == mUserType) {
                    inBlackLevel = true
                    break
                }
            }
        }

        // 白名单 黑名单 判断
        // 如果白名单为空 直接判断黑名单
        // 如果用户在白名单，在判断在不在黑名单
        // 如果 白名单不为空 且 用户不在白名单 不给看
        if (hasWhiteLevel) {
            if (inWhiteLevel) {
                if (inBlackLevel) {
                    return true
                }
            } else {
                return true
            }
        } else {
            if (inBlackLevel) {
                return true
            }
        }
        return false
    }

    private fun isNeedDeleteLevel(
        mUserMobile: String,
        bean: MHomeDiyBean.MHomeDataItem
    ): Boolean {
        var inWhiteUsers = false
        var hasWhiteUsers = false
        var inBlackUsers = false

        // 白名单
        var whiteUsers = bean.white_users
        if (whiteUsers != null && !StringUtils.instance.isEmpty(whiteUsers)) {
            hasWhiteUsers = true
            var idArrays = whiteUsers.split(",")
            for (pos in idArrays.indices) {
                if (mUserMobile == idArrays[pos]) {
                    inWhiteUsers = true
                    break
                }
            }
        }

        // 黑名单
        var blackUsers = bean.black_users
        if (blackUsers != null && !StringUtils.instance.isEmpty(blackUsers)) {
            var idArrays = blackUsers.split(",")
            for (pos in idArrays.indices) {
                if (mUserMobile == idArrays[pos]) {
                    inBlackUsers = true
                    break
                }
            }
        }
        // 白名单 黑名单 判断
        // 如果白名单为空 直接判断黑名单
        // 如果用户在白名单，在判断在不在黑名单
        // 如果 白名单不为空 且 用户不在白名单 不给看
        if (hasWhiteUsers) {
            if (inWhiteUsers) {
                if (inBlackUsers) {
                    return true
                }
            } else {
                return true
            }
        } else {
            if (inBlackUsers) {
                return true
            }
        }
        return false
    }

}