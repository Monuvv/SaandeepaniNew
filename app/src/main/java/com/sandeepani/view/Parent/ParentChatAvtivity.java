package com.sandeepani.view.Parent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.sharedPreference.PrefManager;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseActivity;
import com.sandeepani.view.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vijay on 3/29/15.
 */
public class ParentChatAvtivity extends BaseActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentChatAvtivity.class.getSimpleName();
    private TopBar1 topBar;
    private SwitchChildView switchChild;
    PrefManager sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new PrefManager(this);
        getChatHistoryWebserviceCall();
        setContentView(R.layout.activity_parent_chat);
        setTopBar();
        switchChildBar();
    }


    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Chat Response success");
        Log.i(TAG, responseJson.toString());
        //ArrayList<HashMap<String, String>> chatList = ParentChatHistoryParser.getInstance().getTeacherListForChat(responseJson);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Chat Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.chat));
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.childNameTV.setText("Name");
    }

    public void getChatHistoryWebserviceCall() {
        String Url_ChatHistory = null;

        if (CommonUtils.isNetworkAvailable(this)) {
            //SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if (!StorageManager.readString(this, "username", "").isEmpty()) {
                Url_ChatHistory = getString(R.string.base_url) + getString(R.string.parent_mail) + StorageManager.readString(this, "username", "");
                Log.i("Url_ChatHistory::", Url_ChatHistory);
            }
            WebServiceCall call = new WebServiceCall(this);
            call.getJsonObjectResponse(Url_ChatHistory);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }
}
