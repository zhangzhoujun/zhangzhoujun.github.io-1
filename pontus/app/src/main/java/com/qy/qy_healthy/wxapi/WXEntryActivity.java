package com.qy.qy_healthy.wxapi;

import android.content.Intent;
import android.util.Log;

import com.qy.qysdk.manager.QYWxConstants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * @ClassName WXEntryActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/19 4:01 PM
 * @Version 1.0
 */
public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        try {
//            if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
//                WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
//                String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
//
//                if (extraData != null && (!"".equals(extraData))) {
//                    Intent intent = new Intent();
//                    intent.setAction(ConstantTask.updateWalkStep);
//                    intent.putExtra("data", extraData);
//                    sendBroadcast(intent);
//                }
//            }
            if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
                WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
                String extraData =launchMiniProResp.extMsg;
                Log.d("WXEntryActivity", "获取到的返回参数是 ： -> " + extraData);
                // 步数获取信息返回之后通知SDK
                if (extraData != null && (!"".equals(extraData))) {
                    Intent intent = new Intent();
                    intent.setAction(QYWxConstants.QY_SDK_WX_MINIPROGRAM_BROADCAST);
                    intent.putExtra("data", extraData);
                    intent.putExtra("success", true);
                    sendBroadcast(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(QYWxConstants.QY_SDK_WX_MINIPROGRAM_BROADCAST);
                    intent.putExtra("data", extraData);
                    intent.putExtra("success", false);
                    sendBroadcast(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResp(resp);
        finish();
    }
}
