package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.qm.lib.utils.ColorUtils
import com.qm.lib.widget.dim.AppTextView
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainLiandongBinding
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.ui.liandong.HomeLDFragment
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle


/**
 * @ClassName HomeHotAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/6/20 11:31 AM
 * @Version 1.0
 */
class HomeLDAdapter(
    var context: Context,
    var helper: LayoutHelper,
    var mFragment: Fragment,
    var item: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_liandong, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var homeHotBind: HomeMainLiandongBinding = DataBindingUtil.bind(holder.itemView)!!
        var fragments: ArrayList<Fragment> = ArrayList()

        homeHotBind.retouchBg.setImageUrl(item.retouch)

        // 背景色
        var color = ColorUtils.instance.getColorForString(item.background_color)
        homeHotBind.rootBg.setBackgroundColor(color)
        // tab文字颜色
        homeHotBind.hotTitleLayout.setTabTextColors(
            mFragment.context!!.resources.getColor(R.color.c_666666),
            ColorUtils.instance.getColorForString(item.curr_font_color)
        )
        // tab背景色
        val normal = ContextCompat.getDrawable(context, R.drawable.home_title_tab_press)
        val normalGroup = normal as GradientDrawable
        normalGroup.setColor(ColorUtils.instance.getColorForString(item.curr_background_color))

        homeHotBind.hotTitleVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        for (i in 0 until item.data.size) {
            var fragment = HomeLDFragment()
            val bundle = Bundle()
            bundle.putSerializable("lsit", item.data[i].data)
            bundle.putInt("pos", i)
            bundle.putString("pageKey", item.data[i].pageKey)
            fragment.arguments = bundle
            fragments.add(fragment)
        }

        homeHotBind.hotTitleVp.adapter = object : FragmentStateAdapter(mFragment) {
            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

            override fun getItemCount(): Int {
                return fragments.size
            }
        }

        homeHotBind.hotTitleVp.offscreenPageLimit = 3

        homeHotBind.hotTitleLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until homeHotBind.hotTitleLayout.tabCount) {
                    val view: View = homeHotBind.hotTitleLayout.getTabAt(i)!!.customView ?: return
                    val name =
                        view.findViewById<View>(R.id.name) as AppTextView
                    if (i == tab.position) { // 选中状态
                        name.background =
                            context.resources.getDrawable(R.drawable.home_title_tab_press)
                        name.setTextColor(context.resources.getColor(R.color.white))
                    } else { // 未选中状态
                        name.background =
                            context.resources.getDrawable(R.drawable.home_title_tab_normal)
                        name.setTextColor(context.resources.getColor(R.color.c_666666))
                    }
                }
            }
        })

        TabLayoutMediator(homeHotBind.hotTitleLayout, homeHotBind.hotTitleVp, true,
            TabLayoutMediator.TabConfigurationStrategy { tab, position -> //这里需要根据position修改tab的样式和文字等
                val view: View =
                    LayoutInflater.from(mFragment.context!!)
                        .inflate(R.layout.home_ld_tab_item, null)
                val tv = view.findViewById<AppTextView>(R.id.name)

                var content = SpannableString(item.data[position].title)
                var styleSpan = StyleSpan(Typeface.NORMAL)
                if (tab.isSelected) {
                    styleSpan = StyleSpan(Typeface.BOLD)
                }
                content.setSpan(styleSpan, 0, content.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                tv.text = content
                tab.customView = view
            }).attach()

        homeHotBind.indicatorView
            .setSliderColor(
                mFragment.context!!.resources.getColor(R.color.c_F3F3F3),
                mFragment.context!!.resources.getColor(R.color.color_dark)
            )
            .setSliderWidth(mFragment.context!!.resources.getDimension(R.dimen.dp_10))
            .setSliderHeight(mFragment.context!!.resources.getDimension(R.dimen.dp_3))
            .setSlideMode(IndicatorSlideMode.WORM)
            .setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            .setSliderGap(0F)
            .setupWithViewPager(homeHotBind.hotTitleVp)
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}