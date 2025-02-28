//package com.gos.nodetransfer.jpush;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import com.dim.library.utils.DLog;
//import com.gos.nodetransfer.ui.LoadingActivity;
//import com.gos.nodetransfer.ui.MainActivity;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Iterator;
//
//import cn.jpush.android.api.JPushInterface;
//
///**
// * 自定义接收器
// * <p>
// * 如果不定义这个 Receiver，则：
// * 1) 默认用户会打开主界面
// * 2) 接收不到自定义消息
// */
//public class MyReceiver extends BroadcastReceiver {
//    private static final String TAG = "JIGUANG-MyReceiver";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        try {
//            Bundle bundle = intent.getExtras();
//            DLog.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//
//            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//                DLog.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//                //send the Registration Id to your server...
//
//            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//                DLog.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);
//
//            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//                DLog.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//                DLog.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//
//            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                DLog.d(TAG, "[MyReceiver] 用户点击打开了通知");
//
//                //打开自定义的Activity
//                Intent i = new Intent(context, LoadingActivity.class);
//                i.putExtras(bundle);
//                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(i);
//            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//                DLog.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
//            } else {
//                DLog.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 打印所有的 intent extra 数据
//    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String key : bundle.keySet()) {
//            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
//                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
//                    DLog.i(TAG, "This message has no Extra data");
//                    continue;
//                }
//
//                try {
//                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//                    Iterator<String> it = json.keys();
//
//                    while (it.hasNext()) {
//                        String myKey = it.next();
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " + json.optString(myKey) + "]");
//                    }
//                } catch (JSONException e) {
//                    DLog.e(TAG, "Get message extra JSON error!");
//                }
//
//            } else {
//                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
//            }
//        }
//        return sb.toString();
//    }
//
//    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        DLog.d(TAG, "processCustomMessage ");
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        Intent msgIntent = new Intent(MainActivity.Main.getMESSAGE_RECEIVED_ACTION());
//        msgIntent.putExtra(MainActivity.Main.getKEY_MESSAGE(), message);
//        if (!ExampleUtil.isEmpty(extras)) {
//            try {
//                JSONObject extraJson = new JSONObject(extras);
//                if (extraJson.length() > 0) {
//                    msgIntent.putExtra(MainActivity.Main.getKEY_EXTRAS(), extras);
//                }
//                LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
