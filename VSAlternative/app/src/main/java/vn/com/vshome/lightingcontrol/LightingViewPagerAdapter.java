package vn.com.vshome.lightingcontrol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.com.vshome.account.FragmentLogin;
import vn.com.vshome.account.FragmentSetting;

/**
 * Created by anlab on 7/4/16.
 */
public class LightingViewPagerAdapter extends FragmentStatePagerAdapter {

    public static final int FRAGMENT_LOGIN = 0;
    public static final int FRAGMENT_SETTING = 1;

    private DeviceFragment deviceFragment;
    private SceneFragment sceneFragment;

    public LightingViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (deviceFragment == null) {
                deviceFragment = DeviceFragment.newInstance(0, 0);
            }
            return deviceFragment;
        } else if (position == 1) {
            if (sceneFragment == null) {
                sceneFragment = SceneFragment.newInstance(0, 0);
            }
            return sceneFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
//        if (object instanceof FragmentSetting) {
//            ((FragmentSetting) object).refresh();
//        }
        return super.getItemPosition(object);
    }
}
