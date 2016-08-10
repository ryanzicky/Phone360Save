package com.example.ryanzicky.phone360save.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ryanzicky on 2016/8/9.
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    public BlackNumberOpenHelper(Context context) {
        super(context,"blacknumber.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库表的方法
        sqLiteDatabase.execSQL("create table blacknumber " +
                "(_id integer primary key autoincrement , phone varchar(20), mode varchar(5));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
