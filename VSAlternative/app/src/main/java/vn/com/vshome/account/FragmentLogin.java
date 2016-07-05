package vn.com.vshome.account;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.Arrays;

import in.workarounds.typography.Button;
import in.workarounds.typography.CheckBox;
import in.workarounds.typography.EditText;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.networks.ReturnMessage;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.PasswordFlowerMask;
import vn.com.vshome.utils.PreferenceDefine;
import vn.com.vshome.utils.PreferenceUtils;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ProgressHUD;

/**
 * Created by anlab on 7/4/16.
 */
public class FragmentLogin extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText mUsername, mPassword;
    private CheckBox mRememberPassword;
    private Button mLoginButton;
    private ImageButton mSettingButton;
    private TextView mForgotPassword;
    private PasswordFlowerMask mPasswordMask;


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
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mUsername = (EditText) view.findViewById(R.id.login_username);
        mPassword = (EditText) view.findViewById(R.id.login_password);
        if (mPasswordMask == null) {
            mPasswordMask = new PasswordFlowerMask();
        }
        mPassword.setTransformationMethod(mPasswordMask);

        mLoginButton = (Button) view.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
        mSettingButton = (ImageButton) view.findViewById(R.id.login_setting_button);
        mSettingButton.setOnClickListener(this);
        mRememberPassword = (CheckBox) view.findViewById(R.id.login_remember_password);

        String username = PreferenceUtils.getInstance(getActivity()).getValue(PreferenceDefine.USERNAME, "");
        String password = PreferenceUtils.getInstance(getActivity()).getValue(PreferenceDefine.PASSWORD, "");

        if (!username.equals("")) {
            mUsername.setText(username);
        }
        if (!password.equals("")) {
            mRememberPassword.setChecked(true);
            mPassword.setText(password);
        }

        mForgotPassword = (TextView) view.findViewById(R.id.login_forgot_password);
        mForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginButton) {
            String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();
            if (!isValidUsername(username)
                    || !isValidPassword(password)) {
                Utils.showErrorDialog(R.string.txt_error,
                        R.string.txt_Invalid_User, getActivity());
                return;
            }

            boolean isNetConfigured = PreferenceUtils.getInstance(getActivity()).getValue(PreferenceDefine.NET_CONFIGURED, false);
            if (!isNetConfigured) {
                Utils.showErrorDialog(R.string.txt_error,
                        R.string.txt_network_not_config, getActivity());
                return;
            }

            new LoginTask(username, password).execute("");
        } else if (v == mForgotPassword) {
            Utils.showErrorDialog("Quên mật khẩu",
                    "Hãy liên hệ với admin để lấy lại mật khẩu.\n"
                            + "Hoặc liên hệ với đội HTKH của VSHome theo hotline: 0432252206.", getActivity());
        } else if (v == mSettingButton) {
            ((LoginActivity) getActivity()).changeToSetting();
        }
    }

    private boolean isValidUsername(String username) {
        if (username.length() == 0 || username.length() > 20) {
            return false;
        }

        boolean bool = true;

        String specialChars = "/*!@#$%^&*()\"{}[]|\\?/<>,";
        for (int i = 0; i < username.length(); i++) {
            if (specialChars.contains(username.substring(i, i + 1))) {

                bool = false;
                break;
            }
        }

        return bool;
    }

    private boolean isValidPassword(String password) {
        if (password.length() == 0 || password.length() > 10) {
            return false;
        }
        boolean bool = true;

        String specialChars = "/*!@#$%^&*()\"{}[]|\\?/<>,";
        for (int i = 0; i < password.length(); i++) {
            if (specialChars.contains(password.substring(i, i + 1))) {

                bool = false;
                break;
            }
        }

        return bool;
    }

    class LoginTask extends AsyncTask<String, String, String> {

        private String username, password;

        public LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            ProgressHUD.showLoading(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            if (VSHome.socketManager.canConnect(getActivity(), 0)
                    || VSHome.socketManager.canConnect(getActivity(), 1)) {
                login();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            ProgressHUD.hideLoading(getActivity());
        }

        private void saveUserInfo() {
            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.USERNAME, mUsername.getText().toString());
            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.PASSWORD, mPassword.getText().toString());
        }

        private void login() {
            Logger.LogD("Start login process");
            CommandMessage loginCommand = new CommandMessage();
            loginCommand.setLoginMessage(username, password);

            byte[] b = loginCommand.getByteArray();
            byte[] buffer_login = new byte[1000];
            byte[] buffer_conf = new byte[10];

            try {
                VSHome.socketManager.outputStream.write(b);

                while (true) {
                    if (VSHome.socketManager.inputStream.available() >= 402) {
                        VSHome.socketManager.inputStream.read(buffer_login, 0, 402);
                        Logger.LogD(Arrays.toString(buffer_login));
                        break;
                    }
                    SystemClock.sleep(5);
                }

                ReturnMessage ret = new ReturnMessage(buffer_login);

                if (ret.cmd == CommandMessage.CMD_LOGIN && ret.status == CommandMessage.STATUS_OK) {
                    saveUserInfo();

                    VSHome.userPriority = ret.data[1];

                    while (true) {
                        if (VSHome.socketManager.inputStream.available() > 0) {
                            if (PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.IP_PORT, 0) == 2002
                                    || PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.DNS_PORT, 0) == 2002) {
                                VSHome.socketManager.inputStream.read(buffer_conf, 0, 9);
                            } else {
                                VSHome.socketManager.inputStream.read(buffer_conf, 0, 8);
                            }
                            break;
                        }
                    }

                    final int uid = (Utils.Byte2Unsigned(buffer_conf[0]) << 24)
                            + (Utils.Byte2Unsigned(buffer_conf[1]) << 16)
                            + (Utils.Byte2Unsigned(buffer_conf[2]) << 8)
                            + Utils.Byte2Unsigned(buffer_conf[3]);
                    int configureID = 0;
                    if (PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.IP_PORT, 0) == 2002
                            || PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.DNS_PORT, 0) == 2002) {
                        configureID = Utils.Byte2Unsigned(buffer_conf[8]);
                    }

                    if (uid != PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.UID, 0)
                            || net.the4thdimension.android.Utils.getApplicationVersionCode(getActivity())
                            != PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.VERSION_CODE, 0)) {
                        VSHome.socketManager.outputStream.flush();
                        VSHome.socketManager.outputStream.write(1);

                        int mDataLength = (Utils.Byte2Unsigned(buffer_conf[4]) << 24)
                                + (Utils.Byte2Unsigned(buffer_conf[5]) << 16)
                                + (Utils.Byte2Unsigned(buffer_conf[6]) << 8)
                                + Utils.Byte2Unsigned(buffer_conf[7]);
                        byte[] mBufferData = new byte[mDataLength];
                        int offset = 0;
                        while (mDataLength > 0) {
                            int available = VSHome.socketManager.inputStream.available();
                            VSHome.socketManager.inputStream.read(mBufferData, offset, available);
                            offset += available;
                            mDataLength -= available;
                        }

                        Logger.LogD(Arrays.toString(mBufferData));
                    } else {
                        VSHome.socketManager.outputStream.flush();
                        VSHome.socketManager.outputStream.write(0);
                    }

                    VSHome.socketManager.outputStream.write(1);
                    VSHome.socketManager.outputStream.write(1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}