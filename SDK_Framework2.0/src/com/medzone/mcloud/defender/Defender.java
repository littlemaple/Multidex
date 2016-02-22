/**
 *
 */
package com.medzone.mcloud.defender;

import android.content.Context;
import android.text.TextUtils;

import com.medzone.framework.Log;
import com.medzone.framework.Config;

import cn.jpush.android.api.JPushInterface;

/**
 * @author Robert.
 */
public class Defender {

    /**
     * remote service context.
     */
    private Context context;
    public static final String TAG = "JPush_Local_Defender";

    public Defender(Context context) {
        this.context = context;
        initJPush();
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void initJPush() {
        JPushInterface.setDebugMode(Config.isDeveloperMode || Config.isTesterMode);
        JPushInterface.init(getContext());
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getContext());
//        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;  // 设置为铃声与震动都要
//        JPushInterface.setPushNotificationBuilder(1, builder);
        startJPush();
        JPushInterface.setLatestNotificationNumber(getContext(), CloudPush.NS_MAX_DISPLAY_NUMBER);
        Log.d(TAG, "Defender#进程 initJPush：" + android.os.Process.myPid() + "，initJPush()" + "[" + getContext().hashCode() + "]" + JPushInterface.getConnectionState(getContext()));
    }

    /**
     * 这里先Stop之后才Resume，似乎是JPush内部在部分情况下直接调用Resume并不能恢复成功。
     * 所以先Stop再Resume来增加恢复的成功率。
     */
    public void startJPush() {
        stopJPush();
        checkJPushState(" before startJPush");
        JPushInterface.resumePush(getContext());
        checkJPushState(" before startJPush");
        Log.d(TAG, "Defender#进程 startJPush：" + android.os.Process.myPid() + "，startJPush()" + "[" + getContext().hashCode() + "]");
    }

    /**
     * 当存在主进程与私有进程同时存在时，JPush的操作只针对优先级高的进程进行调度。对优先级低的进程调度将不会生效（似乎直接被抛弃了Bug？）。
     * 但是当主进程被干掉时，私有进程的优先级被提高后。对私有进程的操作都将会生效。（猜测）
     *
     * @author Robert
     */
    public void stopJPush() {
        checkJPushState(" before stopJPush");
        JPushInterface.stopPush(getContext());
        checkJPushState("after stopJPush ");
        Log.d(TAG, "Defender#进程 stopJPush：" + android.os.Process.myPid() + "，stopJPush()" + "[" + getContext().hashCode() + "]");
    }

    public String getRegisterID() {
        checkJPushState("getRegisterID");
        boolean isStop = JPushInterface.isPushStopped(getContext());
        if (isStop) {
            Log.d(TAG, "Defender#进程 the jpush is stopped , try to start service：" + android.os.Process.myPid() + "[" + getContext().hashCode() + "]");
            startJPush();
            Log.d(TAG, "Defender#进程 start jpush completed , is Jpush stopped：" + JPushInterface.isPushStopped(getContext()) + android.os.Process.myPid() + "[" + getContext().hashCode() + "]");
        }
        String ret = JPushInterface.getRegistrationID(getContext());
        Log.d(TAG, "Defender#进程 getRegisterID：" + android.os.Process.myPid() + "，getRegisterID():" + ret + "[" + getContext().hashCode() + "]");
        return ret;
    }

    /**
     * @return
     * @author Robert
     * @deprecated FIXME 并不是检查到JPUSH连接未成功，就要自动发起连接请求。
     */
    public boolean checkConnectState(boolean isAutoConnecting) {
        final boolean ret = JPushInterface.getConnectionState(getContext());
        Log.d(TAG, "Defender#进程 checkConnectState：" + android.os.Process.myPid() + "，checkConnectState():" + ret + "[" + getContext().hashCode() + "]");
        if (!ret) {
            if (TextUtils.isEmpty(getRegisterID())) {
                initJPush();
            }
            if (isAutoConnecting) {
                startJPush();
            }
        }
        return ret;
    }

    private void checkJPushState(String desc) {
        Log.v(TAG, desc + ",call checkJPushState");
        boolean isStop = JPushInterface.isPushStopped(getContext());
        Log.d(TAG, "Defender#进程 checkJPushState: " + android.os.Process.myPid() + "，isPushStopped :" + isStop + "  [" + getContext().hashCode() + "]");
    }

    public boolean isJpushStopped() {
        return JPushInterface.isPushStopped(getContext());
    }

}
