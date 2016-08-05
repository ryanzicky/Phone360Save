package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.SpUtil;
import com.example.ryanzicky.phone360save.utils.ToastUtil;

/**
 * Created by ryanzicky on 2016/8/3.
 */
public class SetUp4Activity extends BaseSetUpActivity{
    private CheckBox cb_box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initUi();
    }

    @Override
    public void showNextPage() {
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        if(open_security){
            Intent intent = new Intent(getApplicationContext(), SetUpOverActivity.class);
            startActivity(intent);

            finish();
            SpUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);

            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtil.show(getApplication(),"请开启防盗保护设置");
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(getApplicationContext(), SetUp3Activity.class);
        startActivity(intent);

        finish();

        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    private void initUi() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //1.是否选中状态的回显
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        //2.根据状态修改checkbox后续的文字显示
        cb_box.setChecked(open_security);
        if(open_security){
            cb_box.setText("安全设置已开启");
        }else{
            cb_box.setText("安全设置已关闭");
        }
        //3.点击过程中，checkbox状态的切换
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //isChecked点击后的状态
                //4.存储点击后的状态
                SpUtil.putBoolean(getApplicationContext(),ConstantValue.OPEN_SECURITY,isChecked);
                //5.根据开启关闭状态，修改显示的文字
                if(isChecked){
                    cb_box.setText("安全设置已开启");
                }else{
                    cb_box.setText("安全设置已关闭");
                }
            }
        });
    }

    /*public void nextPage(View v){
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        if(open_security){
            Intent intent = new Intent(getApplicationContext(), SetUpOverActivity.class);
            startActivity(intent);

            finish();
            SpUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);

            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtil.show(getApplication(),"请开启防盗保护设置");
        }
    }

    public void prePage(View v){
        Intent intent = new Intent(getApplicationContext(), SetUp3Activity.class);
        startActivity(intent);

        finish();

        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }*/
}
