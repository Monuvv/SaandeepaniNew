package com.sandeepani.view.CommonToApp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.sandeepani.threads.HttpConnectThread;
import com.sandeepani.utils.CustomHandler;
import com.pushbots.push.Pushbots;


/**
 * Created by Sandeep on 17-03-2015.
 */
public class BaseFragmentActivity extends FragmentActivity {
    public HttpConnectThread httpConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(CustomHandler.class);
    }
}

