package com.sandeepani.view.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sandeepani.adapters.TeacherEmailsListAdapter;
import com.sandeepani.interfaces.AsyncTaskInterface;
import com.sandeepani.model.MessageModel;
import com.sandeepani.threads.HttpConnectThread;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseActivity;
import com.sandeepani.view.R;
import com.sandeepani.webserviceparser.EmailJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TeacherEmailsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AsyncTaskInterface {
    private TopBar1 topBar;
    private ImageView writeMail;
    private EditText searchET;
    private ListView emailsLV;
    private TeacherEmailsListAdapter adapter;
    private TextView allMailsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_emails);
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
      //  topBar.logoutIV.setVisibility(View.GONE);
        topBar.titleTV.setText(getString(R.string.inbox));
        writeMail = (ImageView) findViewById(R.id.write_mail_iv);
        writeMail.setOnClickListener(this);
        searchET = (EditText) findViewById(R.id.search_et);
        searchET.addTextChangedListener(watcher);
        emailsLV = (ListView) findViewById(R.id.emails_lv);
        allMailsTV = (TextView) findViewById(R.id.all_mails_tv);
        String url = getString(R.string.base_url) + getString(R.string.url_teacher_email);
        httpConnectThread = new HttpConnectThread(this, null, this);
        httpConnectThread.execute(url);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getFilters(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void setMailAdapter(ArrayList<MessageModel> mailsList) {
        adapter = new TeacherEmailsListAdapter(this, R.layout.teacher_email_list_item, mailsList);
        emailsLV.setAdapter(adapter);
        allMailsTV.setText(getString(R.string.all_mails) + "(" + mailsList.size() + ")");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.write_mail_iv:
                Intent intent = new Intent(this, TeacherWriteNewEmailActivity.class);
                startActivity(intent);
                intent = null;
                break;
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            default:
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        CommonUtils.getLogs("Response::::" + object);
        if (object != null) {
            try {
                JSONObject jsonObject = new JSONObject(object);
                ArrayList<MessageModel> mailsList = EmailJsonParser.getInstance().getEmails(this, jsonObject);
                setMailAdapter(mailsList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
