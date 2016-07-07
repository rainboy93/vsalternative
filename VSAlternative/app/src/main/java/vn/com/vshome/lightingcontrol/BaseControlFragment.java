package vn.com.vshome.lightingcontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by anlab on 7/7/16.
 */
public abstract class BaseControlFragment extends Fragment{

    public abstract void freshData(int floorID, int roomID);
}
