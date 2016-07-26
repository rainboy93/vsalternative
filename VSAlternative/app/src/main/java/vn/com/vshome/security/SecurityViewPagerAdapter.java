package vn.com.vshome.security;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.com.vshome.user.UserActionFragment;
import vn.com.vshome.user.UserFragment;

/**
 * Created by anlab on 7/4/16.
 */
public class SecurityViewPagerAdapter extends FragmentStatePagerAdapter {

    public SecurityViewPagerAdapter(FragmentManager fm) {
        super(fm);
        userFragment = UserFragment.newInstance(0);
        userActionFragment = UserActionFragment.newInstance(1);
    }

    private static UserFragment userFragment;
    private static UserActionFragment userActionFragment;

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return userFragment;
        } else if (position == 1) {
            return userActionFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof UserFragment) {
            ((UserFragment) object).updateUserRow();
        } else if (object instanceof UserActionFragment) {

        }
        return super.getItemPosition(object);
    }

    public void updateListUser() {

    }
}
