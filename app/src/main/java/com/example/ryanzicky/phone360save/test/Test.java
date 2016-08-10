package com.example.ryanzicky.phone360save.test;

import android.test.AndroidTestCase;

import com.example.ryanzicky.phone360save.db.dao.BlackNumberDao;
import com.example.ryanzicky.phone360save.db.domain.BlackNumberInfo;

import java.util.List;

/**
 * Created by ryanzicky on 2016/8/9.
 */

public class Test extends AndroidTestCase{
    public void insert(){
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        dao.insert("110","1");
    }

    public void delete(){
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        dao.delete("110");
    }

    public void update(){
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        dao.update("110","2");
    }

    public void findAll(){
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
        List<BlackNumberInfo> blackNumberInfoList = dao.findAll();
    }
}
