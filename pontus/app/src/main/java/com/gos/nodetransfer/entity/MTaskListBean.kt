package com.gos.nodetransfer.entity

import com.dim.library.utils.GsonUtils


/**
 * @ClassName MTaskListBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/21 5:20 PM
 * @Version 1.0
 */
class MTaskListBean(
    var taskList: ArrayList<MTashListItemBean>

) {
    class MTashListItemBean(
        var taskName: String,
        var taskType: Int,
        var taskCode: String,
        var frequency: Int,
        var sort: Int,
        var awardId: Int,
        var rule: String,
        var awards: MTashListItemAwardsBean,
        var perGain: Int,
        var nowGain: Int

    ) {
        fun getMatchRules(): ArrayList<MTashListItemRuleBean> {
            return GsonUtils.listFromJson(rule, MTashListItemRuleBean::class.java) as ArrayList
        }

        fun isFinished(): Boolean {
            return nowGain == perGain
        }

        fun setFinishOnce() {
            nowGain += 1
        }
    }

    class MTashListItemAwardsBean(
        var awardName: String,
        var awardType: String,
        var genre: String,
        var amount: String
    )

    class MTashListItemRuleBean(
        var logic: String,
        var content: String,
        // 完成类型 done_time ： 倒计时
        var condition: String,
        var content_unit: String
    ) {
        fun getContentLong(): Long {
            return content.toLong() * 1000
        }

        fun isDoneTime():Boolean{
            return "done_time" == condition
        }
    }
}