package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ryanzicky.phone360save.R;

/**
 * Created by ryanzicky on 2016/8/3.
 */
public class SetUp3Activity extends Activity{
    private EditText et_phone_number;
    private Button bt_select_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    private void initUI() {
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        bt_select_number = (Button) findViewById(R.id.bt_select_number);

        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回到当前界面的是，接收结果列表
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void nextPage(View v){
        Intent intent = new Intent(getApplicationContext(), SetUp4Activity.class);
        startActivity(intent);

        finish();
    }

    public void prePage(View v){
        Intent intent = new Intent(getApplicationContext(), SetUp2Activity.class);
        startActivity(intent);

        finish();
    }
}
