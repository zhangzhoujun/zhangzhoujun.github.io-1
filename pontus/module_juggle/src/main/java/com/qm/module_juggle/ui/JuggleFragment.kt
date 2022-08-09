package com.qm.module_juggle.ui

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.Observable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.DLog
import com.dim.library.utils.ToastUtils
import com.qm.lib.base.JYListFragment
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.utils.SLSLogUtils.Companion.instance
import com.qm.lib.utils.ScreenUtils
import com.qm.lib.widget.refresh.JYSwipeRefreshLayout
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeFraMainBinding
import com.qm.module_juggle.entity.MHomeDataBean


/**
 * @ClassName HomeFragment
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 7:29 PM
 * @Version 1.0
 */
@Route(path = RouterFragmentPath.JuggleFra.JUGGLE_FRA_INDEX)
class JuggleFragment : JYListFragment<HomeFraMainBinding, JuggleFraViewModel>() {

    private val TAG = "JuggleFragment"

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE)
    var page_key: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_TAG)
    var page_tag: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_DATA)
    var page_data: MHomeDataBean? = null

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_NOBACK)
    var page_no_back: Boolean = false

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_DIALOG)
    var page_dialog: Boolean = false

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.home_fra_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)
        if (page_key == null) {
            page_key = ""
        }
        if (page_tag == null) {
            page_tag = ""
        }
        if (TextUtils.isEmpty(page_key) && page_data == null) {
            ToastUtils.showShort("数据错误，请重试")
            return
        }
//        if (initSensor()) {
//            startStepCounter()
//        }
        if (page_data != null) {
            getBind().refreshLayout.visibility = View.GONE
            getBind().homeRecyclerviewDialog.visibility = View.VISIBLE
        } else {
            getBind().refreshLayout.visibility = View.VISIBLE
            getBind().homeRecyclerviewDialog.visibility = View.GONE
        }
        getVm().initHome(
            getRecyclerView(),
            this,
            activity!!,
            page_key,
            page_no_back,
            page_data,
            page_tag
        )
    }

    override fun initViewObservable() {
        super.initViewObservable()

        getVm().mTitleInfo.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                getVm().initTitle()
                if (getVm().mTitleInfo != null) {
                    if (getVm().mTitleInfo.get()!!.has_head) {
                        if (page_no_back) {
                            getBind().topView.visibility = View.VISIBLE
                        } else {
                            getBind().topView.visibility = View.GONE
                        }
                        getBind().toolbar.visibility = View.VISIBLE
                    } else {
                        getBind().toolbar.visibility = View.GONE
                        getBind().topView.visibility = View.GONE
                    }
                    getRefreshLayout().setRefreshEnable(getVm().mTitleInfo.get()!!.is_pull_refresh)

                    if (getVm().mTitleInfo.get()!!.is_show_to_top) {
                        DLog.d(TAG, "需要显示回到顶部的按钮")
                        setRefreshListener()
                    }
                }
            }

        })
    }

    private fun setRefreshListener() {
        getRecyclerView().addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var mmRvScrollY = 0 // 列表滑动距离
            override fun onScrollStateChanged(
                @NonNull recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                mmRvScrollY += dy
                // DLog.d("SSSSSSSS", "onScrolled: mmRvScrollY: $mmRvScrollY, dy: $dy")
                if (getVm().mTitleInfo.get()!!.is_show_to_top) {
                    if (mmRvScrollY > ScreenUtils.getScreenHeight(context)) {
                        getBind().goTop.visibility = View.VISIBLE
                    } else {
                        getBind().goTop.visibility = View.GONE
                    }
                }

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                //得到当前显示的最后一个item的view
                val lastChildView = layoutManager.getChildAt(layoutManager.childCount - 1)
                lastChildView?.let {
                    //得到lastChildView的bottom坐标值
                    val lastChildBottom = lastChildView.bottom
                    //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                    val recyclerBottom = recyclerView.bottom - recyclerView.paddingBottom
                    //判断lastChildView的bottom值跟recyclerBottom
                    //如果满足则说明是真正的滑动到了底部
                    if (lastChildBottom <= recyclerBottom) {
                        DLog.d("SSSSSSSS", "parent 滑动到底部")
                        getVm().setGroupScrollToBottom(true)
                        if (getVm().isHasLDV()) {
                            getRefreshLayout().setRefreshEnable(false)
                        }
                    } else {
                        getVm().setGroupScrollToBottom(false)
                    }
                }

                val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                DLog.d("OKOKOKOK","lastCompletelyVisibleItemPosition = $lastCompletelyVisibleItemPosition")
                DLog.d("OKOKOKOK","layoutManager.itemCount = ${layoutManager.itemCount}")
                if (lastCompletelyVisibleItemPosition == layoutManager.itemCount - 4) {
                    DLog.d("OKOKOKOK","scrollToBottom")
                    getVm().scrollToBottom()
                }
            }
        })
        getBind().goTop.setOnClickListener {
            getRecyclerView().smoothScrollToPosition(0)
            getBind().goTop.visibility = View.GONE
        }
    }

    private fun getBind(): HomeFraMainBinding {
        return binding as HomeFraMainBinding
    }

    private fun getVm(): JuggleFraViewModel {
        return viewModel as JuggleFraViewModel
    }

    override fun getRefreshLayout(): JYSwipeRefreshLayout {
        return getBind().refreshLayout
    }

    override fun getRecyclerView(): RecyclerView {
        if (page_data != null) {
            return getBind().homeRecyclerviewDialog
        }
        return getBind().homeRecyclerview
    }

    override fun getEmptyContent(): String {
        return "暂无数据"
    }

    // 记步相关 START

    /**
     * 是否含有记步模块
     */
    private fun initSensor(): Boolean {
        var stepDetector = false
        var stepCounter = false
        var manager =
            activity!!.getSystemService(SENSOR_SERVICE) as SensorManager
        val list: List<Sensor> = manager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in list) {
            DLog.e(TAG, "initData: " + sensor.name)
            if (sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                stepDetector = true
            }
            if (sensor.type == Sensor.TYPE_STEP_COUNTER) {
                stepCounter = true
            }
            if (stepDetector && stepCounter) {
                return true
            }
        }
        return stepDetector && stepCounter
    }

    private lateinit var mSensorManager: SensorManager
    private lateinit var mStepSensor: Sensor

    private fun startStepCounter() {
        DLog.d(TAG, "startStepCounter")
        mSensorManager = activity!!.getSystemService(SENSOR_SERVICE) as SensorManager
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        mSensorManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                DLog.d(TAG, "onAccuracyChanged")
            }

            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    DLog.d(TAG, "onSensorChanged 当前步数：${event.values[0]}")
                }
            }

        }, mStepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    // 记步相关 END

    override fun onPauseLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONPAUSE"
        )
    }

    override fun onCreateLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONCREATE"
        )
    }

    override fun onResumeLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONRESUME"
        )
    }

    override fun onDestoryLog() {
        instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONDESTORY"
        )
    }
}