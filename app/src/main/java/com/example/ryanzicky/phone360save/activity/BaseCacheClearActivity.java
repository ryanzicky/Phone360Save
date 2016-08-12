package com.example.ryanzicky.phone360save.activity;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.ryanzicky.phone360save.R;

public class BaseCacheClearActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_clear_cache);

		//1.生成选项卡1
		TabHost.TabSpec tab1 = getTabHost().newTabSpec("clear_chche").setIndicator("缓存清理");
		/*ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(R.drawable.ic_launcher);

		View inflate = View.inflate(getApplicationContext(), R.layout.test, null);
		TabHost.TabSpec tab1 = getTabHost().newTabSpec("clear_chche").setIndicator(inflate);*/

        //2.生成选项卡1
		TabHost.TabSpec tab2 = getTabHost().newTabSpec("sd_clear_cache").setIndicator("sd卡清理");

		//3.告知点中选中选项卡后继续操作
		tab1.setContent(new Intent(this,CacheClearActivity.class));
		tab2.setContent(new Intent(this,SDCacheClearActivity.class));

		//4.将此两个选项卡维护到host（选项卡宿主）中去
		getTabHost().addTab(tab1);
		getTabHost().addTab(tab2);
	}
}
