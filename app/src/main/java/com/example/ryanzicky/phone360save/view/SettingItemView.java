package com.example.ryanzicky.phone360save.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ryanzicky.phone360save.R;

/**
 * Created by ryanzicky on 2016/7/29.
 */

public class SettingItemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.ryanzicky.phone360save";
    private static final String tag = "SettingItemView";
    private TextView tv_title;
    private TextView tv_des;
    private CheckBox cb_box;

    private String mDestitle;
    private String mDesoff;
    private String mDeson;
    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml---->view   将设置界面的一个条目转换成view对象，直接添加到了当前相对布局对应的view中
        View.inflate(context, R.layout.setting_item_view,this);

        //自定义组合控件中的标题描述
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        //获取自定义以及原生属性的操作，写在此处，AttributeSet ttrs对象中获取
        initAttrs(attrs);

        //获取布局文件中定义的字符串，赋值给自定义自合控件的标题
        tv_title.setText(mDestitle);
    }

    /**
     *
     * @param attrs 构造方法中维护好的属性集合
     *              返回属性集合中自定义属性属性值
     */
    private void initAttrs(AttributeSet attrs) {
        /*//获取属性的总个数
        Log.i(tag,"attrs.getAttributeCount() = " + attrs.getAttributeCount());
        //获取属性名称以及属性值
        for(int i = 0; i < attrs.getAttributeCount(); i++){
            Log.i(tag,"attrs.getAttributeName() = " + attrs.getAttributeName(i));
            Log.i(tag,"attrs.getAttributeValue() = " + attrs.getAttributeValue(i));
            Log.i(tag,"分割线======================================================");
        }*/

        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");

        Log.i(tag,mDestitle);
        Log.i(tag,mDesoff);
        Log.i(tag,mDeson);

    }

    /**
     * 判断是否开启的方法
     * @return 返回当前SettingItemView是否选中状态 true开启(返回true) false关闭(返回false)
     */
    public boolean isCheck(){
        //由checkBox的选中结果，决定当前条目是否开启
        return cb_box.isChecked();
    }

    /**
     *
     * @param isCheck   是否作为开启的变量，由点击过程中去做传递
     */
    public void setCheck(boolean isCheck){
        //当前条目在选择的过程中，cb_box选中状态也在跟随变化
        cb_box.setChecked(isCheck);
        if(isCheck){
            //开启
            tv_des.setText(mDeson);
        }else{
            //关闭
            tv_des.setText(mDesoff);
        }
    }
}
