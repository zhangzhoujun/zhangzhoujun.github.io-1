package com.dim.library.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @ClassName IBaseViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/12 4:12 PM
 * @Version 1.0
 */
interface IBaseViewModel : LifecycleObserver{

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(
        owner: LifecycleOwner?,
        event: Lifecycle.Event?
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()

    /**
     * 注册RxBus
     */
    fun registerRxBus()

    /**
     * 移除RxBus
     */
    fun removeRxBus()

}