package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.SpUtil;
import com.example.ryanzicky.phone360save.utils.ToastUtil;

/**
 * Created by ryanzicky on 2016/8/3.
 */
public class SetUp3Activity extends BaseSetUpActivity{
    private EditText et_phone_number;
    private Button bt_select_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    @Override
    public void showNextPage() {
        //点击按钮以后，需要获取输入框中的联系人，再做下一页操作
        String phone = et_phone_number.getText().toString();

        //在sp存储了相关联系人以后才可以跳转到下一界面
//        String contact_phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE,"");
        if(!TextUtils.isEmpty(phone)){
            Intent intent = new Intent(getApplicationContext(), SetUp4Activity.class);
            startActivity(intent);

            finish();

            //如果现在是输入的电话号码，则需要去保存
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE,phone);

            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtil.show(this,"请输入电话号码");
        }
    }

    @Override
    public void showPrePage() {
        Intent intent = new Intent(getApplicationContext(), SetUp2Activity.class);
        startActivity(intent);

        finish();

        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    private void initUI() {
        //显示电话号码输入框
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        //获取联系人电话号码回显过程
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        et_phone_number.setText(phone);

        //点击选择联系人的对话框
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
        if(data != null){
            //1.返回到当前界面的是，接收结果列表
            String phone = data.getStringExtra("phone");
            //2.将特殊字符过滤(中划线转换成空字符串)
            phone = phone.replace("-","").replace(" ","").trim();
            et_phone_number.setText(phone);

            //3.存储联系人至sp中
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE,phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*public void nextPage(View v){
        //点击按钮以后，需要获取输入框中的联系人，再做下一页操作
        String phone = et_phone_number.getText().toString();

        //在sp存储了相关联系人以后才可以跳转到下一界面
//        String contact_phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE,"");
        if(!TextUtils.isEmpty(phone)){
            Intent intent = new Intent(getApplicationContext(), SetUp4Activity.class);
            startActivity(intent);

            finish();

            //如果现在是输入的电话号码，则需要去保存
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE,phone);

            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtil.show(this,"请输入电话号码");
        }
    }

    public void prePage(View v){
        Intent intent = new Intent(getApplicationContext(), SetUp2Activity.class);
        startActivity(intent);

        finish();

        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }*/
}
