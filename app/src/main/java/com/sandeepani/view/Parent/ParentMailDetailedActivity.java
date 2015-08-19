package com.sandeepani.view.Parent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.model.ParentModel;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseFragmentActivity;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vijay on 4/11/15.
 */
public class ParentMailDetailedActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static final String TAG = ParentWriteMailToTeacher.class.getSimpleName();
    private TopBar1 topBar;
    private SwitchChildView switchChild;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private int selectedChildPosition = 0;
    ImageView backButton,mReplyMailIMGV;
    private Dialog dialog = null;
    TextView date,detailedMailTV,regardsFromTV, mailTimeTV,mailTitleTV,mailFromTV ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setSwitchChildDialogueData();
        setContentView(R.layout.activity_parent_detailed_inbox);
        setOnClickListeners();
      //  switchChildBar();
        setTopBar();
        UpdateUI();
        date=(TextView)findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy ");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();


        // Now we display formattedDate value in TextView

        date.setText(formattedDate);
    }

    public void onSwitchChild(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();
//        selectedChildPosition = appController.getSelectedChild();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.reply_imgview:

                Intent intent = new Intent(ParentMailDetailedActivity.this, ParentWriteMailToTeacher.class);
                Bundle b = new Bundle();
                b.putString("mailto", regardsFromTV.getText().toString());
                b.putString("mailToId",mailFromId);
                b.putString("msg",detailedMailTV.getText().toString());
                intent.putExtras(b);
                startActivity(intent);
                break;
          /*  case R.id.write_mailIV:
                startActivity(new Intent(this, com.sandeepani.view.Parent.ParentWriteMailToTeacher.class));
                break;*/
            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
//            case R.id.logoutIV:
//                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
//                Constants.logOut(this);
//
//                Intent i = new Intent(this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//
//                break;

            default:
                //Enter code in the event that that no cases match
        }

    }

    public void setOnClickListeners(){
        backButton = (ImageView) findViewById(R.id.back);
        detailedMailTV = (TextView) findViewById(R.id.mail_details);
        regardsFromTV = (TextView) findViewById(R.id.regards_from);
        mailTimeTV = (TextView) findViewById(R.id.mailTimeTV);
        mailTitleTV = (TextView) findViewById(R.id.mailTitleTV);
        mailFromTV = (TextView) findViewById(R.id.mailFromTV);
        mReplyMailIMGV = (ImageView) findViewById(R.id.reply_imgview);
        backButton.setOnClickListener(this);
        mReplyMailIMGV.setOnClickListener(this);
    }

    public void setSwitchChildDialogueData() {
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void setTopBar() {
        topBar = (TopBar1)findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.INVISIBLE);
        topBar.titleTV.setText(getString(R.string.inbox));
      //  topBar.logoutIV.setOnClickListener(this);
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        StorageManager.readString(this, "username", "");
        switchChild.childNameTV.setText(StorageManager.readString(this, "username", ""));
    }

    String mailFromId=null;
    public void UpdateUI(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String mailFrom = extras.getString("mailFrom");
            String mailDescription = extras.getString("mailDescription");
            String mailTitle = extras.getString("mailTitle");
             mailFromId=extras.getString("mailFromId");

            detailedMailTV.setText(mailDescription);
            mailFromTV.setText("From :"+mailFrom);
            regardsFromTV.setText(mailFrom);
            mailTitleTV.setText(mailTitle);
        }

    }


}
