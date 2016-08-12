package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ToastUtil;

/**
 * Created by ryanzicky on 2016/8/11.
 */
public class EnterPsdActivity extends Activity{
    private String packagename;
    private TextView tv_app_name;
    private ImageView iv_app_icon;
    private EditText et_psd;
    private Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取包名
        packagename = getIntent().getStringExtra("packagename");
        setContentView(R.layout.activity_enter_psd);

        initUI();
        initData();
    }

    private void initData() {
        //通过传递过来的包名获取拦截应用的图标和名称
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packagename, 0);
            Drawable icon = applicationInfo.loadIcon(pm);

            iv_app_icon.setBackgroundDrawable(icon);
            tv_app_name.setText(applicationInfo.loadLabel(pm).toString());


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String psd = et_psd.getText().toString();
                if(!TextUtils.isEmpty(psd)){
                    if(psd.equals("123")){
                        //解锁，进入应用，告知看门狗，不要再监听已经解锁的应用，发送广播
                        Intent intent = new Intent("android.intent.action.SKIP");
                        intent.putExtra("packagename",packagename);
                        sendBroadcast(intent);

                        finish();
                    }else{
                        ToastUtil.show(getApplicationContext(),"密码错误");
                    }
                }else{
                    ToastUtil.show(getApplicationContext(),"请输入密码");
                }
            }
        });
    }

    private void initUI() {
        tv_app_name = (TextView) findViewById(R.id.tv_app_name);
        iv_app_icon = (ImageView) findViewById(R.id.iv_app_icon);
        et_psd = (EditText) findViewById(R.id.et_psd);
        bt_submit = (Button) findViewById(R.id.bt_submit);
    }

    @Override
    public void onBackPressed() {
        //通过隐式意图跳转到桌面
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        super.onBackPressed();
    }
}
