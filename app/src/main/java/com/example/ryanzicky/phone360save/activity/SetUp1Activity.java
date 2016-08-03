package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ryanzicky.phone360save.R;

/**
 * Created by ryanzicky on 2016/8/1.
 */
public class SetUp1Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    public void nextPage(View v){
        Intent intent = new Intent(getApplicationContext(), SetUp2Activity.class);
        startActivity(intent);
    }
}
