package com.example.ryanzicky.phone360save.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.ryanzicky.phone360save.db.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ryanzicky on 2016/8/9.
 */

public class AppLockDao {
    private AppLockOpenHelper appLockOpenHelper;
    private Context context;
    //BlackNumberDao单例模式
    //1.私有化构造方法
    private AppLockDao(Context context){
        this.context = context;
        //创建数据库及其表结构
        appLockOpenHelper = new AppLockOpenHelper(context);
    }
    //2.声明一个当前类的对象
    private static AppLockDao appLockDao = null;
    //3.提供一个方法，如果当前类对象为空，创建一个新的
    public static AppLockDao getInstance(Context context){
        if(appLockDao == null){
            appLockDao = new AppLockDao(context);
        }
        return appLockDao;
    }

    //插入方法
    public void insert(String packageName){
        SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("packageName",packageName);

        db.insert("applock",null,contentValues);

        db.close();

        context.getContentResolver().notifyChange(Uri.parse("content://applock/change"),null);
    }

    //删除方法
    public void delete(String packageName){
        SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("packageName",packageName);

        db.delete("applock","packageName = ?",new String[]{packageName});

        db.close();

        context.getContentResolver().notifyChange(Uri.parse("content://applock/change"),null);
    }

    //查询所有
    public List<String> findAll(){
        SQLiteDatabase db = appLockOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("applock", new String[]{"packageName"}, null, null, null, null, null);

        List<String> lockPackageList = new ArrayList<>();
        while (cursor.moveToNext()){
            lockPackageList.add(cursor.getString(0));
        }
        cursor.close();
        db.close();

        return lockPackageList;
    }
}
