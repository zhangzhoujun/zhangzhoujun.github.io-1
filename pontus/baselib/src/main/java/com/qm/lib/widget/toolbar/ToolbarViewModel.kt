package com.qm.lib.widget.toolbar

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.dim.library.base.AppManager
import com.dim.library.binding.command.BindingAction
import com.dim.library.binding.command.BindingCommand
import com.dim.library.utils.DLog
import com.dim.library.utils.RxUtils
import com.qm.lib.base.BaseAppViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * @ClassName ToolbarViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 1/7/21 5:43 PM
 * @Version 1.0
 */
open class ToolbarViewModel(application: Application) : BaseAppViewModel(application) {

    /**
     * JYToolbar的配置
     */
    var toolbarOptions = ObservableField<JYToolbarOptions>()

    /**
     * 设置JYToolbar的JYToolbarOptions
     * @param options
     */
    open fun setOptions(options: JYToolbarOptions) {
        toolbarOptions.set(options)
    }

    /**
     * 设置JYToolbar的返回点击
     * @param options
     */
    var onBackClick: BindingCommand<*> = BindingCommand<Any?>(object : BindingAction {
        override fun call() {
            setOnBackClick()
        }
    })

    /**
     * 设置JYToolbar的右边按钮的点击
     * @param options
     */
    var onRightClick: BindingCommand<*> = BindingCommand<Any?>(object : BindingAction {
        override fun call() {
            onRightClick()
        }
    })

    /**
     * 设置JYToolbar的右边第二个按钮的点击
     * @param options
     */
    var onRightLeftClick: BindingCommand<*> = BindingCommand<Any?>(object : BindingAction {
        override fun call() {
            onRightLeftClick()
        }
    })

    /**
     * 设置JYToolbar的关闭按钮的点击
     */
    var onCloseClick: BindingCommand<*> = BindingCommand<Any?>(object : BindingAction {
        override fun call() {
            onViewCloseClick()
        }
    })

    /**
     * 子类重写，右边按钮的点击事件
     */
    protected open fun setOnBackClick() {
        DLog.d("JYToolbar --> onBackClick")
        onBackPressed()
    }

    /**
     * 子类重写，右边按钮的点击事件
     */
    protected open fun onRightClick() {
        DLog.d("JYToolbar --> onRightClick")
    }

    /**
     * 子类重写，右边第二个按钮的点击事件
     */
    protected open fun onRightLeftClick() {
        DLog.d("JYToolbar --> onRightLeftClick")
    }

    /**
     * 子类重写，关闭按钮的点击事件
     */
    protected open fun onViewCloseClick() {
        DLog.d("JYToolbar --> onViewCloseClick")
    }

    fun hasLocationPermission(activity: FragmentActivity): Boolean {
        if (lacksPermissions(getApplication(), permissions)) {
            initPermissions(activity)
            return false
        }
        return true
    }

    /**
     * 读写权限  自己可以添加需要判断的权限
     */
    private var permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    private fun lacksPermissions(
        mContexts: Context,
        permissions: Array<String>
    ): Boolean {
        for (permission in permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否缺少权限
     */
    private fun lacksPermission(
        mContexts: Context,
        permission: String
    ): Boolean {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED
    }

    private fun initPermissions(activity: FragmentActivity) {
        // RxPermissions 0.10.2 有个fragment的问题 会导致crash，先 延迟 + 捕获处理下看看情况
        try {
            val rxPermissions =
                RxPermissions(activity)
            Observable.just("")
                .delay(200, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(lifecycleProvider))
                .compose(RxUtils.schedulersTransformer())
                .subscribe {
                    rxPermissions.request(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                        .debounce(1, TimeUnit.SECONDS)
                        .subscribe { aBoolean: Boolean ->
                            onLocationBack(aBoolean)
                        }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun onLocationBack(aBoolean: Boolean) {

    }
}