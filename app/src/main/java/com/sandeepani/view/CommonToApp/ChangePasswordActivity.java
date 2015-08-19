package com.sandeepani.view.CommonToApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.sharedPreference.PrefManager;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar;
import com.sandeepani.view.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Antony on 23-06-2015.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, RequestCompletion {
    public static final String TAG = ChangePasswordActivity.class.getSimpleName();
    PrefManager sharedPref;
    private TopBar topBar;
    private EditText mOldPwdET, mNewPwdET, mConfirmNewPwdET;
    private Button mChangePwdBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        setTopBar();
        initView();
    }

    private void initView() {
        mOldPwdET = (EditText) findViewById(R.id.oldpwd_changepwd);
        mNewPwdET = (EditText) findViewById(R.id.newpwd_changepwd);
        mConfirmNewPwdET = (EditText) findViewById(R.id.newpwd_confirm_changepwd);
        mChangePwdBTN = (Button) findViewById(R.id.changepwdbtn);
        mChangePwdBTN.setOnClickListener(this);
    }

    private void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.changepwd));
        topBar.logoutIV.setOnClickListener(this);
        topBar.backArrowIV.setOnClickListener(this);

        ImageView notification = (ImageView) topBar.findViewById(R.id.notification);
        notification.setVisibility(View.VISIBLE);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePasswordActivity.this, NotificationActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changepwdbtn:
                if (mOldPwdET.getText().length() == 0 || mNewPwdET.getText().length() == 0 || mConfirmNewPwdET.getText().length() == 0) {
                    Constants.showMessage(this, "Sorry", "Please enter data in all fields");
                } else if (!mNewPwdET.getText().toString().equals(mConfirmNewPwdET.getText().toString())) {
                    Constants.showMessage(this, "Sorry", "Password mismatch");
                } else {
                    if (CommonUtils.isNetworkAvailable(this)) {
                        CommonUtils.getLogs("Clicked");
                        Constants.showProgress(ChangePasswordActivity.this);
                        WebServiceCall call = new WebServiceCall(ChangePasswordActivity.this);
                        call.changePasswordRequestApi(mOldPwdET.getText().toString(), mNewPwdET.getText().toString(), mConfirmNewPwdET.getText().toString());
                    } else {
                        CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
                    }
                }
                break;
            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            case R.id.logoutIV:
            SharedPreferences clearSharedPreferenceForLogout;
            clearSharedPreferenceForLogout = getSharedPreferences("MyChild_Preferences", 0);
            SharedPreferences.Editor editor = clearSharedPreferenceForLogout.edit();
            editor.clear();
            editor.commit();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);
        CommonUtils.getLogs("change pwd" + responseJson);
        CommonUtils.getLogs("change pwd" + responseArray);
        parseJson(responseJson);
        Log.i(TAG, responseJson.toString());
    }

    private void parseJson(JSONObject response) {
     boolean status=false;
        try {
            if (response != null) {
                if (response.has("status")) {
                    String str=response.getString("status");
                    if(str.equals("success")) {
                        status=true;
                        Constants.showMessage(this, "Done", getString(R.string.passwordchnagedsuccess));
                    }
                    else {
                        status=false;
                        Constants.showMessage(this, "Sorry","Authentication Error. Try again");
                    }
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void onRequestCompletionError(String error) {
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }
}
