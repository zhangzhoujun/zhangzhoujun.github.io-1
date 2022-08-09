package com.qm.module_school.ui.index

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.qm.lib.entity.MAppConfigBean
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.JYComConst
import com.qm.lib.widget.dim.AppTextView
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qm.module_school.R


/**
 * @ClassName SchoolIndexViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/3 3:57 PM
 * @Version 1.0
 */
class SchoolIndexViewModel(application: Application) : ToolbarViewModel(application) {

    var fragments: ArrayList<Fragment> = ArrayList()
    var mPageItem: MAppConfigBean.MAppConfigitem? = null
    var tabTextAlign = "left"

    var title: ObservableField<String> = ObservableField("商学院")

    override fun onCreate() {
        super.onCreate()
    }

    fun initView(
        mFragment: Fragment,
        vp: ViewPager2,
        tabLayout: TabLayout,
        pageItem: MAppConfigBean.MAppConfigitem?,
        tabTextAlign: String
    ) {
        this.tabTextAlign = tabTextAlign
        fragments.clear()
        vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vp.isUserInputEnabled = false

        if (pageItem?.sub == null || pageItem.sub.size != 2) {
            title.set("商学院")
            val fragment =
                ARouter.getInstance().build(RouterFragmentPath.JuggleFra.JUGGLE_FRA_INDEX)
                    .withString(JYComConst.OPEN_CUSTOM_PAGE, "1606372046361_-641012243")
                    .withBoolean(JYComConst.OPEN_CUSTOM_NOBACK, true)
                    .navigation() as Fragment
            fragments.add(fragment)

            val fragment2 =
                ARouter.getInstance().build(RouterFragmentPath.JuggleFra.JUGGLE_FRA_INDEX)
                    .withString(JYComConst.OPEN_CUSTOM_PAGE, "1606372182892_-935445138")
                    .withBoolean(JYComConst.OPEN_CUSTOM_NOBACK, true)
                    .navigation() as Fragment
            fragments.add(fragment2)
        } else {
            // 9832ccfc453fe3145c2def74c9d5d1ad
            // 1606041154752_79805604
            this.mPageItem = pageItem
            title.set(mPageItem!!.label)
            for (index in 0 until 2) {
                val fragment =
                    RouterManager.instance.getNativeFragment(mPageItem!!.sub[index].page_path)
                if (fragment != null) {
                    fragments!!.add(fragment as Fragment)
                } else {
                    val fragment =
                        ARouter.getInstance()
                            .build(RouterFragmentPath.JuggleFra.JUGGLE_FRA_INDEX)
                            .withString(
                                JYComConst.OPEN_CUSTOM_PAGE,
                                mPageItem!!.sub[index].page_path
                            )
                            .withBoolean(JYComConst.OPEN_CUSTOM_NOBACK, true)
                            .navigation()
                    fragments!!.add(fragment as Fragment)
                }
            }
        }

        initTab(mFragment, vp, tabLayout)
    }

    private fun initTab(mFragment: Fragment, vp: ViewPager2, tabLayout: TabLayout) {
        vp.adapter = object : FragmentStateAdapter(mFragment) {
            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

            override fun getItemCount(): Int {
                return fragments.size
            }
        }

        vp.offscreenPageLimit = 3

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until tabLayout.tabCount) {
                    val view: View = tabLayout.getTabAt(i)!!.customView ?: return
                    val text =
                        view.findViewById<View>(R.id.name) as TextView
                    val textSmall =
                        view.findViewById<View>(R.id.name_small) as TextView
                    val indicator =
                        view.findViewById<View>(R.id.tab_item_indicator)
                    if (tabTextAlign == "left") {
                        text.setTextColor(mFragment.context!!.resources.getColor(R.color.c_333333))
                        textSmall.setTextColor(mFragment.context!!.resources.getColor(R.color.c_333333))
                    } else {
                        text.setTextColor(mFragment.context!!.resources.getColor(R.color.white))
                        textSmall.setTextColor(mFragment.context!!.resources.getColor(R.color.white))
                    }

                    if (i == tab.position) { // 选中状态
                        text.visibility = View.VISIBLE
                        textSmall.visibility = View.INVISIBLE
                        indicator.visibility = View.VISIBLE
                    } else { // 未选中状态
                        text.visibility = View.INVISIBLE
                        textSmall.visibility = View.VISIBLE
                        indicator.visibility = View.INVISIBLE
                    }
                }
            }
        })

        TabLayoutMediator(tabLayout, vp, true,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                val view: View =
                    LayoutInflater.from(mFragment.context!!).inflate(
                        if (tabTextAlign == "left") R.layout.school_tab_item_left else R.layout.school_tab_item,
                        null
                    )
                val tv = view.findViewById<AppTextView>(R.id.name)
                val tvSmall = view.findViewById<AppTextView>(R.id.name_small)
                if (mPageItem?.sub == null || mPageItem!!.sub.size != 2) {
                    tv.text = if (position == 0) "平台动态" else "分享赚钱"
                    tvSmall.text = if (position == 0) "平台动态" else "分享赚钱"
                } else {
                    tv.text =
                        if (position == 0) mPageItem!!.sub[0].label else mPageItem!!.sub[1].label
                    tvSmall.text =
                        if (position == 0) mPageItem!!.sub[0].label else mPageItem!!.sub[1].label
                }
                tab.customView = view

            }).attach()
    }

    internal class ViewHolder(tabView: View) {
        var name: AppTextView = tabView.findViewById(R.id.name)

    }
}