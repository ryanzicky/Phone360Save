package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.db.domain.ProcessInfo;
import com.example.ryanzicky.phone360save.engine.ProcessInfoProvider;
import com.example.ryanzicky.phone360save.utils.ConstantValue;
import com.example.ryanzicky.phone360save.utils.SpUtil;
import com.example.ryanzicky.phone360save.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanzicky on 2016/8/10.
 */
public class ProcessManagerActivity extends Activity implements View.OnClickListener{
    private TextView tv_process_count,tv_memory_info,tv_des;
    private ListView lv_process_list;
    private Button bt_all,bt_reverse,bt_clear,bt_setting;
    private int mProcessCount;
    private List<ProcessInfo> mProcessInfoList;

    private List<ProcessInfo> mSystemList;
    private List<ProcessInfo> mCustomerList;

    private MyAdapter mAdapter;

    private ProcessInfo mProcessInfo;

    private long mAvailSpace;
    private String mStrAvailSpace;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mAdapter = new MyAdapter();
            lv_process_list.setAdapter(mAdapter);

            if(tv_des != null && mCustomerList != null){
                tv_des.setText("用户进程（"+mCustomerList.size()+"）");
            }
        };
    };

    class MyAdapter extends BaseAdapter{
        //获取数据适配器中条目类型的总数,修改成两种(纯文本,图片+文字)
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        //指定索引指向的条目类型,条目类型状态码指定(0(复用系统),1)
        @Override
        public int getItemViewType(int position) {
            if(position == 0 || position == mCustomerList.size()+1){
                //返回0,代表纯文本条目的状态码
                return 0;
            }else{
                //返回1,代表图片+文本条目状态码
                return 1;
            }
        }

        //listView中添加两个描述条目
        @Override
        public int getCount() {
            if(SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM,false)){
                return mCustomerList.size()+mSystemList.size()+2;
            }else{
                return mCustomerList.size() + 1;
            }
        }

        @Override
        public ProcessInfo getItem(int position) {
            if(position == 0 || position == mCustomerList.size()+1){
                return null;
            }else{
                if(position<mCustomerList.size()+1){
                    return mCustomerList.get(position-1);
                }else{
                    //返回系统应用对应条目的对象
                    return mSystemList.get(position - mCustomerList.size()-2);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);

            if(type == 0){
                //展示灰色纯文本条目
                ViewTitleHolder holder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item_title, null);
                    holder = new ViewTitleHolder();
                    holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewTitleHolder) convertView.getTag();
                }
                if(position == 0){
                    holder.tv_title.setText("用户进程("+mCustomerList.size()+")");
                }else{
                    holder.tv_title.setText("系统进程("+mSystemList.size()+")");
                }
                return convertView;
            }else{
                //展示图片+文字条目
                ViewHolder holder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(), R.layout.listview_process_item, null);
                    holder = new ViewHolder();
                    holder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
                    holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                    holder.tv_memory_info = (TextView) convertView.findViewById(R.id.tv_memory_info);
                    holder.cb_box = (CheckBox) convertView.findViewById(R.id.cb_box);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
                holder.tv_name.setText(getItem(position).name);
                String strSize = Formatter.formatFileSize(getApplicationContext(), getItem(position).memSize);
                holder.tv_memory_info.setText(strSize);
                //本应用不能被选中，所以先将checkbox隐藏掉
                if(getItem(position).packageName.equals(getPackageName())){
                    holder.cb_box.setVisibility(View.GONE);
                }else{
                    holder.cb_box.setVisibility(View.VISIBLE);
                }
                holder.cb_box.setChecked(getItem(position).isCheck);

                return convertView;
            }
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_memory_info;
        CheckBox cb_box;
    }

    static class ViewTitleHolder{
        TextView tv_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ProcessManagerActivity","执行了onCreate");
        setContentView(R.layout.activity_process_manager);
        
        initUI();
        initTitleData();
        initListData();
    }

    private void initListData() {
        new Thread(){
            @Override
            public void run() {
                getDate();
                super.run();
            }
        }.start();
    }

    private void getDate(){
        new Thread(){
            @Override
            public void run() {
                mProcessInfoList = ProcessInfoProvider.getProcessInfo(getApplicationContext());
                mSystemList = new ArrayList<ProcessInfo>();
                mCustomerList = new ArrayList<ProcessInfo>();

                for (ProcessInfo processInfo : mProcessInfoList){
                    if(processInfo.isSystem){
                        //系统进程
                        mSystemList.add(processInfo);
                    }else{
                        //用户进程
                        mCustomerList.add(processInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initTitleData() {
        mProcessCount = ProcessInfoProvider.getProcessCount(this);
        tv_process_count.setText("进程总数："+mProcessCount);

        //获取可用内存大小，并且格式化
        long mAvailSpace = ProcessInfoProvider.getAvailSpace(this);
        mStrAvailSpace = Formatter.formatFileSize(this, mAvailSpace);

        //总运行内存大小，并且格式化
        long totalSpace = ProcessInfoProvider.getTotalSpace(this);
        String strTotalSpace = Formatter.formatFileSize(this, totalSpace);

        tv_memory_info.setText("剩余/总共："+ mAvailSpace+"/"+strTotalSpace);
    }

    private void initUI() {
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        tv_memory_info = (TextView) findViewById(R.id.tv_memory_info);

        tv_des = (TextView) findViewById(R.id.tv_des);

        lv_process_list = (ListView) findViewById(R.id.lv_process_list);

        bt_all = (Button) findViewById(R.id.bt_all);
        bt_reverse = (Button) findViewById(R.id.bt_reverse);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_setting = (Button) findViewById(R.id.bt_setting);

        bt_all.setOnClickListener(this);
        bt_reverse.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_setting.setOnClickListener(this);

        lv_process_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                //滚动过程中调用方法
                //i第一个可见条目
                //i1当前一个屏幕的可见条目
                //i2总共条目总数
                if(mCustomerList != null && mSystemList != null){
                    if(i >= mCustomerList.size() + 1){
                        //滚动到了系统条目
                        tv_des.setText("系统进程("+mSystemList.size()+")");
                    }else{
                        //滚动到了用户应用条目
                        tv_des.setText("用户进程("+mCustomerList.size()+")");
                    }
                }
            }
        });

        lv_process_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //view选中条目指向的view对象
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0 || i == mCustomerList.size() + 1){
                    return;
                }else{
                    if(i < mCustomerList.size() + 1){
                        mProcessInfo = mCustomerList.get(i - 1);
                    }else{
                        //返回系统应用对应条目的对象
                        mProcessInfo = mSystemList.get(i - mCustomerList.size() - 2);
                    }
                    if(mProcessInfo != null){
                        if(!mProcessInfo.packageName.equals(getPackageName())){
                            //选中条目指向对象和本应用的包名不一致，才需要去取反状态和设置单选框
                            //状态取反
                            mProcessInfo.isCheck = !mProcessInfo.isCheck;
                            //checkbox显示状态切换
                            //通过选中条目的view对象，findViewByid找到此条目指向的cb_box，然后切换其状态
                            CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
                            cb_box.setChecked(mProcessInfo.isCheck);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_all:
                selectAll();
                break;
            case R.id.bt_reverse:
                selectReverse();
                break;
            case R.id.bt_clear:
                clearAll();
                break;
            case R.id.bt_setting:
                setting();
                break;
        }
    }

    private void setting() {
        Intent intent = new Intent(this, ProcessSettingActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //通知数据适配器刷新
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 清理选中过程
     */
    private void clearAll() {
        //1.选取选中过程
        //2.创建一个记录需要杀死的进程集合
        List<ProcessInfo> killProcessInfoList = new ArrayList<>();
        for(ProcessInfo processInfo : mCustomerList){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            if(processInfo.isCheck){
                //不能在集合循环过程中去移除集合中的对象
//                mCustomerList.remove(processInfo);
                //3.记录需要杀死的用户进程
                killProcessInfoList.add(processInfo);
            }
        }
        for(ProcessInfo processInfo : mSystemList){
            //4.记录需要杀死的系统进程
            killProcessInfoList.add(processInfo);
        }
        //5.循环遍历KillProcessInfoList，然后去移除mCustomerList和mSystemList中的对象
        long totalReleaseSpqce = 0;
        for(ProcessInfo processInfo : killProcessInfoList){
            //6.判断当前进程在哪个集合中，从所在集合中移除
            if(mCustomerList.contains(processInfo)){
                mCustomerList.remove(processInfo);
            }
            if(mSystemList.contains(processInfo)){
                mSystemList.remove(processInfo);
            }
            //7.杀死记录在KillProcessInfoList中的进程
            ProcessInfoProvider.killProcess(this,processInfo);

            //记录释放空间的总大小
            totalReleaseSpqce += processInfo.memSize;
        }
        //8.在集合改变后，需要通知数据适配器刷新
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        //9.进程总数的更新
        mProcessCount -=  killProcessInfoList.size();
        //10.更新可用剩余空间
        mAvailSpace += totalReleaseSpqce;
        //11.更新进程总数和剩余空间大小
        tv_process_count.setText("进程总数："+mProcessCount);
        tv_memory_info.setText("剩余/总共:"+Formatter.formatFileSize(this,mAvailSpace)+"/"+mStrAvailSpace);
        //12.通过弹框告知用户，释放了多少空间，杀死了几个进程
        String totalRelease = Formatter.formatFileSize(this, totalReleaseSpqce);
//        ToastUtil.show(getApplicationContext(),"杀死了"+killProcessInfoList.size()+"个进程,释放了"+totalRelease+"空间");
        //占位符指定数据
        ToastUtil.show(getApplicationContext(),String.format("杀死了%d，释放了%s空间",killProcessInfoList.size(),totalRelease));
    }

    private void selectReverse() {
        //1.将所有集合中的对象isCheck字段设置为true，代表全选，排除当前应用
        for(ProcessInfo processInfo : mCustomerList){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            processInfo.isCheck = !processInfo.isCheck;
        }
        for(ProcessInfo processInfo : mSystemList){
            processInfo.isCheck = !processInfo.isCheck;
        }
        //2.通知数据适配器刷新
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private void selectAll() {
        //1.将所有集合中的对象isCheck字段取反
        for(ProcessInfo processInfo : mCustomerList){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            processInfo.isCheck = true;
        }
        for(ProcessInfo processInfo : mSystemList){
            processInfo.isCheck = true;
        }
        //2.通知数据适配器刷新
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }
}
