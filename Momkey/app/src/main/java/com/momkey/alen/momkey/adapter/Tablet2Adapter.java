package com.momkey.alen.momkey.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.momkey.alen.momkey.fragment.RecordFragment;

/**
 * Created by Alen on 8/23/2016.
 */
public class Tablet2Adapter extends FragmentPagerAdapter {

//left side on tablet
    public Tablet2Adapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        RecordFragment tab1 = new RecordFragment();

                return tab1;
        }


    @Override
    public int getCount() {
        return 1;
    }
}