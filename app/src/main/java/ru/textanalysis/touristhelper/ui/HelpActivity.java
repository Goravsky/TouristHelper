package ru.textanalysis.touristhelper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import ru.textanalysis.touristhelper.R;

public class HelpActivity extends FragmentActivity {

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[];
        private Context context;

        public ViewPagerAdapter(FragmentManager fm, Context cntxt) {
            super(fm);
            context = cntxt;
            tabTitles = new String[] {context.getString(R.string.about_button),
                    context.getString(R.string.guide_button)};
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AboutFragment();
                case 1:
                    return new InstructionFragment();
                default:
                    return new AboutFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        tabLayout = findViewById(R.id.tablayout);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.setupWithViewPager(viewPager);
    }

}
