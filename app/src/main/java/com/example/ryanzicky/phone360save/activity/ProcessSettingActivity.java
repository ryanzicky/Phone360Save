package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.SpUtil;

/**
 * Created by ryanzicky on 2016/8/10.
 */
public class ProcessSettingActivity extends Activity{
    private CheckBox cb_show_system;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_setting);

        initSystemShow();
    }

    private void initSystemShow() {
        cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
        //对之前存储过的状态进行回显
        boolean showSystem = SpUtil.getBoolean(this, ConstantValue.SHOW_SYSTEM, false);
        //单选框的显示状态
        cb_show_system.setChecked(showSystem);

        if(showSystem){
            cb_show_system.setText("显示系统进程");
        }else{
            cb_show_system.setText("隐藏系统进程");
        }
        //对选中状态进行监听
        cb_show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //isChecked就作为是否选中的状态
                if(isChecked){
                    cb_show_system.setText("显示系统进程");
                }else{
                    cb_show_system.setText("隐藏系统进程");
                }
                SpUtil.putBoolean(ProcessSettingActivity.this, ConstantValue.SHOW_SYSTEM,isChecked);
            }
        });
    }
}
