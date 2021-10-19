package com.ckw.filepickerlib.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ckw
 * on 2021/10/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments,List<String> titles) {
        super(fm);

        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}

