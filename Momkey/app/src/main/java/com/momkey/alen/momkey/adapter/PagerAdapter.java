package com.momkey.alen.momkey.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.momkey.alen.momkey.fragment.ListenFragment;
import com.momkey.alen.momkey.fragment.RecordFragment;

/**
 * Created by Tamara on 8/3/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;//number of tabs

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {

        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
//change tabs on swipe
        switch (position) {
            case 0:
              ListenFragment tab1 = new ListenFragment();
                return tab1;
            case 1:
               RecordFragment tab2 = new RecordFragment();

                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}