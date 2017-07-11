package com.kalemao.library.custom.topbar

import com.kalemao.library.base.BasePresenter
import com.kalemao.library.base.BaseView

/**
 * Created by dim on 2017/6/6 16:18
 * 邮箱：271756926@qq.com
 */
interface KLMTopBarContract {

    interface IKLMTopBarView : BaseView{
        fun onLeftClick()
        fun onRightClick()
        fun onRightLeftClick()
    }

    interface IKLMTopBarPresent : BasePresenter{

    }

}