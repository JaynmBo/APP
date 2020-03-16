package com.caobo.app.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import static android.content.Context.WIFI_SERVICE;

public class AndroidUtil {
    private static DisplayMetrics dMetrics;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕的宽度px
     */
    public static int getDisplayW(Context context) {
        if (dMetrics == null) {
            dMetrics = context.getResources().getDisplayMetrics();
        }
        return dMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     */
    public static int getDisplayH(Context context) {
        if (dMetrics == null) {
            dMetrics = context.getResources().getDisplayMetrics();
        }
        return dMetrics.heightPixels;
    }


    /**
     * 广告图(轮播图)高度 首页
     *
     * @param context
     * @return 620 x 274
     */
    public static int getPicHeight(Context context) {
        return getDisplayW(context) * 300 / 750;
    }

    /**
     * 新品、活动、特卖列表广告图 高度
     *
     * @param context
     * @return 620 x 274
     */
    public static int getBlancePicHeight(Context context) {
        return getDisplayW(context) * 250 / 750;
    }

    /**
     * 产品详情页主图高度
     *
     * @param context
     * @return
     */
    public static int getProductDetailsPicHeight(Context context) {
        return getDisplayW(context) * 320 / 750;
    }

    /**
     * 首页广告高度
     *
     * @param context
     * @return
     */
    public static int getHomeAdHight(Context context) {
        return getDisplayW(context) * 160 / 750;
    }

    /**
     * 暂时的产品详情页图片高度
     *
     * @param context
     * @return
     */
    public static int getPicHeights(Context context) {
        return getDisplayW(context) * 300 / 620;
    }

    /**
     * 效果列表图()高度
     *
     * @param context
     * @param distance 表示左右的边距之和
     * @return 592 x 212
     */
    public static int getPicHeight(Context context, int distance) {
        return (getDisplayW(context) - distance) * 212 / 592;
    }

    /**
     * 首页效果列表图()高度
     *
     * @param context
     * @param distance 表示左右的边距之和
     * @return 592 x 212
     */
    public static int getPicHomeHeight(Context context, int distance) {
        return (getDisplayW(context) - distance) / 75 * 52;
    }

    /**
     * 产品列表图
     *
     * @param context
     * @param distance
     * @return 296 x 184
     */
    public static int getPicGridHeight(Context context, int distance) {
        return ((getDisplayW(context) - distance) / 2) * 184 / 296;
    }

    //获取本机WIFI
    public static String getLocalIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();

        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }

    //	3G网络IP
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
     *
     * @return
     * @author SHANHY
     */
    public static String getPsdnIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }


    /**
     * 判断程序是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static String getActivity(Context context) {
        String packname = ""; /* Android5.0之后获取程序锁的方式是不一样的*/
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > 21) {
            // 5.0及其以后的版本
            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            if (null != tasks && tasks.size() > 0) {
                for (ActivityManager.AppTask task:tasks){
                    packname = Objects.requireNonNull(task.getTaskInfo().baseIntent.getComponent()).getClassName();
                }
            }
        } else {
            // 5.0之前
            // 获取正在运行的任务栈(一个应用程序占用一个任务栈) 最近使用的任务栈会在最前面
            // 1表示给集合设置的最大容量
            List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);
            // 获取最近运行的任务栈中的栈顶Activity(即用户当前操作的activity)的包名
            packname = infos.get(0).topActivity.getClassName();
        }
        return  packname;

    }


    /**
     * 软键盘是否显示
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘
     * @param context
     * @param v
     * @return
     */
    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    /**
     * 检查是否存在虚拟按键栏
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }


    /**
     * 判断虚拟按键栏是否重写
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }


}