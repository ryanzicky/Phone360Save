package com.example.ryanzicky.phone360save.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ryanzicky on 2016/7/29.
 */

public class SpUtil {
    private static SharedPreferences sp;
    /**
     * 写入boolean变量至sp中
     * @param context 上下文环境
     * @param key 存储节点名称
     * @param value 存储节点的值Boolean
     */
    public static void putBoolean(Context context,String key,Boolean value){
        //（存储节点文件的名称,读写方式）
        if(sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 读取boolean表示从sp中
     * @param context 上下文环境
     * @param key 存储节点名称
     * @param defValue 没有此节点的默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static boolean getBoolean(Context context,String key,Boolean defValue){
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }
}
