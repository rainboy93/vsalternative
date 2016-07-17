package vn.com.vshome.lightingcontrol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.transition.Scene;

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

    private long floorId, roomId;

    public LightingViewPagerAdapter(FragmentManager fm, long floorId, long roomId) {
        super(fm);
        this.floorId = floorId;
        this.roomId = roomId;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (deviceFragment == null) {
                deviceFragment = DeviceFragment.newInstance(floorId, roomId);
            }
            return deviceFragment;
        } else if (position == 1) {
            if (sceneFragment == null) {
                sceneFragment = SceneFragment.newInstance(floorId, roomId);
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
        if (object instanceof DeviceFragment) {
            ((DeviceFragment) object).resetData(floorId, roomId);
        } else if(object instanceof SceneFragment){
            ((SceneFragment) object).resetData(floorId, roomId);
        }
        return super.getItemPosition(object);
    }

    public void resetData(long floorId, long roomId){
        this.floorId = floorId;
        this.roomId = roomId;
    }
}
