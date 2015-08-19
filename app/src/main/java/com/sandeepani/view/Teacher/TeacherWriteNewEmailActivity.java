package com.sandeepani.view.Teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.sandeepani.interfaces.AsyncTaskInterface;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.threads.HttpConnectThread;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseActivity;
import com.sandeepani.view.R;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherWriteNewEmailActivity extends BaseActivity implements View.OnClickListener, AsyncTaskInterface {
    private TopBar1 topBar;
    private ImageView backButton;
    private EditText mailToEt, mailSubjectET, mailMessageET;
    private Button sendMailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_write_new_email);
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.GONE);
        topBar.titleTV.setText(getString(R.string.inbox));
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(this);
      //  topBar.logoutIV.setVisibility(View.GONE);
        mailToEt = (EditText) findViewById(R.id.mail_to_et);
        mailSubjectET = (EditText) findViewById(R.id.mail_subject_et);
        mailMessageET = (EditText) findViewById(R.id.mail_message_et);
        sendMailBtn = (Button) findViewById(R.id.send_mail_btn);
        sendMailBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.send_mail_btn:
                String mailTo = mailToEt.getText().toString().trim();
                String subject = mailSubjectET.getText().toString().trim();
                String messageBody = mailMessageET.getText().toString().trim();
                if (mailTo.equals("") || subject.equals("") || messageBody.equals("")) {
                    CommonUtils.getToastMessage(this, "Please enter all the fields");
                } else {
                    CommonUtils.getLogs("Newtowrk Status : " + CommonUtils.isNetworkAvailable(this));
                    if (CommonUtils.isNetworkAvailable(this)) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("title", subject);
                            jsonObject.put("messageText", messageBody);
                            jsonObject.put("fromId", StorageManager.readString(this, getString(R.string.pref_username), ""));
                            jsonObject.put("toId", mailTo);
                            httpConnectThread = new HttpConnectThread(this, jsonObject, this);
                            String url = getString(R.string.base_url) + getString(R.string.url_new_mail);
                            httpConnectThread.execute(url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.getToastMessage(this, getString(R.string.network_error));
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        CommonUtils.getLogs("Response:::" + object);
        if (object != null) {
            try {
                JSONObject jsonObject = new JSONObject(object);
                if (jsonObject.has("status")) {
                    if (jsonObject.getString("status").equals("success")) {
                        mailToEt.setText("");
                        mailMessageET.setText("");
                        mailSubjectET.setText("");
                        if (jsonObject.has("status")) {
                            CommonUtils.getToastMessage(this, jsonObject.getString("message"));
                        }
                    } else if (jsonObject.getString("status").equals("error")) {
                        if (jsonObject.has("status")) {
                            CommonUtils.getToastMessage(this, jsonObject.getString("message"));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
