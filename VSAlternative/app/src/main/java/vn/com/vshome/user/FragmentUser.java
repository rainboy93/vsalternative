package vn.com.vshome.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import in.workarounds.typography.Button;
import in.workarounds.typography.CheckBox;
import in.workarounds.typography.EditText;
import in.workarounds.typography.TextView;
import vn.com.vshome.MainActivity;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.account.LoginActivity;
import vn.com.vshome.callback.LoginCallback;
import vn.com.vshome.database.Camera;
import vn.com.vshome.database.Floor;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.database.Room;
import vn.com.vshome.database.Scene;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.networks.ReturnMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.PasswordFlowerMask;
import vn.com.vshome.utils.PreferenceDefine;
import vn.com.vshome.utils.PreferenceUtils;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ProgressHUD;

/**
 * Created by anlab on 7/4/16.
 */
public class FragmentUser extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;

    public FragmentUser() {
    }

    public static FragmentUser newInstance(int sectionNumber) {
        FragmentUser fragment = new FragmentUser();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.user_recycler_view);
    }


    @Override
    public void onClick(View v) {

    }
}