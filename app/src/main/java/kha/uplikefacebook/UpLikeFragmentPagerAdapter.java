package kha.uplikefacebook;

import android.graphics.drawable.Icon;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by Kha on 4/1/2016.
 */
public class UpLikeFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{

    final int PAGE_COUNT = 2;
    private int tabIcon[] ={R.drawable.ic_tab_like,R.drawable.ic_tab_settings2};

    public UpLikeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) return LoginToUpLikeFragment.newInstance();
        return SettingUpLikeFragment.newInstance();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcon[position];
    }
}
