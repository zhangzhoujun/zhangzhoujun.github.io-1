package com.qy.dodule_goods.ui

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.dim.library.utils.StringUtils
import com.dim.library.utils.ToastUtils
import com.qm.lib.entity.MGoodsBean
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.utils.TimeUtils
import com.qm.lib.widget.dim.AppImageView
import com.qm.lib.widget.dim.AppTextView
import com.qm.lib.widget.popupWindow.CommonPopupWindow
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qy.dodule_goods.BR
import com.qy.dodule_goods.R
import com.qy.dodule_goods.databinding.GoodsActMainBinding
import com.youth.banner.listener.OnPageChangeListener
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.text.DecimalFormat

/**
 * @ClassName GoodsViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/9 1:50 PM
 * @Version 1.0
 */
class GoodsViewModel(application: Application) : ToolbarViewModel(application) {
    var mGoodsBean: ObservableField<MGoodsBean.MJuggleGoodsItemBean> = ObservableField()
    lateinit var mBind: GoodsActMainBinding
    lateinit var mActivity: Activity

    override fun onCreate() {
        super.onCreate()
    }

    fun onBuyClick(view: View) {
        SLSLogUtils.instance.sendLogClick(
            "GoodsActivity",
            "",
            "BUY",
            extra = "platfrom = ${mGoodsBean.get()!!.platform} , goodsId = ${mGoodsBean.get()!!.goodsId} , GoodsName = ${mGoodsBean.get()!!.goodsName}"
        )
        showDialog()
        getWebView().loadUrl(mGoodsBean.get()!!.clickUrl)
    }

