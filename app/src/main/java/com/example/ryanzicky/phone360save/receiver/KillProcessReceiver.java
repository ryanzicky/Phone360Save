package com.example.ryanzicky.phone360save.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ryanzicky.phone360save.engine.ProcessInfoProvider;

/**
 * Created by ryanzicky on 2016/8/11.
 */
public class KillProcessReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //杀死进程
        ProcessInfoProvider.killAll(context);
    }
}
