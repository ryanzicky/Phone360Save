package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.SpUtil;
import com.example.ryanzicky.phone360save.view.SettingItemView;

/**
 * Created by ryanzicky on 2016/7/29.
 */
public class SettingActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

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
