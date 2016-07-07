package vn.com.vshome.account;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
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
import vn.com.vshome.callback.LoginCallback;
import vn.com.vshome.database.TblScene;
import vn.com.vshome.database.model.CameraDevice;
import vn.com.vshome.database.model.Floor;
import vn.com.vshome.database.model.LightingDevice;
import vn.com.vshome.database.model.Room;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.networks.ReturnMessage;
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
public class FragmentLogin extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText mUsername, mPassword;
    private CheckBox mRememberPassword;
    private Button mLoginButton;
    private ImageButton mSettingButton;
    private TextView mForgotPassword;
    private PasswordFlowerMask mPasswordMask;
    private LoginTask loginTask;

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

            loginTask = new LoginTask(username, password);
            loginTask.execute("");
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

    class LoginTask extends AsyncTask<String, String, Integer> {

        private String username, password;

        public LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            TimeOutManager.getInstance().startCountDown(new TimeOutManager.TimeOutCallback() {
                @Override
                public void onTimeOut() {
                    ProgressHUD.hideLoading(getActivity());
                }
            }, 15);
            ProgressHUD.showLoading(getActivity());
        }

        @Override
        protected Integer doInBackground(String... params) {
            int login = 0;
            if (VSHome.socketManager.canConnect(getActivity(), 0)
                    || VSHome.socketManager.canConnect(getActivity(), 1)) {
                login = login();
            }
            return login;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == CommandMessage.STATUS_OK){

            } else {
                TimeOutManager.getInstance().cancelCountDown();
                ProgressHUD.hideLoading(getActivity());
                VSHome.socketManager.destroySocket();
                switch (integer) {
                    case 0:
                        Utils.showErrorDialog(R.string.txt_error, R.string.txt_main_connect_problem, getActivity());
                        break;
                    case 1:
                        Utils.showErrorDialog(R.string.txt_error, R.string.txt_disable_user, getActivity());
                        break;
                    case 2:
                        Utils.showErrorDialog(R.string.txt_error, R.string.txt_main_not_accept_connection, getActivity());
                        break;
                    case 3:
                        Utils.showErrorDialog(R.string.txt_error, R.string.txt_auth_fail, getActivity());
                        break;
                    case 4:
                        Utils.showErrorDialog(R.string.txt_error, R.string.txt_max_connection, getActivity());
                        break;
                    case 5:
                        Utils.showErrorDialog(R.string.txt_error, R.string.txt_admin_connecting, getActivity());
                        break;
                    default:
                        Utils.showErrorDialog(R.string.txt_error, R.string.txt_system_error, getActivity());
                        break;
                }
            }
        }

        private void saveUserInfo() {
            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.USERNAME, mUsername.getText().toString());
            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.PASSWORD, mPassword.getText().toString());
        }

        private ArrayList<Floor> mListFloor;

        private int login() {
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
                            VSHome.socketManager.inputStream.read(buffer_conf, 0, 9);
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
                    int lastUID = PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.UID, 0);
                    int lastConfigID = PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.CONFIGURE_ID, 0);

                    if (uid != lastUID || configureID != lastConfigID
                            || net.the4thdimension.android.Utils.getApplicationVersionCode(getActivity())
                            != PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.VERSION_CODE, 0)) {
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

                        processDeviceData(mBufferData);
                        Utils.putDatabase(getActivity(), mListFloor, uid);

                    } else {
                        VSHome.socketManager.outputStream.write(0);
                    }

                    VSHome.socketManager.outputStream.write(1);
                    int count = 1;
                    while (count != 0) {
                        int available = VSHome.socketManager.inputStream.available();
                        if (available > 0) {
                            VSHome.socketManager.inputStream.read(buffer_conf, 0, 10);
                            count--;
                        }
                    }

                    int lastSceneDBVersion = PreferenceUtils.getInstance(getActivity()).getIntValue(PreferenceDefine.SCENE_DB_VERSION, 0);
                    int sceneDBVersion = Utils.Byte2Unsigned(buffer_conf[0]);
                    VSHome.socketManager.startCommunication(new LoginCallback() {
                        @Override
                        public void onLoginSuccess() {
                            Logger.LogD("Login success");
                            TimeOutManager.getInstance().cancelCountDown();
                            ProgressHUD.hideLoading(getActivity());
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            Toaster.showMessage(getActivity(), getResources().getString(R.string.foscam_login_promote_success));
                            getActivity().finishAffinity();
                            VSHome.socketManager.removeLoginCallback();
                        }
                    });
                    if(sceneDBVersion != lastSceneDBVersion){
                        TblScene.deleteAll(TblScene.class);
                        VSHome.socketManager.outputStream.write(1);
                    } else {
                        VSHome.socketManager.outputStream.write(0);
                    }
                    return CommandMessage.STATUS_OK;
                } else  if(ret.cmd == CommandMessage.CMD_LOGIN && ret.status == CommandMessage.STATUS_ERROR){
                    return ret.data[0];
                } else if(ret.cmd == CommandMessage.CMD_ERROR_MESSAGE){
                    return 4;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private void processDeviceData(byte[] mBufferData) {
            mListFloor = new ArrayList<>();
            int index = 0;
            int mNumberFloor = mBufferData[index];
            index++;
            while (mNumberFloor > 0) {
                Floor floor = new Floor();
                int mFloorId = mBufferData[index];
                floor.id = mFloorId;
                index++;
                byte[] bFloorName = Arrays.copyOfRange(mBufferData, index,
                        index + 49);
                String mFloorName = new String(bFloorName);
                for (int i = 0; i < mFloorName.length(); i++) {
                    if (mFloorName.charAt(i) == '\0') {
                        mFloorName = mFloorName.substring(0, i).trim();
                        break;
                    }
                }
                floor.name = mFloorName;
                index += 50;

                int mNumberRoom = mBufferData[index];
                index++;
                while (mNumberRoom > 0) {

                    Room room = new Room();

                    int mRoomId = mBufferData[index];
                    room.id = mRoomId;
                    index += 2;

                    byte[] b2 = Arrays.copyOfRange(mBufferData, index, index + 49);
                    String mRoomName = new String(b2);
                    for (int i = 0; i < mRoomName.length(); i++) {
                        if (mRoomName.charAt(i) == '\0') {
                            mRoomName = mRoomName.substring(0, i);
                            break;
                        }
                    }
                    room.name = mRoomName;
                    index = index + 50;

                    room.floorID = mFloorId;

                    int mNumberLightingDevice = mBufferData[index];
                    index++;

                    while (mNumberLightingDevice > 0) {

                        LightingDevice device = new LightingDevice();

                        device.room_id = mRoomId;

                        int mLightingId = (Utils
                                .Byte2Unsigned((byte) mBufferData[index]) << 8)
                                + Utils.Byte2Unsigned((byte) mBufferData[index + 1]);
                        device.id = mLightingId;
                        index += 2;

                        int mDeviceType = mBufferData[index];
                        device.type_id = mDeviceType;
                        index += 2;

                        byte[] b3 = Arrays.copyOfRange(mBufferData, index,
                                index + 49);
                        String mDeviceName = new String(b3);
                        for (int i = 0; i < mDeviceName.length(); i++) {
                            if (mDeviceName.charAt(i) == '\0') {
                                mDeviceName = mDeviceName.substring(0, i);
                                break;
                            }
                        }
                        device.name = mDeviceName;
                        index += 50;

                        int mStateId = (Utils
                                .Byte2Unsigned((byte) mBufferData[index]) << 8)
                                + Utils.Byte2Unsigned((byte) mBufferData[index + 1]);
                        device.state_id = mStateId;
                        index += 2;

                        int mLCId = mBufferData[index];
                        device.lc_id = mLCId;
                        index++;

                        int mDevId = mBufferData[index];
                        device.dev_id = mDevId;
                        index++;

                        int mDeviceChannel = mBufferData[index];
                        device.channel = mDeviceChannel;
                        index++;
//                        device.setDeviceState(new LightingDeviceState());

                        room.devices.add(device);
                        mNumberLightingDevice--;
                    }

                    int mNumberSecurityDevice = mBufferData[index];
                    index++;
                    while (mNumberSecurityDevice > 0) {
                        int mSecurityId = (int) (mBufferData[index] << 8)
                                + mBufferData[index + 1];
                        index += 2;

                        int mSecurityType = mBufferData[index];
                        index++;

                        // RoomId
                        index++;

                        byte[] b4 = Arrays.copyOfRange(mBufferData, index,
                                index + 49);
                        String mSecurityName = new String(b4);
                        for (int i = 0; i < mSecurityName.length(); i++) {
                            if (mSecurityName.charAt(i) == '\0') {
                                mSecurityName = mSecurityName.substring(0, i);
                                break;
                            }
                        }
                        index += 50;

                        int mSecurityStateId = (int) (mBufferData[index] << 8)
                                + mBufferData[index + 1];
                        index += 2;

                        int mSecuritySCId = mBufferData[index];
                        index++;

                        int mSecurityDevId = mBufferData[index];
                        index++;

                        int mSecurityChannel = mBufferData[index];
                        index++;

                        mNumberSecurityDevice--;
                    }

                    int mNumberOfCamera = mBufferData[index];
                    index++;

                    while (mNumberOfCamera > 0) {
                        CameraDevice foscam = new CameraDevice();
                        int mCameraId = mBufferData[index];
                        foscam.id = mCameraId;
                        index += 1;

                        int mCameraType = mBufferData[index];
                        foscam.device_type = mCameraType;
                        index += 2;

                        foscam.room_id = mRoomId;

                        byte[] b5 = Arrays.copyOfRange(mBufferData, index,
                                index + 49);
                        String mCameraName = new String(b5);
                        for (int i = 0; i < mCameraName.length(); i++) {
                            if (mCameraName.charAt(i) == '\0') {
                                mCameraName = mCameraName.substring(0, i);
                                break;
                            }
                        }
                        foscam.device_name = mCameraName;
                        index += 50;

                        byte[] b6 = Arrays.copyOfRange(mBufferData, index,
                                index + 19);
                        String mCameraAddress = new String(b6);
                        for (int i = 0; i < mCameraAddress.length(); i++) {
                            if (mCameraAddress.charAt(i) == '\0') {
                                mCameraAddress = mCameraAddress.substring(0, i);
                                break;
                            }
                        }
                        foscam.ip_address = mCameraAddress;
                        index += 20;

                        int mCameraLocalPort = (Utils
                                .Byte2Unsigned((byte) mBufferData[index]) << 8)
                                + (Utils.Byte2Unsigned((byte) mBufferData[index + 1]));
                        foscam.local_port = mCameraLocalPort;
                        index += 2;

                        byte[] b7 = Arrays.copyOfRange(mBufferData, index,
                                index + 49);
                        String mCameraDNSAddress = new String(b7);
                        for (int i = 0; i < mCameraDNSAddress.length(); i++) {
                            if (mCameraDNSAddress.charAt(i) == '\0') {
                                mCameraDNSAddress = mCameraDNSAddress.substring(0,
                                        i);
                                break;
                            }
                        }
                        foscam.dns_address = mCameraDNSAddress;
                        index += 50;

                        int mCameraDNSPort = (Utils
                                .Byte2Unsigned((byte) mBufferData[index]) << 8)
                                + (Utils.Byte2Unsigned((byte) mBufferData[index + 1]));
                        index += 2;
                        foscam.web_port = mCameraDNSPort;

                        byte[] b8 = Arrays.copyOfRange(mBufferData, index,
                                index + 19);
                        String mCameraUsername = new String(b8);
                        for (int i = 0; i < mCameraUsername.length(); i++) {
                            if (mCameraUsername.charAt(i) == '\0') {
                                mCameraUsername = mCameraUsername.substring(0, i);
                                break;
                            }
                        }
                        index += 20;
                        foscam.username = mCameraUsername;

                        byte[] b9 = Arrays.copyOfRange(mBufferData, index,
                                index + 19);
                        String mCameraPassword = new String(b9);
                        for (int i = 0; i < mCameraPassword.length(); i++) {
                            if (mCameraPassword.charAt(i) == '\0') {
                                mCameraPassword = mCameraPassword.substring(0, i);
                                break;
                            }
                        }
                        index += 20;
                        foscam.password = mCameraPassword;
                        room.foscams.add(foscam);
                        mNumberOfCamera--;
                    }

                    int mNumberOfSensor = mBufferData[index];
                    index++;

                    if (mNumberOfSensor == 1) {
                        index++;
                        byte[] b10 = Arrays.copyOfRange(mBufferData, index,
                                index + 19);
                        String mSensorIP = new String(b10);
                        for (int i = 0; i < mSensorIP.length(); i++) {
                            if (mSensorIP.charAt(i) == '\0') {
                                mSensorIP = mSensorIP.substring(0, i);
                                break;
                            }
                        }
                        index += 20;

                        int mSensorLocalPort = (int) (mBufferData[index] << 8)
                                + mBufferData[index + 1];
                        index += 2;

                        byte[] b11 = Arrays.copyOfRange(mBufferData, index,
                                index + 49);
                        String mSensorDNSAddress = new String(b11);
                        for (int i = 0; i < mSensorDNSAddress.length(); i++) {
                            if (mSensorDNSAddress.charAt(i) == '\0') {
                                mSensorDNSAddress = mSensorDNSAddress.substring(0,
                                        i);
                                break;
                            }
                        }
                        index += 50;

                        int mSensorDNSPort = (int) (mBufferData[index] << 8)
                                + mBufferData[index + 1];
                        index += 2;

                    }
                    floor.rooms.add(room);
                    mNumberRoom--;
                }
                mListFloor.add(floor);
                mNumberFloor--;
                // Preference.setUID(getActivity(), UID);
            }
        }
    }
}