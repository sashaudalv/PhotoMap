package com.alexdev.photomap.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;

import com.alexdev.photomap.R;
import com.alexdev.photomap.ui.favorites.FavoritesFragment;
import com.alexdev.photomap.ui.interfaces.ReselectableFragment;
import com.alexdev.photomap.ui.map.MapFragment;
import com.alexdev.photomap.ui.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    BottomNavigationViewPager mViewPager;
    @BindView(R.id.navigation)
    BottomNavigationView mBottomNavigationView;
    private FragmentNavPagerAdapter mPagerAdapter;
    private final SparseIntArray mFragmentsIdMap = new SparseIntArray();

    {
        mFragmentsIdMap.put(R.id.navigation_map, 0);
        mFragmentsIdMap.put(R.id.navigation_favorites, 1);
        mFragmentsIdMap.put(R.id.navigation_settings, 2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViewPager(savedInstanceState);
        setBottomNavigationViewListeners();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(
                outState,
                MapFragment.class.getName(),
                mPagerAdapter.getItem(mFragmentsIdMap.get(R.id.navigation_map))
        );
        getSupportFragmentManager().putFragment(
                outState,
                FavoritesFragment.class.getName(),
                mPagerAdapter.getItem(mFragmentsIdMap.get(R.id.navigation_favorites))
        );
    }

    private void initViewPager(Bundle savedInstanceState) {
        mPagerAdapter = new FragmentNavPagerAdapter(getSupportFragmentManager());
        if (savedInstanceState == null) {
            mPagerAdapter.addFragment(MapFragment.newInstance());
            mPagerAdapter.addFragment(FavoritesFragment.newInstance());
        } else {
            mPagerAdapter.addFragment(
                    getSupportFragmentManager().getFragment(
                            savedInstanceState,
                            MapFragment.class.getName()
                    )
            );
            mPagerAdapter.addFragment(
                    getSupportFragmentManager().getFragment(
                            savedInstanceState,
                            FavoritesFragment.class.getName()
                    )
            );
        }
        mPagerAdapter.addFragment(SettingsFragment.newInstance());
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
    }

    private void setBottomNavigationViewListeners() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (mFragmentsIdMap.get(item.getItemId(), -1) >= 0) {
                mViewPager.setCurrentItem(mFragmentsIdMap.get(item.getItemId()), false);
                return true;
            }

            return false;
        });

        mBottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            Fragment fragment = mPagerAdapter.getItem(mFragmentsIdMap.get(item.getItemId()));
            if (fragment instanceof ReselectableFragment)
                ((ReselectableFragment) fragment).onFragmentReselected();
        });
    }

}
