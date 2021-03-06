package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.Md5Util;
import com.example.ryanzicky.phone360save.utils.SpUtil;
import com.example.ryanzicky.phone360save.utils.ToastUtil;

/**
 * Created by ryanzicky on 2016/7/28.
 */
public class HomeActivity extends Activity{

    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mDrawableIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        //初始化数据的方法
        initData();
    }

    private void initData() {
        //准备数据（文字（9组），图片（9张））
        mTitleStr = new String[]{"手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        mDrawableIds = new int[]{
                R.drawable.home_safe,
                R.drawable.home_callmsgsafe,
                R.drawable.home_apps,
                R.drawable.home_taskmanager,
                R.drawable.home_netmanager,
                R.drawable.home_trojan,
                R.drawable.home_sysoptimize,
                R.drawable.home_tools,
                R.drawable.home_settings
        };
        //九宫格控件设置数据适配器(等同于ListView数据适配器)
        gv_home.setAdapter(new MyAdapter());
        //注册九宫格单个条目点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //点中列表条目索引i
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //开启对话框
                        showDialog();
                        break;
                    case 1:
                        //跳转到通信卫士模块
                        startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),AppManagerActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),ProcessManagerActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(),TrafficActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(getApplicationContext(),AnitVirusActivity.class));
                        break;
                    case 6:
//                        startActivity(new Intent(getApplicationContext(),CacheClearActivity.class));
                        startActivity(new Intent(getApplicationContext(),BaseCacheClearActivity.class));
                        break;
                    case 7:
                        //跳转到高级工具功能列表界面
                        Intent intent1 = new Intent(getApplicationContext(),AToolsActivity.class);
                        startActivity(intent1);
                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void showDialog() {
        //判断本地是否有存储密码(sp 字符串)
        String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAVE_PSD,"");
        if(TextUtils.isEmpty(psd)){
            //1.处室设置密码的对话框
            showSetPsdDialog();
        }else{
            //2.确认密码的对话框
            showConfirmPsdDialog();
        }

    }

    /**
     * 确认密码对话框
     */
    private void showConfirmPsdDialog() {
        //需要自己去定义对话框的展示样式，所以需要调用dialog.setView()
        //view是由自己编写的xml转换成的view对象xml----->view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        final View view = View.inflate(this,R.layout.dialog_confirm_psd,null);
        //让对话框显示一个自己定义的对话框界面
//        dialog.setView(view);
        //为了兼容低版本，给对话框设置布局的时候，让其没有内边距
        dialog.setView(view,0,0,0,0);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);

                String confirmPsd = et_confirm_psd.getText().toString();

                if(!TextUtils.isEmpty(confirmPsd)){
                    //将存储在sp中32位的密码，获取出来，然后将输入的密码同样进行md5加密，然后与sp中存储密码比对
                    String psd = SpUtil.getString(getApplicationContext(),ConstantValue.MOBILE_SAVE_PSD,"");
                    if(psd.equals(Md5Util.encoder(confirmPsd))){
                        //进入应用手机防盗模块，开启一个新的activity
//                        Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        Intent intent = new Intent(getApplicationContext(),SetUpOverActivity.class);
                        startActivity(intent);

                        //跳转到新的界面后需要隐藏对话框
                        dialog.dismiss();

                        SpUtil.putString(getApplicationContext(),ConstantValue.MOBILE_SAVE_PSD,psd);
                    }else{
                        ToastUtil.show(getApplication(),"确认密码错误");
                    }
                }else{
                    //提示用户密码输入有空的情况
                    ToastUtil.show(getApplication(),"请输入密码");
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 设置密码对话框
     */
    private void showSetPsdDialog() {
        //需要自己去定义对话框的展示样式，所以需要调用dialog.setView()
        //view是由自己编写的xml转换成的view对象xml----->view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        final View view = View.inflate(this,R.layout.dialog_set_psd,null);
        //让对话框显示一个自己定义的对话框界面
//        dialog.setView(view);
        //为了兼容低版本，给对话框设置布局的时候，让其没有内边距
        dialog.setView(view,0,0,0,0);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
                EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);

                String psd = et_set_psd.getText().toString();
                String confirmPsd = et_confirm_psd.getText().toString();

                if(!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)){
                    if(psd.equals(confirmPsd)){
                        //进入应用手机防盗模块，开启一个新的activity
//                        Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        Intent intent = new Intent(getApplicationContext(),SetUpOverActivity.class);
                        startActivity(intent);

                        //跳转到新的界面后需要隐藏对话框
                        dialog.dismiss();

                        SpUtil.putString(getApplicationContext(),ConstantValue.MOBILE_SAVE_PSD, Md5Util.encoder(psd));
                    }else{
                        ToastUtil.show(getApplication(),"确认密码错误");
                    }
                }else{
                    //提示用户密码输入有空的情况
                    ToastUtil.show(getApplication(),"请输入密码");
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            //条目的总数 文字总数等于图片的张数
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int i) {
            //根据索引拿到的对象
            return mTitleStr[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View concertView, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);

            tv_title.setText(mTitleStr[i]);
            iv_icon.setBackgroundResource(mDrawableIds[i]);
            return view;
        }
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }
}
