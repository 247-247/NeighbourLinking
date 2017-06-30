package com.example.waqarahmed.neighbourlinking.Classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.waqarahmed.neighbourlinking.Fragments.MyPosts;
import com.example.waqarahmed.neighbourlinking.Fragments._Home_wall;

/**
 * Created by Waqar ahmed on 5/7/2017.
 */

public class Pager extends FragmentStatePagerAdapter {
    int tabCount;
    public Pager(FragmentManager fm , int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                _Home_wall home_wall = new _Home_wall();
                return home_wall;
            case 1:
                MyPosts myPosts = new MyPosts();
                return myPosts;

            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return tabCount;
    }
}
