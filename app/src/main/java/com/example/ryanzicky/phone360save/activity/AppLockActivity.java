package com.example.ryanzicky.phone360save.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.db.dao.AppLockDao;
import com.example.ryanzicky.phone360save.db.domain.AppInfo;
import com.example.ryanzicky.phone360save.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanzicky on 2016/8/11.
 */
public class AppLockActivity extends Activity{
    private Button bt_unlock,bt_locked;
    private LinearLayout ll_unlock,ll_locked;
    private TextView tv_unlock,tv_locked;
    private ListView lv_unlock,lv_locked;
    private List<AppInfo> mAppInfoList;
    private List<AppInfo> mLockList,mUnLockList;
    private AppLockDao mDao;
    private MyAdapter mLockAdapter,mUnLockAdapter;
    private TranslateAnimation mTranslateAnimation;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //6.接收到消息，填充已加锁和未加锁的数据适配器
            mLockAdapter = new MyAdapter(true);
            lv_locked.setAdapter(mLockAdapter);

            mUnLockAdapter = new MyAdapter(false);
            lv_unlock.setAdapter(mUnLockAdapter);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_app_lock);

        initUI();
        initData();
        initAnimation();
    }

    /**
     * 初始化平移动画的方法(平移自身的一个宽度大小)
     */
    private void initAnimation() {
        mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        mTranslateAnimation.setDuration(500);
    }

    /**
     * 区分已加锁和未加锁应用
     */
    private void initData() {
        new Thread(){
            @Override
            public void run() {
                //1.获取手机中的应用
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                //2.区分已加锁应用和未加锁应用
                mLockList = new ArrayList<>();
                mUnLockList = new ArrayList<>();
                //3.获取数据库中已加锁应用包名的集合
                mDao = AppLockDao.getInstance(getApplicationContext());
                List<String> lockPackageList = mDao.findAll();
                for(AppInfo appInfo : mAppInfoList){
                    //4.如果循环到的应用的包名在数据库中，则说明是已加锁的应用
                    if(lockPackageList.contains(appInfo.getPackageName())){
                        mLockList.add(appInfo);
                    }else{
                        mUnLockList.add(appInfo);
                    }
                }
                //5.告知主线程，可以使用维护的数据
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        bt_unlock = (Button) findViewById(R.id.bt_unlock);
        bt_locked = (Button) findViewById(R.id.bt_locked);

        ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
        ll_locked = (LinearLayout)findViewById(R.id.ll_locked);

        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        tv_locked = (TextView) findViewById(R.id.tv_locked);

        lv_unlock = (ListView) findViewById(R.id.lv_unlock);
        lv_locked = (ListView)findViewById(R.id.lv_locked);

        bt_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.未加锁列表显示，已加锁列表隐藏
                ll_unlock.setVisibility(View.VISIBLE);
                bt_locked.setVisibility(View.GONE);
                //2.已加锁编程浅色图片，未加锁编程深色图片
                ll_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                bt_locked.setBackgroundResource(R.drawable.tab_right_default);
            }
        });

        bt_locked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.已加锁列表显示，未加锁列表隐藏
                ll_locked.setVisibility(View.VISIBLE);
                ll_unlock.setVisibility(View.GONE);
                //2.未加锁编程浅色图片，已加锁编程深色图片
                bt_unlock.setBackgroundResource(R.drawable.tab_left_default);
                bt_locked.setBackgroundResource(R.drawable.tab_right_pressed);
            }
        });
    }

    class MyAdapter extends BaseAdapter{
        private boolean isLock;
        /**
         *
         * @param isLock isLock用于区分已加锁和未加锁应用的标识 true已加锁 false未加锁
         */
        public MyAdapter(boolean isLock){
            this.isLock = isLock;
        }
        @Override
        public int getCount() {
            if(isLock){
                tv_locked.setText("已加锁的应用："+mLockList.size());
                return mLockList.size();
            }else{
                tv_unlock.setText("未加锁的应用："+mUnLockList.size());
                return mUnLockList.size();
            }
        }

        @Override
        public AppInfo getItem(int i) {
            if(isLock){
                return mLockList.get(i);
            }else{
                return mUnLockList.get(i);
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(),R.layout.list_view_lock_item,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            final AppInfo appInfo = getItem(i);
            final View animationView = convertView;

            holder.iv_icon.setBackgroundDrawable(appInfo.icon);
            holder.tv_name.setText(appInfo.name);
            if(isLock){
                holder.iv_lock.setBackgroundResource(R.drawable.lock);
            }else{
                holder.iv_lock.setBackgroundResource(R.drawable.unlock);
            }
            holder.iv_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //添加动画效果
                    animationView.startAnimation(mTranslateAnimation);//500毫秒
                    //对动画执行过程做事件监听，监听到动画执行完成后，在去移除集合中的数据，操作数据库，刷新适配器
                    mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //动画开始的时候调用的方法
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //动画结束的时候调用的方法
                            if(isLock){
                                //已加锁----->未加锁过程
                                //1.已加锁集合删除一个，未加锁集合添加一个，对象就是getItem方法获取的对象
                                mLockList.remove(appInfo);
                                mUnLockList.add(appInfo);
                                //2.从已加锁的数据库中删除一条数据
                                mDao.delete(appInfo.getPackageName());
                                //3.刷新数据适配器
                                mLockAdapter.notifyDataSetChanged();
                            }else{
                                //未加锁----->已加锁过程
                                //1.未加锁集合删除一个，已加锁集合添加一个，对象就是getItem方法获取的对象
                                mUnLockList.remove(appInfo);
                                mLockList.add(appInfo);
                                //2.已加锁的数据库中插入一条数据
                                mDao.insert(appInfo.getPackageName());
                                //3.刷新数据适配器
                                mUnLockAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            //动画重复的时候调用的方法
                        }
                    });
                }
            });
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_lock;
    }
}
