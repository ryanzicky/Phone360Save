package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.SpUtil;

/**
 * Created by ryanzicky on 2016/8/8.
 */
public class ToastLocationActivity extends Activity{
    private ImageView iv_drag;
    private Button bt_top,bt_bottom;
    private WindowManager mWM;
    private int mScreenHeight;
    private int mScreenWidth;
    private long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);

        initUI();
    }

    private void initUI() {
        //可拖拽双击居中的图片控件
        iv_drag = (ImageView) findViewById(R.id.iv_drag);
        bt_top = (Button) findViewById(R.id.bt_top);
        bt_bottom = (Button) findViewById(R.id.bt_bottom);

        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();

        int locationX = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        int locationY = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y,0);

        //左上角坐标作用在iv_drag上
        //指定宽高都为WRAP_CONTENT
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //将左上角的坐标作用在iv_drag对应的规则参数上
        layoutParams.leftMargin = locationX;
        layoutParams.topMargin = locationY;
        //将以上过则作用在iv_drag上
        iv_drag.setLayoutParams(layoutParams);

        if(locationY > mScreenHeight / 2){
            bt_bottom.setVisibility(View.INVISIBLE);
            bt_top.setVisibility(View.VISIBLE);
        }else{
            bt_top.setVisibility(View.INVISIBLE);
            bt_bottom.setVisibility(View.VISIBLE);
        }

        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.arraycopy(mHits,1,mHits,0,mHits.length - 1);
                if(mHits[mHits.length - 1] - mHits[0] < 500){
                    //满足双击事件后，调用的代码
                    int left = mScreenWidth / 2 - iv_drag.getWidth() / 2;
                    int top = mScreenHeight / 2 - iv_drag.getHeight() / 2;
                    int right = mScreenWidth / 2 + iv_drag.getWidth() / 2;
                    int bottom = mScreenHeight / 2 + iv_drag.getHeight() / 2;

                    //控件按以上规则显示
                    iv_drag.layout(left,top,right,bottom);

                    //存储最终位置
                    SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X,iv_drag.getLeft());
                    SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y,iv_drag.getTop());

                }
            }
        });
        //监听某一个控件的拖拽过程(按下（一次），移动（多次），抬起（一次）)
        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;
            //对不同的事件做不同的逻辑处理
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) motionEvent.getRawX();
                        int moveY = (int) motionEvent.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        //1.当前控件所在屏幕左上角的位置
                        int left = iv_drag.getLeft() + disX;//左端坐标
                        int top = iv_drag.getTop() + disY;//顶端坐标
                        int right = iv_drag.getRight() + disX;//右端坐标
                        int bottom = iv_drag.getBottom() + disY;//地段坐标

                        //容错处理(iv_drag不能拖拽出手机屏幕)
                        if(left < 0){
                            return true;
                        }

                        //右边缘不能超出屏幕
                        if(right > mScreenWidth){
                            return true;
                        }

                        //上边缘不能超出屏幕
                        if(top < 0){
                            return true;
                        }

                        //下边缘不能超出屏幕(屏幕高度 - 22 = 底边缘显示最大值)
                        if(bottom > mScreenHeight - 22){
                            return true;
                        }

                        if(top > mScreenHeight / 2){
                            bt_bottom.setVisibility(View.INVISIBLE);
                            bt_top.setVisibility(View.VISIBLE);
                        }else{
                            bt_top.setVisibility(View.INVISIBLE);
                            bt_bottom.setVisibility(View.VISIBLE);
                        }

                        //2.告知移动的控件，按计算出来的坐标做展示
                        iv_drag.layout(left,top,right,bottom);

                        //3.重置一次真是坐标
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();

                        //4.容错处理
                        break;
                    case MotionEvent.ACTION_UP:
                        //4.存储移动到的位置
                        SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X,iv_drag.getLeft());
                        SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y,iv_drag.getTop());
                        break;
                }
                //在当前的情况下返回false不响应事件,返回true才会响应事件
                //既要相应点击事件，又要响应拖拽过程，则此返回值结果需要修改为false
                return false;
            }
        });
    }
}
