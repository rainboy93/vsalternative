package vn.com.vshome.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;
import java.net.URISyntaxException;

import in.workarounds.typography.Button;
import in.workarounds.typography.EditText;
import vn.com.vshome.R;
import vn.com.vshome.utils.PreferenceDefine;
import vn.com.vshome.utils.PreferenceUtils;
import vn.com.vshome.utils.Utils;

/**
 * Created by anlab on 7/4/16.
 */
public class FragmentSetting extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText mIP, mIPPort, mDNS, mDNSPort;
    private Button mSave, mCancel;

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
        View rootView = inflater.inflate(R.layout.login_setting_fragment, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View view) {
        mIP = (EditText) view.findViewById(R.id.login_setting_ip);
        mIPPort = (EditText) view.findViewById(R.id.login_setting_ip_port);
        mDNS = (EditText) view.findViewById(R.id.login_setting_dns);
        mDNSPort = (EditText) view.findViewById(R.id.login_setting_dns_port);

        refresh();


        mSave = (Button) view.findViewById(R.id.login_setting_button_save);
        mCancel = (Button) view.findViewById(R.id.login_setting_button_cancel);

        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSave) {
            if (!isValidIP4Address(mIP.getText().toString())) {
                Utils.showErrorDialog(getTextFromResource(R.string.txt_error),
                        getTextFromResource(R.string.txt_lan_ip_error));
                mIP.requestFocus();
                return;
            }

            if (!isValidHostNameSyntax(mDNS.getText()
                    .toString())) {
                if (!isValidIP4Address(mDNS.getText()
                        .toString())) {
                    Utils.showErrorDialog(getTextFromResource(R.string.txt_error),
                            getTextFromResource(R.string.txt_wan_addr_error));
                    mDNS.requestFocus();
                    return;
                }
            }

            if (((Integer.parseInt(mIPPort.getText().toString())) > 65535)
                    || ((Integer.parseInt(mIPPort.getText().toString())) == 0)) {
                Utils.showErrorDialog(getTextFromResource(R.string.txt_error),
                        getTextFromResource(R.string.txt_lan_port_error));
                mIPPort.requestFocus();
                return;
            }

            if (((Integer.parseInt(mDNSPort.getText().toString())) > 65535)
                    || ((Integer.parseInt(mDNSPort.getText().toString())) == 0)) {
                Utils.showErrorDialog(getTextFromResource(R.string.txt_error),
                        getTextFromResource(R.string.txt_wan_port_error));
                mDNSPort.requestFocus();
                return;
            }

            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.IP_ADDRESS,
                    mIP.getText().toString());
            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.DNS_ADDRESS,
                    mDNS.getText().toString());
            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.IP_PORT,
                    Integer.parseInt(mIPPort.getText().toString()));
            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.DNS_PORT,
                    Integer.parseInt(mDNSPort.getText().toString()));

            PreferenceUtils.getInstance(getActivity()).setValue(PreferenceDefine.NET_CONFIGURED, true);
            getActivity().onBackPressed();
        } else if (v == mCancel) {
            getActivity().onBackPressed();
        }

    }

    private String getTextFromResource(int id) {
        return getResources().getString(id);
    }

    private boolean isValidIP4Address(String ipAddress) {
        if (ipAddress
                .matches("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")) {
            String[] groups = ipAddress.split("\\.");

            for (int i = 0; i <= 3; i++) {
                String segment = groups[i];
                if (segment == null || segment.length() <= 0) {
                    return false;
                }

                int value = 0;
                try {
                    value = Integer.parseInt(segment);
                } catch (NumberFormatException e) {
                    return false;
                }
                if (value > 255) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValidHostNameSyntax(String candidateHost) {
        if (candidateHost.contains("/")) {
            return false;
        }
        try {
            return new URI("my://userinfo@" + candidateHost + ":80").getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public void refresh(){
        String ip = PreferenceUtils.getInstance(getActivity())
                .getValue(PreferenceDefine.IP_ADDRESS, "");
        int ipPort = PreferenceUtils.getInstance(getActivity())
                .getIntValue(PreferenceDefine.IP_PORT, 0);
        String dns = PreferenceUtils.getInstance(getActivity())
                .getValue(PreferenceDefine.DNS_ADDRESS, "");
        int dnsPort = PreferenceUtils.getInstance(getActivity())
                .getIntValue(PreferenceDefine.DNS_PORT, 0);

        if (!ip.equals("")) {
            mIP.setText(ip);
        }

        if (ipPort != 0) {
            mIPPort.setText(ipPort + "");
        }

        if (!dns.equals("")) {
            mDNS.setText(dns);
        }

        if (dnsPort != 0) {
            mDNSPort.setText(dnsPort + "");
        }
    }
}
