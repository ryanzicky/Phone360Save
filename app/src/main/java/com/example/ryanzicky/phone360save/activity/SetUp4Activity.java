package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.SpUtil;

/**
 * Created by ryanzicky on 2016/8/3.
 */
public class SetUp4Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    public void nextPage(View v){
        Intent intent = new Intent(getApplicationContext(), SetUpOverActivity.class);
        startActivity(intent);

        finish();
        SpUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);
    }

    public void prePage(View v){
        Intent intent = new Intent(getApplicationContext(), SetUp3Activity.class);
        startActivity(intent);

        finish();
    }
}
