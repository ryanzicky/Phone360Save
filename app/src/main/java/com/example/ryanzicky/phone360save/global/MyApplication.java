package com.example.ryanzicky.phone360save.global;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by ryanzicky on 2016/8/12.
 */

public class MyApplication extends Application {
    private static final String tag = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        //捕获全局（应用任意模块）异常

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                //在获取到了未捕获的异常后，处理的方法
                throwable.printStackTrace();
                Log.i(tag,"捕获到了一个程序的异常");

                //将捕获到的异常存储到sd卡
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "error111.log";
                File file = new File(path);
                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    throwable.printStackTrace(printWriter);
                    printWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //上传到服务器
                //结束应用
                System.exit(0);
            }
        });
    }
}
