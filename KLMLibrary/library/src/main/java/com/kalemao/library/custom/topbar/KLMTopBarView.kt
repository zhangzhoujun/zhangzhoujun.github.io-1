package com.kalemao.library.custom.topbar

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.kalemao.library.R
import com.kalemao.library.custom.KLMEduSohoIconTextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 通用的卡乐猫的头部导航栏，包括了标题，左边一个按钮，右边一个按钮，右边按钮的左边还有一个按钮；
 * XML中使用的时候，需要配置一些属性址，如：app:appType="1"，类型1表示卡乐猫国际版的，顶部背景默认为黑色，2为喵秘的；3为卡乐猫国内版本
 * 其他具体属性见attes-Topbar；
 * 如果需要监听点击事件，
 * Activity等调用的地方，需要实现一个KLMTopBarContract.IKLMTopBarView，为按钮的点击事件，
 * 需要传入一个KLMTopBarContract.IKLMTopBarView，方法：setKLMTopBarPresent（），来接受点击事件
 * 当调用View销毁的时候，调用KLMTopBarView的onViewDestory（）来销毁一些事件等，
 *
 * Created by dim on 2017/6/6 14:52
 * 邮箱：271756926@qq.com
 */
class KLMTopBarView : RelativeLayout {

    /**
     * ，类型1表示卡乐猫国际版的，顶部背景默认为黑色，2为喵秘的；3为卡乐猫国内版本
     */
    companion object {
        val TOP_BAR_TYPE_KLM = 1
        val TOP_BAR_TYPE_MM = 2
        val TOP_BAR_TYPE_KLM_CHINA = 3
    }
    private var defaultTextSize = 20F

    private var mContext: Context? = null
    private var mKLMTopBarPresent: KLMTopBarPresent? = null
    private var mKLMTopBarView: KLMTopBarContract.IKLMTopBarView? = null

    private var layout: RelativeLayout? = null
    private var leftView: KLMEduSohoIconTextView? = null
    private var rightView: KLMEduSohoIconTextView? = null
    private var titleView: KLMEduSohoIconTextView? = null
    private var rightLeftView: KLMEduSohoIconTextView? = null
    private var topBarBag: RelativeLayout? = null

    private var titleViewStr: String? = null
    private var titleViewSize: Float = 0F
    private var titleViewColor: Int = 0
    private var iconViewSize: Float = 0F
    private var iconViewColor: Int = 0
    private var topBarColor: Int = 0

    private var leftViewStr: String? = null
    private var rightViewStr: String? = null
    private var rightLeftViewStr: String? = null

    private var leftViewSize: Float = 0F
    private var rightViewSize: Float = 0F
    private var rightLeftViewSize: Float = 0F

    private var leftViewColor: Int = 0
    private var rightViewColor: Int = 0
    private var rightLeftViewColor: Int = 0

    private var appType: Int = 1

    constructor(context: Context) : super(context){
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getConfig(context, attrs)
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getConfig(context, attrs)
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        getConfig(context, attrs)
        initView(context)
    }

    fun setKLMTopBarPresent(topBarView: KLMTopBarContract.IKLMTopBarView?) {
        mKLMTopBarView = topBarView
        mKLMTopBarPresent = KLMTopBarPresent(mKLMTopBarView)
    }

    fun onViewDestory() {
        mKLMTopBarView = null
        mKLMTopBarPresent!!.unSubscribe()
        mKLMTopBarPresent = null
    }

    /**
     * 从xml中获取配置信息
     */
    private fun getConfig(context: Context, attrs: AttributeSet) {
        // TypedArray是一个数组容器用于存放属性值
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar)

