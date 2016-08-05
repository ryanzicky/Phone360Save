package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.engine.AddressDao;

/**
 * Created by ryanzicky on 2016/8/5.
 */
public class QueryAddressActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        //测试代码，测试查询引擎类是否成功
        AddressDao.getAddress("13000201234");
    }
}
