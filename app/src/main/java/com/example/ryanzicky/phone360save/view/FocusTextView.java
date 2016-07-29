package com.example.ryanzicky.phone360save.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ryanzicky on 2016/7/29.
 * 能够获取焦点的方法
 */

public class FocusTextView extends TextView {
    //使用通过java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }

    //由系统调用（带属性+上下文环境的构造方法）
    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //由系统调用（带属性+上下文环境的构造方法，在布局文件中定义的样式文件构造方法）
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写获取焦点的方法,由系统调用，调用的时候默认获取焦点
    @Override
    public boolean isFocused() {
        return true;
    }
}
