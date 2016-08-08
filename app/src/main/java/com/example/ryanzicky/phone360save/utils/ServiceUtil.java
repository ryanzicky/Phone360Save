package com.example.ryanzicky.phone360save.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by ryanzicky on 2016/8/8.
 */

public class ServiceUtil {
    /**
     *
     * @param ctx 上下文环境
     * @param serviceName 判断是否正在运行的服务
     * @return true 运行 false 没有运行
     */
    public static boolean isRuning(Context ctx, String serviceName){
        //1.获取activityManager管理者对象，可以获取当前手机正在运行的服务
        ActivityManager mAM = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        //2.获取手机中正在运行的服务(多少个服务)
        List<ActivityManager.RunningServiceInfo> runningServices = mAM.getRunningServices(100);
        //3.遍历获取所有的服务集合，拿到每一个服务的类的名称，和传递进来的类的名称做比对，如果一致，说明服务正在运行
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            //4.获取每一个运行服务的名称
            if(serviceName.equals(runningServiceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
