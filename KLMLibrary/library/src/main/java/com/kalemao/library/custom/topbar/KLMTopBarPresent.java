package com.kalemao.library.custom.topbar;

import rx.Subscription;

/**
 * Created by dim on 2017/6/6 16:22
 * 邮箱：271756926@qq.com
 */

public class KLMTopBarPresent implements KLMTopBarContract.IKLMTopBarPresent{

    private Subscription mSubscription;

    private KLMTopBarContract.IKLMTopBarView iklmTopBarView;

    KLMTopBarPresent(KLMTopBarContract.IKLMTopBarView klmView){
        this.iklmTopBarView = klmView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    public void doLeftClick(){
        iklmTopBarView.onLeftClick();
    }

    public void doRightClick(){
        iklmTopBarView.onRightClick();
    }

    public void doRightLeftClick(){
        iklmTopBarView.onRightLeftClick();
    }
}
