package vn.com.vshome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import vn.com.vshome.lightingcontrol.LightingControlActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton mButtonLight, mButtonSecurity, mButtonSensor, mButtonSun, mButtonUser, mButtonAdvance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mButtonLight = (ImageButton) findViewById(R.id.main_button_light_control);
        mButtonSecurity = (ImageButton) findViewById(R.id.main_button_security);
        mButtonSensor = (ImageButton) findViewById(R.id.main_button_environment);
        mButtonSun = (ImageButton) findViewById(R.id.main_button_sun_energy);
        mButtonUser = (ImageButton) findViewById(R.id.main_button_user);
        mButtonAdvance = (ImageButton) findViewById(R.id.main_button_advance);

        mButtonLight.setOnClickListener(this);
        mButtonSecurity.setOnClickListener(this);
        mButtonSensor.setOnClickListener(this);
        mButtonSun.setOnClickListener(this);
        mButtonUser.setOnClickListener(this);
        mButtonAdvance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == mButtonLight) {
            intent = new Intent(this, LightingControlActivity.class);
        } else if (v == mButtonSecurity) {

        } else if (v == mButtonSensor) {

        } else if (v == mButtonSun) {

        } else if (v == mButtonUser) {

        } else if (v == mButtonAdvance) {

        }
        if(intent != null){
            startActivity(intent);
        }
    }
}
