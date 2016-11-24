package com.yeyu.dearinformaton.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yeyu.dearinformaton.fragment.videoFragment.VideoDetailFragment;

import java.util.List;

/**
 * Created by gaoyehua on 2016/9/19.
 */
public class VideoPagerAdapter extends FragmentStatePagerAdapter {
    private List<VideoDetailFragment> fragments;

    public VideoPagerAdapter(FragmentManager fm, List<VideoDetailFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
