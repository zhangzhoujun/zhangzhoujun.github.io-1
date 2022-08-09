package com.qm.module_version.ui

import android.app.Application
import android.widget.ProgressBar
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.dim.library.utils.DLog
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.utils.JYAppManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import zlc.season.rxdownload3.RxDownload.create
import zlc.season.rxdownload3.RxDownload.extension
import zlc.season.rxdownload3.core.*
import zlc.season.rxdownload3.extension.ApkInstallExtension
import zlc.season.rxdownload3.extension.ApkInstallExtension.Installed
import zlc.season.rxdownload3.extension.ApkInstallExtension.Installing
import zlc.season.rxdownload3.helper.dispose

/**
 * @ClassName VersionViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/22 11:22 AM
 * @Version 1.0
 */
class DownloadViewModel(application: Application) : BaseAppViewModel(application) {

    private var mProgressBar: ProgressBar? = null

    var icon = ObservableField<String>()
    var desc = ObservableField<String>()

    // 下载百分比
    var percent = ObservableField<String>()

    var forceUpdate = ObservableBoolean(false)

    private var downUrl: String? = null

    // 下载
    private var mMission: Mission? = null
    private var mDisposable: Disposable? = null
    private val mStatus = Status()

    fun initParam(
        versionStr: String,
        descStr: String,
        downUrlStr: String,
        force: Boolean,
        progressBar: ProgressBar
    ) {
        DLog.d("verStr : $versionStr descStr: $descStr downUrlStr: $downUrlStr force :$force")
        icon.set(versionStr)
        desc.set(descStr)
        downUrl = downUrlStr
        forceUpdate.set(force)
        mProgressBar = progressBar

        downApk()
    }

    private fun downApk() {
        mMission = Mission(downUrl!!)
        mMission!!.overwrite = true
        mDisposable = create(mMission!!, true)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { status ->
                DLog.d(
                    "版本更新：Size："
                            + status.downloadSize
                            + " TotalSize: " + status.totalSize
                            + " percent : " + status.percent()
                            + " toString : " + status.toString()
                )
                percent.set(status.percent())
                if (status.totalSize <= 0) {
                    mProgressBar!!.progress = 0
                } else {
                    mProgressBar!!.progress = (status.downloadSize * 100 / status.totalSize).toInt()
                }
                updateState(status)
            }
    }


    fun updateState(status: Status?) {
        var statusStr = ""
        if (status is Normal) {
            statusStr = "开始"
        } else if (status is Suspend) {
            statusStr = "已暂停"
        } else if (status is Waiting) {
            statusStr = "等待中"
        } else if (status is Downloading) {
            statusStr = "暂停"
        } else if (status is Failed) {
            statusStr = "失败"
        } else if (status is Succeed) {
            statusStr = "安装"
            install()
        } else if (status is Installing) {
            statusStr = "安装中"
        } else if (status is Installed) {
            statusStr = "打开"
        }
    }

    private fun install() {
        extension(mMission!!, ApkInstallExtension::class.java).subscribe()
        finish()
    }

    override fun onBackPressed() {
        if (forceUpdate.get()) {
            return
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose(mDisposable)
    }
}