    private fun initView() {
        // 如果没有优惠卷
        if (mGoodsBean.get()!!.couponInfo == null || mGoodsBean.get()!!.couponInfo.size == 0 ||
            StringUtils.isEmpty(mGoodsBean.get()!!.couponInfo[0].discount) || StringUtils.isEmpty(
                mGoodsBean.get()!!.couponInfo[0].couponStartTime
            )
        ) {
            mBind.couponLayout.visibility = View.GONE
            mBind.bottomDes.visibility = View.GONE
        } else {
            var startTime =
                TimeUtils.instance.getDateForYMDPoint(mGoodsBean.get()!!.couponInfo[0].couponStartTime)
            var endTime =
                TimeUtils.instance.getDateForYMDPoint(mGoodsBean.get()!!.couponInfo[0].couponEndTime)
            mBind.couponTime.text = "使用时间：$startTime-$endTime"

            mBind.bottomDes.text = "省¥${mGoodsBean.get()!!.couponInfo[0].discount}"

            mBind.couponLayout.visibility = View.VISIBLE
            mBind.bottomDes.visibility = View.VISIBLE
        }

        if (mGoodsBean.get()!!.sellNum.length >= 5) {
            var sellLong = mGoodsBean.get()!!.sellNum.toDouble()
            mBind.sale.text = "月销量：${DecimalFormat("0.0").format(sellLong / 10000)}万"
        } else {
            mBind.sale.text = "月销量：${mGoodsBean.get()!!.sellNum}"
        }

        var text = "¥${mGoodsBean.get()!!.couponUsedPrice}"

        var count = text.length
        if (text.contains(".")) {
            var textNoPoint = text.split(".")[0]
            count = textNoPoint.length
        }

        val styledText = SpannableString(text)
        styledText.setSpan(
            TextAppearanceSpan(mActivity, R.style.goods_price_small),
            0,
            1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            TextAppearanceSpan(mActivity, R.style.goods_price_big),
            1,
            count,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            TextAppearanceSpan(mActivity, R.style.goods_price_small),
            count,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        mBind.price.setText(styledText, TextView.BufferType.SPANNABLE)
    }

    fun onViewInit(
        goodsBean: MGoodsBean.MJuggleGoodsItemBean,
        bind: GoodsActMainBinding,
        context: Activity
    ) {
        this.mGoodsBean.set(goodsBean)
        this.mBind = bind
        this.mActivity = context

        initTitle()

        initWebView()

        initView()

        observableList.clear()
        for (i in 0 until mGoodsBean!!.get()!!.imageInfo.size) {
            val itemViewModel =
                GoodsItemViewModel(this@GoodsViewModel, mGoodsBean!!.get()!!.imageInfo[i])
            observableList.add(itemViewModel)
        }

        mBind.banner
            .setAdapter(GoodsBannerItemAdapter(mActivity, this.mGoodsBean.get()!!.imageInfo))
            .start()

        mBind.point.text = "1/" + mGoodsBean.get()!!.imageInfo.size

        mBind.banner.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                mBind.point.text = "" + (position + 1) + "/" + mGoodsBean.get()!!.imageInfo.size
            }

        })
    }

    private fun initTitle() {
        val option = JYToolbarOptions()
        option.titleString = "商品详情页"
        option.isNeedNavigate = true
        option.backId = R.mipmap.lib_base_back
        setOptions(option)
    }

    var observableList: ObservableList<GoodsItemViewModel> =
        ObservableArrayList<GoodsItemViewModel>()

    var itemBinding: ItemBinding<GoodsItemViewModel> =
        ItemBinding.of<GoodsItemViewModel>(BR.viewModel, R.layout.goods_item)
    val adapter: BindingRecyclerViewAdapter<GoodsItemViewModel> =
        BindingRecyclerViewAdapter<GoodsItemViewModel>()

    private fun getWebView(): WebView {
        return mBind.webView
    }

    private fun initWebView() {
        val webSettings: WebSettings = getWebView().getSettings()
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.textZoom = 100
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.builtInZoomControls = true
        webSettings.setSupportZoom(true)
        // 触摸焦点起作用
        getWebView().requestFocus()
        getWebView().webViewClient = webViewClient
    }

    private val webViewClient: WebViewClient = object : WebViewClient() {
        // 替代方法为下面注释掉的方法
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("tbopen")
                || url.startsWith("tmall")
                || url.startsWith("weixin://")
                || url.startsWith("alipays://")
                || url.startsWith("alipay")
                || url.startsWith("mailto://")
                || url.startsWith("tel://")
                || url.contains("alipays://platformapi")
            ) {
                showGoodsJumpLayout(url)
            }
            dismissDialog()
            return true
        }
    }

    private var goodsPop: CommonPopupWindow? = null

    // 跳转的UI
    private fun showGoodsJumpLayout(url: String) {
        if (goodsPop != null && goodsPop!!.isShowing) {
            return
        }

        val view: View =
            LayoutInflater.from(mActivity).inflate(R.layout.goods_jump_pop, null)
        goodsPop = CommonPopupWindow.Builder(mActivity)
            .setView(view)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            .setAnimationStyle(R.style.AnimMiddle)
            .setOutsideTouchable(false)
            .setBackGroundLevel(0.4f)
            .create()
        var title = view.findViewById<AppTextView>(R.id.title)
        var toIcon = view.findViewById<AppImageView>(R.id.icon_right)
        var seekbar = view.findViewById<SeekBar>(R.id.seekbar)

        var bottomLayout = view.findViewById<RelativeLayout>(R.id.bottom_layout)
        var bottomCoupon = view.findViewById<AppTextView>(R.id.bottom_price)
        var bottomLeftCoupon = view.findViewById<AppTextView>(R.id.bottom_left_price)
        var bottomRightTime = view.findViewById<AppTextView>(R.id.bottom_right_time)

        if (mGoodsBean.get()!!.platform == "taobao") {
            title.text = "正在跳转淘宝"
            toIcon.setImageUrl(R.mipmap.goods_jump_tb)
        } else if (mGoodsBean.get()!!.platform == "tmall") {
            title.text = "正在跳转天猫"
            toIcon.setImageUrl(R.mipmap.goods_jump_tm)
        }
        seekbar.isClickable = false
        seekbar.isEnabled = false
        seekbar.isSelected = false
        seekbar.isFocusable = false

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress >= 90) {
                    try {
                        SLSLogUtils.instance.sendLogClick(
                            "GoodsActivity",
                            "",
                            "BUY_OPEN",
                            extra = "platfrom = ${mGoodsBean.get()!!.platform} , goodsId = ${mGoodsBean.get()!!.goodsId} , GoodsName = ${mGoodsBean.get()!!.goodsName}"
                        )

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        mActivity.startActivity(intent)
                    } catch (e: Exception) {
                        SLSLogUtils.instance.sendLogClick(
                            "GoodsActivity",
                            "",
                            "BUY_OPEN_FAILED",
                            extra = "platfrom = ${mGoodsBean.get()!!.platform} , goodsId = ${mGoodsBean.get()!!.goodsId} , GoodsName = ${mGoodsBean.get()!!.goodsName}"
                        )

                        ToastUtils.showShort("未安装当前APP，请先下载")
                    } finally {
                        goodsPop!!.dismiss()
                    }
                } else {
                    android.os.Handler().postDelayed({ seekBar.progress = progress + 1 }, 10)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

        })

        seekbar.progress = 1

        if (mGoodsBean.get()!!.couponInfo == null || mGoodsBean.get()!!.couponInfo.size == 0 ||
            StringUtils.isEmpty(mGoodsBean.get()!!.couponInfo[0].discount) || StringUtils.isEmpty(
                mGoodsBean.get()!!.couponInfo[0].couponStartTime
            )
        ) {
            bottomLayout.visibility = View.GONE
        } else {
            var startTime =
                TimeUtils.instance.getDateForMDPoint(mGoodsBean.get()!!.couponInfo[0].couponStartTime)
            var endTime =
                TimeUtils.instance.getDateForMDPoint(mGoodsBean.get()!!.couponInfo[0].couponEndTime)
            bottomRightTime.text = "$startTime-$endTime"

            bottomCoupon.text = "¥${mGoodsBean.get()!!.couponInfo[0].discount}"
            bottomLeftCoupon.text = "¥${mGoodsBean.get()!!.couponInfo[0].discount}"

            bottomLayout.visibility = View.VISIBLE
        }

        goodsPop!!.setOnDismissListener(PopupWindow.OnDismissListener {
            if (goodsPop != null) {
                goodsPop = null
            }
        })
        goodsPop!!.showAtLocation(mBind.root, Gravity.CENTER, 0, 0)
    }
}