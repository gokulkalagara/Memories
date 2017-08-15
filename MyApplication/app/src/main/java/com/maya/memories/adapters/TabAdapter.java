package com.maya.memories.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maya.memories.fragments.DescriptionImageViewFragment;

/**
 * Created by home on 7/25/2017.
 */

public class TabAdapter extends FragmentPagerAdapter
{
    public TabAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return DescriptionImageViewFragment.newInstance(null,"1");

            case 1:
                return DescriptionImageViewFragment.newInstance(null,"2");

            case 2:
                return DescriptionImageViewFragment.newInstance(null,"3");


        }
        return null;

    }

    @Override
    public int getCount() {
        return 3;
    }
}

