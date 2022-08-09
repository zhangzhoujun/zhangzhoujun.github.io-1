package com.dim.library.base

/**
 * @ClassName IBaseActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/12 7:31 PM
 * @Version 1.0
 */
open interface IBaseActivity {
    /**
     * 初始化界面传递参数
     */
    fun initParam()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 初始化界面观察者的监听
     */
    fun initViewObservable()
}
