package vn.com.vshome.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.vshome.R;

/**
 * Created by anlab on 7/4/16.
 */
public class FragmentLogin extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentLogin() {
    }

    public static FragmentLogin newInstance(int sectionNumber) {
        FragmentLogin fragment = new FragmentLogin();
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
