package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.ryanzicky.phone360save.R;

/**
 * Created by ryanzicky on 2016/8/1.
 */
public class SetUp1Activity extends BaseSetUpActivity{
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

        /*//3.创建收拾识别器的对象
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            //4.重写手势识别器，包含按下点和抬起点在移动过程中的方法
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getRawX() - e2.getRawX() > 100){
                    //下一页，由右向左滑动
                    Intent intent = new Intent(SetUp1Activity.this, SetUp2Activity.class);
                    startActivity(intent);

                    finish();

                    //开启平移动画
                    overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
                }

                if(e2.getRawX() - e1.getRawX() > 100){
                    //上一页，由左向右滑动
                }
                return super.onFling(e1,e2,velocityX,velocityY);
            }
        });*/
    }

    @Override
    public void showNextPage() {
        Intent intent = new Intent(getApplicationContext(), SetUp2Activity.class);
        startActivity(intent);

        finish();

        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

    @Override
    public void showPrePage() {
        //第一个界面没有上一页按钮
    }

    /*public void nextPage(View v){
        Intent intent = new Intent(getApplicationContext(), SetUp2Activity.class);
        startActivity(intent);

        finish();

        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }*/

    /*//1.监听当前activity上的触摸时间（按下（1次），滑动（多次），抬起（1次））
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);//将activity中的手机移动操作，交由手势识别器处理
        return super.onTouchEvent(event);
    }*/
}
