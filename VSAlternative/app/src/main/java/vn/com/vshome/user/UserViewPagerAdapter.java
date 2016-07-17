package vn.com.vshome.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.com.vshome.lightingcontrol.DeviceFragment;
import vn.com.vshome.lightingcontrol.SceneFragment;

/**
 * Created by anlab on 7/4/16.
 */
public class UserViewPagerAdapter extends FragmentStatePagerAdapter {

    public UserViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private FragmentUser fragmentUser;

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            fragmentUser = FragmentUser.newInstance(position);
            return fragmentUser;
        } else if(position == 1){

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
//        if (object instanceof DeviceFragment) {
//            ((DeviceFragment) object).resetData(floorId, roomId);
//        } else if(object instanceof SceneFragment){
//            ((SceneFragment) object).resetData(floorId, roomId);
//        }
        return super.getItemPosition(object);
    }
}
