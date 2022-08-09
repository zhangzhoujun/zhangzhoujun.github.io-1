package com.qm.lib.utils

import android.app.Activity
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

/**
 * @ClassName JYDialogUtils
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/22 2:18 PM
 * @Version 1.0
 */
class JYDialogUtils {

    fun showCommonDialog(
        activity: Activity,
        title: String,
        content: String,
        cancel: String = "取消",
        sure: String = "确定",
        cancelListener: JYDialogCancelListener? = null,
        sureListener: JYDialogSureListener? = null
    ) {
        QMUIDialog.MessageDialogBuilder(activity)
            .setTitle(title)
            .setMessage(content)
            .setSkinManager(QMUISkinManager.defaultInstance(activity))
            .addAction(cancel) { dialog, index ->
                run {
                    cancelListener?.onCancelClick()
                    dialog.dismiss()
                }
            }
            .addAction(0, sure, QMUIDialogAction.ACTION_PROP_POSITIVE) { dialog, index ->
                run {
                    sureListener?.onSureClick()
                    dialog.dismiss()
                }
            }.show()
    }

    interface JYDialogCancelListener {
        fun onCancelClick()
    }

    interface JYDialogSureListener {
        fun onSureClick()
    }
}