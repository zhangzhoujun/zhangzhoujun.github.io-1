package com.dim.library.binding.command

/**
 * @ClassName BindingConsumer
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/12 4:37 PM
 * @Version 1.0
 */
interface BindingConsumer<T> {
    fun call(t: T?)
}