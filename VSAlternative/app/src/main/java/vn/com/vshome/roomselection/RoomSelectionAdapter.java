package vn.com.vshome.roomselection;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import vn.com.vshome.database.Floor;

/**
 * Created by anlab on 7/22/16.
 */
public class RoomSelectionAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Floor> mListFloor;

    public RoomSelectionAdapter(FragmentManager fm) {
        super(fm);
        mListFloor = new ArrayList<>();
        List<Floor> list = Floor.listAll(Floor.class, "id");
        if (list != null && list.size() > 0) {
            mListFloor.addAll(list);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return RoomSelectionFragment.newInstance(mListFloor.get(position).getId().intValue());
    }

    @Override
    public int getCount() {
        return mListFloor.size();
    }
}
