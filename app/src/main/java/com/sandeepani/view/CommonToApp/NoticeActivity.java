package com.sandeepani.view.CommonToApp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.interfaces.IOnSwichChildListener;
import com.sandeepani.model.NoticeModel;
import com.sandeepani.model.ParentModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.Parent.ProfileFragmentActivity;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;
import com.sandeepani.webserviceparser.NoticeJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Antony on 04-06-2015.
 */
public class NoticeActivity extends BaseFragmentActivity implements View.OnClickListener, RequestCompletion, IOnSwichChildListener {
    public static final String TAG = NoticeActivity.class.getSimpleName();
    private TopBar1 topBar;
    private SwitchChildView switchChild;
    public AppController appController = null;
    private int selectedChildPosition = 0;
    private String childName = null;
    private Dialog dialog = null;
    private ParentModel parentModel = null;
    private ArrayList<NoticeModel> mNoticeList;
    private ViewFlipper viewFlipper;
    private float lastX;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        setContentView(R.layout.activity_notice);
        parentModel = appController.getParentsData();
        appController.setParentData(parentModel);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        getParentDetailsWebservicescall();
        inflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setTopBar();
        switchChildBar();

        Bundle buddle = getIntent().getExtras();

        if (buddle != null) {
            String fromKey = buddle.getString(getString(R.string.key_from));
            if (fromKey.equals(getString(R.string.key_from_parent))) {
                switchChild.switchChildBT.setVisibility(View.VISIBLE);
                switchChild.setVisibility(View.VISIBLE);
            } else if (fromKey.equals(getString(R.string.key_from_teacher))) {
                switchChild.switchChildBT.setVisibility(View.GONE);
                switchChild.setVisibility(View.GONE);
            }
        }
    }

    public void getParentDetailsWebservicescall() {
        String Url_parent_details = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            //SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
           /* if (!StorageManager.readString(this, "username", "").isEmpty()) {
                Url_parent_details = getString(R.string.base_url) + getString(R.string.parent_url_endpoint) + StorageManager.readString(this, "username", "");
                Log.i("===Url_parent===", Url_parent_details);
            }*/
            String Url_notice = (getString(R.string.base_url)+"/app/notice").replaceAll(" ", "%20");
            WebServiceCall call = new WebServiceCall(this);
            call.getJsonObjectResponse(Url_notice);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.notice));
        topBar.backArrowIV.setOnClickListener(this);
      //  topBar.logoutIV.setOnClickListener(this);
       // ImageView notification = (ImageView) topBar.findViewById(R.id.notification);
       // notification.setVisibility(View.VISIBLE);
    //    notification.setOnClickListener(new View.OnClickListener() {
      //      @Override
        //    public void onClick(View v) {
          //      startActivity(new Intent(NoticeActivity.this, NotificationActivity.class));
           // }
        //});
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.childNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
        switchChild.childNameTV.setOnClickListener(this);
    }

    public void onSwitchChild(int selectedChildPosition) {
        childName = Constants.getChildNameAfterSelecting(selectedChildPosition, appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.child_name:
                startActivity(new Intent(this, ProfileFragmentActivity.class));
                break;

//            case R.id.logoutIV:
//                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
//                Constants.logOut(this);
//                Intent i = new Intent(this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//
//                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
        switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);
        Log.e("asffdfa",responseJson.toString());
        try {
            if (responseJson != null) {
                mNoticeList = NoticeJsonParser.getInstance().getNoticeList(responseJson);
                for (int i = 0; i < mNoticeList.size(); i++) {
                    //  JSONObject jsonObject = jsonArray.getJSONObject(i);
                    NoticeModel noticeModel = mNoticeList.get(i);
                    View view = inflater.inflate(R.layout.activity_notice_subview, viewFlipper, false);
                    TextView mDate = (TextView) view.findViewById(R.id.dateTV);
                    TextView mDay = (TextView) view.findViewById(R.id.daynTV);
                    TextView mSalutation = (TextView) view.findViewById(R.id.salutationTV);
                    TextView mNoticeContent = (TextView) view.findViewById(R.id.noticecontentTV);
                    TextView mSenderName = (TextView) view.findViewById(R.id.senderNameTV);
                    TextView mSignOff = (TextView) view.findViewById(R.id.signOffTV);
                    TextView mDesignation = (TextView) view.findViewById(R.id.designationTV);

                    mDate.setText(noticeModel.getDate().toString());
                    mDay.setText(noticeModel.getDay().toString());
                    mNoticeContent.setText(noticeModel.getMessage().toString());
                    mSenderName.setText(noticeModel.getSenderName().toString());
                    mSignOff.setText(noticeModel.getSignOff().toString());
                    mDesignation.setText(noticeModel.getDesignation().toString());
                    mSalutation.setText(noticeModel.getSalutation().toString());
                    viewFlipper.addView(view);
                }
            } else {
                Toast.makeText(this, "No data..", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Error in notice response::" + error);
        Constants.stopProgress(this);
        Constants.showMessage(this, "unable to request. ", error);
    }

    // Using the following method, we will handle all screen swaps.
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();

                // Handling left to right screen swap.
                if (lastX < currentX) {

                    // If there aren't any other children, just break.
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    // Next screen comes in from left.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

                    // Display next screen.
                    viewFlipper.showNext();
                }

                // Handling right to left screen swap.
                if (lastX > currentX) {

                    // If there is a child (to the left), kust break.
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;

                    // Next screen comes in from right.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

                    // Display previous screen.
                    viewFlipper.showPrevious();
                }
                break;
        }
        return false;
    }
}
