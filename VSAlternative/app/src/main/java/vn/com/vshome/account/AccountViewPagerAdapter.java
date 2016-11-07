package vn.com.vshome.account;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by anlab on 7/4/16.
 */
public class AccountViewPagerAdapter extends FragmentStatePagerAdapter {

    public AccountViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FragmentLogin.newInstance(position);
        } else if (position == 1) {
            return FragmentSetting.newInstance(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof FragmentSetting) {
            ((FragmentSetting) object).refresh();
        }
        return super.getItemPosition(object);
    }
}
