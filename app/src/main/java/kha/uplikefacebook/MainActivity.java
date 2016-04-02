package kha.uplikefacebook;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabStrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new UpLikeFragmentPagerAdapter(getSupportFragmentManager()));

        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        mTabStrip.setViewPager(mViewPager);
    }
}
