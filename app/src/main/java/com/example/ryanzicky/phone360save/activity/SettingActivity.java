package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.service.AddressService;
import com.example.ryanzicky.phone360save.service.BlackNumberService;
import com.example.ryanzicky.phone360save.service.WatchDogService;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.ServiceUtil;
import com.example.ryanzicky.phone360save.utils.SpUtil;
import com.example.ryanzicky.phone360save.view.SettingClickView;
import com.example.ryanzicky.phone360save.view.SettingItemView;

/**
 * Created by ryanzicky on 2016/7/29.
 */
public class SettingActivity extends Activity{
    private String[] mToastStyleDes;
    private int mToastStyle;
    private SettingClickView scv_toast_style;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
        initAddress();
        initToastStyle();
        initLocation();
        initBlacknumber();
        initAppLock();
    }

    /**
     * 初始化程序锁方法
     */
    private void initAppLock() {
        final SettingItemView siv_app_lock = (SettingItemView) findViewById(R.id.siv_app_lock);
        boolean isRunning = ServiceUtil.isRuning(this, "com.example.ryanzicky.phone360save.service.WatchDogService");
        siv_app_lock.setCheck(isRunning);

        siv_app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCheck = siv_app_lock.isCheck();
                siv_app_lock.setCheck(!isCheck);
                if(isCheck){
                    //开启服务
                    startService(new Intent(getApplicationContext(), WatchDogService.class));
                }else {
                    //关闭服务
                    stopService(new Intent(getApplicationContext(), WatchDogService.class));
                }
            }
        });
    }

    /**
     * 拦截黑名单短信电话
     */
    private void initBlacknumber() {
        final SettingItemView siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);
        boolean isRunning = ServiceUtil.isRuning(this, "com.example.ryanzicky.phone360save.service.BlackNumberService");
        siv_blacknumber.setCheck(isRunning);

        siv_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCheck = siv_blacknumber.isCheck();
                siv_blacknumber.setCheck(!isCheck);
                if(isCheck){
                    //开启服务
                    startService(new Intent(getApplicationContext(), BlackNumberService.class));
                }else {
                    //关闭服务
                    stopService(new Intent(getApplicationContext(), BlackNumberService.class));
                }
            }
        });
    }

    /**
     * 双击居中view所在屏幕位置的方法
     */
    private void initLocation() {
        SettingClickView scv_location = (SettingClickView) findViewById(R.id.scv_location);
        scv_location.setTitle("归属地提示框的位置");
        scv_location.setDes("设置归属地提示框的位置");
        scv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ToastLocationActivity.class));
            }
        });
    }

    private void initToastStyle() {
        scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
        scv_toast_style.setTitle("设置归属地显示风格");
        //1.创建描述文字所在的string类型数组
        mToastStyleDes = new String[]{"透明","橙色","蓝色","灰色","绿色"};
        //2.SP获取弹框显示样式的索引值(int)，用于获取描述文字
        mToastStyle = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
        //3.通过索引，获取字符串数组中的文字，显示给描述内容控件
        scv_toast_style.setDes(mToastStyleDes[mToastStyle]);
        //4.监听点击事件，弹出对话框
        scv_toast_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //5.显示弹框样式的对话框
                showToastStyleDialog();
            }
        });
    }

    /**
     * 创建选中显示样式的对话框
     */
    private void showToastStyleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("请选择归属地样式");
        //选择单个条目事件监听
        /**
         * 1.string类型的数组描述颜色文字数组
         * 2.弹出对话框的时候选中条目索引值
         * 3.点击某一个条目触发的点击事件
         */
        builder.setSingleChoiceItems(mToastStyleDes, mToastStyle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {//i选中的索引值
                //(1.记录选中的索引值，2.关闭对话框，3.显示选中色值文字)
                SpUtil.putInt(getApplicationContext(),ConstantValue.TOAST_STYLE,i);
                dialogInterface.dismiss();
                scv_toast_style.setDes(mToastStyleDes[i]);
            }
        });
        //取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 是否显示电话号码归属地的方法
     */
    private void initAddress() {
        final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
        //对服务是否开启的状态做显示
        boolean isRunning = ServiceUtil.isRuning(this, "com.example.ryanzicky.phone360save.service.AddressService");
        siv_address.setCheck(isRunning);
        //点击过程中，状态的切换过程
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回点击前的选中状态
                boolean isCheck = siv_address.isCheck();
                siv_address.setCheck(!isCheck);
                if(!isCheck){
                    //开启服务，管理弹框
                    startService(new Intent(getApplicationContext(),AddressService.class));
                }else{
                    //关闭服务
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                }
            }
        });
    }

    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);

        //获取已有的开关状态,用作显示
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //是否选中，根据上一次存储的结果去做决定
        siv_update.setCheck(open_update);

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果之前是选中的，点击过后，变成为选中
                boolean isCheck = siv_update.isCheck();
                //将原有
                siv_update.setCheck(!isCheck);
                //将去翻后的状态存储到相应sp中
                SpUtil.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isCheck);
            }
        });
    }
}
