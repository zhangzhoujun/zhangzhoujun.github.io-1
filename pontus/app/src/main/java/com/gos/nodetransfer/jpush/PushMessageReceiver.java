//package com.gos.nodetransfer.jpush;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.dim.library.utils.DLog;
//import com.gos.nodetransfer.ui.LoadingActivity;
//
//import cn.jpush.android.api.CmdMessage;
//import cn.jpush.android.api.CustomMessage;
//import cn.jpush.android.api.JPushInterface;
//import cn.jpush.android.api.JPushMessage;
//import cn.jpush.android.api.NotificationMessage;
//import cn.jpush.android.service.JPushMessageReceiver;
//
//public class PushMessageReceiver extends JPushMessageReceiver {
//    private static final String TAG = "JIGUANG-PushMessageReceiver";
//
//    @Override
//    public void onMessage(Context context, CustomMessage customMessage) {
//        DLog.e(TAG, "[onMessage] " + customMessage);
//        processCustomMessage(context, customMessage);
//    }
//
//    @Override
//    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
//        DLog.e(TAG, "[onNotifyMessageOpened] " + message);
//        try {
//            //打开自定义的Activity
//            Intent i = new Intent(context, LoadingActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
//            bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);
//        } catch (Throwable throwable) {
//
//        }
//    }
//
//    @Override
//    public void onMultiActionClicked(Context context, Intent intent) {
//        DLog.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
//        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);
//
//        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
//        if (nActionExtra == null) {
//            DLog.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
//            return;
//        }
//        if (nActionExtra.equals("my_extra1")) {
//            DLog.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
//        } else if (nActionExtra.equals("my_extra2")) {
//            DLog.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
//        } else if (nActionExtra.equals("my_extra3")) {
//            DLog.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
//        } else {
//            DLog.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
//        }
//    }
//
//    @Override
//    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
//        DLog.e(TAG, "[onNotifyMessageArrived] " + message);
//    }
//
//    @Override
//    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
//        DLog.e(TAG, "[onNotifyMessageDismiss] " + message);
//    }
//
//    @Override
//    public void onRegister(Context context, String registrationId) {
//        DLog.e(TAG, "[onRegister] " + registrationId);
//    }
//
//    @Override
//    public void onConnected(Context context, boolean isConnected) {
//        DLog.e(TAG, "[onConnected] " + isConnected);
//    }
//
//    @Override
//    public void onCommandResult(Context context, CmdMessage cmdMessage) {
//        DLog.e(TAG, "[onCommandResult] " + cmdMessage);
//    }
//
//    @Override
//    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
//        super.onTagOperatorResult(context, jPushMessage);
//    }
//
//    @Override
//    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
//        super.onCheckTagOperatorResult(context, jPushMessage);
//    }
//
//    @Override
//    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
//        super.onAliasOperatorResult(context, jPushMessage);
//    }
//
//    @Override
//    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
//        super.onMobileNumberOperatorResult(context, jPushMessage);
//    }
//
//    //send msg to MainActivity
//    private void processCustomMessage(Context context, CustomMessage customMessage) {
//        DLog.e(TAG, "[processCustomMessage] customMessage :" + customMessage.title);
////        if (MainActivity.isForeground) {
////            String message = customMessage.message;
////            String extras = customMessage.extra;
////            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
////            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
////            if (!ExampleUtil.isEmpty(extras)) {
////                try {
////                    JSONObject extraJson = new JSONObject(extras);
////                    if (extraJson.length() > 0) {
////                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
////                    }
////                } catch (JSONException e) {
////
////                }
////
////            }
////            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
////        }
//    }
//
//    @Override
//    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
//        super.onNotificationSettingsCheck(context, isOn, source);
//        DLog.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
//    }
//
//}
