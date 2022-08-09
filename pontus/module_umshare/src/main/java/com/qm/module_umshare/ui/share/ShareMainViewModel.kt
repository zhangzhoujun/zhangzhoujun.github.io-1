package com.qm.module_umshare.ui.share

import android.app.Activity
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.dim.library.utils.DLog
import com.dim.library.utils.ToastUtils
import com.dim.library.utils.Utils
import com.qm.lib.base.BaseBean
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.http.RetrofitClient
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.utils.SavePhotoUtils
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qm.module_umshare.BR
import com.qm.module_umshare.R
import com.qm.module_umshare.databinding.UmActSahreMainBinding
import com.qm.module_umshare.entity.CShareItemBean
import com.qm.module_umshare.entity.MShareMainBean
import com.qm.module_umshare.http.ShareService
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.youth.banner.listener.OnPageChangeListener
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.text.ParseException


/**
 * @ClassName ShareMainViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/19 2:15 PM
 * @Version 1.0
 */
class ShareMainViewModel(application: Application) : ToolbarViewModel(application) {
    private lateinit var mActivity: Activity
    private lateinit var mBind: UmActSahreMainBinding
    private var mShareUrl = ""
    private var mCurrentPos = 0
    private var mItemAdapter: ShareBannerItemAdapter? = null
    private var mShareLayout: RelativeLayout? = null

    private var items = ArrayList<CShareItemBean>()
    private var shareTypeList = ArrayList<String>()

    var observableList: ObservableList<ShareItemViewModel> = ObservableArrayList()

    private fun initTitle() {
        val option = JYToolbarOptions()
        option.titleString = "邀请好友"
        option.isNeedNavigate = true
        option.backId = R.mipmap.lib_base_back
        setOptions(option)
    }

    fun initShareView(activity: Activity, bind: UmActSahreMainBinding) {
        mActivity = activity
        mBind = bind
        mShareLayout = mBind.bannerLayout
        initTitle()
        getShareData()
    }

