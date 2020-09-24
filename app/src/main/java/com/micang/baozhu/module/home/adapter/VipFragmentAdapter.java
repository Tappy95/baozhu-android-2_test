package com.micang.baozhu.module.home.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.micang.baozhu.module.home.VIPFragment;
import com.micang.baselibrary.base.BaseFragment;

import java.util.List;


public class VipFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;
    private List<VIPFragment> mFragments;

    public VipFragmentAdapter(FragmentManager fm, List<VIPFragment> fragments) {
        super(fm);
        mFragmentManager = fm;
        mFragments = fragments;

    }

    public void updateFragments(List<VIPFragment> fragments) {
        for (int i = 0; i < mFragments.size(); i++) {
            final BaseFragment fragment = mFragments.get(i);
            final FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (i > 2) {
                ft.remove(fragment);
                mFragments.remove(i);
                i--;
            }
            ft.commit();
        }
        for (int i = 0; i < fragments.size(); i++) {
            if (i > 2) {
                mFragments.add(fragments.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }


    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
}
