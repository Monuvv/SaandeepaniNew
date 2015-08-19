package com.sandeepani.view.Parent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.adapters.ParentInboxAdapter;
import com.sandeepani.customView.SwitchChildView;
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

/**
 * Created by Vijay on 3/29/15.
 */
public class ParentInboxActivity extends BaseActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentInboxActivity.class.getSimpleName();
    private TopBar1 topBar;
    private SwitchChildView switchChild;
    ImageView writeMail;
    private ParentModel parentModel = null;
    private Dialog dialog = null;
    private int selectedChildPosition = 0;
    private AppController appController = null;
    ListView listView;
    ArrayList<HashMap<String, String>> mailBox;
    TextView mailCount,date1,sentBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_inbox);
        setOnClickListener();
        setTopBar();
        //switchChildBar();
        inboxWebServiceCall();
        //setSwitchChildDialogueData();
        date1=(TextView)findViewById(R.id.date11);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy ");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();


        // Now we display formattedDate value in TextView

        date1.setText(formattedDate);


    }

    @Override
    protected void onResume() {
        super.onResume();
   //     selectedChildPosition = appController.getSelectedChild();
     //   switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            case R.id.mailSent:
                startActivity(new Intent(ParentInboxActivity.this, ParentSentActivity
                        .class));
                break;

            case R.id.write_mailIV:

                startActivity(new Intent(ParentInboxActivity.this, ParentWriteMailToTeacher2.class));

                break;
            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
                break;

//            case R.id.logoutIV:
//                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
//                Constants.logOut(this);
//
//                Intent i = new Intent(this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();

               // break;
            default:
                //Enter code in the event that that no cases match
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("INbox Response success");
        Log.i(TAG, responseJson.toString());
        String numberOfConversations = getNumberOfConversations(responseJson);
        if(!numberOfConversations.contains("0")){
            mailBox = ParentMailBoxParser.getInstance().getEmails(responseJson);
            ParentInboxAdapter adapter = new ParentInboxAdapter(this, mailBox);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View view,int position, long id) {
                    String mailFrom = mailBox.get(position).get("fromName");
                    String mailFromId = mailBox.get(position).get("fromId");
                    String mailTitle = mailBox.get(position).get("title");
                    String mailDescription = mailBox.get(position).get("messageText");
                    Intent intent = new Intent(ParentInboxActivity.this, ParentMailDetailedActivity.class);
                    Bundle b = new Bundle();
                    //b.putInt("position", position);
                    b.putString("mailFrom",mailFrom);
                    b.putString("mailDescription", mailDescription);
                    b.putString("mailTitle", mailTitle);
                    b.putString("mailFromId", mailFromId);


                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
        else {
            Constants.showMessage(this,"No Mails","No mail found ...");
        }
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Inbox Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }


    public void onSwitchChild(int selectedChildPosition) {


        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }

    public void setSwitchChildDialogueData() {
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void setOnClickListener() {
        mailCount = (TextView) findViewById(R.id.mailCount);
      sentBox = (TextView) findViewById(R.id.mailSent);
        writeMail = (ImageView) findViewById(R.id.write_mailIV);
        listView = (ListView) findViewById(R.id.parent_inbox_list);
        writeMail.setOnClickListener(this);
        sentBox.setOnClickListener(this);

    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.inbox));
       // topBar.logoutIV.setOnClickListener(this);
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.switchChildBT.setOnClickListener(this);
    }

    public void inboxWebServiceCall() {
        String Url_inbox =null ;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            Url_inbox = getString(R.string.base_url) + getString(R.string.parent_mail);
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
            mailCount.setText("ALL MAILS"+"("+numberOfConversations+")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("noOfConversations::",numberOfConversations);
        return numberOfConversations;
    }
}