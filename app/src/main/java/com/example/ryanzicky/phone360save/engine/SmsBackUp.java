package com.example.ryanzicky.phone360save.engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ryanzicky on 2016/8/9.
 */

public class SmsBackUp {
    private static int index = 0;
    //备份短信方法

    //A.传递一个进度条所在的对话框
    //B.传递一个进度条

    public static void backup(Context ctx, String path, CallBack callBack){
        FileOutputStream fos = null;
        Cursor cursor = null;
        try {
            //需要用到的对象上下文环境，备份文件夹路径，进度条所在的对话框对象用于备份过程中进度的更新
            //1.获取备份短信写入到文件
            File file = new File(path);
            //2.获取内容解析器，获取短信数据库中的数据
            cursor = ctx.getContentResolver().query(Uri.parse("content://sms/"),
                    new String[]{"address", "date", "type", "body"}, null, null, null);
            //3.文件相应的输出流
            fos = new FileOutputStream(file);
            //4.序列化数据库中读取的数据，放置到xml中
            XmlSerializer xmlSerializer = Xml.newSerializer();
            //5.给此xml做相应设置
            xmlSerializer.setOutput(fos,"utf-8");
            //DTD(xml规范)
            xmlSerializer.startDocument("utf-8",true);
            xmlSerializer.startTag(null,"smss");

            //6.备份短信总数指定
            //A.如果传递进来的是对话框，指定对话框进度条的总数
            //B.如果传递进来的是进度条，指定进度条的总数
//            pd.setMax(cursor.getCount());
            if(callBack != null){
                callBack.setMax(cursor.getCount());
            }

            //7.读取数据库中的每一行的数据写入到xml中
            while(cursor.moveToNext()){
                xmlSerializer.startTag(null,"sms");

                xmlSerializer.startTag(null,"address");
                xmlSerializer.text(cursor.getString(0));
                xmlSerializer.endTag(null,"address");

                xmlSerializer.startTag(null,"date");
                xmlSerializer.text(cursor.getString(1));
                xmlSerializer.endTag(null,"date");

                xmlSerializer.startTag(null,"type");
                xmlSerializer.text(cursor.getString(2));
                xmlSerializer.endTag(null,"type");

                xmlSerializer.startTag(null,"body");
                xmlSerializer.text(cursor.getString(3));
                xmlSerializer.endTag(null,"body");
                xmlSerializer.endTag(null,"sms");

                //8.没循环一次就需要去让进度条叠加
                index++;
                Thread.sleep(500);

                //A.如果传递进来的是对话框，指定对话框进度条的当前百分比
                //B.如果传递进来的是进度条，指定进度条的当前百分比
//                pd.setProgress(index);
                if(callBack != null){
                    callBack.setProgress(index);
                }
            }
            xmlSerializer.endTag(null,"smss");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

                try {
                    if(cursor != null && fos != null){
                        cursor.close();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    //回调
    //1.定义一个接口
    //2.定义接口中未实现的业务逻辑方法（短信总数的设置，备份过程中短信百分比更新）
    //3.传递一个实现了此接口的类的对象(至备份短信的工具类中)，接口的实现类，一定实现了上述两个为实现方法（就决定了使用对话框还是进度条）
    //4.获取传递进来的对象，在合适的地方(设置总数，设置百分比的地方)方法的调用

    public interface CallBack{
        //短信总数设置未实现方法(由自己决定使用对话框.setMax  还是用进度条.setMax)
        public void setMax(int max);
        //备份过程中短信百分比更新（由自己决定使用对话框.setProgress  还是用进度条.setProgress）
        public void setProgress(int index);
    }
}
