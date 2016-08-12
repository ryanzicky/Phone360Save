package com.example.ryanzicky.phone360save.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.ryanzicky.phone360save.activity.EnterPsdActivity;
import com.example.ryanzicky.phone360save.db.dao.AppLockDao;

import java.util.List;

/**
 * Created by ryanzicky on 2016/8/11.
 */
public class WatchDogService extends Service{

    private boolean isWactch;
    private AppLockDao mDao;
    private List<String> mPackageNameList;
    private InnerReceiver mInnerReceiver;
    private String mSkipPackageName;
    private MyContentObserver mContentObserver;

    @Override
    public void onCreate() {
        //维护一个看门狗的死循环，让其时刻监测现在开启的应用是否为程序锁中要去拦截的应用
        mDao = AppLockDao.getInstance(this);
        isWactch = true;
        watch();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SKIP");

        mInnerReceiver = new InnerReceiver();
        registerReceiver(mInnerReceiver,intentFilter);

        //注册一个内容观察者，观察数据库的变化，一旦数据有删除或者删除，则需要让mPackageNameList重新获取一次数据
        mContentObserver = new MyContentObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://applock/change"),true,mContentObserver);
        super.onCreate();
    }

    class MyContentObserver extends ContentObserver{

        public MyContentObserver(Handler handler) {
            super(handler);
        }

        //一旦数据库发生改变的时候调用方法,重新获取包名所在集合的数据
        @Override
        public void onChange(boolean selfChange) {
            new Thread(){
                @Override
                public void run() {
                    mPackageNameList = mDao.findAll();
                }
            }.start();
            super.onChange(selfChange);
        }
    }

    class InnerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            //获取发送广播过程中传递过来的包名，跳过此包名监测过程
            mSkipPackageName = intent.getStringExtra("packagename");
        }
    }

    private void watch() {
        //1.子线程，开启一个可控死循环
        new Thread(){
            @Override
            public void run() {
                mPackageNameList = mDao.findAll();
                while (isWactch){
                    //2.监测现在正在开启的应用，任务栈
                    //3.获取activity管理者对象
                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    //4.获取正在开启的应用的任务栈方法
                    List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                    ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                    //5.获取栈顶的activity，然后获取此activity所在应用包名
                    String packageName = runningTaskInfo.topActivity.getPackageName();
                    //6.拿此包名在已加锁的包名集合中去做比对，如果包含此包名，则需要弹出拦截界面
                    if(mPackageNameList.contains(packageName)){
                        //如果现在检测的声粗，已经解锁了，则不需要弹出拦截界面
                        if(!packageName.equals(mSkipPackageName)){
                            //7.弹出拦截界面
                            Intent intent = new Intent(getApplicationContext(),EnterPsdActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("packagename",packageName);
                            startActivity(intent);
                        }
                    }

                    //睡眠一下，时间片轮转
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        //停止看门狗
        isWactch = false;
        if(mInnerReceiver != null){
            unregisterReceiver(mInnerReceiver);
        }
        super.onDestroy();
    }
}