        val count = ta.indexCount
        for (i in 0..count - 1) {
            val attr = ta.getIndex(i)
            when (attr) {
            // app类型
                R.styleable.Topbar_appType -> appType = ta.getInt(R.styleable.Topbar_appType, 1)
            // 标题内容
                R.styleable.Topbar_titleText -> titleViewStr = ta.getString(R.styleable.Topbar_titleText)
            // 右边图标内容
                R.styleable.Topbar_rightText -> rightViewStr = ta.getString(R.styleable.Topbar_rightText)
            // 右边左边图标内容
                R.styleable.Topbar_rightLeftText -> rightLeftViewStr = ta.getString(R.styleable.Topbar_rightLeftText)
            // 左边图标内容
                R.styleable.Topbar_leftText -> leftViewStr = ta.getString(R.styleable.Topbar_leftText)
            // 标题颜色
                R.styleable.Topbar_titleColor ->
                    // 默认颜色设置为白色
                    titleViewColor = ta.getColor(attr, Color.WHITE)
            // 标题大小
                R.styleable.Topbar_titleSize ->
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    titleViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 36f, resources.displayMetrics).toInt()).toFloat()
            // 整个的背景色
                R.styleable.Topbar_barBg ->
                    // 默认颜色设置为白色
                    topBarColor = ta.getColor(attr, Color.WHITE)
            // icon的大小
                R.styleable.Topbar_iconSize ->
                    iconViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics).toInt()).toFloat()
            // icon的颜色
                R.styleable.Topbar_iconColor ->
                    // 默认颜色设置为白色
                    iconViewColor = ta.getColor(attr, Color.WHITE)
            // 左边icon颜色
                R.styleable.Topbar_leftColor ->
                    // 默认颜色设置为白色
                    leftViewColor = ta.getColor(attr, Color.WHITE)
            // 左边icon大小
                R.styleable.Topbar_leftSize ->
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    leftViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 36f, resources.displayMetrics).toInt()).toFloat()
            // 右边icon颜色
                R.styleable.Topbar_rightColor ->
                    // 默认颜色设置为白色
                    rightViewColor = ta.getColor(attr, Color.WHITE)
            // 右边icon大小
                R.styleable.Topbar_rightSize ->
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    rightViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 36f, resources.displayMetrics).toInt()).toFloat()
            // 右边左边icon颜色
                R.styleable.Topbar_rightLeftColor ->
                    // 默认颜色设置为白色
                    rightLeftViewColor = ta.getColor(attr, Color.WHITE)
            // 右边左边icon大小
                R.styleable.Topbar_rightLeftSize ->
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    rightLeftViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 36f, resources.displayMetrics).toInt()).toFloat()
            }
        }
        // 用完务必回收容器
        ta.recycle()
    }

    private fun initView(context: Context) {
        this.mContext = context
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_custom_klm_head, this, true)
        leftView = layout.findViewById(R.id.klm_topbar_left) as KLMEduSohoIconTextView
        titleView = layout.findViewById(R.id.klm_topbar_title) as KLMEduSohoIconTextView
        rightView = layout.findViewById(R.id.klm_topbar_right) as KLMEduSohoIconTextView
        rightLeftView = layout.findViewById(R.id.klm_topbar_right_left) as KLMEduSohoIconTextView
        topBarBag = layout.findViewById(R.id.klm_topbar_bg) as RelativeLayout
        // 左边的按钮
        if (TextUtils.isEmpty(leftViewStr)) {
            leftView!!.visibility = View.GONE
        } else {
            leftView!!.setText(leftViewStr)
            leftView!!.visibility = View.VISIBLE
        }
        leftView!!.setOnClickListener { mKLMTopBarPresent?.doLeftClick() }
        // 右边的按钮
        if (TextUtils.isEmpty(rightViewStr)) {
            rightView!!.visibility = View.GONE
        } else {
            rightView!!.setText(rightViewStr)
            rightView!!.visibility = View.VISIBLE
        }
        rightView!!.setOnClickListener { mKLMTopBarPresent?.doRightClick() }
        // 右边的左边的按钮
        if (TextUtils.isEmpty(rightLeftViewStr)) {
            rightLeftView!!.visibility = View.GONE
        } else {
            rightLeftView!!.setText(rightLeftViewStr)
            rightLeftView!!.visibility = View.VISIBLE
        }
        rightLeftView!!.setOnClickListener { mKLMTopBarPresent?.doRightLeftClick() }
        showTypeForColor()
    }

    /**
     * 设置标题内容, 如果不需要设置大小，大小传null
     */
    fun setTopBarTitle(title: String, size: Float?) {
        if (size != null) {
            titleViewSize = size
        }
        setTopBarTitle(title)
    }

    /**
     * 设置标题内容
     */
    fun setTopBarTitle(title: String) {
        doAsync {
            uiThread {
                if (!TextUtils.isEmpty(title)) {
                    titleViewStr = title
                    if (titleView != null) {
                        titleView!!.text = titleViewStr
                        titleView!!.visibility = View.VISIBLE
                        if (titleViewSize > 0)
                            titleView!!.textSize = titleViewSize
                    }
                }
            }
        }
    }

    /**
     * 设置左边按钮内容, 如果不需要设置大小，大小传null
     */
    fun setTopBarLeft(title: String, size: Float?) {
        if (size != null) {
            leftViewSize = size
        }
        setTopBarLeft(title)
    }

    /**
     * 设置左边按钮内容
     */
    fun setTopBarLeft(title: String) {
        doAsync {
            uiThread {
                if (!TextUtils.isEmpty(title)) {
                    leftViewStr = title
                    if (leftView != null) {
                        leftView!!.text = leftViewStr
                        leftView!!.visibility = View.VISIBLE
                        if (leftViewSize > 0)
                            leftView!!.textSize = leftViewSize
                    }
                }
            }
        }
    }

    fun setTopBarLeftVisiable(vis: Boolean) {
        doAsync {
            uiThread {
                if (vis) {
                    leftView!!.visibility = View.VISIBLE
                } else {
                    leftView!!.visibility = View.INVISIBLE
                }
            }
        }
    }

    /**
     * 设置右边按钮内容, 如果不需要设置大小，大小传null
     */
    fun setTopBarRight(title: String, size: Float?) {
        if (size != null) {
            rightViewSize = size
        }
        setTopBarRight(title)
    }

    fun setTopBarRight(title: String, size: Float?, color: Int) {
        if (size != null) {
            rightViewSize = size
        }
        if (color > 0) {
            rightViewColor = color
        }
        setTopBarRight(title)
    }

    fun setTopBarRightVisiable(vis: Boolean) {
        doAsync {
            uiThread {
                if (vis) {
                    rightView!!.visibility = View.VISIBLE
                } else {
                    rightView!!.visibility = View.INVISIBLE
                }
            }
        }
    }

    /**
     * 设置右边按钮内容
     */
    fun setTopBarRight(title: String) {
        doAsync {
            uiThread {
                if (!TextUtils.isEmpty(title)) {
                    rightViewStr = title
                    if (rightView != null) {
                        rightView!!.text = rightViewStr
                        rightView!!.visibility = View.VISIBLE
                        if (rightViewSize > 0)
                            rightView!!.textSize = rightViewSize
                    }
                }
            }
        }
    }

    /**
     * 设置右边左边按钮内容, 如果不需要设置大小，大小传null
     */
    fun setTopBarRightLeft(title: String, size: Float?) {
        if (size != null) {
            rightLeftViewSize = size
        }
        setTopBarRightLeft(title)
    }

    fun setTopBarRightLeft(title: String, size: Float?, color: Int) {
        if (size != null) {
            rightLeftViewSize = size
        }
        if (color > 0) {
            rightLeftViewColor = color
        }
        setTopBarRightLeft(title)
    }

    /**
     * 设置右边左边按钮内容
     */
    fun setTopBarRightLeft(title: String) {
        doAsync {
            uiThread {
                if (!TextUtils.isEmpty(title)) {
                    rightLeftViewStr = title
                    if (rightLeftView != null) {
                        rightLeftView!!.text = rightLeftViewStr
                        rightLeftView!!.visibility = View.VISIBLE
                        if (rightLeftViewSize > 0)
                            rightLeftView!!.textSize = rightLeftViewSize
                    }
                }
            }
        }
    }

    /**
     * 设置右边图标的小红点显示
     */
    fun setTopBarRightPoint() {
        rightView!!.isTipOn = true
    }

    /**
     * 设置右边图标的小红隐藏
     */
    fun setTopBarRightPointDismiss() {
        rightView!!.isTipOn = false
    }

    /**
     * 设置右边左边图标的小红点显示
     */
    fun setTopBarRightLeftPoint() {
        rightLeftView!!.isTipOn = true
    }

    /**
     * 设置右边左边图标的小红点隐藏
     */
    fun setTopBarRightLeftPointDismiss() {
        rightLeftView!!.isTipOn = false
    }

    fun setTopBarType(type: Int) {
        this@KLMTopBarView.appType = type
        showTypeForColor()
    }

    fun showTypeForColor() {
        // 如果是卡乐猫的
        if (appType == 1) {
            topBarBag!!.background = resources.getDrawable(R.color.com_bg_klm)
            titleView!!.setTextColor(resources.getColor(R.color.white))
            leftView!!.setTextColor(resources.getColor(R.color.white))
            rightView!!.setTextColor(resources.getColor(R.color.white))
            rightLeftView!!.setTextColor(resources.getColor(R.color.white))
            leftView!!.textSize = defaultTextSize
            rightView!!.textSize = defaultTextSize
            rightLeftView!!.textSize = defaultTextSize
            titleView!!.textSize = 18F
        } else if (appType == 2) {
            topBarBag!!.background = resources.getDrawable(R.color.com_bg_mm)
            titleView!!.setTextColor(resources.getColor(R.color.white))
            leftView!!.setTextColor(resources.getColor(R.color.white))
            rightView!!.setTextColor(resources.getColor(R.color.white))
            rightLeftView!!.setTextColor(resources.getColor(R.color.white))
            leftView!!.textSize = defaultTextSize
            rightView!!.textSize = defaultTextSize
            rightLeftView!!.textSize = defaultTextSize
            titleView!!.textSize = 18F
        } else if (appType == 3) {
            topBarBag!!.background = resources.getDrawable(R.color.klm_F5)
            titleView!!.setTextColor(resources.getColor(R.color.klm_333))
            leftView!!.setTextColor(resources.getColor(R.color.klm_333))
            rightView!!.setTextColor(resources.getColor(R.color.klm_333))
            rightLeftView!!.setTextColor(resources.getColor(R.color.klm_333))
            leftView!!.textSize = defaultTextSize
            rightView!!.textSize = defaultTextSize
            rightLeftView!!.textSize = defaultTextSize
            titleView!!.textSize = 18F
        }
        // 标题颜色
        if (titleViewColor > 0) {
            titleView!!.setTextColor(resources.getColor(titleViewColor))
        }
        // 标题大小
        if (titleViewSize > 0) {
            titleView!!.textSize = titleViewSize
        }
        // 整个的背景色
        if (topBarColor > 0) {
            layout!!.background = resources.getDrawable(topBarColor)
        }
        // icon的大小
        if (iconViewSize > 0) {
            leftView!!.textSize = iconViewSize
            rightView!!.textSize = iconViewSize
            rightLeftView!!.textSize = iconViewSize
        }
        // icon的颜色
        if (iconViewColor > 0) {
            leftView!!.setTextColor(iconViewColor)
            rightView!!.setTextColor(iconViewColor)
            rightLeftView!!.setTextColor(iconViewColor)
        }
        // title的
        if (!TextUtils.isEmpty(titleViewStr)) {
            titleView!!.text = titleViewStr
        }
        if (titleViewSize > 0) {
            titleView!!.textSize = titleViewSize
        }
        if (leftViewColor > 0) {
            leftView!!.setTextColor(leftViewColor)
        }
        if (rightViewColor > 0) {
            rightView!!.setTextColor(rightViewColor)
        }
        if (rightLeftViewColor > 0) {
            rightLeftView!!.setTextColor(rightLeftViewColor)
        }
        if (leftViewSize > 0) {
            leftView!!.textSize = leftViewSize
        }
        if (rightViewSize > 0) {
            rightView!!.textSize = rightViewSize
        }
        if (rightLeftViewSize > 0) {
            rightLeftView!!.textSize = rightLeftViewSize
        }
    }
}