    var itemBinding: ItemBinding<ShareItemViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.um_share_main_item)
    val adapter: BindingRecyclerViewAdapter<ShareItemViewModel> =
        BindingRecyclerViewAdapter<ShareItemViewModel>()

    private fun getShareData() {
        val service = RetrofitClient.getInstance().create(ShareService::class.java)

        RetrofitClient.getInstance()
            .execute(service.getShareInfo(getRequestBody(BaseBean<Any>())),
                object : AppObserver<BaseResultBean<MShareMainBean>>() {
                    override fun onSuccess(o: BaseResultBean<MShareMainBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            mShareUrl = o.data.shareUrl
                            shareTypeList = o.data.shareTypes
                            initShareType()
                            mItemAdapter = ShareBannerItemAdapter(mActivity, o.data)
                            mBind.banner
                                .setAdapter(mItemAdapter)
                                .setBannerGalleryEffect(
                                    36, 20, 1F
                                )
                            mBind.des.text = o.data.desc
                            mBind.banner.addOnPageChangeListener(object : OnPageChangeListener {
                                override fun onPageScrollStateChanged(state: Int) {

                                }

                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {

                                }

                                override fun onPageSelected(position: Int) {
                                    mCurrentPos = position
                                }

                            })
                        }
                    }
                })
    }

    override fun onCreate() {
        super.onCreate()

    }

    private fun initShareType() {
        for (index in 0 until shareTypeList.size) {
            items.add(
                CShareItemBean(
                    getDrawableByPos(shareTypeList[index]),
                    getShareNameByType(shareTypeList[index]),
                    shareTypeList[index]
                )
            )
        }
        initRecyclerView()
    }

    private fun getDrawableByPos(type: String): Drawable? {
        var context = Utils.getContext()
        when (type) {
            "save" -> return context.resources.getDrawable(R.drawable.um_share_download)
            "wechat" -> return context.resources.getDrawable(R.drawable.um_share_wechat)
            "wechat_circle" -> return context.resources.getDrawable(R.drawable.um_share_monent)
            "copy" -> return context.resources.getDrawable(R.drawable.um_share_copy)
        }
        return null
    }

    private fun initRecyclerView() {
        var count = items.size - 1
        for (i in 0..count) {
            DLog.d("initRecyclerView i = $i")
            var item = ShareItemViewModel(
                this,
                items[i],
                object : ShareItemViewModel.OnShareItemClickListener {
                    override fun onItemClick(type: String) {
                        onShareItemCLick(type)
                    }

                })
            observableList.add(item)
        }
    }

    private fun onShareItemCLick(type: String) {
        when (type) {
            "save" -> doDownload()
            "wechat" -> doWechatShare()
            "wechat_circle" -> doMonenttShare()
            "copy" -> doCopy()
        }
    }

    private fun getShareNameByType(type: String): String {
        when (type) {
            "save" -> return "保存图片"
            "wechat" -> return "微信"
            "wechat_circle" -> return "朋友圈"
            "copy" -> return "复制链接"
        }
        return ""
    }

    private fun doWechatShare() {
        SLSLogUtils.instance.sendLogClick("ShareMainActivity", "", "WECHAT")
        doUmShare(SHARE_MEDIA.WEIXIN)
    }

    private fun doMonenttShare() {
        SLSLogUtils.instance.sendLogClick("ShareMainActivity", "", "WECHAT_CIRCLE")
        doUmShare(SHARE_MEDIA.WEIXIN_CIRCLE)
    }

    private fun doQQShare() {
        SLSLogUtils.instance.sendLogClick("ShareMainActivity", "", "QQ")
        doUmShare(SHARE_MEDIA.QQ)
    }

    private fun doQZoneShare() {
        SLSLogUtils.instance.sendLogClick("ShareMainActivity", "", "QZONE")
        doUmShare(SHARE_MEDIA.QZONE)
    }

    private fun captureView(view: View): Bitmap {
        val bm =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bm))

        val bmEx = Bitmap.createBitmap(
            bm,
            mBind.bannerWidLayout.left,
            0,
            mBind.bannerWidLayout.width,
            view.height
        )
        return bmEx
    }

    private fun doDownload() {
        SLSLogUtils.instance.sendLogClick("ShareMainActivity", "", "DOWNLOAD")
        try {
            val savePhoto = SavePhotoUtils(mActivity)
            var url = savePhoto.saveImageToGallery(captureView(mShareLayout!!))
            if (!TextUtils.isEmpty(url)) {
                ToastUtils.showShort("保存成功")
            } else {
                ToastUtils.showShort("保存失败")
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun doCopy() {
        SLSLogUtils.instance.sendLogClick("ShareMainActivity", "", "COPY")
        val cm =
            Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData =
            ClipData.newPlainText("Label", mShareUrl)
        cm.setPrimaryClip(mClipData)
        ToastUtils.showShort("复制成功")
    }

    private fun doUmShare(platfrom: SHARE_MEDIA) {
        var bitmap = captureView(mShareLayout!!)
        var image = UMImage(mActivity, bitmap)

        ShareAction(mActivity).withMedia(image)
            .setPlatform(platfrom)
            .setCallback(object : UMShareListener {
                override fun onResult(p0: SHARE_MEDIA) {
                    DLog.d("onResult")
                    SLSLogUtils.instance.sendLogClick(
                        "ShareMainActivity",
                        "",
                        "SHARE_RESULT",
                        extra = "type = $p0.name , resoult = $p0"
                    )
                }

                override fun onCancel(p0: SHARE_MEDIA) {
                    DLog.d("onCancel")
                    SLSLogUtils.instance.sendLogClick(
                        "ShareMainActivity",
                        "",
                        "SHARE_CANCEL",
                        extra = "type = $p0.name , resoult = $p0"
                    )
                }

                override fun onError(p0: SHARE_MEDIA, p1: Throwable) {
                    DLog.d("onError -> $p1")
                    SLSLogUtils.instance.sendLogClick(
                        "ShareMainActivity",
                        "",
                        "SHARE_ERROR",
                        extra = "type = $p0.name , resoult = $p0 , Throwable = $p1"
                    )
                }

                override fun onStart(p0: SHARE_MEDIA) {
                    DLog.d("onStart")
                    SLSLogUtils.instance.sendLogClick(
                        "ShareMainActivity",
                        "",
                        "SHARE_START",
                        extra = "type = $p0.name , resoult = $p0"
                    )
                }

            }).share()
    }
}