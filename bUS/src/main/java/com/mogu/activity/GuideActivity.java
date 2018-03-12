package com.mogu.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;

import com.mogu.fragment.GuideFragment;
import com.mogu.utils.ViewPagerIndicator;

public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private FragmentManager mFragments;
    private ViewPagerIndicator mViewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.viewPagerIndicator);
        mViewPagerIndicator.setNum(GuideFragment.imgsRes.length);
        mFragments = getSupportFragmentManager();
        MyFragmentPagerAdapter myFragemnt = new MyFragmentPagerAdapter(
                mFragments);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            public void onPageSelected(int arg0) {

            }

            public void onPageScrolled(int position, float perc, int arg2) {
                mViewPagerIndicator.move(position, perc);

            }

            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mViewPager.setAdapter(myFragemnt);
    }

    static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager mFragments) {
            super(mFragments);
        }

        public Fragment getItem(int position) {
            GuideFragment guideFragment = new GuideFragment();
            if (position == getCount() - 1) {
                guideFragment.setVisible();
            }
            if (position == getCount() - 2) {
                guideFragment.setVisible3();
            }
            if (position == getCount() - 3) {
                guideFragment.setVisible2();
            }
            if (position == getCount() - 4) {
                guideFragment.setVisible1();
            }
            guideFragment.setImage(position);

            return guideFragment;
        }

        @Override
        public int getCount() {
            return GuideFragment.imgsRes.length;
        }
    }
}
