package com.example.waqarahmed.neighbourlinking.Classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.waqarahmed.neighbourlinking.Fragments.Home_Wall;
import com.example.waqarahmed.neighbourlinking.Fragments.UserProfile;

/**
 * Created by Waqar ahmed on 5/28/2017.
 */

public class Pager_offers extends FragmentStatePagerAdapter {
    int tabCount;
    public Pager_offers(FragmentManager fm , int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Home_Wall home_wall = new Home_Wall();
                return home_wall;
            case 1:
                UserProfile userProfile = new UserProfile();
                return userProfile;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
