package com.wd.tech.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * date:2019/2/19 16:53
 * author:陈国星(陈国星)
 * function:
 */
public class FragmentViewAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public FragmentViewAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
