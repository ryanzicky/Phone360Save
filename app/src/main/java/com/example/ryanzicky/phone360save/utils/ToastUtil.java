package com.example.ryanzicky.phone360save.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ryanzicky on 2016/7/28.
 */

public class ToastUtil {
    //打印输出

    /**
     *
     * @param ctx 上下文环境
     * @param msg 打印文本
     */
    public static void show(Context ctx, String msg){
        Toast.makeText(ctx,msg,0).show();

    }
}
