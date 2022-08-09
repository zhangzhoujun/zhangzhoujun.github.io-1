package com.dim.library.binding.command

/**
 * @ClassName BindingFunction
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/12 4:38 PM
 * @Version 1.0
 */
interface BindingFunction<T> {
    fun call(): T
}