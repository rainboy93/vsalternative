package vn.com.vshome.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.com.vshome.R;

/**
 * Created by anlab on 7/4/16.
 */
public class FragmentSetting extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentSetting() {
    }

    public static FragmentSetting newInstance(int sectionNumber) {
        FragmentSetting fragment = new FragmentSetting();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }
}
