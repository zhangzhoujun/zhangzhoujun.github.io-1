package com.qm.lib.base;

/**
 * @ClassName BaseBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/31 11:40 AM
 * @Version 1.0
 */
public class BaseBean<T> {

    public BaseBean(T data) {
        this.data = data;
    }

    public BaseBean() {
        // 服务端这个参数必须传，迁就老司机，给个空对象
        this.data = (T) new CBaseEmptyBean();
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
