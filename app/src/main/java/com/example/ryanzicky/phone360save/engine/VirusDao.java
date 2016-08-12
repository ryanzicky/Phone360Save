package com.example.ryanzicky.phone360save.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanzicky on 2016/8/5.
 */

public class VirusDao {
    //1.指定访问数据库的路径
    public static String path = "data/data/com.example.ryanzicky.phone360save/files/antivirus.db";
    //2.开启数据库，查询数据库中表对应的md5码
    public static List<String> getVirusList(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.query("datable", new String[]{"md5"}, null, null, null, null, null);

        List<String> virusList = new ArrayList<>();
        while (cursor.moveToNext()){
            String virus = cursor.getString(0);
            virusList.add(virus);
        }
        cursor.close();
        db.close();

        return virusList;
    }
}
