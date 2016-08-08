package vn.com.vshome.security;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by anlab on 7/4/16.
 */
public class SecurityViewPagerAdapter extends FragmentStatePagerAdapter {

    private SecurityFragment securityFragment;

    public SecurityViewPagerAdapter(FragmentManager fm) {
        super(fm);
        securityFragment = SecurityFragment.newInstance(0);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return securityFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
