package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.ryanzicky.phone360save.R;

/**
 * Created by ryanzicky on 2016/8/4.
 */

public abstract class BaseSetUpActivity extends Activity {
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            //4.重写手势识别器，包含按下点和抬起点在移动过程中的方法
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getRawX() - e2.getRawX() > 100){
                    //下一页，由右向左滑动
                    //如果在第一个设置界面中由右向左滑动，跳转的第二个界面
                    //如果在第二个设置界面中由右想做滑动，跳转到第三个界面
                    //如果在第三个设置界面中由右向左滑动，跳转的第四个界面
                    //如果在第四个设置界面中由右想做滑动，跳转到设置完成界面

                    showNextPage();
                }

                if(e2.getRawX() - e1.getRawX() > 100){
                    //上一页，由左向右滑动
                    //如果在第一个设置界面中由左向右滑动
                    //如果在第二个设置界面中由左向右滑动，跳转到第一个界面
                    //如果在第三个设置界面中由左向右滑动，跳转的第二个界面
                    //如果在第四个设置界面中由左向右滑动，跳转到第三个界面
                    showPrePage();
                }
                return super.onFling(e1,e2,velocityX,velocityY);
            }
        });
    }

    //抽象方法，定义跳转到下一页的方法
    public abstract void showNextPage();

    //抽象方法，定义跳转到上一页的方法
    public abstract void showPrePage();

    //1.监听当前activity上的触摸时间（按下（1次），滑动（多次），抬起（1次））
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);//将activity中的手机移动操作，交由手势识别器处理
        return super.onTouchEvent(event);
    }

    //统一处理每一个界面中的上一页下一页按钮
    public void nextPage(View view){
        //将跳转到下一页的代码逻辑，交由子类
        showNextPage();
    }

    //统一处理每一个界面中的上一页下一页按钮
    public void prePage(View view){
        //将跳转到上一页的代码逻辑，交由子类
        showPrePage();
    }
}
