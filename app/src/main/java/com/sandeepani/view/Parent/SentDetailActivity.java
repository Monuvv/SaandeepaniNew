package com.sandeepani.view.Parent;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sandeepani.adapters.SentAdapter;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseFragmentActivity;
import com.sandeepani.view.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SentDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static final String TAG = ParentWriteMailToTeacher.class.getSimpleName();
    private TopBar1 topBar;
    ImageView backButton,mReplyMailIMGV;
    private Dialog dialog = null;
    SentAdapter ptime;
    String time=ptime.s;
    TextView date,detailedMailTV,regardsFromTV, mailTimeTV,mailTitleTV,mailFromTV ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_detail);
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

        backButton.setOnClickListener(this);
    }
    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.INVISIBLE);
        topBar.titleTV.setText(getString(R.string.inbox));
        // topBar.logoutIV.setOnClickListener(this);
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
            mailTimeTV.setText(time);
        }

    }


}
