package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.engine.SmsBackUp;

import java.io.File;

/**
 * Created by ryanzicky on 2016/8/5.
 */
public class AToolsActivity extends Activity{
    private TextView tv_query_phone_address,tv_sms_backup;
    private ProgressBar pb_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);

        //电话归属地查询方法
        initPhoneAddress();
        initSmsBackUp();
    }

    private void initSmsBackUp() {
        tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        tv_sms_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSmsBckUpDialog();
            }
        });
    }

    private void showSmsBckUpDialog() {
        //1.创建一个带进度条的对话框
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.ic_launcher);
        progressDialog.setTitle("短信备份");
        //2.指定进度条的样式为水平
        progressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
        //3.展示进度条
        progressDialog.show();
        //4.直接调用备份短信方法即可
        new Thread(){
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sms360.xml";
                SmsBackUp.backup(getApplicationContext(), path, new SmsBackUp.CallBack() {
                    @Override
                    public void setMax(int max) {
                        //由开发者自己决定使用对话框还是进度条
                        progressDialog.setMax(max);
                        pb_bar.setMax(max);
                    }

                    @Override
                    public void setProgress(int index) {
                        //由开发者自己决定使用对话框还是进度条
                        progressDialog.setProgress(index);
                        pb_bar.setProgress(index);
                    }
                });

                progressDialog.dismiss();
            }
        }.start();

    }

    private void initPhoneAddress() {
        tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);
        tv_query_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),QueryAddressActivity.class));
            }
        });
    }
}
