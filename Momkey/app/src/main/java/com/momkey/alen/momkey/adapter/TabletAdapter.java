package com.momkey.alen.momkey.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.momkey.alen.momkey.fragment.ListenFragment;

/**
 * Created by Alen on 8/22/2016.
 */
public class TabletAdapter extends FragmentPagerAdapter {

// right side on tablet

    public TabletAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        ListenFragment tab2 = new ListenFragment();


        return tab2;
    }
    @Override
    public int getCount() {
        return 1;
    }

}