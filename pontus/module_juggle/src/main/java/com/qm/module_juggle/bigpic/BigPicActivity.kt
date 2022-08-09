package com.qm.module_juggle.bigpic

import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.dim.library.utils.DLog
import com.dim.library.utils.StringUtils
import com.dim.library.utils.ToastUtils
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.SavePhotoUtils
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeActPicbigBinding
import com.youth.banner.listener.OnPageChangeListener
import java.text.ParseException

/**
 * @ClassName GuideActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/14 9:44 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.JuggleAct.JUGGLE_BIGPIC)
class BigPicActivity : JYActivity<HomeActPicbigBinding, BigPicViewModel>() {

    private var mCurrentPos = 0
    private var mTotal = 0
    private var listData = ArrayList<String>()

    @JvmField
    @Autowired(name = JYComConst.BIG_PIC)
    var bigPic: String = ""

    @JvmField
    @Autowired(name = JYComConst.BIG_PIC_POSITION)
    var position: Int = 0

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.home_act_picbig
    }

    private fun getBind(): HomeActPicbigBinding {
        return binding as HomeActPicbigBinding
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)

        if (StringUtils.isEmpty(bigPic)) {
            ToastUtils.showShort("数据错误，请重试")
            finish()
            return
        }

        var picData = bigPic.split(",")

        for (element in picData) {
            listData.add(element)
        }
        mTotal = listData.size

        var adapter =
            BigpicItemAdapter(this, listData, object : BigpicItemAdapter.OnBiPicClickListener {
                override fun onBigPicClick(position: Int) {
                    onBackPressed()
                }

            })
        getBind().banner.adapter = adapter

        getBind().banner.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                DLog.d("滑动到了$position")
                mCurrentPos = position
                showPosition()
            }

        })

        showPosition()
        getBind().save.setOnClickListener {
            Glide.with(this).asBitmap().load(listData[mCurrentPos])
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        doDownload(resource)
                    }
                })
        }

        getBind().banner.currentItem = position
    }

    private fun doDownload(bitmap: Bitmap) {
        try {
            val savePhoto = SavePhotoUtils(this)
            var url = savePhoto.saveImageToGallery(bitmap)
            if (!TextUtils.isEmpty(url)) {
                ToastUtils.showShort("保存成功")
            } else {
                ToastUtils.showShort("保存失败")
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun showPosition() {
        getBind().num.text = "${(mCurrentPos + 1)}/$mTotal"
    }
}