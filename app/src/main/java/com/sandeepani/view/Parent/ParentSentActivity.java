package com.sandeepani.view.Parent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.adapters.SentAdapter;
import com.sandeepani.model.ParentModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseActivity;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;
import com.sandeepani.webserviceparser.ParentMailBoxParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParentSentActivity extends BaseActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentSentActivity.class.getSimpleName();
    public Integer i;
    private TopBar1 topBar;
    private ParentModel parentModel = null;
    private Dialog dialog = null;
    private AppController appController = null;
    ListView listView;
    ArrayList<HashMap<String, String>> mailBox,mm;
    TextView mailCount,date1,sentBox;

    public static String li;
    List<String> arr;
    SentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_sent);
        setOnClickListener();
        inboxWebServiceCall();
        setTopBar();
        //setSwitchChildDialogueData();
//        date1=(TextView)findViewById(R.id.date1TV);
//        Calendar c = Calendar.getInstance();
//        //  c.setTimeInMillis(smsMessage.getTimestampMillis());
//        int date=c.get(Calendar.DATE);
//        int month=c.get(Calendar.MONTH);
//        int year=c.get(Calendar.YEAR);
//        String S=String.valueOf(date);
//        String S1=String.valueOf(month);
//        String S2=String.valueOf(year);
//        System.out.println("Current time => "+c.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy ");
//        String formattedDate = df.format(c.getTime());
//        // formattedDate have current date/time
//        Toast.makeText(this, S + " " + S1 + " " + S2, Toast.LENGTH_SHORT).show();
//        //Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
//
//
//        // Now we display formattedDate value in TextView
//
//        date1.setText(formattedDate);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //     selectedChildPosition = appController.getSelectedChild();
        //   switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            default:
                //Enter code in the event that that no cases match
        }}
    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray)
    {
        CommonUtils.getLogs("INbox Response success");
        Log.i(TAG, responseJson.toString());
        String numberOfConversations = getNumberOfConversations(responseJson);
        if(!numberOfConversations.contains("0")) {
            mailBox = ParentMailBoxParser.getInstance().getEmails(responseJson);
//            arr = new ArrayList<String>(ArrayList.asList(mailBox));

            adapter = new SentAdapter(this, mailBox);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

//                    tid = mailBox.get(position).get("threadId");
//                    i = Integer.parseInt(tid);
                    String mailFrom = mailBox.get(position).get("toId");
                    String mailFromId = mailBox.get(position).get("fromId");
                    String mailTitle = mailBox.get(position).get("title");
                    String mailDescription = mailBox.get(position).get("messageText");

                    // Toast.makeText(getApplicationContext(),mailFrom +"  "+mailTitle,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ParentSentActivity.this, SentDetailActivity.class);
                    Bundle b = new Bundle();
                    //b.putInt("position", position);
                    b.putString("mailFrom", mailFrom);
                    b.putString("mailDescription", mailDescription);
                    b.putString("mailTitle", mailTitle);
                    b.putString("mailFromId", mailFromId);

                    intent.putExtras(b);
                    startActivity(intent);

                }
            });
        }
        Constants.stopProgress(this);
    }
    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Inbox Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }
    public void setOnClickListener() {
        listView = (ListView) findViewById(R.id.parent_sent_list);



    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.title_activity_parent_sent));
        //   topBar.logoutIV.setOnClickListener(this);
    }
    public void inboxWebServiceCall() {
        String Url_inbox = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            Url_inbox = getString(R.string.base_url) + getString(R.string.mail_sent);
            Log.i("TimetableURL", Url_inbox);
            WebServiceCall call = new WebServiceCall(this);
            call.getJsonObjectResponse(Url_inbox);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }
    public String getNumberOfConversations(JSONObject responseJson){
        String numberOfConversations = null;
        try {
            numberOfConversations = responseJson.getString("numberOfConversations");
            // mailCount.setText("ALL MAILS"+"("+numberOfConversations+")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("noOfConversations::",numberOfConversations);
        return numberOfConversations;
    }
}




