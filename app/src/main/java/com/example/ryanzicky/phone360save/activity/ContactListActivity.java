package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.ryanzicky.phone360save.R;

/**
 * Created by ryanzicky on 2016/8/3.
 */
public class ContactListActivity extends Activity{
    protected static final String tag = "ContactListActivity";
    private ListView lv_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initUI();
        initData();
    }

    /**
     * 获取系统联系人数据的方法
     */
    private void initData() {
        //因为读取系统联系人可能是一个耗时操作，放置到子线程中处理
        new Thread(){
            @Override
            public void run() {
                //1.获取内容解析器对象
                ContentResolver contentResolver = getContentResolver();
                //2.做查询系统联系人数据库表过程（读取联系人权限）
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null, null, null);
                //3.循环游标直到没有数据为止
                while(cursor.moveToNext()){
                    String id = cursor.getString(0);
                    Log.i(tag,"id = " + id);
                }
                cursor.close();
            }
        }.start();
    }

    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
    }
}
