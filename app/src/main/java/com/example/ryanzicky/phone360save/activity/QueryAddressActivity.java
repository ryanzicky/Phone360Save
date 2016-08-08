package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.engine.AddressDao;

/**
 * Created by ryanzicky on 2016/8/5.
 */
public class QueryAddressActivity extends Activity{
    private EditText et_phone;
    private Button bt_query;
    private TextView tv_query_result;
    private String mAddress;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //4.控件使用查询结果
            tv_query_result.setText(mAddress);
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        initUI();
    }

    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        bt_query = (Button) findViewById(R.id.bt_query);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);

        //1.点击查询功能，注册按钮的点击事件
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = et_phone.getText().toString();
                if(!TextUtils.isEmpty(phone)){
                    //2.查询是耗时操作，开启子线程
                    query(phone);
                }else{
                    //抖动
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    et_phone.startAnimation(shake);

                    //手机震动效果(vibrator 震动)
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    //震动的毫秒值
                    vibrator.vibrate(2000);
                    //规律震动(震动规则(不震动时间，震动时间，不震动时间，震动时间......)，重复次数)
                    vibrator.vibrate(new long[]{2000,5000,2000,5000},-1);
                }
            }
        });

        //5.实时查询（监听输入框变化）
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = et_phone.getText().toString();
                query(phone);
            }
        });
    }

    /**
     * 获取电话号码归属地
     * @param phone 查询的电话号码
     */
    protected void query(final String phone) {
        new Thread(){
            @Override
            public void run() {
                mAddress  = AddressDao.getAddress(phone);
                //3.消息机制，告知主线程查询结束，可以去使用查询结果
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
