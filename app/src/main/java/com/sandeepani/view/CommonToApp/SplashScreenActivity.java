package com.sandeepani.view.CommonToApp;

import android.content.Intent;
import android.os.Bundle;

import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.view.Parent.ParentHomeActivity;
import com.sandeepani.view.R;
import com.sandeepani.view.Teacher.TeacherHomeActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends BaseActivity {
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Pushbots.sharedInstance().init(SplashScreenActivity.this);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String access_token = StorageManager.readString(SplashScreenActivity.this, getString(R.string.pref_access_token), "");
                Intent intent = null;
                if (!access_token.equals("")) {
                    WebServiceCall.getToken = access_token;
                    String role = StorageManager.readString(SplashScreenActivity.this, getString(R.string.pref_role), "");
                    if (role.equals("ROLE_PARENT")) {
                        intent = new Intent(SplashScreenActivity.this, ParentHomeActivity.class);
                    } else {
                        intent = new Intent(SplashScreenActivity.this, TeacherHomeActivity.class);
                    }
                } else {
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
        timer = new Timer();
        timer.schedule(task, 2000);                        // timer is scheduled after 2 seconds
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
