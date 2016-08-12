package com.example.ryanzicky.phone360save.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.ryanzicky.phone360save.R;
import com.example.ryanzicky.phone360save.engine.CommonNumberDao;

import java.util.List;

/**
 * Created by ryanzicky on 2016/8/11.
 */
public class CommonNumberQueryActivity extends Activity {
    private ExpandableListView elv_common_number;
    private List<CommonNumberDao.Group> mGroup;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number);

        initUI();
        initData();
    }

    /**
     * 给可扩展listview准备数据，并且填充
     */
    private void initData() {
        CommonNumberDao commonNumberDao = new CommonNumberDao();
        mGroup = commonNumberDao.getGroup();

        mAdapter = new MyAdapter();
        elv_common_number.setAdapter(mAdapter);
        //给可扩展listview注册点击事件
        elv_common_number.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                startCall(mAdapter.getChild(i, i1).number);
                return false;
            }
        });
    }

    private void startCall(String number) {
        //开启系统的打电话界面
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private void initUI() {
        elv_common_number = (ExpandableListView) findViewById(R.id.elv_common_number);
    }

    class MyAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return mGroup.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return mGroup.get(i).childList.size();
        }

        @Override
        public CommonNumberDao.Group getGroup(int i) {
            return mGroup.get(i);
        }

        @Override
        public CommonNumberDao.Child getChild(int i, int i1) {
            return mGroup.get(i).childList.get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        //dip = dp
        //dpi = ppi 像素密度（每一个英寸上分布的像素点的个数）
        @Override
        public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText("            "+getGroup(i).name);
            textView.setTextColor(Color.RED);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(), R.layout.elv_child_item, null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);

            tv_name.setText(getChild(i,i1).name);
            tv_number.setText(getChild(i,i1).number);
            return view;
        }

        //孩子节点是否响应事件
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
